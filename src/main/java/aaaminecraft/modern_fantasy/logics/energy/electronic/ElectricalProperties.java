package aaaminecraft.modern_fantasy.logics.energy.electronic;

import aaaminecraft.modern_fantasy.logics.energy.core.EnergyPropertyType;

/**
 * 電気エネルギーに関するProperty定義。
 *
 * <p>
 *     電気エネルギー固有の物理量を識別するための
 *     EnergyPropertyTypeをまとめて管理する。
 * </p>
 */

public class ElectricalProperties {

    private ElectricalProperties() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 電圧を表すProperty
     * {@code 単位} : V(ボルト)
     */
    public static final EnergyPropertyType<Double> VOLTAGE = new EnergyPropertyType<>("electrical:voltage", Double.class);

    /**
     * 電流を表すProperty
     * {@code 単位} : A(アンペア)
     */
    public static final EnergyPropertyType<Double> CURRENT = new EnergyPropertyType<>("electrical:current", Double.class);

    /**
     * 電力を表すProperty
     * {@code 単位} : W(ワット)
     */
    public static final EnergyPropertyType<Double> POWER = new EnergyPropertyType<>("electrical:power", Double.class);

    /**
     * 電気抵抗を表すProperty
     * {@code 単位} : Ω(オーム)
     */
    public static final EnergyPropertyType<Double> RESISTANCE = new EnergyPropertyType<>("electrical:resistance", Double.class);
}
