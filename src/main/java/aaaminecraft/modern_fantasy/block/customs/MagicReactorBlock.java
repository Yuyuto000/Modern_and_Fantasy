package aaaminecraft.modern_fantasy.block.customs;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class MagicReactorBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public MagicReactorBlock(Properties properties) {
        super(properties);
        registerDefaultState(
                this.stateDefinition.any().setValue(LIT, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}