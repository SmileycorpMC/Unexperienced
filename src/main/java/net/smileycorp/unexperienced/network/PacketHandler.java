package net.smileycorp.unexperienced.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.smileycorp.unexperienced.CommonConfigHandler;
import net.smileycorp.unexperienced.Constants;

public class PacketHandler {

	public static SimpleChannel NETWORK_INSTANCE;

	public static void initPackets() {
		NETWORK_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Constants.MODID, "main"),
				()-> "1", "1"::equals, "1"::equals);
		NETWORK_INSTANCE.registerMessage(0, BoolMessage.class, PacketHandler::writeMessage,
				PacketHandler::readMessage, (T, K)-> processSyncMessage(T, K.get()));
	}

	private static void writeMessage(BoolMessage msg, FriendlyByteBuf buf) {
		try {
			msg.write(buf);
		}
		catch(Exception e){}
	}

	private static BoolMessage readMessage(FriendlyByteBuf buf) {
		BoolMessage msg = new BoolMessage();
		try {
			msg.read(buf);
		}
		catch(Exception e){}
		return msg;
	}

	public static void processSyncMessage(BoolMessage message, Context ctx) {
		ctx.enqueueWork(() ->  DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> CommonConfigHandler.drinkBottlesClient = message.getValue()));
		ctx.setPacketHandled(true);
	}

}
