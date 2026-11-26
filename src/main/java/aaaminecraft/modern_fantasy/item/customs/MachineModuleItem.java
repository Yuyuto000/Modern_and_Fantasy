package aaaminecraft.modern_fantasy.item.customs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Set;

/**
 * <h1>MachineModuleItemクラス</h1>
 *
 * 機械に装着するモジュールアイテムの基底クラス。
 *
 * <p>
 *     このクラスはモジュールのアイテムとしての基本性能と、
 *     装着可能な機械の管理を担当する。
 * </p>
 *
 * <p>
 *     個別のモジュールが担当する加工機能やレシピ、
 *     エネルギー消費などの処理は担当せず、機械側の処理システムに委譲する。
 * </p>
 *
 * <p>
 *     対応する機械はResourceLocationによって識別する。
 *     これにより、異なる機械システム間でも共通の
 *     互換性判定方式を採用できる。
 * </p>
 */
public class MachineModuleItem extends Item {

    /**
     * このモジュールを装着可能な機械のID一覧。
     *
     * <p>
     *     Setは不変コピーとして保持する。
     * </p>
     */
    private final Set<ResourceLocation> compatibleMachines;

    /**
     * MachineModuleItemを生成する。
     *
     * @param properties アイテムの基本プロパティ。
     * @param compatibleMachines 装着可能な機械のID一覧。
     */
    public MachineModuleItem(
            Properties properties,
            Set<ResourceLocation> compatibleMachines
    ) {
        super(properties);

        if (compatibleMachines == null)
            throw new IllegalArgumentException("Compatible machines cannot be null");
        this.compatibleMachines = Set.copyOf(compatibleMachines);
    }

    /**
     * 指定された機械にこのモジュールを装着できるか判定する。
     *
     * @param machineID 判定対象のID
     * @return 装着可能ならtrue
     */
    public boolean isCompatibleWith(ResourceLocation machineID) {
        if (machineID == null) return false;
        return compatibleMachines.contains(machineID);
    }

    /**
     * このモジュールを装着可能な機械のID一覧を取得する。
     *
     * @return 装着可能な機械のID一覧
     */
    public Set<ResourceLocation> getCompatibleMachines() {
        return compatibleMachines;
    }
}
