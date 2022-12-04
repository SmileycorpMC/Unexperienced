package net.smileycorp.unexperienced;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

public class CommonConfigHandler {

	public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec config;

	public static BooleanValue disableXP;
	public static BooleanValue directXP;
	static BooleanValue drinkBottles;
	public static boolean drinkBottlesClient;
	public static ConfigValue<Integer> bottleExperience;

	static {
		builder.push("general");
		disableXP = builder.comment("Should xp drops be disabled?").define("disableXP", true);

		directXP = builder.comment("Should mob xp drops be given directly to the player?").define("directXP", false);

		drinkBottles = builder.comment("Can Bottles of enchanting be drunk to gain experience?").define("drinkBottles", true);

		bottleExperience = builder.comment("How many experience points to Bottles of Enchanting give if drunk?").define("bottleExperience", 7);

		builder.pop();
		config = builder.build();
	}

	public static boolean canDrinkBottles() {
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.CLIENT ? drinkBottlesClient : drinkBottles.get();
	}
}
