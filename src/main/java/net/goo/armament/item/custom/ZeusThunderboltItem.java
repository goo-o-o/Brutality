package net.goo.armament.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.armament.Armament;
import net.goo.armament.client.item.renderer.ZeusThunderboltItemRenderer;
import net.goo.armament.entity.custom.ThrownZeusThunderboltEntity;
import net.goo.armament.item.ArmaTridentItem;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.registry.ModItems;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.goo.armament.util.ModResources.FANTASY;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ZeusThunderboltItem extends ArmaTridentItem implements Vanishable {
    private static final UUID SPEED_BOOST_UUID = UUID.fromString("f9d0a647-4999-4637-b4a0-7f768a65b5db");  // Unique UUID for speed boost modifier

    public ZeusThunderboltItem(Properties pProperties, String identifier, ModItemCategories category) {
        super(pProperties, identifier, category);
        this.identifier = identifier;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_BOOST_UUID, "Tool modifier",2, AttributeModifier.Operation.ADDITION));
        Multimap<Attribute, AttributeModifier> defaultModifiers = builder.build();
        this.colors = new int[][] {{255, 215, 86}, {164, 92, 0}};
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(Map.of(Enchantments.LOYALTY, 5, Enchantments.INFINITY_ARROWS, 1), stack);
        return stack;
    }

    @SubscribeEvent
    public static void onGrindstoneUse(GrindstoneEvent.OnPlaceItem event) {
        ItemStack bottomItem = event.getBottomItem();
        ItemStack topItem = event.getTopItem();
        ItemStack targetStack = bottomItem.isEmpty() ? topItem : bottomItem;

        if (targetStack.getItem() == ModItems.ZEUS_THUNDERBOLT_TRIDENT.get()) {
            ItemStack resultStack = targetStack.copy();

            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(resultStack);
            enchantments = enchantments.entrySet().stream().filter(entry ->
                            entry.getKey().isCurse() || entry.getKey() == Enchantments.LOYALTY || entry.getKey() == Enchantments.INFINITY_ARROWS)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            enchantments.put(Enchantments.LOYALTY, 5);
            enchantments.put(Enchantments.INFINITY_ARROWS, 1);
            EnchantmentHelper.setEnchantments(enchantments, resultStack);
            event.setXp(0);
            event.setOutput(resultStack);
        }
    }

    @Override
    public void setRiptideEnabled(Boolean enabled) {
        super.setRiptideEnabled(false);
    }

    @Override
    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {
        int riptideLevel = EnchantmentHelper.getRiptide(pStack);
        ThrownZeusThunderboltEntity thrownEntity = new ThrownZeusThunderboltEntity(pLevel, player, pStack);
        thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, launchVel + (isRiptideEnabled ? (float) riptideLevel : 0) * 0.5F, 1.0F);
        if (player.getAbilities().instabuild) {
            thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        pLevel.addFreshEntity(thrownEntity);
        pLevel.playSound((Player) null, thrownEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (!player.getAbilities().instabuild || !this.isInfinite) {
            player.getInventory().removeItem(pStack);
        }
    }

}
