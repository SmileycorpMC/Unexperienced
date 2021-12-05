package net.smileycorp.unexperienced;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ModDefinitions.modid, name=ModDefinitions.name, version=ModDefinitions.version)
@EventBusSubscriber(modid = ModDefinitions.modid)
public class Unexperienced {

	@Instance
	public static Unexperienced INSTANCE;

	public Unexperienced() {
		MinecraftForge.EVENT_BUS.register(this);
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new ClientHandler());
		}
	}

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile());
		ConfigHandler.syncConfig();
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		Item exp_bottle = ForgeRegistries.ITEMS.getValue(new ResourceLocation("exp_bottle"));
		ForgeRegistries.ITEMS.register(new ItemDrinkableExpBottle<>(exp_bottle));
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityXPOrb && ConfigHandler.disableXP) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (!player.world.isRemote) {
			PacketHandler.NETWORK_INSTANCE.sendTo(new BoolMessage(ConfigHandler.drinkBottles), (EntityPlayerMP) player);
		}
	}

}
