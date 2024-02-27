package net.smileycorp.unexperienced;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	public static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.modid);
	
	public static void initPackets() {
		NETWORK_INSTANCE.registerMessage(ConfigSyncHandler.class, BoolMessage.class, 0, Side.CLIENT);
	}
	
	public static class ConfigSyncHandler implements IMessageHandler<BoolMessage, IMessage> {

		public ConfigSyncHandler() {}

		@Override
		public IMessage onMessage(BoolMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> ConfigHandler.drinkBottlesClient = message.getValue());
			return null;
		}
	}
	
}
