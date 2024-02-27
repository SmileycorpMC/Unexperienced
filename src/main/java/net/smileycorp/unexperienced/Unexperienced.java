package net.smileycorp.unexperienced;

import java.util.Collection;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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

@Mod(modid = Constants.modid, name= Constants.name, version= Constants.version)
@EventBusSubscriber(modid = Constants.modid)
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
		EntityLivingBase entity = event.getEntityLiving();
		if (ConfigHandler.directXP &! (entity instanceof EntityPlayer)) {
			EntityPlayer player = event.getAttackingPlayer();
			if (player == null) player = entity.world.getClosestPlayerToEntity(entity, 16);
			addExperience(player, event.getDroppedExperience());
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityXPOrb && (ConfigHandler.disableXP || ConfigHandler.directXP)) {
			if (ConfigHandler.directXP &! entity.world.isRemote) {
				EntityPlayer player = entity.world.getClosestPlayerToEntity(entity, 16);
				if (player !=null) addExperience(player, ((EntityXPOrb)entity).xpValue);
			}
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityDeath(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!event.getEntityLiving().world.isRemote) {
			if (ConfigHandler.directXP && entity instanceof EntityDragon) {
				DragonFightManager fightManager = ((EntityDragon) entity).getFightManager();
				if (fightManager != null) {
					Collection<EntityPlayerMP> players = fightManager.bossInfo.getPlayers();
					int amount = (int) Math.ceil(((double)(fightManager.hasPreviouslyKilledDragon() ? 500 : 12000))/(double)players.size());
					for (EntityPlayer player : players) addExperience(player, amount);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (!player.world.isRemote) PacketHandler.NETWORK_INSTANCE.sendTo(new BoolMessage(ConfigHandler.drinkBottles), (EntityPlayerMP) player);
	}

	public static void addExperience(EntityPlayer player, int xpValue) {
		if (player == null) return;
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
