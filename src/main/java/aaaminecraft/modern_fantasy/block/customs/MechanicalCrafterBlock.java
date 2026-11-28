package aaaminecraft.modern_fantasy.block.customs;

import aaaminecraft.modern_fantasy.block.blockentity.MechanicalCrafterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * <h1>MechanicalCrafterBlockクラス</h1>
 *
 * <p>
 *     機械加工モジュールを交換することで機能を切り替えられる
 *     多目的加工機のブロック本体。
 * </p>
 *
 * <p>加工処理やエネルギー消費は、後続のシステムで実装する。</p>
 */
public class MechanicalCrafterBlock extends BaseEntityBlock {

    /**
     * Mechanical Crafterのブロックを生成する。
     *
     * @param properties ブロックのプロパティ
     */
    public MechanicalCrafterBlock(Properties properties) {
        super(properties);
    }


    /**
     * Mechanical CrafterのBlockEntityを生成する。
     *
     * @param pos ブロック座標クラス(BlockPos)
     * @param state ブロック状態クラス(BlockState)
     * @return 生成されたBlockEntity
     */
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MechanicalCrafterBlockEntity(pos, state);
    }

    /**
     * @param world ワールド
     * @param state ブロック状態クラス(BlockState)
     * @param blockEntityType BlockEntityの型
     * @param <T> BlockEntityの型
     * @return Ticker
     */
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntityType) {
        return null;
    }

    /**
     * ブロックの描画形式を取得する。
     *
     * @param state ブロック状態クラス(BlockState)
     * @return 描画形式
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
