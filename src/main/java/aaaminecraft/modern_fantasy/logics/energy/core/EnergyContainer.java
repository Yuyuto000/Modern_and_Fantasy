package aaaminecraft.modern_fantasy.logics.energy.core;

/**
 * エネルギーを保持するコンテナ。
 *
 * <p>
 *     指定されたEnergyDefinitionに対応するエネルギーを保持し、
 *     エネルギーの受け入れ・取り出し・容量管理を行う。
 * </p>
 *
 * <p>
 *     このクラスは電気・魔術などのエネルギー種別に依存しない
 *     共通のエネルギー保持機構として機能する。
 * </p>
 */
public class EnergyContainer {

    /**
     * 保持するエネルギーの定義。
     */
    private final EnergyDefinition definition;

    /**
     * 最大エネルギー容量。
     */
    private final double capacity;

    /**
     * 現在のエネルギー保有量。
     */
    private double amount;

    /**
     * EnergyContainerを生成する。
     *
     * @param definition 保持するエネルギーの定義
     * @param capacity 最大容量
     *
     * @throws IllegalArgumentException definitionがnullまたはcapacityが負数・非有限値の場合
     */
    public EnergyContainer(EnergyDefinition definition, double capacity) {
        if (definition == null) throw new IllegalArgumentException("[MF-ENERGY-026] Energy definition cannot be null.");
        if (!Double.isFinite(capacity) || capacity < 0.0) throw new IllegalArgumentException("[MF-ENERGY-027] Capacity must be a finite non-negative value.");

        this.definition = definition;
        this.capacity = capacity;
        this.amount = 0.0;
    }

    /**
     * エネルギーを受け入れる。
     *
     * @param energy 受け入れるエネルギー量
     * @return 実際に受け入れたエネルギー量
     *
     * @throws IllegalArgumentException energyが不正な場合
     */
    public double receive(EnergyAmount energy){
        validateEnergy(energy);

        double accepted = Math.min(energy.amount(), capacity - amount);
        amount += accepted;

        return accepted;
    }

    /**
     * エネルギーを取り出す。
     *
     * @param requestedAmount 要求するエネルギー量
     * @return 実際に呼び出したエネルギー量
     * @throws IllegalArgumentException 要求量が負数・非有限値の場合
     */
    public double extract(double requestedAmount){
        if (!Double.isFinite(requestedAmount) || requestedAmount < 0.0) throw new IllegalArgumentException("[MF-ENERGY-028] Requested amount must be a finite non-negative value.");

        double extracted = Math.min(requestedAmount, amount);
        amount -= extracted;

        return extracted;
    }

    /**
     * 現在のエネルギー量を取得する。
     * @return 現在の保有量
     */
    public double getAmount() {
        return amount;
    }

    /**
     * 最大容量を取得する。
     * @return 最大容量
     */
    public double getCapacity() {
        return capacity;
    }

    /**
     * 空き容量を取得する。
     * @return 現在の空き容量
     */
    public double getRemainingCapacity() {
        return capacity - amount;
    }

    /**
     * 保有しているエネルギーの定義を取得する。
     * @return エネルギー定義
     */
    public EnergyDefinition getDefinition() {
        return definition;
    }

    /**
     * 現在の保有量をEnergyAmountとして取得する。
     * @return 現在のエネルギー量を示す不変オブジェクト
     */
    public EnergyAmount getEnergy() {
        return new EnergyAmount(definition, amount);
    }

    /**
     * コンテナが満杯であるか判定する。
     * @return 満杯の場合true
     */
    public boolean isFull() {
        return amount >= capacity;
    }

    /**
     * コンテナが空であるか判定する。
     * @return 空の場合true
     */
    public boolean isEmpty() {
        return amount <= 0.0;
    }

    private void validateEnergy(EnergyAmount energy){
        if (energy == null) throw new IllegalArgumentException("[MF-ENERGY-029] Energy cannot be null.");
        if (!definition.getClass().equals(
                energy.definition().getClass()
        )) throw new IllegalArgumentException("[MF-ENERGY-030] Energy type does not match container definition.");
    }
}
