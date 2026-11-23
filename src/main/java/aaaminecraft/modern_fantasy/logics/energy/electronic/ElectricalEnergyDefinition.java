package aaaminecraft.modern_fantasy.logics.energy.electronic;

import aaaminecraft.modern_fantasy.logics.energy.core.EnergyDefinition;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyProperty;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyUnit;

/**
 * <h1>ElectricalEnergyDefinition</h1>
 *
 * 電気エネルギーの定義クラス
 *
 * <p>
 *     SI単位系に準拠し、エネルギー量の単位として
 *     ジュール(J)を使用する。
 * </p>
 *
 * <p>
 *     電気エネルギーに関連する物理特性を登録・管理する。
 * </p>
 */
public class ElectricalEnergyDefinition extends EnergyDefinition {

    /**
     * 電気エネルギーの単位定義。
     * SI単位系におけるエネルギーの単位はジュール(J)。
     */
    public static final EnergyUnit UNIT = new EnergyUnit("si:joule", "J","ジュール");

    /**
     * 電気エネルギー定義を生成する
     */
    public ElectricalEnergyDefinition(){
        super("Electronical Energy", UNIT);

        registerProperty(new EnergyProperty<>(ElectricalProperties.VOLTAGE, 0.0));
        registerProperty(new EnergyProperty<>(ElectricalProperties.CURRENT, 0.0));
        registerProperty(new EnergyProperty<>(ElectricalProperties.POWER, 0.0));
        registerProperty(new EnergyProperty<>(ElectricalProperties.RESISTANCE, 0.0));

    }
}
