package aaaminecraft.modern_fantasy.block.customs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnergyCableBlock extends Block {

    private static final VoxelShape CENTER_VOXEL = Block.box(6,6,6,10,10,10);
    private static final VoxelShape NORTH_VOXEL = Block.box(6,6,0,10,10,6);
    private static final VoxelShape SOUTH_VOXEL = Block.box(6,6,10,10,10,16);
    private static final VoxelShape EAST_VOXEL = Block.box(10,6,6,16,10,10);
    private static final VoxelShape WEST_VOXEL = Block.box(0,6,6,6,10,10);
    private static final VoxelShape UP_VOXEL = Block.box(6,10,6,10,16,10);
    private static final VoxelShape DOWN_VOXEL = Block.box(6,0,6,10,6,10);

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST  = BlockStateProperties.EAST;
    public static final BooleanProperty WEST  = BlockStateProperties.WEST;
    public static final BooleanProperty UP    = BlockStateProperties.UP;
    public static final BooleanProperty DOWN  = BlockStateProperties.DOWN;

    public EnergyCableBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(NORTH, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(EAST, Boolean.FALSE)
                .setValue(WEST, Boolean.FALSE)
                .setValue(UP, Boolean.FALSE)
                .setValue(DOWN, Boolean.FALSE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }
    
    private boolean canConnect(LevelAccessor world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        return defaultBlockState()
                .setValue(NORTH, canConnect(world, pos))
                .setValue(SOUTH, canConnect(world, pos))
                .setValue(EAST, canConnect(world, pos))
                .setValue(WEST, canConnect(world, pos))
                .setValue(UP, canConnect(world, pos))
                .setValue(DOWN, canConnect(world, pos));
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {

        return switch (direction){
            case NORTH -> state.setValue(NORTH, canConnect(world, neighborPos));
            case SOUTH -> state.setValue(SOUTH, canConnect(world, neighborPos));
            case EAST -> state.setValue(EAST, canConnect(world, neighborPos));
            case WEST -> state.setValue(WEST, canConnect(world, neighborPos));
            case UP -> state.setValue(UP, canConnect(world, neighborPos));
            case DOWN -> state.setValue(DOWN, canConnect(world, neighborPos));
        };
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context){

        VoxelShape shape = CENTER_VOXEL;

        if (state.getValue(NORTH)){
            shape = Shapes.or(shape, NORTH_VOXEL);
        }

        if (state.getValue(SOUTH)){
            shape = Shapes.or(shape, SOUTH_VOXEL);
        }

        if (state.getValue(EAST)){
            shape = Shapes.or(shape, EAST_VOXEL);
        }

        if (state.getValue(WEST)){
            shape = Shapes.or(shape, WEST_VOXEL);
        }

        if (state.getValue(UP)){
            shape = Shapes.or(shape, UP_VOXEL);
        }

        if (state.getValue(DOWN)){
            shape = Shapes.or(shape, DOWN_VOXEL);
        }

        return shape;
    }

}
