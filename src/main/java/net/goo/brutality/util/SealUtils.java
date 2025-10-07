package net.goo.brutality.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.entity.spells.cosmic.StarStreamEntity;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.registry.ModAttributes;
import net.mcreator.terramity.entity.BombFlowerItemProjectileEntity;
import net.mcreator.terramity.init.TerramityModEntities;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemDecorator;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.UUID;

public class SealUtils implements IItemDecorator {
    private static final String SEAL = "seal";

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(stack);
        if (sealType != null) {
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/seals/" + sealType.toString().toLowerCase(Locale.ROOT) + "_seal.png");
            guiGraphics.blit(texture, xOffset, yOffset, 0, 0, 16, 16, 16, 16);
            return false;
        }
        return false;
    }

    public enum SEAL_TYPE {
        NONE(false),
        BLACK(false),
        BLUE(false),
        GREEN(false),
        ORANGE(false),
        PINK(false),
        PURPLE(false),
        RED(false),
        CYAN(false),
        YELLOW(false),
        BOMB(true),
        GLASS(false),
        QUANTITE(false),
        VOID(true),
        COSMIC(false);

        private final boolean showDescription;

        SEAL_TYPE(boolean showDescription) {
            this.showDescription = showDescription;
        }

        public Boolean showDescription() {
            return showDescription;
        }
    }

    public static SEAL_TYPE getSealType(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.hasTag()) return null;
        String seal = stack.getOrCreateTag().getString(SEAL);
        if (seal.isEmpty()) return null;
        try {
            return SEAL_TYPE.valueOf(seal.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }

    }

    public static void handleSealAttributes(SEAL_TYPE sealType, ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        if (sealType != null && slot == LivingEntity.getEquipmentSlotForItem(stack)) {
            Multimap<Attribute, AttributeModifier> modifiers = cir.getReturnValue();
            Multimap<Attribute, AttributeModifier> newModifiers = ArrayListMultimap.create(modifiers);
            UUID modifierUUID = UUID.nameUUIDFromBytes(("seal_" + sealType.name() + "_" + slot.getName()).getBytes());


            switch (sealType) {
                case BLACK -> newModifiers.put(ModAttributes.LETHALITY.get(), new AttributeModifier(
                        modifierUUID, "black_seal", 2F, AttributeModifier.Operation.ADDITION
                ));
                case BLUE -> newModifiers.put(Attributes.ARMOR, new AttributeModifier(
                        modifierUUID, "blue_seal", 3.0F, AttributeModifier.Operation.ADDITION
                ));
                case GREEN -> newModifiers.put(Attributes.LUCK, new AttributeModifier(
                        modifierUUID, "green_seal", 1.0F, AttributeModifier.Operation.ADDITION
                ));
                case ORANGE -> newModifiers.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(), new AttributeModifier(
                        modifierUUID, "orange_seal", 0.05F, AttributeModifier.Operation.MULTIPLY_BASE
                ));
                case PINK -> newModifiers.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(
                        modifierUUID, "pink_seal", 0.05F, AttributeModifier.Operation.MULTIPLY_BASE
                ));
                case PURPLE -> newModifiers.put(ModAttributes.TENACITY.get(), new AttributeModifier(
                        modifierUUID, "purple_seal", 0.15F, AttributeModifier.Operation.MULTIPLY_BASE
                ));
                case RED -> newModifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(
                        modifierUUID, "red_seal", 3.0F, AttributeModifier.Operation.ADDITION
                ));
                case CYAN -> newModifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                        modifierUUID, "teal_seal", 3.0F, AttributeModifier.Operation.ADDITION
                ));
                case YELLOW -> newModifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(
                        modifierUUID, "yellow_seal", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL
                ));
                case QUANTITE -> newModifiers.put(ModAttributes.ARMOR_PENETRATION.get(), new AttributeModifier(
                        modifierUUID, "quantite_seal", 0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL
                ));
                case GLASS -> {
                    newModifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                            modifierUUID, "glass_seal", 0.35F,
                            AttributeModifier.Operation.MULTIPLY_TOTAL));
                    newModifiers.put(Attributes.ARMOR, new AttributeModifier(
                            modifierUUID, "glass_seal", -1,
                            AttributeModifier.Operation.MULTIPLY_TOTAL));
                    newModifiers.put(ModAttributes.DAMAGE_TAKEN.get(), new AttributeModifier(
                            modifierUUID, "glass_seal", 0.25F,
                            AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            }
            cir.setReturnValue(newModifiers);
        }
    }

    public static void addSeal(ItemStack stack, SEAL_TYPE type) {
        stack.getOrCreateTag().putString(SEAL, type.name());
    }

    public static void handleSealProc(Level level, LivingEntity attacker, Entity victim, @Nullable SEAL_TYPE sealType) {
        if (sealType != null) {
            RandomSource random = level.getRandom();
            switch (sealType) {
                case BOMB -> {
                    BombFlowerItemProjectileEntity bombFlower =
                            new BombFlowerItemProjectileEntity(TerramityModEntities.BOMB_FLOWER_ITEM_PROJECTILE.get(), victim.getRandomX(0.5F), victim.getY(0.5F), victim.getRandomZ(0.5F), level);
                    bombFlower.setOwner(attacker);
                    bombFlower.setSilent(true);
                    bombFlower.setBaseDamage(4.0F);
                    bombFlower.setKnockback(5);
                    bombFlower.setCritArrow(false);
                    bombFlower.setDeltaMovement(
                            Mth.randomBetween(random, -0.5F, 0.5F),
                            Mth.randomBetween(random, -0.5F, 0.5F),
                            Mth.randomBetween(random, -0.5F, 0.5F)
                    );
                    level.addFreshEntity(bombFlower);
                    level.playSound(null, victim.getX(), victim.getY(), victim.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 1.0F, 1.0F / (RandomSource.create().nextFloat() * 0.5F + 1.0F));
                }
                case COSMIC -> {
                    StarStreamEntity spellEntity = new StarStreamEntity(BrutalityModEntities.STAR_STREAM_ENTITY.get(), level);
                    spellEntity.setSpellLevel(0);
                    Vec3 randomPos = new Vec3(
                            victim.getRandomX(2),
                            victim.getY(0.5F) + Mth.nextFloat(random, 7.5F, 12.5F),
                            victim.getRandomZ(2));
                    spellEntity.setPos(randomPos);
                    spellEntity.setOwner(attacker);
                    Vec3 targetPos = victim.getPosition(1).add(0, victim.getBbHeight() * 0.5, 0);

                    Vec3 direction = targetPos.subtract(randomPos).normalize();

                    spellEntity.shoot(direction.x, direction.y, direction.z, 1.5F, 1.5F);

                    level.addFreshEntity(spellEntity);
                    level.playSound(null, attacker.getX(), attacker.getY(0.5), attacker.getZ(),
                            BrutalityModSounds.BASS_BOP.get(), SoundSource.AMBIENT,
                            1.5F, Mth.nextFloat(random, 0.7F, 1.2F));
                }
            }
        }
    }


    public static void handleSealProc(Level level, LivingEntity attacker, Entity victim, ItemStack stack) {
        SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(stack);
        handleSealProc(level, attacker, victim, sealType);
    }

}
