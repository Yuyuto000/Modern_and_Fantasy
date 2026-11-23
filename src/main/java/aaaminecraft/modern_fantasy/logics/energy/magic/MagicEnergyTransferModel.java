package aaaminecraft.modern_fantasy.logics.energy.magic;

import aaaminecraft.modern_fantasy.logics.energy.core.*;

import java.util.Objects;

/**
 * <h1>MagicEnergyTransferModelクラス</h1>
 *
 * 魔法エネルギー専用の転送モデル。
 *
 * <p>
 *     現段階では基本転送モデルを利用し、
 *     魔法エネルギーの損失なし転送を行う。
 * </p>
 *
 * <p>
 *     将来的には魔法エネルギーの反応状態や蓄積量、
 *     媒体の性質などを考慮した転送処理を実装する。
 * </p>
 */
public class MagicEnergyTransferModel implements EnergyTransferModel {

    private final BasicEnergyTransferModel basicModel = new BasicEnergyTransferModel();

    /**
     * 魔法エネルギーを転送する。
     *
     * @param source          転送元コンテナ
     * @param target          転送先コンテナ
     * @param requestedAmount 要求する転送量
     * @param context         転送経路などの追加情報
     * @return 転送結果
     * @throws NullPointerException 引数がnullの場合
     * @throws IllegalArgumentException 引数が不正な場合、または対応しないエネルギーの場合
     */
    @Override
    public EnergyTransferResult transfer(
            EnergyContainer source,
            EnergyContainer target,
            double requestedAmount,
            EnergyTransferContext context
    ) {
        Objects.requireNonNull(source, "Source cannot be null");

        if (!(source.getDefinition() instanceof MagicEnergyDefinition)) throw new IllegalArgumentException("Source must contain magic energy");
        if (target == null || !(target.getDefinition() instanceof MagicEnergyDefinition)) throw new IllegalArgumentException("Target must contain magic energy");

        return basicModel.transfer(source, target, requestedAmount, context);
    }
}
