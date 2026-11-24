package aaaminecraft.modern_fantasy.block.blockentity;

import aaaminecraft.modern_fantasy.logics.energy.core.EnergyContainer;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyDefinition;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyPortAccess;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyPortManager;
import aaaminecraft.modern_fantasy.logics.machine.core.MachineState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * <h1>MachineBlockEntityクラス</h1>
 *
 * すべての機械BlockEntityの共通基盤となる抽象クラス。
 *
 * <p>
 *     機械の稼働状況、エネルギー保持、エネルギーポート管理、
 *     及び機械データの保存・復元を担当する。
 * </p>
 *
 * <p>
 *     発電処理やエネルギー消費処理など、機械固有の動作は
 *     継承先のクラスで実装する。
 * </p>
 *
 * <p>
 *     このクラスはEnergyPortAccessを実装し、
 *     エネルギーネットワークとの接続を可能にする。
 * </p>
 */
public abstract class MachineBlockEntity extends BlockEntity implements EnergyPortAccess {

    /**
     * NBT保存キー:機械の稼働状態。
     */
    private static final String TAG_MACHINE_ENABLED = "machine_enabled";

    /**
     * NBT保存キー:エネルギー保有量。
     */
    private static final String TAG_ENERGY_AMOUNT = "energy_amount";

    /**
     * 機械の稼働状態。
     */
    private final MachineState machineState;

    /**
     * 機械が保持するエネルギー。
     */
    private final EnergyContainer energyContainer;

    /**
     * 機械が持つエネルギーポートを管理する。
     */
    private final EnergyPortManager energyPortManager;

    /**
     * 機械BlockEntityを生成する。
     *
     * @param type BlockEntityType
     * @param pos Blockの座標クラス(BlockPos)
     * @param state Blockの状態クラス(BlockState)
     * @param definition 使用するエネルギーの定義
     * @param energyCapacity エネルギー保持容量
     */
    protected MachineBlockEntity(
            BlockEntityType<?> type,
            BlockPos pos,
            BlockState state,
            EnergyDefinition definition,
            double energyCapacity
    ) {
        super(type, pos, state);

        this.machineState = new MachineState(false);
        this.energyContainer = new EnergyContainer(definition,  energyCapacity);
        this.energyPortManager = new EnergyPortManager();
    }

    /**
     * 機械の稼働状態を取得する。
     *
     * @return MachineState
     */
    public MachineState getMachineState() {
        return machineState;
    }

    /**
     * 機械が有効化されているかを取得する。
     *
     * @return 有効化されている場合はtrue
     */
    public boolean isMachineActive() {
        return machineState.isEnabled();
    }

    public void setMachineActive(boolean enabled) {
        if (machineState.isEnabled() == enabled) return;

        machineState.setEnabled(enabled);
        setChanged();
    }

    /**
     * 機械を有効化する。
     */
    public void enableMachine() {
        setMachineActive(true);
    }

    /**
     * 機械を無効化する。
     */
    public void disableMachine() {
        setMachineActive(false);
    }

    /**
     * 機械の稼働状態を反転する。
     */
    public void toggleMachine() {
        setMachineActive(!machineState.isEnabled());
    }

    /**
     * 機械が保持するエネルギーコンテナを取得する。
     *
     * @return EnergyContainer
     */
    @Override
    public EnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    /**
     * 機械のエネルギーポート管理オブジェクトを取得する。
     *
     * @return EnergyPortManager
     */
    @Override
    public EnergyPortManager getEnergyPortManager() {
        return energyPortManager;
    }

    /**
     * 機械固有のデータをNBTに保存する。
     *
     * @param tag 保存先NBT
     */
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putBoolean(TAG_MACHINE_ENABLED, machineState.isEnabled());
        tag.putDouble(TAG_ENERGY_AMOUNT, energyContainer.getAmount());
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

        machineState.setEnabled(tag.getBoolean(TAG_MACHINE_ENABLED));
        double savedAmount = 0.0;

        if (tag.contains(TAG_ENERGY_AMOUNT, Tag.TAG_DOUBLE)) savedAmount = tag.getDouble(TAG_ENERGY_AMOUNT);

        // 不正な値を排除する
        if (!Double.isFinite(savedAmount) || savedAmount < 0.0) savedAmount = 0.0;

        // 現在の容量を超えないように補正する
        savedAmount = Math.min(savedAmount, energyContainer.getCapacity());
        energyContainer.setAmount(savedAmount);
    }
}
