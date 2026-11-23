package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.*;

/**
 * <h1>EnergyNetworkManagerクラス</h1>
 *
 * <p>
 *     複数のEnergyNetworkを統括管理するクラス。
 * </p>
 *
 * <p>
 *     接続の追加・削除に応じてネットワークの統合・分割を行い、
 *     接続点とネットワークの所属関係を管理する。
 * </p>
 *
 * <p>
 *     このクラスはネットワーク構造の管理を担当するため、
 *     エネルギーの生成・蓄積・伝達などは担当しない。
 * </p>
 *
 * <p>
 *     このクラスのインスタンスは
 *     MinecraftのWorld単位で管理することを想定する。
 * </p>
 *
 * @see EnergyNetwork
 * @see EnergyNetworkGraph
 */
public class EnergyNetworkManager {

    /**
     * UUIDをキーとしてネットワーク本体を保持する。
     */
    private final Map<UUID, EnergyNetwork> networks = new HashMap<>();

    /**
     * 接続点と所属ネットワークの対応関係を保持するMap。
     * {@code key(EnergyConnectionPoint)} : 接続点
     * {@code Value(UUID)}                : 所属ネットワークのUUID
     */
    private final Map<EnergyConnectionPoint, UUID> pointNetworkIndex = new HashMap<>();

    /**
     * 新しい一意なネットワークを生成する。
     *
     * <p>
     *     既存のネットワークとUUIDが重複しないことを確認する。
     * </p>
     *
     * @return 新しく生成されたネットワーク。
     */
    private EnergyNetwork createUniqueNetwork(){

        EnergyNetwork network;

        do {
            network = new EnergyNetwork();
        } while (networks.containsKey(network.getId()));

        return  network;
    }

    /**
     * ネットワークをManagerへ登録する。
     *
     * <p>
     *     ネットワーク本体と所属接続点の対応表を更新する。
     * </p>
     *
     * @param network 登録するネットワーク。
     */
    private void registerNetwork(EnergyNetwork network){
        networks.put(network.getId(), network);

        for (EnergyConnectionPoint point : network.getConnectionPoints()) {
            pointNetworkIndex.put(point, network.getId());
        }
    }

    /**
     * ネットワークをManagerから登録解除する。
     *
     * <p>
     *     ネットワーク本体と所属接続点の対応表を解除する。
     * </p>
     *
     * @param network 解除するネットワーク。
     */
    private void unregisterNetwork(EnergyNetwork network){
        networks.remove(network.getId());

        for (EnergyConnectionPoint point : network.getConnectionPoints()) {
            pointNetworkIndex.remove(point);
        }
    }

    /**
     * 新しい接続を追加し、必要に応じてネットワークを統合する。
     *
     * <p>
     *     両端点の所属状況によって処理を変更する。
     * </p>
     *
     * <ul>
     *     <li>両端点が未登録:新規ネットワークを生成する</li>
     *     <li>片方のみ登録済み:既存ネットワークに追加する</li>
     *     <li>同じネットワークに所属:既存ネットワークに追加する</li>
     *     <li>異なるネットワークに所属:2つを統合する</li>
     * </ul>
     *
     * @param connection 追加する接続情報。null不可。
     *
     * @return 接続が追加された場合true。
     *
     * @throws IllegalArgumentException connectionがnullの場合。エラーコード:[MF-ENERGY-006]
     */
    public boolean addConnection(EnergyConnection connection) {

        if (connection == null) throw new IllegalArgumentException("[MF-ENERGY-004] Connection cannot be null");

        EnergyConnectionPoint endA = connection.endA();
        EnergyConnectionPoint endB = connection.endB();

        UUID idA = pointNetworkIndex.get(endA);
        UUID idB = pointNetworkIndex.get(endB);

        // 両端点がまだネットワークに所属していない場合
        if (idA == null && idB == null) {

            EnergyNetwork network = createUniqueNetwork();

            network.addConnection(connection);
            registerNetwork(network);

            return true;
        }

        // 片方が既存ネットワークに所属している場合
        if (idA != null && idB == null) {

            EnergyNetwork network = networks.get(idA);

            if (network == null) throw new IllegalArgumentException("[MF-ENERGY-006] Network index is inconsistent.");
            if (!network.addConnection(connection)) return false;

            pointNetworkIndex.put(endB, idA);

            return true;
        }

        if (idA == null) {

            EnergyNetwork network = networks.get(idB);

            if (network == null) throw new IllegalArgumentException("[MF-ENERGY-006] Network index is inconsistent.");
            if (!network.addConnection(connection)) return false;

            pointNetworkIndex.put(endA, idB);

            return true;
        }

        // 両端点が同じネットワークに所属している
        if (idA.equals(idB)) {

            EnergyNetwork network = networks.get(idA);

            if (network == null) throw new IllegalArgumentException("[MF-ENERGY-006] Network index is inconsistent.");

            return network.addConnection(connection);
        }

        // 異なる2つのネットワークを統合する
        EnergyNetwork networkA = networks.get(idA);
        EnergyNetwork networkB = networks.get(idB);

        if (networkA == null || networkB == null) throw new IllegalArgumentException("[MF-ENERGY-006] Network index is inconsistent.");

        EnergyNetwork mergedNetwork = createUniqueNetwork();

        // 既存ネットワークAの接続を移す
        for (EnergyConnection existing : networkA.getAllConnections()){
            mergedNetwork.addConnection(existing);
        }

        // 既存ネットワークBの接続を移す
        for (EnergyConnection existing : networkB.getAllConnections()){
            mergedNetwork.addConnection(existing);
        }

        // 新しい接続を追加する
        mergedNetwork.addConnection(connection);

        // 古いネットワークを削除
        unregisterNetwork(networkA);
        unregisterNetwork(networkB);

        // 統合後のネットワークを登録
        registerNetwork(mergedNetwork);

        return true;
    }

