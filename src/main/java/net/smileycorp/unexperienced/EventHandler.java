package net.smileycorp.unexperienced;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkDirection;
import net.smileycorp.unexperienced.network.BoolMessage;
import net.smileycorp.unexperienced.network.PacketHandler;

@EventBusSubscriber(modid = ModDefinitions.MODID)
public class EventHandler {

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onXPDrop(LivingExperienceDropEvent event) {
		if (CommonConfigHandler.directXP.get() &! (event.getEntityLiving() instanceof PlayerEntity)) {
			PlayerEntity player = event.getAttackingPlayer();
			addExperience(player, event.getDroppedExperience());
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof ExperienceOrbEntity && (CommonConfigHandler.disableXP.get() || CommonConfigHandler.directXP.get())) {
			if (CommonConfigHandler.directXP.get() &! entity.level.isClientSide) {
				PlayerEntity player = entity.level.getNearestPlayer(entity, 8.0D);
				if (player !=null) addExperience(player, ((ExperienceOrbEntity)entity).getValue());
			}
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (!event.getEntityLiving().level.isClientSide) {
			if (CommonConfigHandler.directXP.get() && entity instanceof EnderDragonEntity) {
				DragonFightManager fightManager = ((EnderDragonEntity) entity).getDragonFight();
				if (fightManager != null) {
					Collection<ServerPlayerEntity> players = fightManager.dragonEvent.getPlayers();
					int amount = (int) Math.ceil(((double)(fightManager.hasPreviouslyKilledDragon() ? 500 : 12000))/(double)players.size());
					for (ServerPlayerEntity player : players) addExperience(player, amount);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		if (!player.level.isClientSide && player instanceof ServerPlayerEntity)
			PacketHandler.NETWORK_INSTANCE.sendTo(new BoolMessage(CommonConfigHandler.drinkBottles.get()),
					((ServerPlayerEntity)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void addExperience(PlayerEntity player, int xpValue) {
		new ExperienceOrbEntity(player.level, 0, 0, 0, xpValue).playerTouch(player);
	}

}
