package net.goo.armament.item;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.*;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Armament.MOD_ID);

    public static final RegistryObject<SwordItem> SUPERNOVA_SWORD = ITEMS.register("supernova_sword",
            () -> new SupernovaSwordItem(Tiers.NETHERITE, 5, -2.6F, new Item.Properties()));

    public static final RegistryObject<SwordItem> SEEKER_OF_KNOWLEDGE_SWORD = ITEMS.register("seeker_of_knowledge_sword",
            () -> new SeekerOfKnowledgeSwordItem(Tiers.NETHERITE, 1, -2.6F, new Item.Properties()));

    public static final RegistryObject<Item> ZEUS_THUNDERBOLT_TRIDENT = ITEMS.register("zeus_thunderbolt",
            () -> new ZeusThunderboltItem(new Item.Properties()
                    .stacksTo(1).fireResistant().setNoRepair().rarity(Rarity.EPIC)));

    public static final RegistryObject<SwordItem> TERRATON_HAMMER = ITEMS.register("terraton_hammer",
            () -> new TerratonHammerItem(Tiers.NETHERITE, 45, 0F, new Item.Properties()));

    public static final RegistryObject<AxeItem> DIVINE_RHITTA_AXE = ITEMS.register("divine_rhitta_axe",
            () -> new DivineRhittaAxeItem(Tiers.NETHERITE, 8, -3.1F, new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
