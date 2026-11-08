
package aaaminecraft.modern_fantasy.network;

import aaaminecraft.modern_fantasy.block.customs.ModernSofaBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SofaLieC2SPacket {

    private final BlockPos pos;

    public SofaLieC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(SofaLieC2SPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
    }

    public static SofaLieC2SPacket decode(FriendlyByteBuf buffer) {
        return new SofaLieC2SPacket(buffer.readBlockPos());
    }

    public static void handle(SofaLieC2SPacket packet, Supplier<NetworkEvent.Context> ctx) {

        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {

            ServerPlayer player = context.getSender();

            if (player == null || !player.isAlive() || player.isSpectator()) return;
            if (player.isPassenger()) return;

            BlockPos pos = packet.pos;

            if (!player.serverLevel().hasChunkAt(pos)) return;
            if (player.distanceToSqr(Vec3.atCenterOf(pos)) > 36.0D) return;

            if (player.serverLevel().getBlockState(pos).getBlock() instanceof ModernSofaBlock sofaBlock) {
                sofaBlock.handleLieRequest(player.serverLevel(), pos, player);
            }
        });

        context.setPacketHandled(true);
    }
}
