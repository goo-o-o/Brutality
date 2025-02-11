package net.goo.armament.item.custom;

import net.goo.armament.item.ArmaGeoItem;
import net.goo.armament.item.ArmaSwordItem;
import net.goo.armament.item.ModItemCategories;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Consumer;

public class TestSwordItem extends ArmaSwordItem {
    public TestSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category);
        this.colors = new int[][] {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {150, 0, 150}};
    }

    @Override
    public String geoIdentifier() {
        return "event_horizon";
    }

    @Override
    public String model(ItemStack stack) {
        return geoIdentifier();
    }

    @Override
    public String texture(ItemStack stack) {
        return model(stack);
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }

    @Override
    public <T extends Item & ArmaGeoItem> void initGeo(Consumer<IClientItemExtensions> consumer, int rendererID) {
        super.initGeo(consumer, 0);
    }

}
