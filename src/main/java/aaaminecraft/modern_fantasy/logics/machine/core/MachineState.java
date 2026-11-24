package aaaminecraft.modern_fantasy.logics.machine.core;

/**
 * <h1>MachineStateクラス</h1>
 *
 * 機械の稼働状態を管理するクラス。
 *
 * <p>このクラスは機械がプレイヤーによって有効化されているかどうかを管理する。</p>
 * <p>エネルギーの供給状態や処理の進行状態などは管理しない。</p>
 */
public class MachineState {

    /**
     * 機械が有効化されているかどうか
     */
    private boolean enabled;

    /**
     * 初期状態を指定してMachineStateを生成する。
     *
     * @param enabled 初期稼働状態
     */
    public MachineState(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 機械が有効化されているかを取得する。
     *
     * @return 有効化されている場合はtrue
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 機械の有効・無効を設定する。
     *
     * @param enabled 有効化する場合はtrue
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 機械を有効化する。
     */
    public void  enable() {
        this.enabled = true;
    }

    /**
     * 機械を無効化する。
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * 機械の有効・無効を反転させる。
     */
    public void toggle() {
        this.enabled = !this.enabled;
    }
}
