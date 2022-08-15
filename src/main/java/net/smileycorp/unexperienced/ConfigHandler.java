package net.smileycorp.unexperienced;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigHandler {

	public static Configuration config;

	public static boolean disableXP;
	public static boolean directXP;
	static boolean drinkBottles;
	static boolean drinkBottlesClient;
	public static int bottleExperience;
	public static boolean hideBar;
	private static String[] showBarItems;
	private static String[] showBarBlocks;

	public static void syncConfig(){
		try{
			config.load();
			disableXP = config.get("general", "disableXP",
					true, "Should xp drops be disabled.").getBoolean();

			directXP = config.get("general", "directXP",
					false, "Should mob xp drops be given directly to the player?").getBoolean();

			drinkBottles = config.get("general", "drinkBottles",
					true, "Can Bottles of enchanting be drunk to gain experience?").getBoolean();

			bottleExperience = config.get("general", "bottleExperience",
					7, "How many experience points to Bottles of Enchanting give if drunk?").getInt();

			hideBar = config.get("client", "hideBar",
					false, "Should the exp bar be hidden?").getBoolean();

			showBarItems = config.get("client", "showBarItems",
					new String[]{"minecraft:experience_bottle"}, "Items that when held show the xp bar, if hideBar is false").getStringList();

			showBarBlocks = config.get("client", "showBarBlocks",
					new String[]{"minecraft:enchanting_table", "minecraft:anvil"}, "Blocks that when hovered over show the xp bar, if hideBar is false").getStringList();

		} catch (Exception e) {
		} finally {
			if (config.hasChanged()) config.save();
		}
	}

	public static boolean canDrinkBottles() {
		return FMLCommonHandler.instance().getSide() == Side.CLIENT ? drinkBottlesClient : drinkBottles;
	}

	public static boolean shouldShowBar(ItemStack stack) {
		if (stack != null) {
			String item = stack.getItem().getRegistryName().toString();
			for (String check : showBarItems) if (check.equals(item)) return true;
		}
		return false;
	}

	public static boolean shouldShowBar(IBlockState state) {
		if (state != null) {
			String block = state.getBlock().getRegistryName().toString();
			for (String check : showBarBlocks) if (check.equals(block)) return true;
		}
		return false;
	}
}
