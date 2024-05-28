package berdnne.brendensmod.fluid;

import berdnne.brendensmod.BrendensMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModFluids {

    public static FlowableFluid MILK;
    public static FlowableFluid FLOWING_MILK;
    public static Block MILK_BLOCK;

    public static void register(){

        BrendensMod.LOGGER.info("Registering Mod Fluids");

        MILK = Registry.register(Registries.FLUID, new Identifier(BrendensMod.MOD_ID, "milk"), new MilkFluid.Still());
        FLOWING_MILK = Registry.register(Registries.FLUID, new Identifier(BrendensMod.MOD_ID, "flowing_milk"), new MilkFluid.Flowing());
        MILK_BLOCK = Registry.register(Registries.BLOCK, new Identifier(BrendensMod.MOD_ID, "milk"), new FluidBlock(ModFluids.MILK,
                AbstractBlock.Settings.copy(Blocks.WATER)));

    }

}
