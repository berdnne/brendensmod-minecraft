package berdnne.brendensmod.mixin;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Items.class)
public abstract class StickEatingMixin {

    @Shadow
    public static final Item STICK = Items.register("stick", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(1).saturationModifier(0.3f).build())));

}
