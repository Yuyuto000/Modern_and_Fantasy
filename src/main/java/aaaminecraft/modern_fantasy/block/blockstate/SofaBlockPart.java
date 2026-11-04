package aaaminecraft.modern_fantasy.block.blockstate;

import net.minecraft.util.StringRepresentable;

public enum SofaBlockPart implements StringRepresentable {
    HEAD("head"),
    FOOT("foot");

    private final String name;

    SofaBlockPart(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}