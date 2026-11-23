package aaaminecraft.modern_fantasy.logics.energy.magic;

import aaaminecraft.modern_fantasy.logics.energy.core.EnergyPropertyType;

/**
 * <h1>MagicPropertiesクラス</h1>
 *
 * 魔法エネルギーに関するProperty定義
 *
 * <p>
 *     魔子及びマジカニウムの理論に基づく
 *     魔法エネルギー固有の特性を識別する。
 * </p>
 */
public class MagicProperties {

    private MagicProperties() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 魔法エネルギーの保有可能容量。
     * マジカニウムが保持できる魔法エネルギーの上限を表す。
     * {@code 単位} : MU
     */
    public static final EnergyPropertyType<Double> CAPACITY =
            new EnergyPropertyType<>("magic:capacity", Double.class);

    /**
     * 魔法のエネルギーの反応状態。
     * 魔法エネルギーが現在どのような反応状態にあるかを表す。
     */
    public static final EnergyPropertyType<String> REACTION_STATE =
            new EnergyPropertyType<>("magic:reaction_state", String.class);

    /**
     * 魔法エネルギーの集積効率。
     * マジカニウムが周囲の魔子を集積する性質を
     * 数値化したもの。
     * 単位は独自係数のため参照なし。
     */
    public static final EnergyPropertyType<Double> ACCUMULATION_RATE =
            new EnergyPropertyType<>("magic:accumulation_rate", Double.class);

    /**
     * 魔法エネルギーの放出閾値。
     * 魔術現象の発生に関係するエネルギー量の閾値。
     * {@code 単位} : MU
     */
    public static final EnergyPropertyType<Double> RELEASE_THRESHOLD =
            new EnergyPropertyType<>("magic:release_threshold", Double.class);

    /**
     * 魔法エネルギーと科学エネルギーの自然変換効率。
     * 機械的な変換効率ではなく、自然環境下での共通可変数である。
     * 機械的な変換効率は機械別に定義される。
     * {@code 範囲} : 0.0 ~ 1.0
     */
    public static final EnergyPropertyType<Double> NATURAL_CONVERSION_EFFICIENCY =
            new EnergyPropertyType<>("magic:natural_conversion_efficiency", Double.class);
}
