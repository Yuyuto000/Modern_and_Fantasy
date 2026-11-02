package aaaminecraft.modern_fantasy.block.customs;

import aaaminecraft.modern_fantasy.block.blockstate.TablePart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModernDiningTableBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<TablePart> PART = EnumProperty.create("part", TablePart.class);

    public ModernDiningTableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(PART, TablePart.SINGLE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, PART);
    }

    // 天板
    private static final VoxelShape TABLE_TOP = Block.box(0, 14, 0, 16, 16, 16);
    // 脚
    private static final VoxelShape LEG_NW = Block.box(1, 0, 1, 3, 14, 3);
    private static final VoxelShape LEG_NE = Block.box(13, 0, 1, 15, 14, 3);
    private static final VoxelShape LEG_SW = Block.box(1, 0, 13, 3, 14, 15);
    private static final VoxelShape LEG_SE = Block.box(13, 0, 13, 15, 14, 15);

    // Single：脚4本
    private static final VoxelShape SHAPE_SINGLE = Shapes.or(TABLE_TOP, LEG_NW, LEG_NE, LEG_SW, LEG_SE);

    // Half：脚2本
    private static final VoxelShape SHAPE_HALF = Shapes.or(TABLE_TOP, LEG_NW, LEG_SW);

    // Corner：脚1本
    private static final VoxelShape SHAPE_CORNER = Shapes.or(TABLE_TOP, LEG_NW);

    private BlockState calculateTableState(Level level, BlockPos pos, BlockState state) {
        boolean north = isSameTable(level, pos.north());
        boolean east  = isSameTable(level, pos.east());
        boolean south = isSameTable(level, pos.south());
        boolean west  = isSameTable(level, pos.west());

        int count = 0;
        if (north) count++;
        if (east) count++;
        if (south) count++;
        if (west) count++;

        // 単独
        if (count == 0) {
            return state.setValue(PART, TablePart.SINGLE);
        }

        // 1×2の端
        if (count == 1) {
            Direction rotation;

            if (north) rotation = Direction.WEST;
            else if (east) rotation = Direction.NORTH;
            else if (south) rotation = Direction.EAST;
            else rotation = Direction.SOUTH;

            return state
                    .setValue(PART, TablePart.HALF)
                    .setValue(FACING, rotation);
        }

        // 2×2の角
        if (count == 2) {
            Direction rotation = null;

            if (north && east) {
                rotation = Direction.WEST;
            } else if (east && south) {
                rotation = Direction.NORTH;
            } else if (south && west) {
                rotation = Direction.EAST;
            } else if (west && north) {
                rotation = Direction.SOUTH;
            }

            if (rotation != null) {
                return state
                        .setValue(PART, TablePart.CORNER)
                        .setValue(FACING, rotation);
            }
        }

        // 想定外の接続
        return state.setValue(PART, TablePart.SINGLE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(PART, TablePart.SINGLE);

        return calculateTableState(
                context.getLevel(),
                context.getClickedPos(),
                state
        );
    }

    private TablePart determinePart(Level level, BlockPos pos) {
        boolean north = isSameTable(level, pos.north());
        boolean east = isSameTable(level, pos.east());
        boolean south = isSameTable(level, pos.south());
        boolean west = isSameTable(level, pos.west());
        int count = 0;

        if (north) count++;
        if (east) count++;
        if (south) count++;
        if (west) count++;

        // 周囲に1個もない
        if (count == 0) {
            return TablePart.SINGLE;
        }

        // 1個だけ接続
        if (count == 1) {
            return TablePart.HALF;
        }

        // 2個が直角方向
        if (count == 2) {

            if ((north && east)
                    || (east && south)
                    || (south && west)
                    || (west && north)) {

                return TablePart.CORNER;
            }
        }

        // それ以外は2×2を超えるので単独扱い
        return TablePart.SINGLE;
    }

    private static VoxelShape rotateShape(VoxelShape shape, Direction direction) {
        VoxelShape result = Shapes.empty();

        for (AABB box : shape.toAabbs()) {
            double minX = box.minX;
            double minY = box.minY;
            double minZ = box.minZ;
            double maxX = box.maxX;
            double maxY = box.maxY;
            double maxZ = box.maxZ;

            AABB rotated;

            switch (direction) {
                case EAST -> rotated = new AABB(
                        minZ, minY, 1.0 - maxX,
                        maxZ, maxY, 1.0 - minX
                );

                case SOUTH -> rotated = new AABB(
                        1.0 - maxX, minY, 1.0 - maxZ,
                        1.0 - minX, maxY, 1.0 - minZ
                );

                case WEST -> rotated = new AABB(
                        1.0 - maxZ, minY, minX,
                        1.0 - minZ, maxY, maxX
                );

                default -> rotated = box;
            }

            result = Shapes.or(result, Shapes.create(rotated));
        }

        return result;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape baseShape = switch (state.getValue(PART)) {
            case SINGLE -> SHAPE_SINGLE;
            case HALF -> SHAPE_HALF;
            case CORNER -> SHAPE_CORNER;
        };

        return rotateShape(baseShape, state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    private boolean isSameTable(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() == this;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean flag) {
        super.neighborChanged(state, level, pos, block, fromPos, flag);

        if (!level.isClientSide) {
            BlockState newState = calculateTableState(level, pos, state);

            if (!newState.equals(state)) {
                level.setBlock(pos, newState, Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
            }
        }
    }
}
