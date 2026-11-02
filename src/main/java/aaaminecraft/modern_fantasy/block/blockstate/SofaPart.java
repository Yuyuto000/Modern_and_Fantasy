package aaaminecraft.modern_fantasy.block.blockstate;

import net.minecraft.util.StringRepresentable;

public enum SofaPart implements StringRepresentable {

    SINGLE("single"),
    HALF_LEFT("half_left"),
    HALF_RIGHT("half_right"),
    CORNER_LEFT("corner_left"),
    CORNER_RIGHT("corner_right"),
    CENTER("center");

    private final String name;

    SofaPart(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
