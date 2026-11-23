package aaaminecraft.modern_fantasy.logics.energy.core;

/**
 * <h1>EnergyTransferResultクラス</h1>
 *
 * エネルギー転送の結果を表すクラス。
 *
 * <p>
 *     転送要求量、転送元から取り出された量、
 *     転送先に到達した量、および伝達損失を管理する。
 * </p>
 * @param requestedAmount 要求された転送量
 * @param extractedAmount 転送元から取り出された量
 * @param receivedAmount 転送先に到達した量
 * @param lossAmount 伝達によって失われた量
 */
public record EnergyTransferResult(
        double requestedAmount,
        double extractedAmount,
        double receivedAmount,
        double lossAmount
) {

    /**
     * 転送結果を生成する。
     *
     * @throws IllegalArgumentException 各数値が負数・非有限値の場合、または転送量の整合性が取れない場合
     */
    public EnergyTransferResult {
        if (!isValidAmount(requestedAmount)
        || !isValidAmount(extractedAmount)
        || !isValidAmount(receivedAmount)
        || !isValidAmount(lossAmount)) throw new  IllegalArgumentException("[MF-ENERGY-034] Transfer amounts must be finite and non-negative");

        double tolerance = Math.max(1.0, extractedAmount) * 1.0E-9;

        if (extractedAmount > requestedAmount +  tolerance)
            throw new IllegalArgumentException("[MF=ENERGY-035] Extracted amount cannot exceed requested amount.");
        if (receivedAmount > extractedAmount + tolerance)
            throw new IllegalArgumentException("[MF-ENERGY-036] Received amount cannot exceed extracted amount.");
        if (Math.abs(extractedAmount - receivedAmount - lossAmount) > tolerance)
            throw new IllegalArgumentException("[MF-ENERGY-037] Transfer loss does not match the extracted and received amounts.");
    }

    /**
     * 転送が成功したか判定する。
     *
     * @return 転送先にエネルギーが到達した場合true
     */
    public boolean isSuccessful() {
        return receivedAmount > 0.0;
    }

    /**
     * 要求された転送量を全て取り出せたか判定する。
     *
     * @return 要求量と取り出し量がほぼ等しい場合true
     */
    public boolean isFullyExtracted() {
        return Math.abs(requestedAmount - extractedAmount)
                <= Math.max(1.0, requestedAmount) * 1.0E-9;
    }

    /**
     * 転送損失が発生したか判定する。
     *
     * @return 損失が発生した場合true
     */
    public boolean hasLoss() {
        return lossAmount > 0.0;
    }

    /**
     * 数値が有限かつ非負であることを検証する。
     *
     * @param value 検証対象の数値
     * @return 有効な場合true
     */
    private static boolean isValidAmount(double value) {
        return Double.isFinite(value) && value >= 0.0;
    }
}
