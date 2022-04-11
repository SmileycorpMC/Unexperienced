package net.smileycorp.unexperienced.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.smileycorp.unexperienced.CommonConfigHandler;
import net.smileycorp.unexperienced.ModDefinitions;

public class PacketHandler {

	public static SimpleChannel NETWORK_INSTANCE;

	public static void initPackets() {
		NETWORK_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModDefinitions.modid, "main"),
				()-> "1", "1"::equals, "1"::equals);
		NETWORK_INSTANCE.registerMessage(0, BoolMessage.class, (t, buf)->{try {t.write(buf);}catch(Exception e){}},
				(buf)-> {try{new BoolMessage().read(buf);}catch(Exception e){} return null;}, (T, K)-> processSyncMessage(T, K.get()));
	}

	public static void processSyncMessage(BoolMessage message, Context ctx) {
		ctx.enqueueWork(() ->  DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> CommonConfigHandler.drinkBottlesClient = message.getValue()));
		ctx.setPacketHandled(true);
	}

}
