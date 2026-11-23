package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <h1>EnergyNetworkTransferServiceクラス</h1>
 *
 * エネルギーネットワークを介した転送処理を管理するサービス。
 *
 * <p>
 *     ポートの取得、転送方向の確認、ネットワーク経路の探索、
 *     エネルギー転送処理を統合する。
 * </p>
 */
public class EnergyNetworkTransferService {

    private final EnergyPortRegistry portRegistry;
    private final EnergyNetworkManager networkManager;
    private final EnergyNetworkPathfinder pathfinder;
    private final EnergyTransferService transferService;

    /**
     * コンストラクタ。
     *
     * @param portRegistry ポートレジストリ
     * @param networkManager ネットワーク管理者
     * @param pathfinder 経路探索クラス
     * @param transferService エネルギー転送サービス
     */
    public EnergyNetworkTransferService(
            EnergyPortRegistry portRegistry,
            EnergyNetworkManager networkManager,
            EnergyNetworkPathfinder pathfinder,
            EnergyTransferService transferService
    ) {
        this.portRegistry = Objects.requireNonNull(portRegistry, "Port registry cannot be null.");
        this.networkManager = Objects.requireNonNull(networkManager, "Network manager cannot be null.");
        this.pathfinder = Objects.requireNonNull(pathfinder, "Pathfinder cannot be null.");
        this.transferService = Objects.requireNonNull(transferService, "Transfer service cannot be null.");
    }

    /**
     * ネットワーク経由でエネルギーを転送する。
     *
     * @param sourcePoint 転送元ポート
     * @param destinationPoint 転送先ポート
     * @param requestedAmount 要求する転送量
     * @return 転送結果
     * @throws IllegalArgumentException ポートが未登録、転送方向が不正、同一ネットワークに存在しない、経路が存在しない場合
     * @throws IllegalStateException ポート所有ブロックがエネルギーコンテナを持たない場合
     */
    public EnergyTransferResult transfer(
            EnergyConnectionPoint sourcePoint,
            EnergyConnectionPoint destinationPoint,
            double requestedAmount
    ) {
        Objects.requireNonNull(sourcePoint, "Source point cannot be null.");
        Objects.requireNonNull(destinationPoint, "Destination point cannot be null.");

        // 転送先・転送元のポート所有ブロックを取得する
        EnergyPortAccess sourceAccess = portRegistry.get(sourcePoint);
        EnergyPortAccess destAccess = portRegistry.get(destinationPoint);

        if (sourceAccess == null || destAccess == null) throw new IllegalArgumentException("Both points must be registered.");

        // ポートの転送方向を確認する
        EnergyPort sourcePort = sourceAccess.getEnergyPort(sourcePoint);
        EnergyPort destinationPort = destAccess.getEnergyPort(destinationPoint);

        if (sourcePort == null || destinationPort == null) throw new IllegalStateException("Registered port could not be found in its owner.");
        if (!sourcePort.canExtract()) throw new IllegalArgumentException("Source port cannot extracted energy.");
        if (!destinationPort.canReceive()) throw new IllegalArgumentException("Destination port cannot receive energy.");

        // 両方のポートが同じネットワークに所属しているか確認する
        EnergyNetwork sourceNetwork = networkManager.getNetworkAt(sourcePoint);
        EnergyNetwork destNetwork = networkManager.getNetworkAt(destinationPoint);

        if (sourceNetwork == null
                || destNetwork == null
                || !sourceNetwork.getId().equals(destNetwork.getId())) {
            throw new IllegalArgumentException("Ports must belong to the same network.");
        }

        // 転送経路を探索する
        Optional<List<EnergyConnection>> path =
                pathfinder.findPath(
                        sourceNetwork,
                        sourcePoint,
                        destinationPoint
                );

        if (path.isEmpty()) throw new IllegalArgumentException("No transfer path exists between the ports.");

        // ブロックが保持する共有エネルギーコンテナを取得する
        EnergyContainer sourceContainer = sourceAccess.getEnergyContainer();
        EnergyContainer destinationContainer = destAccess.getEnergyContainer();

        if (sourceContainer == null || destinationContainer == null) throw new IllegalStateException("Both blocks must have an energy container.");

        // 経路情報を転送コンテキストに渡す
        EnergyTransferContext context = new EnergyTransferContext(path.get());

        return transferService.transfer(
                sourceContainer,
                destinationContainer,
                requestedAmount,
                context
        );
    }
}
