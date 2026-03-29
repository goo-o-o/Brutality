package net.goo.brutality.client.renderers.shaders.outline;

import net.goo.brutality.client.renderers.shaders.BrutalityShaders;
import net.goo.brutality.common.item.weapon.sword.RoyalGuardianSword;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class OutlineStyles {
    public static final Map<Item, OutlineStyle> ITEM_STYLES = new HashMap<>();

    private static Player pendingPlayer = null;
    private static ItemStack pendingStack = null;

    public static void push(Player player, ItemStack stack) {
        pendingPlayer = player;
        pendingStack = stack;
    }

    public static void clear() {
        pendingPlayer = null;
        pendingStack = null;
    }

    @Nullable
    public static Player getPlayer() {
        return pendingPlayer;
    }

    public static ItemStack getStack() {
        return pendingStack;
    }

    private static void register(Item item, OutlineStyle style) {
        ITEM_STYLES.put(item, style);
    }

    public static void registerAll() {
        register(BrutalityItems.ROYAL_GUARDIAN_SWORD.get(), new OutlineStyle(3, BrutalityShaders.itemOutlinePostShader) {
            @Override
            public int getColor(ItemStack stack, @Nullable Player player) {
                if (player == null || !player.isUsingItem())
                    return FastColor.ARGB32.color(0, 0, 0, 0);
                return FastColor.ARGB32.color(
                        (int) (RoyalGuardianSword.getChargePercent(player) * 255), 255, 255, 0);
            }
        });
        register(BrutalityItems.MAX.get(), new OutlineStyle(3, BrutalityShaders.maxSwordOutlinePostShader) {
            @Override
            public int getColor(ItemStack stack, @Nullable Player player) {
                if (player == null) return 0;

                if (player.isUsingItem()) {
                    int useTime = player.getTicksUsingItem(); // Cleaner than manual subtraction
                    double period = 16;
                    float radians = (float) (useTime * (2.0 * Math.PI / period));
                    float alpha = (Mth.sin(radians) + 1.0f) / 2.0f;
                    int alphaInt = (int) (alpha * 255);

                    return FastColor.ARGB32.color(alphaInt, 255, 255, 0);
                }

                if (!MaxSwordOutlineShader.START_TIMES.containsKey(player.getUUID())) return 0;

                long startTime = MaxSwordOutlineShader.START_TIMES.getLong(player.getUUID());
                long currentTime = player.level().getGameTime();

                // 1. Calculate Duration in Ticks based on Attack Speed attribute
                double speed = player.getAttributeValue(Attributes.ATTACK_SPEED);
                float totalDuration = (float) (20.0 / speed);

                // 2. Calculate Progress (0.0 to 1.0)
                float elapsed = (float) (currentTime - startTime);
                float progress = elapsed / totalDuration;

                // If the animation is over, clean up and return hidden
                if (progress > 1.0f) {
                    MaxSwordOutlineShader.START_TIMES.removeLong(player.getUUID());
                    return 0;
                }

                // 3. Smooth "In and Out" Triangle Math
                // y = 1 - |(x - 0.5) * 2|
                float alpha = 1.0f - Math.abs((progress - 0.5f) * 2.0f);

                // 4. Smoothstep the curve (0.0 -> 1.0 -> 0.0)
                alpha = Mth.clamp(alpha, 0.0f, 1.0f);
                alpha = alpha * alpha * (3.0f - 2.0f * alpha);

                // 5. Build the ARGB Color
                int alphaInt = (int) (alpha * 255);
                // Using White (#FFFFFF) as the base, applying our dynamic alpha
                return (alphaInt << 24) | 0xFFFFFF;
            }
        });
    }

    // thickness is in pixels


}