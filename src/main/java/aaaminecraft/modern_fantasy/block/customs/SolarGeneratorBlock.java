package aaaminecraft.modern_fantasy.block.customs;

import aaaminecraft.modern_fantasy.block.blockentity.SolarGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * <h1>SolarGeneratorBlock</h1>
 *
 * 太陽光発電機。
 *
 * <p>
 *     Minecraftのワールド上に設置される太陽光発電機を定義する。
 *     発電処理及びエネルギー管理はSolarGeneratorBlockEntityに委譲する。
 * </p>
 *
 * <p>
 *     サーバー側でのみ発電Tickを実行する。
 * </p>
 */
public class SolarGeneratorBlock extends BaseEntityBlock {

    /**
     * 太陽光発電Blockを生成する。
     *
     * @param properties Blockの基本プロパティ
     */
    public SolarGeneratorBlock(Properties properties) {
        super(properties);
    }

    /**
     * このBlockに対応するBlockEntityを生成する。
     *
     * @param pos Blockの座標
     * @param state Blockの状態
     * @return SolarGeneratorBlockEntity
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    /**
     * 太陽光発電機のTickerを取得する。
     *
     * @param level ワールド
     * @param state Blockの状態
     * @param type BlockEntityType
     * @param <T> BlockEntity型
     * @return サーバー側の発電Ticker
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (level.isClientSide) return null;
        return createTickerHelper(
                type,
                /* 登録後にSolarGeneratorBlockEntity.TYPEへ変更 */
                null,
                SolarGeneratorBlockEntity::serverTick
        );
    }
}
