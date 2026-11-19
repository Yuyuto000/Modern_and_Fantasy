package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.Set;
import java.util.UUID;

/**
 * <h1>EnergyNetworkクラス</h1>
 *
 * <p>
 *     1つの独立したエネルギーネットワークを表現するクラス。
 * </p>
 *
 * <p>
 *     ネットワークのUUIDと接続グラフを保持し、
 *     ネットワークの構造変更を管理する。
 * </p>
 *
 * <p>
 *     エネルギーの蓄積、伝達、配分などの処理は担当しない。
 *     それらはエネルギー固有のシステムの責務である。
 * </p>
 *
 * @see EnergyNetworkGraph
 */
public class EnergyNetwork {

    /**
     * ネットワークを一意に識別するUUID。
     * ネットワーク生成時に発行し、保存・復元時には同じUUIDを引き継ぐ。
     */
    private final UUID id;

    /**
     * ネットワークの接続構造を管理するグラフ。
     */
    private final EnergyNetworkGraph graph;

    /**
     * ネットワークの構造変更回数。
     * 接続の追加・削除が成功するたびに増加する。
     */
    private long revision;

    /**
     * 新しいネットワークを生成する。
     *
     * <p>
     *     UUIDは自動生成される。
     * </p>
     */
    public EnergyNetwork() {
        this(UUID.randomUUID());
    }

    /**
     * 指定されたUUIDを持つネットワークを生成する。
     *
     * <p>
     *     主に保存されたネットワークの復元時に使用する。
     * </p>
     *
     * @param id ネットワークに割り当てるUUID。null不可。
     *
     * @throws IllegalArgumentException UUIDがnullの場合。エラーコード:[MF-ENERGY-005]
     */
    public EnergyNetwork(UUID id){
        if (id == null) throw new IllegalArgumentException("[MF-ENERGY-005] Network ID cannot be null.");

        this.id = id;
        this.graph = new EnergyNetworkGraph();
        this.revision = 0;
    }

    /**
     * ネットワークのUUIDを取得する。
     * @return ネットワーク固有のUUID。
     */
    public UUID getId() {
        return id;
    }

    /**
     * ネットワークの更新回数を取得する。
     *
     * @return 現在の更新回数。
     */
    public long getRevision() {
        return revision;
    }

    /**
     * ネットワークに接続を追加する。
     *
     * <p>
     *     追加に成功した場合のみrevision値を増加させる。
     * </p>
     *
     * @param connection 追加する情報。null不可。
     *
     * @return 追加にせいこうした場合true。
     *
     * @throws IllegalArgumentException connectionがnullの場合。エラーコード:[MF-ENERGY-004]
     */
    public boolean addConnection(EnergyConnection connection) {
        boolean add = graph.addConnection(connection);

        if (add) revision++;
        return add;
    }

    /**
     * ネットワークから接続を削除する。
     *
     * <p>
     *     削除に成功した場合のみrevision値を増加させる。
     * </p>
     *
     * @param connection 削除する接続情報。null不可。
     *
     * @return 削除に成功した場合true。
     *
     * @throws IllegalArgumentException connectionがnullの場合。エラーコード:[MF-ENERGY-004]
     */
    public boolean removeConnection(EnergyConnection connection) {
        boolean remove = graph.removeConnection(connection);

        if (remove) revision++;
        return remove;
    }

    /**
     * 指定された接続点に接続されている接続情報を取得する。
     *
     * @param point 検索対象の接続点。
     *
     * @return 接続情報のSet。
     */
    public Set<EnergyConnection> getConnections(EnergyConnectionPoint point) {
        return graph.getConnections(point);
    }

    /**
     * 指定された接続点に直接接続されている接続点を取得する。
     *
     * @param point 検索対象の接続点。
     *
     * @return 隣接する接続点のSet。
     */
    public Set<EnergyConnectionPoint> getConnectedPoints(EnergyConnectionPoint point) {
        return graph.getConnectedPoints(point);
    }

    /**
     * 指定された接続点から到達可能な接続点を全て取得する。
     *
     * @param start 探索開始地点。
     *
     * @return 到達可能な接続点のSet。
     */
    public Set<EnergyConnectionPoint> traverse(EnergyConnectionPoint start) {
        return graph.traverse(start);
    }

    /**
     * 2つの接続点間に接続経路が存在するか判定する。
     *
     * @param start 探索開始地点。
     * @param target 到達目標地点。
     *
     * @return 接続経路が存在する場合true。
     */
    public boolean isConnected(EnergyConnectionPoint start, EnergyConnectionPoint target) {
        return graph.isConnected(start, target);
    }

    /**
     * 指定された接続点が属する連結部分を取得する。
     *
     * @param point 検索開始地点。
     *
     * @return 連結部分を構成する接続点のSet。
     */
    public Set<EnergyConnectionPoint> findConnectedComponent(EnergyConnectionPoint point) {
        return graph.findConnectedComponent(point);
    }

    /**
     * ネットワークに登録されている接続地点を取得する
     *
     * @return 接続数。
     */
    public int getConnectionPointCount() {
        return graph.getConnectionPointCount();
    }

    /**
     * ネットワークに接続点が存在するか判定する。
     *
     * @param point 判定対象の接続点。
     *
     * @return 存在する場合true。
     */
    public boolean containsPoint(EnergyConnectionPoint point) {
        return graph.containsPoint(point);
    }

    /**
     * ネットワークに接続が存在するか判定する。
     *
     * @param connection 判定対象の接続。
     *
     * @return 存在する場合true。
     */
    public boolean containsConnection(EnergyConnection connection) {
        return graph.containsConnection(connection);
    }

    /**
     * ネットワークに登録されている全ての接続を取得する。
     *
     * @return ネットワーク内の全ての接続情報。
     */
    public Set<EnergyConnection> getAllConnections() {
        return graph.getAllConnections();
    }

    /**
     * ネットワークに登録されている全ての接続点を取得する。
     *
     * @return ネットワーク内のすべての接続点。
     */
    public Set<EnergyConnectionPoint> getConnectionPoints() {
        return graph.getConnectionPoints();
    }

    /**
     * ネットワークが空か判定する。
     *
     * @return 接続点が1つも登録されていない場合はtrue。
     */
    public boolean isEmpty() {
        return graph.getConnectionPointCount() == 0;
    }
}
