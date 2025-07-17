package net.goo.brutality.item.weapon.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.item.BrutalityAutoFullbrightItemRenderer;
import net.goo.brutality.item.base.BrutalityScytheItem;
import net.goo.brutality.registry.ModParticles;
import net.goo.brutality.registry.ModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class DarkinScythe extends BrutalityScytheItem {
    protected String[] types = new String[]{"", "_shadow_assasin", "_rhaast"};
    private final UUID DARKIN_SCYTHE_MS_UUID = UUID.fromString("c0aa6baf-16a7-45e1-a65c-cfb82a65ff4c");
    private final UUID DARKIN_SCYTHE_AS_UUID = UUID.fromString("5386a75c-a1df-4b83-a62d-04dcb0fce7ff");
    private final UUID DARKIN_SCYTHE_AD_UUID = UUID.fromString("57b3f5d9-9f58-45d0-80f5-11ad07675bc6");

    public DarkinScythe(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, identifier, rarity, descriptionComponents);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public String texture(ItemStack stack) {
        return "darkin_scythe" + types[ModUtils.getCustomModelData(stack)];
    }

    @Override
    public String model(ItemStack stack) {
        return "darkin_scythe" + types[ModUtils.getCustomModelData(stack)];
    }


    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, BrutalityAutoFullbrightItemRenderer.class);
    }



    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        if (slot == EquipmentSlot.MAINHAND) {
            float attackSpeed, attackDamage;


            switch (ModUtils.getCustomModelData(stack)) {
                case 1 -> {
                    attackSpeed = -0F;
                    attackDamage = 6;
                }
                case 2 -> {
                    attackSpeed = -3.1F;
                    attackDamage = 21;
                }
                default -> {
                    attackSpeed = -2.5F;
                    attackDamage = 6;
                }

            }

            modifiers.put(
                    Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(
                            DARKIN_SCYTHE_AD_UUID,
                            "Weapon damage",
                            attackDamage,
                            AttributeModifier.Operation.ADDITION
                    )
            );
            modifiers.put(
                    Attributes.ATTACK_SPEED,
                    new AttributeModifier(
                            DARKIN_SCYTHE_AS_UUID,
                            "Weapon speed",
                            attackSpeed,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            modifiers.put(
                    Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(
                            DARKIN_SCYTHE_MS_UUID,
                            "Move speed",
                            (ModUtils.getCustomModelData(stack) == 1) ? .4 : 0,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    )
            );
        }
        return modifiers;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pUsedHand == InteractionHand.OFF_HAND) return InteractionResultHolder.fail(stack);
        int customModelData = (ModUtils.getCustomModelData(stack) + 1) % 3;

        if (pPlayer.isCrouching()) {


            stack.getOrCreateTag().putInt(CUSTOM_MODEL_DATA, (customModelData));
            pLevel.playSound(null, pPlayer.getOnPos(), ModSounds.DARKIN_SCYTHE_TRANSFORM.get(customModelData).get(), SoundSource.PLAYERS);

            pPlayer.getCooldowns().addCooldown(stack.getItem(), 80);

        } else {
            double length = (customModelData == 1) ? 7 : 5;
            double halfWidth = 0.5;

            Vec3 lookDir = pPlayer.getLookAngle();
            AABB hitbox = pPlayer.getBoundingBox()
                    .expandTowards(lookDir.x * length, lookDir.y * length, lookDir.z * length)
                    .inflate(halfWidth, halfWidth, halfWidth);

            List<LivingEntity> entities = pLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    hitbox,
                    e -> e != pPlayer && e.isAlive()
            );

            customModelData = ModUtils.getCustomModelData(stack);


            if (customModelData == 1) {
                if (pLevel instanceof ClientLevel clientLevel)
                    for (LivingEntity entity : entities)
                        for (int i = 0; i < 3; i++)
                            clientLevel.addParticle(ModParticles.SHADOW_ASSASIN_PARTICLE.get(), entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(),
                                    clientLevel.random.nextFloat() - 0.5, clientLevel.random.nextFloat() - 0.5, clientLevel.random.nextFloat() - 0.5);

                for (LivingEntity entity : entities) {
                    entity.hurt(pPlayer.damageSources().indirectMagic(pPlayer, pPlayer), 6);
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                }

                pPlayer.getCooldowns().addCooldown(stack.getItem(), 30);
                pLevel.playSound(pPlayer, pPlayer.getOnPos(),
                        ModSounds.GROUND_IMPACT.get(), SoundSource.PLAYERS, 1.0F, Mth.nextFloat(pLevel.random, 0.75F, 1F));
                return InteractionResultHolder.consume(stack);


            } else if (customModelData == 2) {
                if (pLevel instanceof ClientLevel clientLevel)
                    for (LivingEntity entity : entities)
                        for (int i = 0; i < 3; i++)
                            clientLevel.addParticle(ModParticles.RHAAST_PARTICLE.get(), entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(),
                                    clientLevel.random.nextFloat() - 0.5, clientLevel.random.nextFloat() - 0.5, clientLevel.random.nextFloat() - 0.5);

                for (LivingEntity entity : entities) {
                    entity.hurt(pPlayer.damageSources().playerAttack(pPlayer), 8);
                    entity.push(0, 0.35, 0);
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                }

                pPlayer.getCooldowns().addCooldown(stack.getItem(), 30);
                pLevel.playSound(pPlayer, pPlayer.getOnPos(),
                        ModSounds.GROUND_IMPACT.get(), SoundSource.PLAYERS, 1.0F, Mth.nextFloat(pLevel.random, 0.75F, 1F));

                return InteractionResultHolder.consume(stack);
            }

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }




}
