package net.smileycorp.unexperienced.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.smileycorp.unexperienced.CommonConfigHandler;

@Mixin(ExperienceBottleItem.class)
public class MixinExperienceBottleItem {

	@Inject(at=@At("HEAD"), method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", cancellable = true)
	public void use(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult<?>> callback) {
		if (CommonConfigHandler.canDrinkBottles()) {
			player.startUsingItem(hand);
			callback.setReturnValue(ActionResult.sidedSuccess(player.getItemInHand(hand), world.isClientSide()));
			callback.cancel();
		}
	}

}
