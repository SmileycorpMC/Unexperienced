package net.smileycorp.unexperienced.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.smileycorp.unexperienced.CommonConfigHandler;
import net.smileycorp.unexperienced.EventHandler;

@Mixin(Item.class)
public class MixinItem {

	@Inject(at=@At("HEAD"), method = "m_8105_(Lnet/minecraft/world/item/ItemStack;)I", cancellable = true, remap = false)
	public void getUseDuration(ItemStack stack, CallbackInfoReturnable<Integer> callback) {
		if (CommonConfigHandler.canDrinkBottles() && stack.getItem() == Items.EXPERIENCE_BOTTLE) {
			callback.setReturnValue(32);
			callback.cancel();
		}
	}

	@Inject(at=@At("HEAD"), method = "m_6164_(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/UseAnim;", cancellable = true, remap = false)
	public void getUseAnimation(ItemStack stack, CallbackInfoReturnable<UseAnim> callback) {
		if (CommonConfigHandler.canDrinkBottles() && stack.getItem() == Items.EXPERIENCE_BOTTLE) {
			callback.setReturnValue(UseAnim.DRINK);
			callback.cancel();
		}
	}

	@Inject(at=@At("HEAD"), method = "m_5922_(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;", cancellable = true, remap = false)
	public void finishUsingItem(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> callback) {
		if (CommonConfigHandler.canDrinkBottles() && stack.getItem() == Items.EXPERIENCE_BOTTLE) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				EventHandler.addExperience(player, CommonConfigHandler.bottleExperience.get());
				if (player instanceof ServerPlayer) {
					CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
				}
				if (stack.getCount()>1 &! player.isCreative()) {
					ItemStack drop = new ItemStack(Items.GLASS_BOTTLE);
					if (!player.getInventory().add(drop)) {
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
