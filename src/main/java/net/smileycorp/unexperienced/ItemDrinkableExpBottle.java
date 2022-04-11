package net.smileycorp.unexperienced;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemDrinkableExpBottle<T extends Item> extends ItemExpBottle {
	
	private T wrapped;
	
	public ItemDrinkableExpBottle(T base) {
		super();
		setRegistryName("minecraft", "experience_bottle");
		setUnlocalizedName("expBottle");
		wrapped = base;
	}
	
 	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
        return CommonConfigHandler.canDrinkBottles() ? 32 : wrapped.getMaxItemUseDuration(stack);
    }

    @Override
	public EnumAction getItemUseAction(ItemStack stack) {
        return CommonConfigHandler.canDrinkBottles() ? EnumAction.DRINK : wrapped.getItemUseAction(stack);
    }

    @Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    	if (CommonConfigHandler.canDrinkBottles()) {
	        player.setActiveHand(hand);
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    	} return wrapped.onItemRightClick(world, player, hand);
    }

    @Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
    	if (CommonConfigHandler.canDrinkBottles()) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.addExperience(CommonConfigHandler.bottleExperience);
				if (player instanceof EntityPlayerMP) {
					CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
				}
				if (stack.getCount()>1 &! player.isCreative()) {
					player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
				}
			}
			stack.shrink(1);
			return stack.getCount() < 1 ? new ItemStack(Items.GLASS_BOTTLE) : stack;
    	} return wrapped.onItemUseFinish(stack, world, entity);
	}
	
}
