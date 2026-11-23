package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.Objects;

/**
 * <h1>EnergyPortクラス</h1>
 *
 * <p>
 *     ポートは接続位置とエネルギーの転送方向を管理する。
 *     エネルギー自体は保持せず、BlockEntity側の
 *     EnergyContainerを共有して利用する。
 * </p>
 */
public record EnergyPort(
        EnergyConnectionPoint point,
        Direction direction
) {
    /**
     * ポートの転送方向
     */
    public enum Direction {

        /** エネルギー入力専用 */
        INPUT,

        /** エネルギー出力専用 */
        OUTPUT,

        /** 入出力の両方に対応 */
        BIDIRECTIONAL
    }

    /**
     * コンストラクタ。
     *
     * @throws NullPointerException 引数がnullの場合
     */
    public EnergyPort {
        Objects.requireNonNull(point, "Connection point cannot be null.");
        Objects.requireNonNull(direction, "Direction cannot be null.");
    }

    /**
     * このポートがエネルギーを受け入れられるか確認する。
     *
     * @return 入力可能ならtrue
     */
    public boolean canReceive() {
        return direction == Direction.INPUT || direction == Direction.BIDIRECTIONAL;
    }

    /**
     * このポートからエネルギーを送出できるか確認する。
     *
     * @return 出力可能ならtrue
     */
    public boolean canExtract() {
        return direction == Direction.OUTPUT ||  direction == Direction.BIDIRECTIONAL;
    }
}
