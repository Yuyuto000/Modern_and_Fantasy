package aaaminecraft.modern_fantasy.logics.machine.core;

import aaaminecraft.modern_fantasy.logics.energy.core.EnergyContainer;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyDefinition;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyPortAccess;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyPortManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntity extends BlockEntity implements EnergyPortAccess {

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
}
