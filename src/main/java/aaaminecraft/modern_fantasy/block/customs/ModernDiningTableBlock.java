package aaaminecraft.modern_fantasy.block.customs;

import aaaminecraft.modern_fantasy.block.processing.TablePart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        TablePart part = determinePart(level, pos);
        return  this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(PART, part);
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

    private boolean isSameTable(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() == this;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean flag) {
        super.neighborChanged(state, level, pos, block, fromPos, flag);

        if (!level.isClientSide) {
            BlockState newState = state.setValue(PART, TablePart.SINGLE);
            if (!newState.equals(state)) {
                level.setBlock(pos, newState, Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
            }
        }
    }
}
