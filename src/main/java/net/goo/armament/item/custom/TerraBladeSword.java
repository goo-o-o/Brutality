package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.entity.BEAM_TYPES;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.c2sDamageItemPacket;
import net.goo.armament.network.c2sSwordBeamPacket;
import net.goo.armament.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerraBladeSword extends ArmaSwordItem {

    public TerraBladeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.colors = TERRA_BLADE_COLORS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }


    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();

        if (item instanceof TerraBladeSword) {
            if (!player.getCooldowns().isOnCooldown(item)) {
                player.getCooldowns().addCooldown(item, 20);
                event.getLevel().playSound(player, player.getOnPos(), ModSounds.TERRA_BLADE_USE.get(), SoundSource.PLAYERS);
                PacketHandler.sendToServer(new c2sSwordBeamPacket(BEAM_TYPES.TERRA_BEAM, 3.5F));
                PacketHandler.sendToServer(new c2sDamageItemPacket(1, true));

            }
        }
    }
}
