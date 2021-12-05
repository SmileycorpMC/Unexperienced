package net.smileycorp.unexperienced;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientHandler {
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void renderOverlay(RenderGameOverlayEvent event) {
		if (event.getType() == ElementType.EXPERIENCE && ConfigHandler.hideBar) {
			event.setCanceled(true);
		}
	}
	
}
