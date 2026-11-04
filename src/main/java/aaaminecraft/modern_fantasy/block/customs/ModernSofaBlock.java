package aaaminecraft.modern_fantasy.block.customs;

import aaaminecraft.modern_fantasy.block.blockentity.ModernSofaBlockEntity;
import aaaminecraft.modern_fantasy.block.blockstate.SofaBlockPart;
import aaaminecraft.modern_fantasy.block.blockstate.SofaVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModernSofaBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final EnumProperty<SofaBlockPart> BLOCK_PART = EnumProperty.create("block_part", SofaBlockPart.class);
    public static final EnumProperty<SofaVariant> VARIANT = EnumProperty.create("variant", SofaVariant.class);

    public ModernSofaBlock(Properties properties) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(BLOCK_PART, SofaBlockPart.FOOT)
                        .setValue(VARIANT, SofaVariant.SINGLE)
        );
    }

    // BlockState
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BLOCK_PART, VARIANT);
    }

    // BlockEntity
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModernSofaBlockEntity(pos, state);
    }

    // 設置判定
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx){

        Direction facing = ctx.getHorizontalDirection();

        BlockPos footPos = ctx.getClickedPos();
        BlockPos headPos = footPos.relative(facing);

        Level level = ctx.getLevel();

        if (!level.getBlockState(headPos).canBeReplaced()){
            return null;
        }

        return defaultBlockState()
                .setValue(FACING, facing)
                .setValue(BLOCK_PART, SofaBlockPart.FOOT)
                .setValue(VARIANT, SofaVariant.SINGLE);
    }

    // Head設置
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){

        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.isClientSide){
            return;
        }

        Direction facing = state.getValue(FACING);
        BlockPos footPos = pos;
        BlockPos headPos = footPos.relative(facing);

        BlockState headState = state.setValue(BLOCK_PART, SofaBlockPart.HEAD);
        level.setBlock(headPos, headState, Block.UPDATE_ALL);

        BlockEntity footEntity = level.getBlockEntity(footPos);
        BlockEntity headEntity = level.getBlockEntity(headPos);

        if (footEntity instanceof ModernSofaBlockEntity foot && headEntity instanceof ModernSofaBlockEntity head) {
            foot.setConnectedPos(headPos);
            head.setConnectedPos(headPos);
        }

        updatePairVariants(level, footPos, headPos);
    }

    // ペア位置取得
    public static BlockPos getConnectedPos(BlockPos pos, BlockState state){

        Direction facing = state.getValue(FACING);

        return state.getValue(BLOCK_PART) == SofaBlockPart.HEAD ? pos.relative(facing.getOpposite()) : pos.relative(facing);
    }

    // ペア検証
    public static boolean isValidPair(LevelAccessor level, BlockPos pos, BlockState state){

        if (state.getBlock() instanceof ModernSofaBlock == false){
            return false;
        }

        BlockPos connectedPos = getConnectedPos(pos, state);
        BlockState connectedState = level.getBlockState(connectedPos);

        if (connectedState.getBlock() != state.getBlock()){
            return false;
        }

        if (!connectedState.hasProperty(BLOCK_PART) || !connectedState.hasProperty(FACING)){
            return false;
        }

        SofaBlockPart expectedPart = state.getValue(BLOCK_PART) == SofaBlockPart.HEAD ? SofaBlockPart.FOOT : SofaBlockPart.HEAD;

        return connectedState.getValue(BLOCK_PART) == expectedPart && connectedState.getValue(FACING) == state.getValue(FACING);
    }

    // 隣接更新
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        BlockPos connectedPos = getConnectedPos(pos, state);

        if (direction == Direction.UP || direction == Direction.DOWN) {
            return super.updateShape(state, direction, newState, level, pos, neighbourPos);
        }

        // HEAD・FOOTのどっちかが失われた場合
        if (neighbourPos.equals(connectedPos) && !isValidPair(level, pos, state)) {
            return Blocks.AIR.defaultBlockState();
        }

        // 外観を再計算する
        return super.updateShape(state, direction, newState, level, pos, neighbourPos).setValue(VARIANT, calculateVariant(level, pos, state));
    }

    // 外観自動判定メソッド
    private SofaVariant calculateVariant(LevelAccessor level, BlockPos pos, BlockState state){
        Direction facing = state.getValue(FACING);

        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        Direction forward = facing;
        Direction backward = facing.getOpposite();

        boolean hasLeft = isConnectedSofa(level, pos, state, pos.relative(left));
        boolean hasRight = isConnectedSofa(level, pos, state, pos.relative(right));

        boolean hasForward = isConnectedSofa(level, pos, state, pos.relative(forward));
        boolean hasBackward = isConnectedSofa(level, pos, state, pos.relative(backward));

        boolean hasLongitudinal = hasForward || hasBackward;

        if (hasLeft && hasRight){
            return SofaVariant.CENTER;
        }
        if (hasLeft && hasLongitudinal){
            return SofaVariant.CORNER_LEFT;
        }
        if (hasRight && hasLongitudinal){
            return  SofaVariant.CORNER_RIGHT;
        }
        if (hasLeft){
            return SofaVariant.HALF_LEFT;
        }
        if (hasRight){
            return SofaVariant.HALF_RIGHT;
        }

        return SofaVariant.SINGLE;
    }

    private boolean isConnectedSofa(LevelAccessor level, BlockPos currentPos,  BlockState currentState, BlockPos candidatePos){
        if (candidatePos.equals(getConnectedPos(currentPos, currentState))){
            return false;
        }

        return level.getBlockState(candidatePos).getBlock() == this;
    }

    private void updatePairVariants(LevelAccessor level, BlockPos footPos, BlockPos headPos){
        BlockState footState = level.getBlockState(footPos);
        BlockState headState = level.getBlockState(headPos);

        if (footState.getBlock() != this || headState.getBlock() != this){
            return;
        }

        SofaVariant footVariant = calculateVariant(level, footPos, footState);
        SofaVariant headVariant = calculateVariant(level, headPos, headState);

        level.setBlock(footPos, footState.setValue(VARIANT, footVariant), Block.UPDATE_ALL);
        level.setBlock(headPos, headState.setValue(VARIANT, headVariant), Block.UPDATE_ALL);
    }

    // 破壊時のペア削除
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {

        if (!level.isClientSide && state.getBlock() != newState.getBlock()){
            BlockPos connectedPos = getConnectedPos(pos, state);
            BlockState connectedState = level.getBlockState(connectedPos);

            if (connectedState.getBlock() == this){
                level.setBlock(connectedPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    // VoxelShape
    private static final VoxelShape SHAPE_SINGLE = Shapes.or(
            Block.box(3,1,1,13,6,13),
            Block.box(0,1,0,3,12,13),
            Block.box(13,1,0,16,12,13),
            Block.box(0,1,13,16,12,16),
            Block.box(0,0,0,1,1,1),
            Block.box(15,0,0,16,1,1),
            Block.box(0,0,15,1,1,16),
            Block.box(15,0,15,16,1,16)
    );
    private static final VoxelShape SHAPE_HALF_LEFT = Shapes.or(
            Block.box(0,1,1,13,6,13),
            Block.box(0,1,0,3,12,13),
            Block.box(0,1,13,16,12,16),
            Block.box(0,0,0,1,1,1),
            Block.box(15,0,0,16,1,1),
            Block.box(0,0,15,1,1,16),
            Block.box(15,0,15,16,1,16)
    );
    private static final VoxelShape SHAPE_HALF_RIGHT = Shapes.or(
            Block.box(3,1,1,16,6,13),
            Block.box(13,1,0,16,12,13),
            Block.box(0,1,13,16,12,16),
            Block.box(0,0,0,1,1,1),
            Block.box(15,0,0,16,1,1),
            Block.box(0,0,15,1,1,16),
            Block.box(15,0,15,16,1,16)
    );
    private static final VoxelShape SHAPE_CORNER_LEFT = SHAPE_HALF_LEFT;
    private static final VoxelShape SHAPE_CORNER_RIGHT = SHAPE_HALF_RIGHT;
    private static final VoxelShape SHAPE_CENTER = Shapes.or(
            Block.box(0,1,1,16,6,13),
            Block.box(0,1,13,16,2,16),
            Block.box(0,0,0,1,1,1),
            Block.box(15,0,0,16,1,1),
            Block.box(0,0,15,1,1,16),
            Block.box(15,0,15,16,1,16)
    );

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        VoxelShape shape = switch (state.getValue(VARIANT)){
            case SINGLE -> SHAPE_SINGLE;
            case HALF_LEFT ->  SHAPE_HALF_LEFT;
            case HALF_RIGHT ->  SHAPE_HALF_RIGHT;
            case CORNER_LEFT ->  SHAPE_CORNER_LEFT;
            case CORNER_RIGHT ->  SHAPE_CORNER_RIGHT;
            case CENTER ->  SHAPE_CENTER;
        };

        return rotateShape(shape, state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    private static VoxelShape rotateShape(VoxelShape shape, Direction facing) {
        VoxelShape result = Shapes.empty();

        for (AABB box : shape.toAabbs()){
            AABB rotated = switch (facing){
                case EAST -> new AABB(
                        1.0D - box.maxZ,
                        box.minY,
                        box.minZ,
                        1.0D - box.minZ,
                        box.maxY,
                        box.maxX
                );
                case SOUTH -> new AABB(
                        1.0D - box.maxZ,
                        box.minY,
                        1.0D - box.minZ,
                        1.0D - box.minZ,
                        box.maxY,
                        1.0D - box.maxX
                );
                case WEST -> new AABB(
                        box.maxZ,
                        box.minY,
                        1.0D - box.minZ,
                        box.minZ,
                        box.maxY,
                        1.0D - box.maxX
                );

                default -> box;
            };

            result = Shapes.or(result, Shapes.create(rotated));
        }

        return result;
    }
}
