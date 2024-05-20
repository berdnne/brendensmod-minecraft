package berdnne.brendensmod.item;

import berdnne.brendensmod.BrendensMod;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item LOTTERY_TICKET = registerItem("lottery_ticket", new Item(new Item.Settings()));

    private static void addItemsToToolsItemGroup(FabricItemGroupEntries entries){
        entries.add(LOTTERY_TICKET);
    }

    private static Item registerItem(String name, Item item) {

        return Registry.register(Registries.ITEM, new Identifier(BrendensMod.MOD_ID, name), item);

    }

    public static void registerModItems(){

        BrendensMod.LOGGER.info("Registering Mod Items for " + BrendensMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolsItemGroup);

    }

}
