package aaaminecraft.modern_fantasy.logics.energy.core;

/**
 * <h1>EnergyPortAccess</h1>
 *
 * エネルギーポートとエネルギーコンテナへのアクセスを提供するインタフェース。
 *
 * <p>
 *     このインタフェースを実装するBlockEntityは、
 *     自身のエネルギーポートと共有エネルギーコンテナを公開する。
 * </p>
 */
public interface EnergyPortAccess {

    /**
     * エネルギーポート管理者を取得する。
     *
     * @return エネルギーポート管理者
     */
    EnergyPortManager getEnergyPortManager();

    /**
     * エネルギーコンテナを取得する。
     *
     * @return エネルギーコンテナ。エネルギーを保持しない場合はnull
     */
    EnergyContainer getEnergyContainer();

    /**
     * 指定した接続位置のエネルギーポートを取得する。
     *
     * @param point 接続位置
     * @return エネルギーポート
     */
    default EnergyPort getEnergyPort(EnergyConnectionPoint point) {
        return getEnergyPortManager().get(point);
    }

    /**
     * 指定した接続位置が入力可能か確認する。
     *
     * @param point 接続位置
     * @return 入力可能ならtrue
     */
    default boolean canReceiveEnergy(EnergyConnectionPoint point) {
        EnergyPort port = getEnergyPort(point);

        return port != null && port.canReceive() && getEnergyContainer() != null;
    }

    /**
     * 指定した接続位置が出力可能か確認する。
     *
     * @param point 接続位置
     * @return 出力可能ならtrue
     */
    default boolean canExtractEnergy(EnergyConnectionPoint point) {
        EnergyPort port = getEnergyPort(point);

        return port != null && port.canExtract() && getEnergyContainer() != null;
    }
}
