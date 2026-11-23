package aaaminecraft.modern_fantasy.logics.energy.core;

/**
 * <h1>EnergyTransferクラス</h1>
 *
 * <p>
 *     異なるEnergyContainer間でエネルギーを移動させる。
 *     エネルギーの種類が一致している場合のみ転送できる。
 *     現段階では伝達損失を考慮しない。
 * </p>
 */
public final class EnergyTransfer {

    private EnergyTransfer() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * エネルギーを別のコンテナへ転送する。
     *
     * @param source 転送元コンテナ
     * @param target 転送先コンテナ
     * @param requestedAmount 要求する転送量
     * @return 実際に転送されたエネルギー量
     *
     * @throws IllegalArgumentException 引数がnull,要求量が負数・非有限値の場合、またはエネルギーの種類が異なる場合
     */
    public static double transfer(
            EnergyContainer source,
            EnergyContainer target,
            double requestedAmount
    ) {
        if (source == null || target == null) throw new IllegalArgumentException("[MF-ENERGY-031] Source and target cannot be null.");
        if (!Double.isFinite(requestedAmount) || requestedAmount < 0.0) throw new IllegalArgumentException("[MF-ENERGY-032] Requested transfer amount must be finite and non-negative.");
        if (!source.getDefinition().getClass().equals(
                target.getDefinition().getClass()
        )) throw new IllegalArgumentException("[MF-ENERGY-033] Cannot transfer between different energy type.");

        // 同じコンテナへの輸送は行わない。
        if (source == target) return 0.0;

        // 転送可能な最大容量を計算する。
        double transferableAmount = Math.min(
                requestedAmount,
                Math.min(source.getAmount(), target.getRemainingCapacity())
        );

        if (transferableAmount <= 0.0) return 0.0;

        // 転送先へエネルギーを受け入れる。
        double receivedAmount = target.receive(
                new EnergyAmount(source.getDefinition(), transferableAmount)
        );

        // 実際に受け入れられた量だけ転送先から取り出す。
        source.extract(receivedAmount);

        return receivedAmount;
    }
}
