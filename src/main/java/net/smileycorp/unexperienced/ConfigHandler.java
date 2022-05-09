package net.smileycorp.unexperienced;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigHandler {

	public static Configuration config;

	public static boolean disableXP;
	static boolean drinkBottles;
	static boolean drinkBottlesClient;
	public static int bottleExperience;
	public static boolean hideBar;

	public static void syncConfig(){
		try{
			config.load();
			disableXP = config.get("general", "disableXP",
					true, "Should xp drops be disabled.").getBoolean();

			drinkBottles = config.get("general", "drinkBottles",
					true, "Can Bottles of enchanting be drunk to gain experience?").getBoolean();

			bottleExperience = config.get("general", "bottleExperience",
					7, "How many experience points to Bottles of Enchanting give if drunk?").getInt();

			hideBar = config.get("client", "hideBar",
					false, "Should the exp bar be hidden?").getBoolean();

		} catch (Exception e) {
		} finally {
			if (config.hasChanged()) config.save();
		}
	}

	public static boolean canDrinkBottles() {
		return FMLCommonHandler.instance().getSide() == Side.CLIENT ? drinkBottlesClient : drinkBottles;
	}
}
