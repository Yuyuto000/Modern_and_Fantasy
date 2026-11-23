package aaaminecraft.modern_fantasy.logics.energy.core;

/**
 * <h1>EnergyAmountクラス</h1>
 * <p>
 * エネルギーの実体と保有量を表すクラス。
 *
 * <p>
 * EnergyDefinitionによって定義されたエネルギーの種類と、
 * そのエネルギーが現在保有している量を管理する。
 * </p>
 *
 * <p>
 * このクラスは不変オブジェクトとして設計されている。
 * エネルギー量を変更する場合は新しいEnergyAmountインスタンスを生成する。
 * </p>
 *
 * <p>
 * 異なる種類のエネルギー同士を直接演算することはできない。
 * </p>
 *
 * @param definition エネルギーの種類を定義するオブジェクト。
 * @param amount     現在のエネルギー保有量
 */
public record EnergyAmount(EnergyDefinition definition, double amount) {

    /**
     * EnergyAmountを生成する。
     *
     * @param definition エネルギーの種類を定義するオブジェクト
     * @param amount     エネルギー保有量
     * @throws IllegalArgumentException Definitionがnullの場合またはamountが負数・非有限値の場合
     */
    public EnergyAmount {

        if (definition == null) throw new IllegalArgumentException("[MF-ENERGY-020] Energy definition cannot be null");
        if (!Double.isFinite(amount) || amount < 0.0)
            throw new IllegalArgumentException("[MF-ENERGY-021] Energy amount must be a finite non-negative value.");

    }

    /**
     * エネルギーの種類を取得する。
     *
     * @return エネルギー定義
     */
    @Override
    public EnergyDefinition definition() {
        return definition;
    }

    /**
     * エネルギー保有量を取得する。
     *
     * @return エネルギー単位
     */
    @Override
    public double amount() {
        return amount;
    }

    /**
     * エネルギーの単位を取得する。
     *
     * @return エネルギー単位
     */
    public EnergyUnit getUnit() {
        return definition.getUnit();
    }

    /**
     * 同じ種類のエネルギーを加算する。
     *
     * @param target 加算対象のエネルギー
     * @return 加算結果を示す新しいEnergyAmountインスタンス
     * @throws IllegalArgumentException targetがnullの場合もしくはエネルギーの種類が異なる場合
     * @throws ArithmeticException      演算結果が有限値でない場合
     */
    public EnergyAmount add(EnergyAmount target) {
        validateCompatible(target);

        double result = this.amount + target.amount;
        if (!Double.isFinite(result))
            throw new ArithmeticException("[MF-ENERGY-022] Energy addition resulted in a non-finite value.");

        return new EnergyAmount(definition, result);
    }

    /**
     * 同じ種類のエネルギーを減算する。
     *
     * @param target 減算対象のエネルギー
     * @return 減算結果を表す新しいEnergyAmount
     * @throws IllegalArgumentException targetがnullの場合、エネルギーの種類が異なる場合、または減算結果が負数になる場合。
     */
    public EnergyAmount subtract(EnergyAmount target) {
        validateCompatible(target);

        if (this.amount < target.amount)
            throw new IllegalArgumentException("[MF-ENERGY-023] Insufficient energy amount for subtraction.");
        return new EnergyAmount(definition, this.amount - target.amount);
    }

    /**
     * エネルギー量が0であるか判定する
     *
     * @return エネルギー量が0の場合true
     */
    public boolean isZero() {
        return amount == 0.0;
    }

    /**
     * 演算対象のエネルギーが同じ種類であることを検証する。
     *
     * <p>
     * エネルギーの種類はEnergyDefinitionのクラスによって識別する。
     * </p>
     *
     * @param target 検証対象のエネルギー
     * @throws IllegalArgumentException targetがnullの場合、またはエネルギーの種類が異なる場合
     */
    private void validateCompatible(EnergyAmount target) {

        if (target == null) throw new IllegalArgumentException("[MF-ENERGY-024] Other energy amount cannot be null.");
        if (!this.definition.getClass().equals(
                target.definition.getClass()
        )) throw new IllegalArgumentException("[MF-ENERGY-025] Cannot operate on different energy types.");
    }
}
