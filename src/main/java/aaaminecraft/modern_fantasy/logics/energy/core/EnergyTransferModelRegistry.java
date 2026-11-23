package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <h1>EnergyTransferModelRegisterクラス</h1>
 *
 * エネルギー転送モデルを管理するレジストリ。
 *
 * <p>
 *     エネルギー定義のクラスと転送モデルを対応付け、
 *     エネルギーの種類に応じた転送モデルを取得する。
 * </p>
 */
public class EnergyTransferModelRegistry {

    private final Map<Class <? extends EnergyDefinition>, EnergyTransferModel> models = new HashMap<>();

    /**
     * 転送モデルを登録する。
     *
     * @param definitionClass エネルギー定義クラス
     * @param model 登録する転送モデル
     * @throws NullPointerException 引数がnullの場合
     * @throws IllegalArgumentException 既に登録済みの場合
     */
    public void register(Class <? extends EnergyDefinition> definitionClass, EnergyTransferModel model) {

        Objects.requireNonNull(definitionClass,"Definition class cannot be null.");
        Objects.requireNonNull(model,"Transfer model cannot be null.");

        if (models.containsKey(definitionClass)) throw new IllegalArgumentException("A transfer model already registered for: " + definitionClass.getName());

        models.put(definitionClass, model);
    }

    /**
     * エネルギー定義に基づく転送モデルを取得する。
     *
     * @param definition エネルギー定義
     * @return 対応する転送モデル
     * @throws NullPointerException 定義がnullの場合
     * @throws IllegalArgumentException 対応するモデルが未登録の場合
     */
    public EnergyTransferModel getModel(EnergyDefinition definition) {
        Objects.requireNonNull(definition,"Energy Definition cannot be null.");

        EnergyTransferModel model = models.get(definition.getClass());

        if (model == null) throw new IllegalArgumentException("No transfer model registered for: " + definition.getClass().getName());

        return model;
    }

    /**
     * エネルギー定義に対応する転送モデルが登録済みか確認する。
     *
     * @param definition エネルギー定義
     * @return 登録済みの場合true
     */
    public boolean hasModel(EnergyDefinition definition) {
        Objects.requireNonNull(definition,"Energy Definition cannot be null.");

        return models.containsKey(definition.getClass());
    }

    /**
     * 転送モデルの登録を解除する。
     *
     * @param definitionClass エネルギー定義クラス
     * @return 登録を解除できた場合true
     */
    public boolean unregister(Class <? extends EnergyDefinition> definitionClass) {
        Objects.requireNonNull(definitionClass,"Definition class cannot be null.");

        return models.remove(definitionClass) != null;
    }

    /**
     * 登録されているモデルを全て解除する。
     */
    public void clear() {
        models.clear();
    }

    /**
     * 登録されている転送モデル数を取得する。
     *
     * @return 登録数
     */
    public int getModelCount() {
        return models.size();
    }
}
