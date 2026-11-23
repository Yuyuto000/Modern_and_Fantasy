package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * エネルギーポートとネットワークを総合的に制御するクラス。
 *
 * <p>
 *     ポートの登録・解除、ネットワークの接続・切断を管理する。
 * </p>
 */
public class EnergyNetworkController {

    private final EnergyPortRegistry portRegistry;
    private final EnergyNetworkManager manager;

    /**
     * コンストラクタ。
     *
     * @param portRegistry ポートレジストリ
     * @param manager ネットワーク管理者
     */
    public EnergyNetworkController(EnergyPortRegistry portRegistry, EnergyNetworkManager manager) {
        this.portRegistry = Objects.requireNonNull(portRegistry, "Port registry cannot be null");
        this.manager = Objects.requireNonNull(manager, "Network manager cannot be null");
    }

    /**
     * ブロックのポートを登録する。
     *
     * @param access ポートを公開するブロック
     */
    public void registerBlock(EnergyPortAccess access) {
        Objects.requireNonNull(access, "Port access cannot be null");
        portRegistry.register(access);
    }

    /**
     * ブロックのポートを登録解除し、関連するネットワーク接続を削除する。
     *
     * @param access 登録解除するブロック
     * @return 1つ以上のポートを解除した場合true
     */
    public boolean unregisterBlock(EnergyPortAccess access) {
        Objects.requireNonNull(access, "Port access cannot be null");

        Set<EnergyConnection> connectionsToRemove = new HashSet<>();

        // ブロックが持つ各ポートの接続を収集する
        for (EnergyPort port : access.getEnergyPortManager().getAll()){
            EnergyNetwork network = manager.getNetworkAt(port.point());
            if (network == null) continue;

            connectionsToRemove.addAll(network.getConnections(port.point()));
        }

        // 収集したネットワークから削除する
        for (EnergyConnection connection : connectionsToRemove){
            manager.removeConnection(connection);
        }

        return portRegistry.unregister(access);
    }

    /**
     * 2つのポートをネットワーク接続する。
     *
     * @param connection 接続情報
     * @throws IllegalArgumentException 接続先が未登録の場合
     */
    public void connect(EnergyConnection connection) {
        Objects.requireNonNull(connection, "Connection cannot be null");
        if (!portRegistry.contains(connection.endA()) || !portRegistry.contains(connection.endB())){
            throw new IllegalArgumentException("Both connection points must be registered.");
        }

        manager.addConnection(connection);
    }

    /**
     * 2つのポート間の接続を解除する。
     *
     * @param connection 接続情報
     * @return 接続を解除できた場合true
     */
    public boolean disconnect(EnergyConnection connection) {
        Objects.requireNonNull(connection, "Connection cannot be null");

        EnergyNetwork network = manager.getNetworkAt(connection.endA());
        if (network == null || !network.containsConnection(connection)) return false;

        manager.removeConnection(connection);
        return true;
    }

    /**
     * ポートレジストリを取得する。
     *
     * @return ポートレジストリ
     */
    public EnergyPortRegistry getPortRegistry() {
        return portRegistry;
    }

    /**
     * ネットワーク管理者を取得する。
     *
     * @return ネットワーク管理者
     */
    public EnergyNetworkManager getNetworkManager() {
        return manager;
    }
}
