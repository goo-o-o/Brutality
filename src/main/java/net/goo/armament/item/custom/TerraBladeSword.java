package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.entity.ArmaEffectEntity;
import net.goo.armament.entity.ArmaVisualTypes;
import net.goo.armament.item.ArmaSwordItem;
import net.goo.armament.item.ModItemCategories;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.function.Consumer;

import static net.goo.armament.util.ModResources.TERRA_BLADE_COLORS;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerraBladeSword extends ArmaSwordItem {

    public TerraBladeSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.colors = TERRA_BLADE_COLORS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public <T extends Item & ArmaGeoItem> void initGeo(Consumer<IClientItemExtensions> consumer, int rendererID) {
        super.initGeo(consumer, 0);
    }

    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().getItem() instanceof TerraBladeSword) {
//            PacketHandler.sendToServer(new c2sTerraBeamPacket());
            ArmaEffectEntity.createInstance(player, null, ArmaVisualTypes.TERRA_BEAM.get());
        }
    }


}
