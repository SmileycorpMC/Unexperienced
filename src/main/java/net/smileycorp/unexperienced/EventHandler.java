package net.smileycorp.unexperienced;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkDirection;
import net.smileycorp.unexperienced.network.BoolMessage;
import net.smileycorp.unexperienced.network.PacketHandler;

import java.util.Collection;

@EventBusSubscriber(modid = Constants.MODID)
public class EventHandler {

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onXPDrop(LivingExperienceDropEvent event) {
		if (CommonConfigHandler.directXP.get() &! (event.getEntity() instanceof Player)) {
			Player player = event.getAttackingPlayer();
			addExperience(player, event.getDroppedExperience());
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityJoinWorld(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof ExperienceOrb && (CommonConfigHandler.disableXP.get() || CommonConfigHandler.directXP.get())) {
			if (CommonConfigHandler.directXP.get() &! entity.level().isClientSide) {
				Player player = entity.level().getNearestPlayer(entity, 8.0D);
				if (player !=null) addExperience(player, ((ExperienceOrb)entity).getValue());
			}
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if (!event.getEntity().level().isClientSide) {
			if (CommonConfigHandler.directXP.get() && entity instanceof EnderDragon) {
				EndDragonFight fightManager = ((EnderDragon) entity).getDragonFight();
				if (fightManager != null) {
					Collection<ServerPlayer> players = fightManager.dragonEvent.getPlayers();
					int amount = (int) Math.ceil(((double)(fightManager.hasPreviouslyKilledDragon() ? 500 : 12000))/(double)players.size());
					for (ServerPlayer player : players) addExperience(player, amount);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();
		if (!player.level().isClientSide && player instanceof ServerPlayer)
			PacketHandler.NETWORK_INSTANCE.sendTo(new BoolMessage(CommonConfigHandler.drinkBottles.get()),
					((ServerPlayer)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		if (event.getItemStack() != null && event.getItemStack().getItem() == Items.EXPERIENCE_BOTTLE
				&& CommonConfigHandler.canDrinkBottles()) {
			event.getToolTip().add(1, MutableComponent.create(new TranslatableContents("tooltip.unexperienced.ExperienceBottle",
					"Drinkable", new Object[0])));
		}
	}

	public static void addExperience(Player player, int xpValue) {
		if (player != null) new ExperienceOrb(player.level(), 0, 0, 0, xpValue).playerTouch(player);
	}

}
