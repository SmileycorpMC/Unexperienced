package net.smileycorp.unexperienced.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.smileycorp.unexperienced.client.ClientConfigHandler;

@Mixin(Gui.class)
public class MixinGui {

	@Inject(at=@At("HEAD"), method = "m_93071_(Lcom/mojang/blaze3d/vertex/PoseStack;I)V", cancellable = true, remap = false)
	public void renderExperienceBar(PoseStack poseStack, int p_93073_, CallbackInfo callback) {
		if (!ClientConfigHandler.hideBar.get()) return;
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (ClientConfigHandler.shouldShowBar(player.getMainHandItem())) return;
		if (ClientConfigHandler.shouldShowBar(player.getOffhandItem())) return;
		HitResult hovered = mc.hitResult;
		if (hovered != null) {
			if (hovered.getType() == HitResult.Type.BLOCK) {
				BlockState state = mc.level.getBlockState(new BlockPos(hovered.getLocation()));
				if (ClientConfigHandler.shouldShowBar(state)) return;
			}
		}
		callback.cancel();
	}


}
