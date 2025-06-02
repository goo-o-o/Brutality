package net.goo.armament.item.noir;

import net.goo.armament.Armament;
import net.goo.armament.client.renderers.armor.ArmaAutoFullbrightArmorRenderer;
import net.goo.armament.item.base.ArmaArmorItem;
import net.goo.armament.item.ModArmorMaterials;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.util.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID)
public class NoirArmorItem extends ArmaArmorItem {
    public NoirArmorItem(Type pType, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(ModArmorMaterials.NOIR, pType, pProperties, identifier, category, rarity, abilityCount);
    }

    @SubscribeEvent
    public static void onRenderNametag(RenderNameTagEvent event) {
        if (event.getEntity() instanceof Player player)
            if (ModUtils.hasFullArmorSet(player, ModArmorMaterials.NOIR)) {
                event.setResult(Event.Result.DENY);
            }
    }

    @Override
    public <R extends GeoArmorRenderer<?>> void initGeoArmor(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeoArmor(consumer, ArmaAutoFullbrightArmorRenderer.class);
    }
}
