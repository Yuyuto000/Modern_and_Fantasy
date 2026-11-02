package aaaminecraft.modern_fantasy.block.customs;

import aaaminecraft.modern_fantasy.block.blockstate.SofaPart;
import aaaminecraft.modern_fantasy.entity.ModEntities;
import aaaminecraft.modern_fantasy.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModernSofaBlock extends HorizontalDirectionalBlock {

    // 座る高さ（ブロック下端からの相対値）
    private static final double SOFA_SIT_HEIGHT = -0.1D;

    // 座る位置の前後左右オフセット
    private static final double SOFA_SIT_OFFSET_X = 0.0D;
    private static final double SOFA_SIT_OFFSET_Z = 0.0D;

    public static final EnumProperty<SofaPart> PART = EnumProperty.create("part", SofaPart.class);
    public ModernSofaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, SofaPart.SINGLE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, PART);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(PART, SofaPart.SINGLE);
    }

    // VoxelShape


    private static final VoxelShape SHAPE_SINGLE = Shapes.or(
            // 座面
            Block.box(3, 1, 1, 13, 6, 13),

            // 左肘掛け
            Block.box(0, 1, 0, 3, 12, 13),

            // 右肘掛け
            Block.box(13, 1, 0, 16, 12, 13),

            // 背もたれ
            Block.box(0, 1, 13, 16, 12, 16),

            // 脚
            Block.box(0, 0, 0, 1, 1, 1),
            Block.box(15, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 1, 1, 16),
            Block.box(15, 0, 15, 16, 1, 16)
    );

    private static final VoxelShape SHAPE_HALF_LEFT = Shapes.or(
            // 座面
            Block.box(0, 1, 1, 13, 6, 13),

            // 左肘掛け
            Block.box(0, 1, 0, 3, 12, 13),

            // 背もたれ
            Block.box(0, 1, 13, 16, 12, 16),

            // 脚
            Block.box(0, 0, 0, 1, 1, 1),
            Block.box(15, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 1, 1, 16),
            Block.box(15, 0, 15, 16, 1, 16)
    );

    private static final VoxelShape SHAPE_HALF_RIGHT = Shapes.or(
            // 座面
            Block.box(3, 1, 1, 16, 6, 13),

            // 右肘掛け
            Block.box(13, 1, 0, 16, 12, 13),

            // 背もたれ
            Block.box(0, 1, 13, 16, 12, 16),

            // 脚
            Block.box(0, 0, 0, 1, 1, 1),
            Block.box(15, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 1, 1, 16),
            Block.box(15, 0, 15, 16, 1, 16)
    );

    private static final VoxelShape SHAPE_CORNER_LEFT = SHAPE_HALF_LEFT;
    private static final VoxelShape SHAPE_CORNER_RIGHT = SHAPE_HALF_RIGHT;

    private static final VoxelShape SHAPE_CENTER = Shapes.or(
            // 座面
            Block.box(0, 1, 1, 16, 6, 13),

            // 背もたれ
            Block.box(0, 1, 13, 16, 12, 16),

            // 脚
            Block.box(0, 0, 0, 1, 1, 1),
            Block.box(15, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 1, 1, 16),
            Block.box(15, 0, 15, 16, 1, 16)
    );


    private static final VoxelShape[] SHAPES = {
            SHAPE_SINGLE,
            SHAPE_HALF_LEFT,
            SHAPE_HALF_RIGHT,
            SHAPE_CORNER_LEFT,
            SHAPE_CORNER_RIGHT,
            SHAPE_CENTER
    };

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(PART)) {
            case SINGLE -> SHAPE_SINGLE;
            case HALF_LEFT -> SHAPE_HALF_LEFT;
            case HALF_RIGHT -> SHAPE_HALF_RIGHT;
            case CORNER_LEFT -> SHAPE_CORNER_LEFT;
            case CORNER_RIGHT -> SHAPE_CORNER_RIGHT;
            case CENTER -> SHAPE_CENTER;
        };

        return rotateShape(shape, state.getValue(FACING));
    }

    private static VoxelShape rotateShape(VoxelShape shape, Direction facing) {
        VoxelShape result = Shapes.empty();

        for (AABB box : shape.toAabbs()){
            AABB rotated = switch (facing) {
                case EAST -> new AABB(
                        1.0 - box.maxZ, box.minY, box.minX,
                        1.0 - box.minZ, box.maxY, box.maxX
                );
                case SOUTH -> new AABB(
                        1.0 - box.maxX, box.minY, 1.0 - box.maxZ,
                        1.0 - box.minX, box.maxY, 1.0 - box.minZ
                );
                case WEST -> new AABB(
                        box.minZ, box.minY, 1.0 - box.maxX,
                        box.maxZ, box.maxY, 1.0 - box.minX
                );
                default -> box;
            };

            result = Shapes.or(result, Shapes.create(rotated));
        }

        return result;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        if (!level.isClientSide()){
            level.scheduleTick(pos, this, 1);
        }

        return super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random){
        BlockState newState = calculatePart(level, pos, state);

        if (newState != state) {
            level.setBlock(pos, newState, 3);
        }
    }

    private BlockState calculatePart(Level level, BlockPos pos, BlockState state){
        Direction facing = state.getValue(FACING);
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();
        boolean hasLeft = isSofa(level, pos.relative(left));
        boolean hasRight = isSofa(level, pos.relative(right));

        SofaPart part;

        if (hasLeft && hasRight) {
            part = SofaPart.CENTER;
        } else if (hasLeft) {
            part = SofaPart.HALF_LEFT;
        } else if (hasRight) {
            part = SofaPart.HALF_RIGHT;
        } else  {
            part = SofaPart.SINGLE;
        }

        return state.setValue(PART, part);
    }

    private boolean isSofa(Level level, BlockPos pos){
        return level.getBlockState(pos).getBlock() == this;
    }

    // ソファのEntityマネジメント
    private boolean hasSeat(Level level, BlockPos pos){
        return !level.getEntitiesOfClass(SeatEntity.class, new AABB(pos)).isEmpty();
    }

    private void createSeat(Level level, BlockPos pos, Player player){
        if (hasSeat(level, pos)){
            return;
        }

        SeatEntity seat = new SeatEntity(ModEntities.SEAT.get(), level);
        seat.setPos(
                pos.getX() + 0.5D + SOFA_SIT_OFFSET_X,
                pos.getY(),
                pos.getZ() + 0.5D + SOFA_SIT_OFFSET_Z
        );

        // 座面の高さをEntityに設定
        seat.setSitHeight(SOFA_SIT_HEIGHT);

        if (level.addFreshEntity(seat)){
            player.startRiding(seat);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()){
            for (SeatEntity seat : level.getEntitiesOfClass(SeatEntity.class, new AABB(pos))) {
                seat.discard();
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
        if (hand != InteractionHand.MAIN_HAND){
            return InteractionResult.PASS;
        }

        if (level.isClientSide){
            return InteractionResult.SUCCESS;
        }

        // 使用中の座席はPASS
        if (hasSeat(level, pos)){
            return InteractionResult.PASS;
        }

        if (!player.isCrouching()){
            createSeat(level, pos, player);
            return InteractionResult.CONSUME;
        }

        //寝ころび
        if (hasTwoAdjacentEmptySeats(level, pos, state)) {
            // TODO:寝ころび用Entityの実装
            return InteractionResult.CONSUME;
        }

        return  InteractionResult.PASS;
    }

    private boolean hasTwoAdjacentEmptySeats(Level level, BlockPos pos, BlockState state){
        Direction facing = state.getValue(FACING);
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        return isEmptySofa(level, pos.relative(left)) || isEmptySofa(level, pos.relative(right));
    }

    private boolean isEmptySofa(Level level, BlockPos pos){
        return isSofa(level, pos) && !hasSeat(level, pos);
    }
}
