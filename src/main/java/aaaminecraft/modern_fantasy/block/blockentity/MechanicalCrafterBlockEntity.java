package aaaminecraft.modern_fantasy.block.blockentity;

import aaaminecraft.modern_fantasy.MainEntrance;
import aaaminecraft.modern_fantasy.item.customs.MachineModuleItem;
import aaaminecraft.modern_fantasy.logics.energy.electronic.ElectricalEnergyDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>MechanicalCrafterBlockEntity</h1>
 *
 * メカニカルクラフターのBlockEntity。
 *
 * <P>
 *     機械共通基盤であるMachineBlockEntityを継承し、
 *     メカニカルクラフター固有のモジュール管理を担当する。
 * </P>
 *
 * <p>
 *     現段階ではモジュールの装着・保持・保存・復元を実装する。
 *     加工及びレシピ管理処理は別システムにて実装。
 * </p>
 */
public class MechanicalCrafterBlockEntity extends MachineBlockEntity{

    /**
     * 機械のRegistry ID
     */
    private static final ResourceLocation MACHINE_ID =
            new ResourceLocation(MainEntrance.MODID, "mechanical_crafter");

    /**
     * NBT保存キー：モジュールインベントリ。
     */
    private static final String TAG_MODULE_INVENTORY = "module_inventory";

    /**
     * メカニカルクラフターのエネルギー保存容量。
     */
    private static final double ENERGY_CAPACITY = 10000.0;

    /**
     * モジュール専用インベントリ。
     *
     * <p>
     *     1スロットのみを持ち、
     *     メカニカルクラフターに対応したMachineModuleItemのみ装着できる。
     * </p>
     */
    private final ItemStackHandler moduleInventory = new ItemStackHandler(1) {

        /**
         * スロットにアイテムを挿入できるか判定する。
         *
         * @param slot  スロット番号
         * @param stack 挿入するアイテム
         * @return 装着可能ならtrue
         */
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot != 0 || stack.isEmpty()) return false;
            if (!(stack.getItem() instanceof MachineModuleItem module)) return false;

            return module.isCompatibleWith(MACHINE_ID);
        }

        /**
         * スロットの最大スタック数を取得する。
         *
         * @param slot スロット番号
         * @return 最大スタック数(1)
         */
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        /**
         * インベントリの内容が変更された際に呼び出される。
         *
         * @param slot 変更されたスロット番号
         */
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    /**
     * メカニカルクラフターを生成する。
     *
     * @param pos Blockの座標クラス(BlockPos)
     * @param state Blockの状態クラス(BlockState)
     */
    public MechanicalCrafterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MECHANICAL_CRAFTER.get(), pos, state, new ElectricalEnergyDefinition(), ENERGY_CAPACITY);
    }

    /**
     * モジュールインベントリを取得する。
     *
     * @return モジュール専用ItemStackHandler
     */
    public ItemStackHandler getModuleInventory() {
        return moduleInventory;
    }

    /**
     * 現在装着されているモジュールを取得する。
     *
     * @return 装備されているモジュール。未装着なら空のItemStack
     */
    public ItemStack getInstalledModule() {
        return moduleInventory.getStackInSlot(0);
    }

    /**
     * モジュールが装着されているか判定する。
     *
     * @return 装着されている場合はtrue
     */
    public boolean hasModule() {
        return !getInstalledModule().isEmpty();
    }

    /**
     * 現在装着されているモジュールが有効か判定する。
     *
     * @return 有効なモジュールが装着されている場合はtrue
     */
    public boolean hasValidModule() {
        ItemStack stack = getInstalledModule();

        if (stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof MachineModuleItem module)) return false;

        return module.isCompatibleWith(MACHINE_ID);
    }

    /**
     * 機械固有のデータをNBTに保存する。
     *
     * @param tag 保存先NBT
     */
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put(TAG_MODULE_INVENTORY, moduleInventory.serializeNBT());
    }

    /**
     * NBTから機械固有のデータを復元する。
     *
     * <p>
     *     保存データが現在のエネルギー容量を超えている場合は、
     *     現在の最大容量まで補正する。
     * </p>
     *
     * @param tag 読み込み元NBT
     */
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_MODULE_INVENTORY, Tag.TAG_COMPOUND))
            moduleInventory.deserializeNBT(tag.getCompound(TAG_MODULE_INVENTORY));
    }
}
