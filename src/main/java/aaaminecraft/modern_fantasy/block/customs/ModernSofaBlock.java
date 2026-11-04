
package aaaminecraft.modern_fantasy.block.customs;

import aaaminecraft.modern_fantasy.block.blockentity.ModernSofaBlockEntity;
import aaaminecraft.modern_fantasy.block.blockstate.SofaVariant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModernSofaBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final EnumProperty<SofaVariant> VARIANT =
            EnumProperty.create("variant", SofaVariant.class);

    public ModernSofaBlock(Properties properties) {
        super(properties);
        registerDefaultState(
                stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(VARIANT, SofaVariant.SINGLE)
        );
    }

    // BlockState
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, VARIANT);
    }

    // BlockEntity
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModernSofaBlockEntity(pos, state);
    }

    // 設置時のBlockStateを決定
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();

        return defaultBlockState()
                .setValue(FACING, facing)
                .setValue(VARIANT, calculateVariant(context.getLevel(), pos, facing));
    }

    // 隣接更新
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        BlockState updatedState =
                super.updateShape(state, direction, newState, level, pos, neighbourPos);

        // 水平方向の隣接変化だけを判定する
        if (direction.getAxis().isHorizontal()) {
            SofaVariant variant = calculateVariant(level, pos, updatedState.getValue(FACING));

            return updatedState.setValue(VARIANT, variant);
        }

        return updatedState;
    }

    // 外観自動判定
    private SofaVariant calculateVariant(LevelAccessor level, BlockPos pos, Direction facing) {
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        boolean hasLeft = hasAdjacentSofa(level, pos, left);
        boolean hasRight = hasAdjacentSofa(level, pos, right);

        boolean hasForward = hasAdjacentSofa(level, pos, facing);
        boolean hasBackward = hasAdjacentSofa(level, pos, facing.getOpposite());

        boolean hasLongitudinal = hasForward || hasBackward;

        if (hasLeft && hasRight) {
            return SofaVariant.CENTER;
        }

        if (hasLeft && hasLongitudinal) {
            return SofaVariant.CORNER_LEFT;
        }

        if (hasRight && hasLongitudinal) {
            return SofaVariant.CORNER_RIGHT;
        }

        if (hasLeft) {
            return SofaVariant.HALF_LEFT;
        }

        if (hasRight) {
            return SofaVariant.HALF_RIGHT;
        }

        return SofaVariant.SINGLE;
    }

    // 指定方向に別のソファがあるか判定
    private boolean hasAdjacentSofa(LevelAccessor level, BlockPos pos, Direction direction) {
        BlockPos targetPos = pos.relative(direction);
        BlockState targetState = level.getBlockState(targetPos);

        return targetState.getBlock() == this;
    }

    // 破壊処理
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    // VoxelShape
    private static final VoxelShape SHAPE_SINGLE = Shapes.or(
            Block.box(3, 1, 1, 13, 6, 13),
            Block.box(0, 1, 0, 3, 12, 13),
            Block.box(13, 1, 0, 16, 12, 13),
            Block.box(0, 1, 13, 16, 12, 16),
            Block.box(0, 0, 0, 1, 1, 1),
            Block.box(15, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 1, 1, 16),
            Block.box(15, 0, 15, 16, 1, 16)
    );
    private static final VoxelShape SHAPE_HALF_LEFT = Shapes.or(
            Block.box(0, 1, 1, 13, 6, 13),
            Block.box(0, 1, 0, 3, 12, 13),
            Block.box(0, 1, 13, 16, 12, 16),
            Block.box(0, 0, 0, 1, 1, 1),
            Block.box(15, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 1, 1, 16),
            Block.box(15, 0, 15, 16, 1, 16)
    );
    private static final VoxelShape SHAPE_HALF_RIGHT = Shapes.or(
            Block.box(3, 1, 1, 16, 6, 13),
            Block.box(13, 1, 0, 16, 12, 13),
            Block.box(0, 1, 13, 16, 12, 16),
            Block.box(0, 0, 0, 1, 1, 1),
            Block.box(15, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 1, 1, 16),
            Block.box(15, 0, 15, 16, 1, 16)
    );
    private static final VoxelShape SHAPE_CORNER_LEFT = SHAPE_HALF_LEFT;
    private static final VoxelShape SHAPE_CORNER_RIGHT = SHAPE_HALF_RIGHT;
    private static final VoxelShape SHAPE_CENTER = Shapes.or(
            Block.box(0, 1, 1, 16, 6, 13),
            Block.box(0, 1, 13, 16, 2, 16),
            Block.box(0, 0, 0, 1, 1, 1),
            Block.box(15, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 1, 1, 16),
            Block.box(15, 0, 15, 16, 1, 16)
    );

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(VARIANT)) {
            case SINGLE -> SHAPE_SINGLE;
            case HALF_LEFT -> SHAPE_HALF_LEFT;
            case HALF_RIGHT -> SHAPE_HALF_RIGHT;
            case CORNER_LEFT -> SHAPE_CORNER_LEFT;
            case CORNER_RIGHT -> SHAPE_CORNER_RIGHT;
            case CENTER -> SHAPE_CENTER;
        };

        return rotateShape(shape, state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    // VoxelShapeをFACINGに合わせて回転
    private static VoxelShape rotateShape(VoxelShape shape, Direction facing) {
        if (facing == Direction.NORTH) {
            return shape;
        }

        VoxelShape result = Shapes.empty();

        for (AABB box : shape.toAabbs()) {
            AABB rotated = switch (facing) {
                case EAST -> new AABB(
                        1.0D - box.maxZ,
                        box.minY,
                        box.minX,
                        1.0D - box.minZ,
                        box.maxY,
                        box.maxX
                );
                case SOUTH -> new AABB(
                        1.0D - box.maxX,
                        box.minY,
                        1.0D - box.maxZ,
                        1.0D - box.minX,
                        box.maxY,
                        1.0D - box.minZ
                );
                case WEST -> new AABB(
                        box.minZ,
                        box.minY,
                        1.0D - box.maxX,
                        box.maxZ,
                        box.maxY,
                        1.0D - box.minX
                );

                default -> box;
            };

            result = Shapes.or(result, Shapes.create(rotated));
        }

        return result;
    }
}
