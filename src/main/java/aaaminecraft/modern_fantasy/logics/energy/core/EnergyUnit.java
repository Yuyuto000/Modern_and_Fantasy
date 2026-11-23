package aaaminecraft.modern_fantasy.logics.energy.core;

/**
 * <h1>EnergyUnitクラス</h1>
 *
 * エネルギー量を表現する単位を定義する
 *
 * @param id 単位の識別ID
 * @param symbol 単位記号
 * @param displayName 表示名称
 */
public record EnergyUnit(String id, String symbol, String displayName) {

    public EnergyUnit {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("[MF=ENERGY-016] Unit ID cannot be null or blank");
        if (symbol == null || symbol.isBlank()) throw new IllegalArgumentException("[MF=ENERGY-017] Unit symbol cannot be null or blank");
        if (displayName == null || displayName.isBlank()) throw new IllegalArgumentException("[MF=ENERGY-018] Unit display name cannot be null or blank]");
    }
}
