package aaaminecraft.modern_fantasy.logics.energy.core;

/**
 * <h1>EnergyPropertyクラス</h1>
 *
 * エネルギーが持つ固有特性と、その値を保持するクラス。
 * <p>
 *     EnergyPropertyTypeによって指定された型に従い、
 *     特定の値の型安全に管理する。
 * </p>
 *
 * @param <T> 特定型の型
 */
public record EnergyProperty<T>(EnergyPropertyType<T> type, T value) {

    /**
     * EnergyPropertyを生成する。
     *
     * @param type 特性の種類と値の型を定義する識別型
     * @param value 特性の値
     *
     * @throws IllegalArgumentException typeとvalueがnullの場合、あるいはvalueが指定された型と一致しない場合
     */
    public EnergyProperty{
        if (type == null) throw new IllegalArgumentException("[MF-ENERGY-010] Property type cannot be null");
        if (value == null) throw new IllegalArgumentException("[MF-ENERGY-011] Property value cannot be null");
        if (!type.valueClass().isInstance(value)) throw new IllegalArgumentException("[MF-ENERGY-012] Property value does not match the specified type");
    }

    /**
     * 特性の種類を取得する。
     *
     * @return 特性の種類
     */
    public EnergyPropertyType<T> getType() {
        return type;
    }

    /**
     * 特性の値を取得する。
     *
     * @return 特性値
     */
    public T getValue() {
        return value;
    }
}
