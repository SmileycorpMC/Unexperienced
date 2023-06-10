package net.smileycorp.unexperienced;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.smileycorp.unexperienced.client.ClientConfigHandler;
import net.smileycorp.unexperienced.network.PacketHandler;

@Mod(Constants.MODID)
public class Unexperienced {

	public Unexperienced() {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigHandler.config);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfigHandler.config);
		PacketHandler.initPackets();
	}

}
