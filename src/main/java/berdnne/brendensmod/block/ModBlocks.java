package berdnne.brendensmod.block;

import berdnne.brendensmod.BrendensMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block THE_BLOCK = registerBlock("the_block", new Block(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.ANVIL)));

    private static Block registerBlock(String name, Block block) {

        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(BrendensMod.MOD_ID, name), block);

    }

    private static Item registerBlockItem(String name, Block block) {

        return Registry.register(Registries.ITEM, new Identifier(BrendensMod.MOD_ID, name), new BlockItem(block, new Item.Settings()));

    }

    public static void registerModBlocks(){

        BrendensMod.LOGGER.info("Registering Mod Blocks for " + BrendensMod.MOD_ID);

    }

}
