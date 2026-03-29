package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class Censored extends BrutalityCurioItem {
    public Censored(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static <T extends Entity> boolean shouldRedact(T entity) {
        if (entity instanceof LivingEntity living) {
            return CuriosApi.getCuriosInventory(living)
                    .map(handler -> handler.isEquipped(BrutalityItems.CENSORED.get())
                            || handler.isEquipped(BrutalityItems.REDACTED.get()))
                    .orElse(false);
        }
        return false;
    }

    public static ResourceLocation REDACTED_SKIN = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/entity/player/censored.png");

}
