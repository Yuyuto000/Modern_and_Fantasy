package aaaminecraft.modern_fantasy.block.customs;

import aaaminecraft.modern_fantasy.block.blockstate.SofaPart;
import aaaminecraft.modern_fantasy.entity.ModEntities;
import aaaminecraft.modern_fantasy.entity.SeatEntity;
import aaaminecraft.modern_fantasy.entity.SofaEntity;
import com.mojang.logging.LogUtils;
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
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class ModernSofaBlock extends HorizontalDirectionalBlock {

    // 座る高さ（ブロック下端からの相対値）
    private static final double SOFA_SIT_HEIGHT = -0.1D;

    // 座る位置の前後左右オフセット
    private static final double SOFA_SIT_OFFSET_X = 0.0D;
    private static final double SOFA_SIT_OFFSET_Z = 0.0D;

    // 寝転がり時の高さ
    private static final double SOFA_SLEEP_HEIGHT = 0.55D;

    public static final EnumProperty<SofaPart> PART = EnumProperty.create("part", SofaPart.class);
    private static final Logger log = LogUtils.getLogger();

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
        if (!level.isClientSide) {

            // 通常着席Entityを削除
            for (SeatEntity seat : level.getEntitiesOfClass(
                    SeatEntity.class,
                    new AABB(pos)
            )) {
                seat.discard();
            }

            // このブロックを占有している寝転がりEntityを削除
            for (SofaEntity sofa : level.getEntitiesOfClass(SofaEntity.class, new AABB(pos).inflate(1.0D), entity -> entity.occupies(pos))) {
                sofa.discard();
            }
        }

        super.playerWillDestroy(level, pos, state, player);
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

    // SofaEntityが指定ブロックを占有しているか
    private boolean hasSofaEntity(Level level, BlockPos pos) {
        return !level.getEntitiesOfClass(SofaEntity.class, new AABB(pos).inflate(1.0D), entity -> entity.occupies(pos)).isEmpty();
    }

    // 寝転がり可能なソファパーツか
    private boolean isValidSleepPart(BlockState state) {
        SofaPart part = state.getValue(PART);
        return part == SofaPart.HALF_LEFT || part == SofaPart.HALF_RIGHT || part == SofaPart.CENTER;
    }

    // 寝転がりEntityを生成
    private void createSofaEntity(Level level, BlockPos headPos, BlockPos footPos, Player player) {
        SofaEntity sofa = new SofaEntity(ModEntities.SOFA.get(), level);

        sofa.setLayout(headPos, footPos, SOFA_SLEEP_HEIGHT);

        if (!level.addFreshEntity(sofa)) {
            return;
        }

        boolean success = player.startRiding(sofa, true);

        if (!success) {
            sofa.discard();
        }

        log.info("[SofaBlock] startRiding={} isPassenger={}", success, player.isPassenger());
    }

    private BlockPos findFootPos(Level level, BlockPos headPos, BlockState headState) {
        Direction facing = headState.getValue(FACING);

        Direction right = facing.getClockWise();
        Direction left = facing.getCounterClockWise();

        BlockPos rightPos = headPos.relative(right);
        BlockPos leftPos = headPos.relative(left);

        boolean rightValid = isValidFootCandidate(level, rightPos);
        boolean leftValid = isValidFootCandidate(level, leftPos);

        // 右側を優先
        if (rightValid) {
            return rightPos;
        }

        if (leftValid) {
            return leftPos;
        }

        return null;
    }

    private boolean isValidFootCandidate(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() != this) {
            return false;
        }

        if (!isValidSleepPart(state)) {
            return false;
        }

        // 足側に着席者がいたら不可
        if (hasSeat(level, pos)) {
            return false;
        }

        // 既に寝転がりに使われていたら不可
        return !hasSofaEntity(level, pos);
    }

    // ネットワーク経由の寝転がり要求をサーバー側で処理
    public void handleLieRequest(ServerLevel level, BlockPos headPos, Player player) {
        // 操作対象との距離を検証
        if (player.distanceToSqr(Vec3.atCenterOf(headPos)) > 36.0D) {
            return;
        }

        // プレイヤーが既に乗車中なら拒否
        if (player.isPassenger()) {
            return;
        }

        BlockState state = level.getBlockState(headPos);

        // ソファ以外なら拒否
        if (state.getBlock() != this) {
            return;
        }

        // 寝転がりに使用できるPARTか確認
        if (!isValidSleepPart(state)) {
            return;
        }

        // 頭側が既に別の寝転がりEntityに占有されていないか確認
        if (hasSofaEntity(level, headPos)) {
            return;
        }

        // 足側を検索
        BlockPos footPos = findFootPos(level, headPos, state);

        if (footPos == null) {
            return;
        }

        // Entity生成
        createSofaEntity(level, headPos, footPos, player);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (player.isPassenger()) {
            return InteractionResult.PASS;
        }

        if (hasSeat(level, pos) || hasSofaEntity(level, pos)) {
            return InteractionResult.PASS;
        }

        createSeat(level, pos, player);
        return InteractionResult.CONSUME;
    }
}
