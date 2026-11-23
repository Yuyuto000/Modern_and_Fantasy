package aaaminecraft.modern_fantasy.logics.energy;

import aaaminecraft.modern_fantasy.logics.energy.core.EnergyTransferModelRegistry;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyTransferService;
import aaaminecraft.modern_fantasy.logics.energy.electronic.ElectricalEnergyDefinition;
import aaaminecraft.modern_fantasy.logics.energy.electronic.ElectricalEnergyTransferModel;
import aaaminecraft.modern_fantasy.logics.energy.magic.MagicEnergyDefinition;
import aaaminecraft.modern_fantasy.logics.energy.magic.MagicEnergyTransferModel;

import java.util.Objects;

/**
 * <h1>EnergyTransferSystemクラス</h1>
 *
 * エネルギー転送システムの初期化と管理を行う。
 *
 * <p>
 *     各エネルギーの転送モデルをレジストリに登録し、
 *     転送サービスを提供する。
 * </p>
 *
 * @param registry 転送モデルレジストリ
 * @param service 転送サービス
 */
public record EnergyTransferSystem(
        EnergyTransferModelRegistry registry,
        EnergyTransferService service
) {

    /**
     * コンストラクタ。
     */
    public EnergyTransferSystem {
        Objects.requireNonNull(registry, "Registry cannot be null.");
        Objects.requireNonNull(service, "Service cannot be null.");
    }

    public static EnergyTransferSystem createDefault() {
        EnergyTransferModelRegistry registry = new EnergyTransferModelRegistry();

        registry.register(ElectricalEnergyDefinition.class, new ElectricalEnergyTransferModel());
        registry.register(MagicEnergyDefinition.class, new MagicEnergyTransferModel());

        EnergyTransferService service = new EnergyTransferService(registry);

        return new EnergyTransferSystem(registry, service);
    }
}
