package net.smileycorp.unexperienced;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemDrinkableExpBottle extends ItemExpBottle {

	public ItemDrinkableExpBottle() {
		super();
		setRegistryName("minecraft", "experience_bottle");
		setUnlocalizedName("expBottle");
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return ConfigHandler.canDrinkBottles() ? 32 : super.getMaxItemUseDuration(stack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return ConfigHandler.canDrinkBottles() ? EnumAction.DRINK : super.getItemUseAction(stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (ConfigHandler.canDrinkBottles()) {
			player.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		} return super.onItemRightClick(world, player, hand);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
		if (ConfigHandler.canDrinkBottles()) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.addExperience(ConfigHandler.bottleExperience);
				if (player instanceof EntityPlayerMP) {
					CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
				}
				if (stack.getCount()>1 &! player.isCreative()) {
					player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
				}
			}
			stack.shrink(1);
			return stack.getCount() < 1 ? new ItemStack(Items.GLASS_BOTTLE) : stack;
		} return super.onItemUseFinish(stack, world, entity);
	}

}
