package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.List;
import java.util.Objects;

/**
 * エネルギー転送時に必要な追加情報を保持するクラス。
 *
 * <p>
 *     転送経路など、エネルギーの転送計算に必要な情報を管理する。
 * </p>
 *
 * <p>
 *     経路情報を持たない直接転送にも対応。
 * </p>
 *
 * @param path
 */
public record EnergyTransferContext(List<EnergyConnection> path) {

    /**
     * コンストラクタ。
     *
     * @throws NullPointerException 経路がnullの場合
     */
    public EnergyTransferContext {
        Objects.requireNonNull(path, "Transfer path cannot be null");
        path = List.copyOf(path);
    }

    /**
     * 経路情報を持たない直接転送用のContextを生成する。
     *
     * @return 直接転送用Context
     */
    public static EnergyTransferContext direct() {
        return new EnergyTransferContext(List.of());
    }

    /**
     * 転送経路が存在するか確認する。
     *
     * @return 経路が存在する場合true
     */
    public boolean hasPath() {
        return !path.isEmpty();
    }

    /**
     * 経路上の接続数を取得する。
     *
     * @return 接続数
     */
    public int getPathLength() {
        return path.size();
    }
}
