package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.*;

/**
 * <h1>EnergyPortManagerクラス</h1>
 *
 * ブロックが持つエネルギーポートを管理するクラス。
 *
 * <p>
 *     複数のEnergyPortを接続位置によって管理する。
 *     エネルギーの保持や転送処理は担当しない。
 * </p>
 */
public class EnergyPortManager {

    private final Map<EnergyConnectionPoint, EnergyPort> ports = new HashMap<>();

    /**
     * ポートを登録する。
     *
     * @param port 登録するポート
     * @throws NullPointerException ポートがnullの場合
     * @throws IllegalArgumentException 同じ接続位置が登録済みの場合
     */
    public void register(EnergyPort port) {
        Objects.requireNonNull(port, "Port cannot be null.");

        EnergyConnectionPoint point = port.point();

        if (ports.containsKey(point)) throw new IllegalArgumentException("A port is already registered at: " + point);
        ports.put(point, port);
    }

    /**
     * 指定した接続位置のポートを取得する。
     *
     * @param point 接続位置
     * @return ポート。存在しない場合はnull
     */
    public EnergyPort get(EnergyConnectionPoint point) {
        Objects.requireNonNull(point, "Connection point cannot be null.");
        return ports.get(point);
    }

    /**
     * 指定した接続位置のポートを登録解除する。
     *
     * @param point 接続位置
     * @return 削除できた場合true
     */
    public boolean unregister(EnergyConnectionPoint point) {
        Objects.requireNonNull(point, "Connection point cannot be null.");
        return ports.remove(point) != null;
    }

    /**
     * 指定した接続位置のポートが登録されているか確認する。
     *
     * @param point 接続位置
     * @return 登録済みならtrue
     */
    public boolean contains(EnergyConnectionPoint point) {
        Objects.requireNonNull(point, "Connection point cannot be null.");
        return ports.containsKey(point);
    }

    /**
     * 登録されている全てのポートを取得する。
     *
     * @return 変更不可能なポート一覧
     */
    public Collection<EnergyPort> getAll() {
        return Collections.unmodifiableCollection(ports.values());
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
     * 登録されているポートをすべて解除する。
     */
    public void clear() {
        ports.clear();
    }

    /**
     * ポートが一つも登録されていないか確認する。
     *
     * @return 空ならtrue
     */
    public boolean isEmpty() {
        return ports.isEmpty();
    }
}
