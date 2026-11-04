
package aaaminecraft.modern_fantasy.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class ModernSofaBlockEntity extends BlockEntity {

    private static final String TAG_LYING_PLAYER = "LyingPlayer";
    private static final String TAG_SEATED_PLAYER = "SeatedPlayer";

    @Nullable
    private UUID lyingPlayer;

    @Nullable
    private UUID seatedPlayer;

    public ModernSofaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MODERN_SOFA.get(), pos, state);
    }

    // 利用状態の取得
    @Nullable
    public UUID getLyingPlayer() {
        return lyingPlayer;
    }

    @Nullable
    public UUID getSeatedPlayer() {
        return seatedPlayer;
    }

    public boolean isLyingOccupied() {
        return lyingPlayer != null;
    }

    public boolean isSeatOccupied() {
        return seatedPlayer != null;
    }

    // 利用状態の設定
    public boolean setLyingPlayer(@Nullable UUID player) {
        if (player != null && lyingPlayer != null) {
            return false;
        }

        lyingPlayer = player;
        setChangedAndSync();
        return true;
    }

    public boolean setSeatedPlayer(@Nullable UUID player) {
        if (player != null && seatedPlayer != null) {
            return false;
        }

        seatedPlayer = player;
        setChangedAndSync();
        return true;
    }

    // 利用状態の解除
    public void clearLyingPlayer() {
        lyingPlayer = null;
        setChangedAndSync();
    }

    public void clearSeatedPlayer() {
        seatedPlayer = null;
        setChangedAndSync();
    }

    public void clearOccupants() {
        lyingPlayer = null;
        seatedPlayer = null;
        setChangedAndSync();
    }

    // 利用状態の一括設定
    public void setOccupants(@Nullable UUID lying, @Nullable UUID seated) {
        lyingPlayer = lying;
        seatedPlayer = seated;
        setChangedAndSync();
    }

    // 保存
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (lyingPlayer != null) {
            tag.putUUID(TAG_LYING_PLAYER, lyingPlayer);
        }

        if (seatedPlayer != null) {
            tag.putUUID(TAG_SEATED_PLAYER, seatedPlayer);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        lyingPlayer = tag.hasUUID(TAG_LYING_PLAYER) ? tag.getUUID(TAG_LYING_PLAYER) : null;
        seatedPlayer = tag.hasUUID(TAG_SEATED_PLAYER) ? tag.getUUID(TAG_SEATED_PLAYER) : null;
    }

    // クライアント同期
    private void setChangedAndSync() {
        setChanged();

        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
