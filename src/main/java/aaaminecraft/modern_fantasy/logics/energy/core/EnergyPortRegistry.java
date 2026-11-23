package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.*;

/**
 * <h1>EnergyPortRegistryクラス</h1>
 *
 * エネルギーポートと、それを所有するブロックを関連付けて管理する。
 *
 * <p>
 *     接続位置をキーとしてEnergyPortAccessを登録し、
 *     ネットワーク側から対応するブロックへのポートへアクセスできるようにする。
 * </p>
 *
 * <p>
 *     このレジストリはワールド単位で保持することを想定している。
 * </p>
 */
public class EnergyPortRegistry {

    private final Map<EnergyConnectionPoint, EnergyPortAccess> ports = new HashMap<>();

    /**
     * ブロックが持つすべてのポートを登録する。
     *
     * <p>
     *     途中で登録に失敗した場合は、今回の登録処理を取り消す。
     * </p>
     *
     * @param access ポートを公開するブロック
     * @throws NullPointerException accessがnullの場合
     * @throws IllegalArgumentException 登録位置が重複している場合
     */
    public void register(EnergyPortAccess access) {
        Objects.requireNonNull(access, "Port access cannot be null.");

        Collection<EnergyPort> blockPorts = access.getEnergyPortManager().getAll();

        // 重複がないことを先に確認する
        for (EnergyPort port : blockPorts) {
            EnergyConnectionPoint point = port.point();

            if (ports.containsKey(point)) throw new IllegalStateException("A port is already registered at: " + point);
        }

        // 全ての確認が終わってから登録する。
        for (EnergyPort port : blockPorts) {
            ports.put(port.point(), access);
        }
    }

    /**
     * ブロックが持つすべてのポートを登録解除する。
     *
     * @param access 登録解除するブロック。
     * @return 1つ以上のポートを解除した場合true
     */
    public boolean unregister(EnergyPortAccess access) {
        Objects.requireNonNull(access, "Port access cannot be null.");

        boolean removed = false;

        for (EnergyPort port : access.getEnergyPortManager().getAll()) {
            EnergyConnectionPoint point = port.point();

            removed |= ports.remove(point, access);
        }

        return removed;
    }

    /**
     * 接続位置からポートの所有ブロックを取得する。
     *
     * @param point 接続位置
     * @return 所有ブロック。未登録の場合はnull
     */
    public EnergyPortAccess get(EnergyConnectionPoint point) {
        Objects.requireNonNull(point, "Connection point cannot be null.");
        return ports.get(point);
    }

    /**
     * 接続位置が登録されているか確認する。
     *
     * @param point 接続位置
     * @return 登録済みならtrue
     */
    public boolean contains(EnergyConnectionPoint point) {
        Objects.requireNonNull(point, "Connection point cannot be null.");
        return ports.containsKey(point);
    }

    /**
     * 登録されているポート数を取得する。
     *
     * @return ポート数
     */
    public int size() {
        return ports.size();
    }

    /**
     * 登録されているすべての接続位置を取得する。
     *
     * @return 変更不可能な接続位置一覧
     */
    public Collection<EnergyConnectionPoint> getConnectionPoints() {
        return Collections.unmodifiableCollection(ports.keySet());
    }

    /**
     * すべてのポート登録を解除する。
     */
    public void clear() {
        ports.clear();
    }
}
