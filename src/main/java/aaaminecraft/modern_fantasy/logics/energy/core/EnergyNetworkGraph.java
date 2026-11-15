package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.*;

/**
 * <h1>EnergyNetworkGraph</h1>
 *
 * <p>エネルギーネットワークの接続構造を管理するクラス</p>
 *
 * <p>
 *     EnergyConnectionPointを頂点(Node)、
 *     EnergyConnectionを辺(Edge)として扱う
 *     無向グラフ構造を提供する。
 * </p>
 * <p>
 *     このクラスは接続関係のみを管理する。
 *     エネルギー転送、経路探索、ネットワーク結合・分割は担当しない。
 * </p>
 */
public class EnergyNetworkGraph {

    /**
     * 各接続点から伸びる接続(Edge)を保持する
     * <p>
     *      key:接続点(Node)
     *      value:その接続点(Node9に接続されている接続(Edge)の集合
     *  </p>
     */
    private final Map<EnergyConnectionPoint, Set<EnergyConnection>> connections = new HashMap<>();

    /**
     * 新しい接続をグラフに追加する。
     *
     * <p>
     *     接続(Edge)の両端点をMapに登録し、
     *     それぞれの接続点(Node)に対応するSetへ接続情報を追加する。
     * </p>
     * @param connection 追加する接続情報。null不可。
     * @return 新しい接続(Edge)が追加された場合true。既に同じ接続(Edge9が存在する場合false。
     *
     * @throws IllegalArgumentException connectionがnullの場合。エラーコード:[MF-ENERGY-004]
     */
    public boolean addConnection(EnergyConnection connection){
        if (connection == null) throw new IllegalArgumentException("[MF-ENERGY-004] Connection cannot be null.");

        EnergyConnectionPoint endA = connection.endA();
        EnergyConnectionPoint endB = connection.endB();

        Set<EnergyConnection> connectionsA = connections.computeIfAbsent(endA, key -> new HashSet<>());
        Set<EnergyConnection> connectionsB = connections.computeIfAbsent(endB, key -> new HashSet<>());

        boolean addedA = connectionsA.add(connection);
        boolean addedB = connectionsB.add(connection);

        return addedA && addedB;
    }

    /**
     * 指定された接続(Edge)をグラフから削除する。
     *
     * <p>
     *     接続(Edge)の両端点にある接続集合から削除する。
     *     接続(Edge)がなくなった接続点(node9もMapから削除する。
     * </p>
     *
     * @param connection　削除する接続情報
     * @return 接続存在し、削除できた場合true。存在しなかった場合false。
     *
     * @throws IllegalArgumentException containsがnullの場合。エラーコード:[MF-ENERGY-004]
     */

    public boolean removeConnection(EnergyConnection connection){
        if (connection == null) throw new IllegalArgumentException("[MF-ENERGY-004] Connection cannot be null.");

        EnergyConnectionPoint endA = connection.endA();
        EnergyConnectionPoint endB = connection.endB();

        Set<EnergyConnection> connectionsA = connections.get(endA);
        Set<EnergyConnection> connectionsB = connections.get(endB);

        // 両端のどちらかが存在しない時は削除しない
        if (connectionsA ==  null || connectionsB == null) return false;
        if (!connectionsA.contains(connection)) return false;

        connectionsA.remove(connection);
        connectionsB.remove(connection);

        if (connectionsA.isEmpty()) connections.remove(endA);
        if (connectionsB.isEmpty()) connections.remove(endB);

        return true;
    }

    /**
     * 指定された接続点に接続されている接続点(Node)情報を取得する
     *
     * <p>
     *     返却されるSetは変更不可。
     *     グラフ内部の接続情報を直接変更することはできない。
     * </p>
     *
     * @param point 検索対象の接続点(Node)
     * @return 接続情報のSet。接続点(Node)が存在しない場合は空のSet。
     */
    public Set<EnergyConnection> getConnections(EnergyConnectionPoint point){
        Set<EnergyConnection> result = connections.get(point);

        if (result == null) return Collections.emptySet();

        return Collections.unmodifiableSet(result);
    }

    /**
     * 指定された接続点(Node)に直接接続されている接続点(Node)を取得する。
     *
     * @param point 検索対象の接続点(Node)。
     * @return 隣接する接続点(Node)のSet。
     */
    public Set<EnergyConnectionPoint> getConnectedPoints(EnergyConnectionPoint point){
        Set<EnergyConnectionPoint> result = new HashSet<>();

        for (EnergyConnection connection : getConnections(point)){
            result.add(connection.getOtherEnd(point));
        }

        return Collections.unmodifiableSet(result);
    }

    /**
     * グラフに登録されている接続点(Node)の一覧を取得する。
     *
     * @return 登録されている接続点(Node)のSet。
     */
    public Set<EnergyConnectionPoint> getConnectedPoints(){
        return Collections.unmodifiableSet(connections.keySet());
    }

    /**
     * グラフに登録されている接続数を取得する。
     *
     * @return 接続数。
     */
    public int getConnectionCount(){
        int total = 0;

        for (Set<EnergyConnection> set : connections.values()){
            total += set.size();
        }

        // 各接続数は両端点に登録されるため2で割る
        return total / 2;
    }

    /**
     * グラフに接続点(Node)が登録されているか判定する。
     *
     * @param point 判定対象の接続点(Node)
     *
     * @return 登録されている場合はtrue。されていない場合はfalse。
     */
    public boolean containsPoint(EnergyConnectionPoint point){
        return connections.containsKey(point);
    }

    /**
     * グラフに接続(Edge)が登録されているか判定する
     *
     * @param connection 判定対象の接続(Edge)。
     *
     * @return 登録されている場合はtrue。されていない場合はfalse。
     */
    public boolean containsConnection(EnergyConnection connection){
        if (connection == null) return false;

        Set<EnergyConnection> result = connections.get(connection.endA());

        return result != null && result.contains(connection);
    }

    /**
     * グラフに登録されている接続点(Node)の数を取得する。
     *
     * @return 接続点(Node)の数。
     */
    public int getConnectionPointCount() {
        return connections.size();
    }

    /**
     * グラフに登録されている接続情報をすべて削除する。
     */
    public void clear() {
        connections.clear();
    }
}
