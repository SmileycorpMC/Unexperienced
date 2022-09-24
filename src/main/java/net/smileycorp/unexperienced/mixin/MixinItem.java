package net.smileycorp.unexperienced.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.world.World;
import net.smileycorp.unexperienced.CommonConfigHandler;

@Mixin(Item.class)
public class MixinItem {

	@Inject(at=@At("HEAD"), method = "getUseDuration(Lnet/minecraft/item/ItemStack;)I", cancellable = true)
	public void getUseDuration(ItemStack stack, CallbackInfoReturnable<Integer> callback) {
		if (CommonConfigHandler.canDrinkBottles() && stack.getItem() == Items.EXPERIENCE_BOTTLE) {
			callback.setReturnValue(32);
			callback.cancel();
		}
	}

	@Inject(at=@At("HEAD"), method = "getUseAnimation(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/UseAction;", cancellable = true)
	public void getUseAnimation(ItemStack stack, CallbackInfoReturnable<UseAction> callback) {
		if (CommonConfigHandler.canDrinkBottles() && stack.getItem() == Items.EXPERIENCE_BOTTLE) {
			callback.setReturnValue(UseAction.DRINK);
			callback.cancel();
		}
	}

	@Inject(at=@At("HEAD"), method = "finishUsingItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", cancellable = true)
	public void finishUsingItem(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable<ItemStack> callback) {
		if (CommonConfigHandler.canDrinkBottles() && stack.getItem() == Items.EXPERIENCE_BOTTLE) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				player.giveExperiencePoints(CommonConfigHandler.bottleExperience.get());
				if (player instanceof ServerPlayerEntity) {
					CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)player, stack);
				}
				if (stack.getCount()>1 &! player.isCreative()) {
					ItemStack drop = new ItemStack(Items.GLASS_BOTTLE);
					if (!player.inventory.add(drop)) {
						player.drop(drop, false);
					}
				}
			}
			stack.shrink(1);
			callback.setReturnValue(stack.getCount() < 1 ? new ItemStack(Items.GLASS_BOTTLE) : stack);
			callback.cancel();
		}
	}

}
