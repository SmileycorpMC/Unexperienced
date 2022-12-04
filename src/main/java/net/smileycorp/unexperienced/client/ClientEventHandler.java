package net.smileycorp.unexperienced.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.smileycorp.unexperienced.ModDefinitions;

@EventBusSubscriber(modid = ModDefinitions.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void renderOverlay(RenderGameOverlayEvent event) {
		if (event.getType() == ElementType.EXPERIENCE && ClientConfigHandler.hideBar.get()) {
			Minecraft mc = Minecraft.getInstance();
			ClientPlayerEntity player = mc.player;
			if (ClientConfigHandler.shouldShowBar(player.getMainHandItem())) return;
			if (ClientConfigHandler.shouldShowBar(player.getOffhandItem())) return;
			RayTraceResult hovered = mc.hitResult;
			if (hovered != null) {
				if (hovered.getType() == RayTraceResult.Type.BLOCK) {
					BlockState state = mc.level.getBlockState(new BlockPos(hovered.getLocation()));
					if (ClientConfigHandler.shouldShowBar(state)) return;
				}
			}
			event.setCanceled(true);
		}
	}

}
