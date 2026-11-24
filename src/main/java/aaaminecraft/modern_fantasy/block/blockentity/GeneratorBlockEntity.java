package aaaminecraft.modern_fantasy.block.blockentity;

import aaaminecraft.modern_fantasy.logics.energy.core.EnergyAmount;
import aaaminecraft.modern_fantasy.logics.energy.core.EnergyDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * <h1>GeneratorBlockEntityクラス</h1>
 *
 * すべての発電機BlockEntityの共通基盤となる抽象クラス。
 *
 * <p>
 *     MachineBlockEntityを継承し、発電処理の共通関連を担当する。
 * </p>
 *
 * <p>
 *     発電量の計算は継承先のクラスに委ね、
 *     実際のエネルギー供給と容量管理は本クラスで行う。
 * </p>
 *
 * <p>
 *     発電処理はサーバー側で毎tick実行する。
 * </p>
 */
public abstract class GeneratorBlockEntity extends MachineBlockEntity{

    /**
     * GeneratorBlockEntityを生成する。
     *
     * @param type           BlockEntityType
     * @param pos            Blockの座標クラス(BlockPos)
     * @param state          Blockの状態クラス(BlockState)
     * @param definition     使用するエネルギーの定義
     * @param energyCapacity エネルギー保持容量
     */
    protected GeneratorBlockEntity(
            BlockEntityType<?> type,
            BlockPos pos,
            BlockState state,
            EnergyDefinition definition,
            double energyCapacity
    ) {
        super(type, pos, state, definition, energyCapacity);
    }

    /**
     * 発電量を計算する。
     *
     * <p>
     *     周囲のブロック、設備、環境条件などを調査し、
     *     今回のTickで発電可能なエネルギー量を返す。
     * </p>
     *
     * <p>
     *     返却する値は、実際に発電可能なエネルギー量であり、
     *     EnergyContainerの空き容量を考慮する必要はない。
     * </p>
     *
     * @return 今回のtickで発電可能なエネルギー量
     */
    protected abstract double calculateGenerationAmount();

    /**
     * 発電機のサーバー側tick処理。
     *
     * <p>
     *     BlockEntityTypeから毎tick呼び出す。
     * </p>
     *
     * @param level ワールドのインスタンス
     * @param pos Blockの座標クラス(BlockPos)
     * @param state Blockの状態クラス(BlockState)
     * @param generator 発電機BlockEntity
     */
    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            GeneratorBlockEntity generator
    ) {
        // クライアント側では発電処理を行わない。
        if (level.isClientSide) return;
        
        // プレイヤーによって無効化されている場合は停止する。
        if (!generator.isMachineActive()) return;
        
        generator.generatorEnergy();
    }

    private void generatorEnergy() {

        // 空き容量がなければ発電しない。
        if (getEnergyContainer().isFull()) return;

        // 継承先から今回の発電可能量を取得する。
        double generationAmount = calculateGenerationAmount();

        // 発電量が不正の場合は実装エラーとして扱う。
        if (!Double.isFinite(generationAmount) || generationAmount < 0.0)
            throw new IllegalStateException("[MF-MACHINE-001] Generate amount must be a finite non-negative value.");

        // 発電量が0なら処理を終了する。
        if (generationAmount <= 0.0) return;

        // エネルギーコンテナに供給する。
        EnergyAmount generatedEnergy = new EnergyAmount(
                getEnergyContainer().getDefinition(),
                generationAmount
        );

        double acceptedAmount = getEnergyContainer().receive(generatedEnergy);

        // 実際にエネルギーが増加したときのみ保存対象にする。
        if (acceptedAmount > 0.0) setChanged();
    }
}
