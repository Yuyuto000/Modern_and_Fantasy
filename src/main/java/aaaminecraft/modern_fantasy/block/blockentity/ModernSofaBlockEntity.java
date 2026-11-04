package aaaminecraft.modern_fantasy.block.blockentity;

import aaaminecraft.modern_fantasy.block.blockstate.SofaBlockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class ModernSofaBlockEntity extends BlockEntity {

    private static final String TAG_CONNECTED_POS = "ConnectedPos";
    private static final String TAG_LYING_PLAYER = "LyingPlayer";
    private static final String TAG_SEATED_PLAYER = "SeatedPlayer";

    @Nullable
    private BlockPos connectedPos;

    @Nullable
    private UUID lyingPlayer;

    @Nullable
    private UUID seatedPlayer;

    public ModernSofaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MODERN_SOFA.get(), pos, state);
    }

    // ==================================================
    // 接続先
    // ==================================================

    @Nullable
    public BlockPos getConnectedPos() {
        return connectedPos;
    }

    public void setConnectedPos(@Nullable BlockPos pos) {
        if (pos == null) {
            connectedPos = null;
        } else {
            connectedPos = pos.immutable();
        }

        setChangedAndSync();
    }

    // 利用者
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

    public void setOccupants(@Nullable UUID lying, @Nullable UUID seated) {
        lyingPlayer = lying;
        seatedPlayer = seated;
        setChangedAndSync();
    }

    // HEAD / FOOT判定
    public boolean isHead() {
        return level != null
                && level.getBlockState(worldPosition).hasProperty(aaaminecraft.modern_fantasy.block.customs.ModernSofaBlock.BLOCK_PART)
                && level.getBlockState(worldPosition).getValue(aaaminecraft.modern_fantasy.block.customs.ModernSofaBlock.BLOCK_PART) == SofaBlockPart.HEAD;
    }
    // 保存

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (connectedPos != null) {
            tag.putLong(TAG_CONNECTED_POS, connectedPos.asLong());
        }

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

        connectedPos = tag.contains(TAG_CONNECTED_POS)
                ? BlockPos.of(tag.getLong(TAG_CONNECTED_POS))
                : null;

        lyingPlayer = tag.hasUUID(TAG_LYING_PLAYER)
                ? tag.getUUID(TAG_LYING_PLAYER)
                : null;

        seatedPlayer = tag.hasUUID(TAG_SEATED_PLAYER)
                ? tag.getUUID(TAG_SEATED_PLAYER)
                : null;
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