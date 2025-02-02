package net.goo.armament.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.armament.item.custom.client.renderer.JackpotHammerItemRenderer;
import net.goo.armament.particle.ModParticles;
import net.goo.armament.sound.ModSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

public class JackpotHammerItem extends TieredItem implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final float attackSpeedModifier;

    public JackpotHammerItem(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pProperties);
        this.attackSpeedModifier = pAttackSpeedModifier;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        // Add the existing modifiers using the super method
        Multimap<Attribute, AttributeModifier> existingModifiers = super.getAttributeModifiers(slot, stack);
        for (Attribute attribute : existingModifiers.keySet()) {
            for (AttributeModifier modifier : existingModifiers.get(attribute)) {
                builder.put(attribute, modifier);
            }
        }

        // Apply the attack speed modifier if the item is in the main hand or off hand
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedModifier, AttributeModifier.Operation.ADDITION));
        }

        // Build and return the immutable multimap
        return builder.build();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.armament.jackpot.desc.1"));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("item.armament.jackpot.desc.2"));
        pTooltipComponents.add(Component.translatable("item.armament.jackpot.desc.3"));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private JackpotHammerItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    renderer = new JackpotHammerItemRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();
        int damage;
        if (!level.isClientSide && player instanceof ServerPlayer) {
            if (player.getAttackStrengthScale(1.0F) == 1) {

                damage = level.random.nextInt(4, 14);

                if (damage <= 8) {
                    player.displayClientMessage(Component.literal(damage + " damage"), true);

                } else {
                    int msgPicker = level.random.nextInt(2);

                    player.displayClientMessage(Component.literal("\u00a7" +
                            (msgPicker == 0 ? "a\u00a7l\u00a7oCHA-CHING! " : msgPicker == 1 ? "b\u00a7l\u00a7oHUGE WIN! " : "c\u00a7l\u00a7oJACKPOT! ") + "\u00a7r" + damage + " damage"), true);

                    int randomIndex = level.random.nextInt(ModSounds.JACKPOT_SOUNDS.size() + 1);
                    RegistryObject<SoundEvent> randomJackpotSound = ModSounds.JACKPOT_SOUNDS.get(randomIndex);
                    Holder<SoundEvent> soundHolder = Holder.direct(randomJackpotSound.get());

                    ((ServerPlayer) player).connection.send(new ClientboundSoundPacket(
                            soundHolder,
                            SoundSource.PLAYERS,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            1.0F,
                            1.0F,
                            player.getId()
                    ));

                    ((ServerPlayer) player).connection.send(new ClientboundLevelParticlesPacket(ModParticles.POKER_CHIP_RED_PARTICLE.get(), true, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 0, 0, 0, 0, 50));

                    ((ServerPlayer) player).connection.send(new ClientboundLevelParticlesPacket(ModParticles.POKER_CHIP_GREEN_PARTICLE.get(), true, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 0, 0, 0, 0, 50));

                    ((ServerPlayer) player).connection.send(new ClientboundLevelParticlesPacket(ModParticles.POKER_CHIP_BLUE_PARTICLE.get(), true, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 0, 0, 0, 0, 50));

                    ((ServerPlayer) player).connection.send(new ClientboundLevelParticlesPacket(ModParticles.POKER_CHIP_YELLOW_PARTICLE.get(), true, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 0, 0, 0, 0, 50));
                }

            } else {
                damage = 0;
            }

        entity.hurt(entity.damageSources().playerAttack(player), damage);

        }

        return true;
    }

}
