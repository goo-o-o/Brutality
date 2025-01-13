package net.goo.arma.item;

import net.goo.arma.Arma;
import net.goo.arma.item.custom.SupernovaSwordItem;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Arma.MOD_ID);

    public static final RegistryObject<Item> SUPERNOVA_SWORD = ITEMS.register("supernova_sword",
            () -> new SupernovaSwordItem(Tiers.NETHERITE, 5, -2, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
