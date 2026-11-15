package aaaminecraft.modern_fantasy.logics.energy.core;

import net.minecraft.core.BlockPos;

public record EnergyConnectionPoint(
        BlockPos position,
        int index
) implements Comparable<EnergyConnectionPoint>{
    /**
     * <h1>接続口の情報を生成する</h1>
     * <p>
     *     BlockPos.Mutableなどの可変座標が渡された場合に備え、
     *     不変のBlockPosとして保持する
     * </p>
     */
    public EnergyConnectionPoint {
        if (position == null){
            throw new IllegalArgumentException("[Modern Fantasy System FATAL] Connection position cannot be null");
        }

        if (index < 0){
            throw new IllegalArgumentException("[Modern Fantasy System FATAL] Connection index cannot be negative");
        }

        position = position.immutable();
    }

    /**
     * <h1>接続点を追加する</h1>
     * <p>
     *     まず座標を比較し、同じ座標なら接続口IDを比較する。
     *     TreeSetなどで順序を安定させるために使用。
     * </p>
     */
    @Override
    public int compareTo(EnergyConnectionPoint other) {
        int positionCompare = position.compareTo(other.position);
        if (positionCompare != 0){
            return positionCompare;
        }

        return Integer.compare(this.index, other.index);
    }

}
