package net.smileycorp.unexperienced;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
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
		PacketHandler.initPackets();
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		ForgeRegistries.ITEMS.register(new ItemDrinkableExpBottle());
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onXPDrop(LivingExperienceDropEvent event) {
		if (ConfigHandler.directXP &! (event.getEntityLiving() instanceof EntityPlayer)) {
			EntityPlayer player = event.getAttackingPlayer();
			addExperience(player, event.getDroppedExperience());
			event.setCanceled(true);
		}
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
		if (!player.world.isRemote) PacketHandler.NETWORK_INSTANCE.sendTo(new BoolMessage(ConfigHandler.drinkBottles), (EntityPlayerMP) player);
	}

	public static void addExperience(EntityPlayer player, int xpValue) {
		player.onItemPickup(new EntityXPOrb(player.world, 0, 0, 0, xpValue), 1);
		ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, player);
		if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
			float ratio = itemstack.getItem().getXpRepairRatio(itemstack);
			int i = Math.min(roundAverage(xpValue * ratio), itemstack.getItemDamage());
			xpValue -= roundAverage(i / ratio);
			itemstack.setItemDamage(itemstack.getItemDamage() - i);
		}
		if (xpValue > 0) player.addExperience(xpValue);
	}

	private static int roundAverage(float value) {
		double floor = Math.floor(value);
		return (int) floor + (Math.random() < value - floor ? 1 : 0);
	}

}
