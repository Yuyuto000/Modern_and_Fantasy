package aaaminecraft.modern_fantasy.logics.energy.core;

/**
 * <h1>EnergyTransfer</h1>
 *
 * <p>
 *     エネルギーの種類ごとに異なる伝達方式を実装するための
 *     共通インタフェースとして機能する。
 * </p>
 *
 * <p>
 *     電気エネルギーや魔法エネルギーなど、
 *     各エネルギー固有の伝達計算は実装クラスに委ねる。
 * </p>
 */
public interface EnergyTransferModel {

    /**
     * エネルギーを転送する。
     *
     * <p>
     *    転送元からエネルギーを取り出し、
     *    転送先へ伝達する。
     *    伝達損失や固有の制約は実装クラスが処理する。
     * </p>
     *
     * @param source 転送元コンテナ
     * @param target 転送先コンテナ
     * @param requestedAmount 要求する転送量
     * @return 転送結果
     *
     * @throws IllegalArgumentException 引数が不正な場合、または対応しないエネルギーの場合
     */
    EnergyTransferResult transfer(
            EnergyContainer source,
            EnergyContainer target,
            double requestedAmount
    );
}
