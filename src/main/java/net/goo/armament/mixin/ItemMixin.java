package net.goo.armament.mixin;

import net.goo.armament.client.renderers.armor.ArmaArmorRenderer;
import net.goo.armament.client.renderers.item.ArmaItemRenderer;
import net.goo.armament.item.ArmaArmorItem;
import net.goo.armament.item.ArmaGeoItem;
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
        if (this instanceof ArmaGeoItem geoItem) {
            geoItem.initGeo(consumer, ArmaItemRenderer.class);
            if (geoItem instanceof ArmaArmorItem armorItem) {
                armorItem.initGeoArmor(consumer, ArmaArmorRenderer.class);
            }
        }
    }
}