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

    /**
     * 指定された接続点からBFS(幅優先探索)を実行する。
     *
     * <p>
     *     開始地点から到達可能な全ての接続点を探索する。
     *     開始地点自身も結果に含まれる。
     * </p>
     *
     * <p>
     *     探索中はQueueで次に探索する接続点を管理し、
     *     Setで探索済みの接続点を記録する。
     * </p>
     *
     * @param start 探索開始地点
     * @return 開始地点から到達可能な接続点の集合。開始地点がグラフに存在しない場合は空集合。
     */
    public Set<EnergyConnectionPoint> traverse(EnergyConnectionPoint start){

        if (start == null || !connections.containsKey(start)) return Collections.emptySet();

        // 探索待ちの接続点を保持するキュー
        Queue<EnergyConnectionPoint> queue = new ArrayDeque<>();

        // 探索炭の接続点を記録する集合
        Set<EnergyConnectionPoint> visited = new HashSet<>();

        // 開始地点を登録
        queue.add(start);
        visited.add(start);

        // 探索対象がなくなるまで繰り返す
        while (!queue.isEmpty()){

            // 次に探索する接続点を取得
            EnergyConnectionPoint current = queue.poll();

            // 現在の接続点に隣接する点を取得
            for (EnergyConnectionPoint neighbor : getConnectedPoints(current)){

                // 未探索の場合のみキューに登録
                if (visited.add(neighbor)) queue.add(neighbor);
            }
        }

        return Collections.unmodifiableSet(visited);
    }

    /**
     * 2つの接続点間に接続経路が存在するか判定する。
     *
     * @param start 探索開始地点
     * @param target 到達目標点
     * @return 両地点が同一、または接続経路が存在する場合はTrue。
     */
    public boolean isConnected(EnergyConnectionPoint start, EnergyConnectionPoint target){

        if (start == null || target == null) return false;
        if (start.equals(target)) return connections.containsKey(start);

        return traverse(start).contains(target);
    }

    /**
     * 指定された接続点が属する連結成分を取得する。
     *
     * <p>
     *     連結成分とは、互いに経路で到達できる接続点の集合。
     * </p>
     * @param point 指定地点が属する連結成分
     * @return 指定地点が属する連結成分。存在しない場合は空集合。
     */
    public Set<EnergyConnectionPoint> findConnectedComponent(EnergyConnectionPoint point){
        return traverse(point);
    }

    /**
     * グラフに登録されているすべての接続を取得する。
     *
     * <p>
     *     両端点から同じ接続が参照されているため、
     *     Setによって重複を排除する。
     * </p>
     *
     * @return グラフ内の全ての接続情報。
     */
    public Set<EnergyConnection> getAllConnections(){
        Set<EnergyConnection> result = new HashSet<>();

        for (Set<EnergyConnection> set : connections.values()){
            result.addAll(set);
        }

        return Collections.unmodifiableSet(result);
    }

    /**
     * グラフに登録されているすべての接続点を取得する。
     *
     * @return 登録されている接続点の集合。
     */
    public Set<EnergyConnectionPoint> getConnectionPoints(){
        return Set.copyOf(connections.keySet());
    }
}
