
package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.Objects;

/**
 * <h1>EnergyConnection</h1>
 *
 * <p>
 * 2つのEnergyConnectionPointを結び、
 * エネルギーネットワークグラフの辺（Edge）として機能する。
 * </p>
 *
 * <p>
 * このクラスは接続関係のみを保持する。
 * エネルギーの伝達や損失計算は担当しない。
 * </p>
 *
 * @param endA 接続の一方の端点。null不可。
 * @param endB 接続のもう一方の端点。null不可。
 */
public record EnergyConnection(
        EnergyConnectionPoint endA,
        EnergyConnectionPoint endB
) {

    /**
     * 接続情報を生成する。
     *
     * @throws IllegalArgumentException
     *         いずれかの接続点がnullの場合、
     *         または両端点が同一の場合。
     */
    public EnergyConnection {
        if (endA == null || endB == null) throw new IllegalArgumentException("[MF-ENERGY-001] Connection endpoint cannot be null.");
        if (endA.equals(endB)) throw new IllegalArgumentException("[MF-ENERGY-002] Connection endpoints cannot be identical.");
    }

    /**
     * 指定された接続点から反対側の接続点を取得する。
     *
     * <p>
     * endAが指定された場合はendBを、
     * endBが指定された場合はendAを返す。
     * </p>
     *
     * @param point 基準となる接続点。
     *              この接続のendAまたはendBである必要がある。
     *
     * @return 指定された接続点とは反対側の接続点。
     *
     * @throws IllegalArgumentException
     *         指定された接続点がこの接続に含まれていない場合。
     */
    public EnergyConnectionPoint getOtherEnd(EnergyConnectionPoint point) {
        if (endA.equals(point)) {
            return endB;
        }

        if (endB.equals(point)) {
            return endA;
        }

        throw new IllegalArgumentException("[MF-ENERGY-003] The specified point is not part of this connection.");
    }

    /**
     * 指定された接続点が、この接続の端点か判定する。
     *
     * @param point 判定対象の接続点。
     *
     * @return 端点である場合はtrue、それ以外はfalse。
     */
    public boolean isEnd(EnergyConnectionPoint point) {
        return endA.equals(point) || endB.equals(point);
    }

    /**
     * 接続方向を考慮せず、両端点が一致すれば同一接続とする。
     *
     * @param obj 比較対象のオブジェクト。
     *
     * @return 同一の接続であればtrue。
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EnergyConnection other)) return false;

        return (endA.equals(other.endA) && endB.equals(other.endB)) || (
                endA.equals(other.endB) && endB.equals(other.endA));
    }

    /**
     * 接続順序に依存しないハッシュ値を生成する。
     *
     * @return 両端点のハッシュ値を組み合わせた値。
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(endA) + Objects.hashCode(endB);
    }
}
