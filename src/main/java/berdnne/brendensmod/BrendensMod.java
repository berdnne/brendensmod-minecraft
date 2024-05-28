package berdnne.brendensmod;

import berdnne.brendensmod.block.ModBlocks;
import berdnne.brendensmod.fluid.ModFluids;
import berdnne.brendensmod.item.ModItemGroups;
import berdnne.brendensmod.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrendensMod implements ModInitializer {

	public static final String MOD_ID = "brendensmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModFluids.register();
	}
}