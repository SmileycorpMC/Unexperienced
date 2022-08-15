package net.smileycorp.unexperienced;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientHandler {

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void renderOverlay(RenderGameOverlayEvent event) {
		if (event.getType() == ElementType.EXPERIENCE && ConfigHandler.hideBar) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayerSP player = mc.player;
			if (ConfigHandler.shouldShowBar(player.getHeldItemMainhand())) return;
			if (ConfigHandler.shouldShowBar(player.getHeldItemOffhand())) return;
			RayTraceResult hovered = mc.objectMouseOver;
			if (hovered.typeOfHit == RayTraceResult.Type.BLOCK) {
				IBlockState state = mc.world.getBlockState(hovered.getBlockPos());
				if (ConfigHandler.shouldShowBar(state)) return;
			}
			event.setCanceled(true);
		}
	}

}
