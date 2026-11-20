package aaaminecraft.modern_fantasy.logics.energy.core;

/**
 * <h1>EnergyPropertyTypeクラス</h1>
 *
 * <p>
 *     エネルギーごとに異なる特性を定義するための識別型。
 *     特定型の具体的な値はEnergyPropertyで管理する。
 * </p>
 *
 * @param <T> この特性が保持する値
 */
public record EnergyPropertyType<T>(String id, Class<T> valueClass) {

    /**
     * EnergyPropertyTypeを生成する。
     *
     * @param id 特性を識別する一意のID
     * @param valueClass 特性が保持する値のクラス
     * @throws IllegalArgumentException IDまたは値をのクラスが不正な場合。エラーコード:[MF-ENERGY-008],[MF-ENERGY-009]
     */
    public EnergyPropertyType{
        if (id == null || id.isBlank()) throw new IllegalArgumentException("[MF-ENERGY-008] Property type ID cannot be null or blank");
        if (valueClass == null) throw new IllegalArgumentException("[MF-ENERGY-009] Property value class cannot be null");
    }
}
