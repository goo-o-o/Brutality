package net.goo.armament.item;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.SupernovaSwordItem;
import net.goo.armament.item.custom.TestTridentItem;
import net.goo.armament.item.custom.ZeusThunderboltItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Armament.MOD_ID);

    public static final RegistryObject<SwordItem> SUPERNOVA_SWORD = ITEMS.register("supernova_sword",
            () -> new SupernovaSwordItem(Tiers.NETHERITE, 5, -2, new Item.Properties()));

    public static final RegistryObject<Item> ZEUS_THUNDERBOLT_TRIDENT = ITEMS.register("zeus_thunderbolt",
            () -> new ZeusThunderboltItem(new Item.Properties()));

    public static final RegistryObject<Item> TEST_TRIDENT = ITEMS.register("test_trident",
            () -> new TestTridentItem(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
