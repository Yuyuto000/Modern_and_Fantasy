package aaaminecraft.modern_fantasy.block.processing;

import net.minecraft.util.StringRepresentable;

public enum TablePart implements StringRepresentable {

    SINGLE,
    HALF,
    CORNER;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