    /**
     * 指定された接続を解除し、必要に応じてネットワークを分割する。
     *
     * <p>
     *     接続解除後、BFSによって連結成分を再探索する。
     *     複数の連結成分が見つかった時、それぞれを
     *     新しいネットワークとして構築する。
     * </p>
     *
     * @param connection 削除する接続情報。null不可。
     *
     * @return 接続が解除された場合true。
     *
     * @throws IllegalArgumentException connectionがnullの場合。
     * 処理場所でエラーの種類が違うのでカスタムクラッシュレポートを確認すること。
     */
    public boolean removeConnection(EnergyConnection connection) {

        if (connection == null) throw new IllegalArgumentException("[MF-ENERGY-004] Connection cannot be null");

        UUID networkId = pointNetworkIndex.get(connection.endA());
        if (networkId == null) return false;

        EnergyNetwork network = networks.get(networkId);
        if (network == null) throw new IllegalArgumentException("[MF-ENERGY-006] Network index is inconsistent.");

        // 指定された接続を解除
        if (!network.removeConnection(connection)) return false;

        // 接続点がなくなった場合ネットワークを削除
        if (network.isEmpty()) {
            unregisterNetwork(network);
            return  true;
        }

        // 残った接続点から連結成分を探索する
        Set<EnergyConnectionPoint> visited = new HashSet<>();
        List<Set<EnergyConnectionPoint>> components = new ArrayList<>();

        for (EnergyConnectionPoint point : network.getConnectionPoints()){
            if (visited.contains(point)) continue;

            Set<EnergyConnectionPoint> component = network.findConnectedComponent(point);
            visited.addAll(component);
            components.add(component);
        }

        // 分割が発生していない場合は終了
        if (components.size() <= 1) return true;

        // 分割前の接続情報を保存
        Set<EnergyConnection> oldConnections = network.getAllConnections();

        // 古いネットワークを解除
        unregisterNetwork(network);

        // 各連結成分を新しいネットワークとして再構築
        for (Set<EnergyConnectionPoint> component : components){

            EnergyNetwork separatedNetwork = createUniqueNetwork();

            for (EnergyConnection existing : oldConnections){

                if (component.contains(existing.endA()) && component.contains(existing.endB())) separatedNetwork.addConnection(existing);
            }

            registerNetwork(separatedNetwork);
        }

        return true;
    }

    /**
     * UUIDからネットワークを取得する。
     *
     * @param id 検索対象のケットワークUUID。
     *
     * @return 該当するネットワーク。存在しない場合はnull。
     */
    public EnergyNetwork getNetwork(UUID id){
        return networks.get(id);
    }

    /**
     * 接続点から所属ネットワークを取得する。
     *
     * @param point 検索対象の接続点。
     *
     * @return 所属ネットワーク。存在しない場合null。
     */
    public EnergyNetwork getNetworkAt(EnergyConnectionPoint point){

        UUID id = pointNetworkIndex.get(point);
        if (id == null) return null;

        return networks.get(id);
    }

    /**
     * Managerが管理するすべてのネットワークを取得する。
     *
     * @return ネットワークからのMap。変更不可。
     */
    public Map<UUID, EnergyNetwork> getNetworks(){
        return Map.copyOf(networks);
    }

    /**
     * 管理しているネットワーク数を取得する。
     *
     * @return ネットワーク数。
     */
    public int getNetworkCount(){
        return networks.size();
    }

    /**
     * 指定された接続点が登録されているか判定する。
     *
     * @param point 判定対象の接続点。
     *
     * @return 登録されている場合true。
     */
    public boolean containsPoint(EnergyConnectionPoint point){
        return pointNetworkIndex.containsKey(point);
    }

    /**
     * 指定されたUUIDのネットワークが登録されているか判定する。
     *
     * @param id 判定対象のUUID。
     *
     * @return 登録されている場合true。
     */
    public boolean containsNetwork(UUID id){
        return networks.containsKey(id);
    }

    /**
     * 管理している全てのネットワークを削除する
     */
    public void clear(){
        networks.clear();
        pointNetworkIndex.clear();
    }
}
