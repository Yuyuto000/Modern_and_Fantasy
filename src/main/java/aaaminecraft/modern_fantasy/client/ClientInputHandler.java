package aaaminecraft.modern_fantasy.client;

import aaaminecraft.modern_fantasy.block.customs.ModernSofaBlock;
import aaaminecraft.modern_fantasy.network.NetworkHandler;
import aaaminecraft.modern_fantasy.network.SofaLieC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = "modern_fantasy",
        value = Dist.CLIENT
)
public class ClientInputHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        while (ModKeyMappings.SOFA_LIE.consumeClick()) {

            if (!(minecraft.hitResult instanceof BlockHitResult hit)) {
                continue;
            }

            if (hit.getType() != HitResult.Type.BLOCK) {
                continue;
            }

            BlockPos pos = hit.getBlockPos();

            if (!(minecraft.level.getBlockState(pos).getBlock() instanceof ModernSofaBlock)) {
                continue;
            }

            NetworkHandler.CHANNEL.sendToServer(new SofaLieC2SPacket(pos));
        }
    }
}