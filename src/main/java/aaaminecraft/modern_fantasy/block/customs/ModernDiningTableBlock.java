package aaaminecraft.modern_fantasy.block.customs;

import net.minecraft.world.level.block.Block;

public class ModernDiningTableBlock extends Block {

    private final int width;
    private final int height;

    public ModernDiningTableBlock(Properties properties, int width, int height) {
        super(properties);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
