package net.smileycorp.unexperienced.client;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ClientConfigHandler {

	public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec config;

	public static BooleanValue hideBar;
	private static ConfigValue<List<? extends String>> showBarItems;
	private static ConfigValue<List<? extends String>> showBarBlocks;

	static {
		builder.push("general");
		hideBar = builder.comment("Should the exp bar be hidden?").define("hideBar", true);
		showBarItems = builder.comment("Items that when held show the xp bar, when hideBar is true.")
				.define("showBarItems", Lists.newArrayList("minecraft:experience_bottle"));
		showBarBlocks = builder.comment("Blocks that when hovered over show the xp bar, when hideBar is true.")
				.define("showBarBlocks", Lists.newArrayList("minecraft:enchanting_table", "minecraft:anvil", "minecraft:grindstone"));
		builder.pop();
		config = builder.build();
	}

	public static boolean shouldShowBar(ItemStack stack) {
		if (stack != null) {
			String item = stack.getItem().getRegistryName().toString();
			if (showBarItems.get().contains(item)) return true;
		}
		return false;
	}

	public static boolean shouldShowBar(BlockState state) {
		if (state != null) {
			String block = state.getBlock().getRegistryName().toString();
			if (showBarBlocks.get().contains(block)) return true;
		}
		return false;
	}

}
