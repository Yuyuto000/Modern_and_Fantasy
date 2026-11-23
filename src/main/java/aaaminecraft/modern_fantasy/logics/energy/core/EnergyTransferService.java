package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.Objects;

/**
 * <h1>EnergyTransferServiceクラス</h1>
 * <p>
 * エネルギー転送処理を管理するサービスクラス。
 *
 * <p>
 * エネルギー定義に対応する転送モデルをレジストリから取得し、
 * 実際の転送処理を実行する。
 * </p>
 */
public record EnergyTransferService(EnergyTransferModelRegistry registry) {

    /**
     * コンストラクタ。
     *
     * @param registry 転送モデルレジストリ。
     * @throws NullPointerException レジストリがnullの場合
     */
    public EnergyTransferService(EnergyTransferModelRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "Transfer model cannot be null");
    }

    /**
     * エネルギーを転送する。
     *
     * @param source          転送元コンテナ
     * @param target          転送先コンテナ
     * @param requestedAmount 要求する転送量
     * @param context         転送経路などの追加情報
     * @return 転送結果
     * @throws NullPointerException     引数がnullの場合
     * @throws IllegalArgumentException 要求量が負数もしくは非有限値、または対応する転送モデルが存在しない場合
     */
    public EnergyTransferResult transfer(
            EnergyContainer source,
            EnergyContainer target,
            double requestedAmount,
            EnergyTransferContext context
    ) {
        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(target, "Target cannot be null");
        Objects.requireNonNull(context, "Context cannot be null");

        EnergyTransferModel model = registry.getModel(source.getDefinition());

        return model.transfer(source, target, requestedAmount, context);
    }

    /**
     * 転送経路を指定せずにエネルギーを転送する。
     *
     * @param source          転送元コンテナ
     * @param target          転送先コンテナ
     * @param requestedAmount 要求する転送量
     * @return 転送結果
     */
    public EnergyTransferResult transfer(
            EnergyContainer source,
            EnergyContainer target,
            double requestedAmount
    ) {
        return transfer(
                source,
                target,
                requestedAmount,
                EnergyTransferContext.direct()
        );
    }

    /**
     * 使用しているレジストリを取得する。
     *
     * @return 転送モデルレジストリ
     */
    @Override
    public EnergyTransferModelRegistry registry() {
        return registry;
    }
}
