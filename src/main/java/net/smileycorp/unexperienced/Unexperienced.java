package net.smileycorp.unexperienced;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.NetworkDirection;
import net.smileycorp.unexperienced.client.ClientConfigHandler;
import net.smileycorp.unexperienced.network.BoolMessage;
import net.smileycorp.unexperienced.network.PacketHandler;

@Mod(ModDefinitions.modid)
@EventBusSubscriber(modid = ModDefinitions.modid)
public class Unexperienced {

	public Unexperienced() {
		MinecraftForge.EVENT_BUS.register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigHandler.config);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfigHandler.config);
		PacketHandler.initPackets();
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof ExperienceOrbEntity && CommonConfigHandler.disableXP.get()) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		if (!player.level.isClientSide && player instanceof ServerPlayerEntity)
			PacketHandler.NETWORK_INSTANCE.sendTo(new BoolMessage(CommonConfigHandler.drinkBottles.get()),
					((ServerPlayerEntity)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

}
