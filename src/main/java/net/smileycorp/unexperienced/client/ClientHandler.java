package net.smileycorp.unexperienced.client;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientHandler {

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void renderOverlay(RenderGameOverlayEvent event) {
		if (event.getType() == ElementType.EXPERIENCE && ClientConfigHandler.hideBar.get()) {
			event.setCanceled(true);
		}
	}

}
