package net.smileycorp.unexperienced.client;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ClientConfigHandler {

	public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec config;

	public static BooleanValue hideBar;

	static {
			builder.push("general");

			hideBar = builder.comment("Should the exp bar be hidden?").define("hideBar", false);

			builder.pop();
			config = builder.build();
	}

}
