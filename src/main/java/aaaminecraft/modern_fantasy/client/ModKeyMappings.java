package aaaminecraft.modern_fantasy.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(
        modid = "modern_fantasy",
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = net.minecraftforge.api.distmarker.Dist.CLIENT
)
public class ModKeyMappings {

    public static final KeyMapping SOFA_LIE =
            new KeyMapping("key.modern_fantasy.sofa_lie", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.modern_fantasy");

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(SOFA_LIE);
    }
}