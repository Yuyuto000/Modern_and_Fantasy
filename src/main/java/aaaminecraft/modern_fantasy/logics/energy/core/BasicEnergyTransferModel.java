package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.Objects;

/**
 * <h1>BasicEnergyTransferModelクラス</h1>
 *
 * 損失なしの基本エネルギー転送モデル。
 *
 * <p>
 *     転送元から転送先へ、要求量と双方の容量に応じて
 *     エネルギーを転送する。
 * </p>
 *
 * <p>
 *     転送情報は使用せず、直接転送を基本とする。
 * </p>
 */
public class BasicEnergyTransferModel implements  EnergyTransferModel {

    /**
     * エネルギーを転送する。
     *
     * @param source 転送元コンテナ
     * @param target 転送先コンテナ
     * @param requestedAmount 要求する転送量
     * @param context 転送経路などの追加情報
     * @return 転送結果
     *
     * @throws NullPointerException 引数がnullの場合
     * @throws IllegalArgumentException 要求量がnullや負数・非有限値などの不正な場合または対応しないエネルギーの場合
     */
    @Override
    public EnergyTransferResult transfer(
            EnergyContainer source,
            EnergyContainer target,
            double requestedAmount,
            EnergyTransferContext context
    ) {
        Objects.requireNonNull(source, "Source cannot be null.");
        Objects.requireNonNull(target, "Target cannot be null.");
        Objects.requireNonNull(context, "Context cannot be null.");

        if (!Double.isFinite(requestedAmount) || requestedAmount < 0) throw new IllegalArgumentException("Requested amount must be finite and non-negative.");
        if (source.getDefinition().getClass()
        != target.getDefinition().getClass()) throw new IllegalArgumentException("Source definition are incompatible.");
        if (source == target || requestedAmount == 0) {
            return new EnergyTransferResult(requestedAmount, 0, 0, 0);
        }

        double transferable = Math.min(
                requestedAmount,
                Math.min(source.getAmount(), target.getRemainingCapacity())
        );
        double received = target.receive(
                new EnergyAmount(source.getDefinition(), transferable)
        );
        double extracted = source.extract(received);

        return new EnergyTransferResult(requestedAmount, extracted, received, extracted - received);
    }
}
