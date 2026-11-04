package aaaminecraft.modern_fantasy.network;

import aaaminecraft.modern_fantasy.MainEntrance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL =
            NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(MainEntrance.MODID, "main"),
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals,
                    PROTOCOL_VERSION::equals
            );

    private static int packetId = 0;

    public static void register(){
/*
        CHANNEL.registerMessage(
                packetId++,
                SofaLieC2SPacket.class,
                SofaLieC2SPacket::encode,
                SofaLieC2SPacket::decode,
                SofaLieC2SPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
*/
    }
}
