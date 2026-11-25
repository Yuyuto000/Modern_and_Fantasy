package aaaminecraft.modern_fantasy.block.blockentity;

import aaaminecraft.modern_fantasy.logics.energy.electronic.ElectricalEnergyDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * <h1>SolarGeneratorBlockEntityクラス</h1>
 *
 * <p>
 *     昼夜および天候に応じて発電量を変化させる。
 *     発電した電気エネルギーは、親クラスのGeneratorBlockEntityが
 *     管理するEnergyContainerへ格納される。
 * </p>
 *
 * <p>この発電機は設置した場所による発電制限を持たない。</p>
 */
public class SolarGeneratorBlockEntity extends GeneratorBlockEntity {

    /**
     * 基本発電量(1Tickあたり)
     */
    private static final double BASE_GENERATION = 100.0;

    /**
     * 雨天時の発電倍率
     */
    private static final double RAIN_MULTIPLIER = 0.5;

    /**
     * 雷雨時の発電効率。
     */
    private static final double THUNDER_MULTIPLIER = 0.25;

    /**
     * 太陽光発電を生成する。
     *
     * @param pos 設置位置
     * @param state ブロックの状態
     */
    public SolarGeneratorBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        super(ModBlockEntities.SOLAR_GENERATOR.get(), pos, state, new ElectricalEnergyDefinition(), 10000.0);
    }

    /**
     * 環境条件に応じた発電可能量を計算する。
     *
     * @return 1tickで発電可能なエネルギー量
     */
    @Override
    protected double calculateGenerationAmount() {

        Level level = getLevel();
        if (level == null) return 0.0;

        // 夜間は発電しない。
        if (!level.isDay()) return 0.0;

        // 雷雨時は発電量を大きく低下させる。
        if (level.isThundering()) return BASE_GENERATION * THUNDER_MULTIPLIER;

        // 雷雨時は発電量を低下させる。
        if (level.isRaining()) return BASE_GENERATION * RAIN_MULTIPLIER;

        // 晴天時は最大発電量。
        return BASE_GENERATION;
    }
}
