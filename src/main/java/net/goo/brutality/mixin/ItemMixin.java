package net.goo.brutality.mixin;

import net.goo.brutality.client.renderers.armor.ArmaArmorRenderer;
import net.goo.brutality.client.renderers.item.BrutalityItemRenderer;
import net.goo.brutality.item.base.BrutalityArmorItem;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemMixin {


    @Inject(method = "initializeClient", at = @At("TAIL"), remap = false)
    public void initializeClient(Consumer<IClientItemExtensions> consumer, CallbackInfo ci) {
        if (this instanceof BrutalityGeoItem geoItem) {
            geoItem.initGeo(consumer, BrutalityItemRenderer.class);
            if (geoItem instanceof BrutalityArmorItem armorItem) {
                armorItem.initGeoArmor(consumer, ArmaArmorRenderer.class);
            }
        }
    }
}