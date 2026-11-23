package aaaminecraft.modern_fantasy.logics.energy.magic;

import aaaminecraft.modern_fantasy.logics.energy.core.EnergyDefinition;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyProperty;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyUnit;

/**
 * <h1>MagicEnergyDefinitionクラス</h1>
 *
 * <p>
 *     魔子とマジカニウムの相互作用によって発生する
 *     独立したエネルギー形態を定義する。
 * </p>
 *
 * <p>
 *     エネルギー量の単位には独自単位である
 *     Magic Unit (MU)を使用する。
 * </p>
 *
 * <p>
 *     魔子の個別シミュレーションは行わず、
 *     魔法エネルギーの数値によってその状態を表現する。
 * </p>
 */
public class MagicEnergyDefinition extends EnergyDefinition {

    /**
     * 魔法エネルギーの単位定義
     */
    public static final EnergyUnit UNIT = new EnergyUnit("magic:magic_unit","MU","マジックユニット");

    /**
     * 魔法エネルギー定義を生成する。
     */
    public MagicEnergyDefinition() {
        super("Magic Energy", UNIT);

        registerProperty(new EnergyProperty<>(MagicProperties.CAPACITY, 0.0));
        registerProperty(new EnergyProperty<>(MagicProperties.REACTION_STATE, "normal"));
        registerProperty(new EnergyProperty<>(MagicProperties.ACCUMULATION_RATE, 0.0));
        registerProperty(new EnergyProperty<>(MagicProperties.RELEASE_THRESHOLD, 0.0));
        registerProperty(new EnergyProperty<>(MagicProperties.NATURAL_CONVERSION_EFFICIENCY, 1.0));
    }
}
