package aaaminecraft.modern_fantasy.logics.energy.electronic;

import aaaminecraft.modern_fantasy.logics.energy.core.*;

import java.util.Objects;

/**
 * <h1>ElectricalEnergyTransferModelクラス</h1>
 *
 * 電気エネルギー専用の転送モデル。
 *
 * <p>
 *     基本転送モデルを利用し、
 *     電気エネルギーの損失なし転送を行う。
 * </p>
 *
 * <p>
 *     将来的には電圧階級、電流、ケーブル抵抗、
 *     経路損失などを考慮した転送処理を実装する。
 * </p>
 */
public class ElectricalEnergyTransferModel implements EnergyTransferModel {

    private final BasicEnergyTransferModel basicModel = new BasicEnergyTransferModel();

    /**
     * 電気エネルギーを転送する。
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

        if (!(source.getDefinition() instanceof ElectricalEnergyDefinition))
            throw new IllegalArgumentException("Source must contain electrical energy.");
        if (target == null || !(target.getDefinition() instanceof ElectricalEnergyDefinition))
            throw new IllegalArgumentException("Target must contain electrical energy.");

        return basicModel.transfer(source, target, requestedAmount, context);
    }
}
