package net.goo.brutality.client.gui.meters;

import com.mojang.blaze3d.systems.RenderSystem;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.config.BrutalityClientConfig;
import net.goo.brutality.util.CommonConstants;
import net.mcreator.terramity.init.TerramityModItems;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.mcreator.terramity.procedures.PrismaticRingMathProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Brutality.MOD_ID)
public abstract class CooldownMeter implements IGuiOverlay {

    public enum AbilityCooldownPosition {
        BOTTOM_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        },
        BOTTOM_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth - CommonConstants.bigPad - elementWidth;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        },
        LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight / 2 - CommonConstants.smallPad - elementHeight;
            }
        },
        RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth - CommonConstants.bigPad - elementWidth;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight / 2 - CommonConstants.smallPad - elementHeight;
            }
        },
        HOTBAR_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 + 91 + CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        },
        HOTBAR_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 - 91 - CommonConstants.bigPad - elementWidth;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        };

        public abstract int getX(int screenWidth, int elementWidth);

        public abstract int getY(int screenHeight, int elementHeight);
    }

    public enum ArmorSetCooldownPosition {
        BOTTOM_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return CommonConstants.bigPad + elementWidth + CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        },
        BOTTOM_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth - CommonConstants.bigPad - elementWidth - elementWidth - CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        },
        LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight / 2 + CommonConstants.smallPad;
            }
        },
        RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth - CommonConstants.bigPad - elementWidth;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight / 2 + CommonConstants.smallPad;
            }
        },
        HOTBAR_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 + 91 + CommonConstants.bigPad + elementWidth + CommonConstants.smallPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        },
        HOTBAR_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 - 91 - CommonConstants.bigPad - elementWidth - elementWidth - CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        };

        public abstract int getX(int screenWidth, int elementWidth);

        public abstract int getY(int screenHeight, int elementHeight);
    }


    public static CooldownMeter get(CommonConstants.CooldownType type) {
        Supplier<CooldownMeter> sup = CommonConstants.REGISTRY.get(type);
        return sup != null ? sup.get() : null;
    }

    private static float getPercentage(MobEffectInstance inst, int maxTicks) {
        return inst.getDuration() / (float) maxTicks;
    }

    public static class ArmorSetAbilityCooldownMeter extends CooldownMeter {
        public static int maxTicks;
        public static ItemStack itemStack;

        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            MobEffectInstance inst = player.getEffect(TerramityModMobEffects.ARMOR_SET_ABILITY_COOLDOWN.get());
            if (inst == null || itemStack.isEmpty()) return;

            float percentage = getPercentage(inst, maxTicks); // 1.0 = full, 0.0 = empty

            ArmorSetCooldownPosition pos = BrutalityClientConfig.ARMOR_SET_COOLDOWN_POSITION.get();
            int x = pos.getX(guiGraphics.guiWidth(), CommonConstants.itemWidth);
            int y = pos.getY(guiGraphics.guiHeight(), CommonConstants.itemHeight);

            if (pos == ArmorSetCooldownPosition.HOTBAR_LEFT) {
                if (player.hasItemInSlot(EquipmentSlot.OFFHAND)) {
                    x -= CommonConstants.offhandSlotSize + CommonConstants.bigPad; // Offhand slot + padding
                }
            }

            if (RageMeter.shouldRender(player)) {
                RageMeter.Style style = BrutalityClientConfig.RAGE_METER_STYLE.get();
                RageMeter.Position ragePos = BrutalityClientConfig.RAGE_METER_POSITION.get();
                if (style == RageMeter.Style.CLASSIC) {
                    int bgW = RageMeter.ClassicRageMeterRenderer.bgW;
                    if (pos == ArmorSetCooldownPosition.HOTBAR_LEFT && ragePos == RageMeter.Position.HOTBAR_LEFT) {
                        x -= bgW + CommonConstants.bigPad;
                    } else if (pos == ArmorSetCooldownPosition.HOTBAR_RIGHT && ragePos == RageMeter.Position.HOTBAR_RIGHT) {
                        x += bgW + CommonConstants.bigPad;
                    } else if (pos == ArmorSetCooldownPosition.BOTTOM_LEFT && ragePos == RageMeter.Position.BOTTOM_LEFT) {
                        x += bgW + CommonConstants.bigPad;
                    } else if (pos == ArmorSetCooldownPosition.BOTTOM_RIGHT && ragePos == RageMeter.Position.BOTTOM_RIGHT) {
                        x -= bgW + CommonConstants.bigPad;
                    }
                }
            }

            guiGraphics.pose().pushPose();
            guiGraphics.setColor(0.5F, 0.5F, 0.5F, 1F);
            guiGraphics.renderItem(itemStack, x, y);
            guiGraphics.setColor(1, 1, 1, 1);

            if (percentage > 0f) {
                int brightHeight = (int) (CommonConstants.itemHeight * (1 - percentage));  // how much is filled
                guiGraphics.enableScissor(x, y + (CommonConstants.itemHeight - brightHeight), x + CommonConstants.itemWidth, y + CommonConstants.itemHeight);

                guiGraphics.renderItem(itemStack, x, y);  // bright item

                guiGraphics.disableScissor();
            }

            RenderSystem.disableBlend();
            guiGraphics.pose().popPose();

        }
    }

    public static class AbilityCooldownMeter extends CooldownMeter {
        public static int maxTicks;
        public static ItemStack itemStack;

        // ALWAYS REMEMBER TO SORT FROM HIGHEST, IMPORTANT FOR FUNCTIONALITY
        private static final List<AbilityEntry> ABILITIES = List.of(
                new AbilityEntry(TerramityModItems.ALL_SEEING_SCARF.get(), 2400),

                new AbilityEntry(TerramityModItems.WEIGHTED_DIE.get(), 1320),
                new AbilityEntry(TerramityModItems.CHAOS_HEART_RING.get(), 1320),

                new AbilityEntry(TerramityModItems.DIVERGENCY_GAUNTLET.get(), 1200),
                new AbilityEntry(TerramityModItems.GAIAS_GRACE.get(), 1200),
                new AbilityEntry(TerramityModItems.POWER_BRACELETS.get(), 1200),
                new AbilityEntry(TerramityModItems.THE_PILL.get(), 1200),
                new AbilityEntry(TerramityModItems.ADRENALINE_TABLETS.get(), 1200),
                new AbilityEntry(TerramityModItems.FERMENTED_APPLE_CIDER.get(), 1200),

                new AbilityEntry(TerramityModItems.MELATONIN_GUMMIES.get(), 900),
                new AbilityEntry(TerramityModItems.ULTRA_SNIFFER_FUR.get(), 900),
                new AbilityEntry(TerramityModItems.CRYSTAL_HEART.get(), 900),
                new AbilityEntry(TerramityModItems.CARDBOARD_POP_UP_FORT.get(), 900),
                new AbilityEntry(TerramityModItems.ANALEPTIC_AMPHORA.get(), 900),
                new AbilityEntry(TerramityModItems.SHINOBIS_SUBSTITUTE.get(), 900),
                new AbilityEntry(TerramityModItems.BURNING_SPIRIT_RING.get(), 900),
                new AbilityEntry(TerramityModItems.MORPHINE_PILLS.get(), 900),
                new AbilityEntry(TerramityModItems.AMP_SHIELD.get(), 900),
                new AbilityEntry(TerramityModItems.NULL_SCARF.get(), 900),

                new AbilityEntry(TerramityModItems.IBUPROFEN_CAPSULES.get(), 800),

                new AbilityEntry(TerramityModItems.GIANT_SNIFFERS_HOOF.get(), 700),
                new AbilityEntry(TerramityModItems.POCKET_UNIVERSE.get(), 700),
                new AbilityEntry(TerramityModItems.HELLROK_GAUNTLET.get(), 700),

                new AbilityEntry(TerramityModItems.ENERGIZED_CORE.get(), 600),

                new AbilityEntry(TerramityModItems.ARCHANGEL_HALO.get(), 200),
                new AbilityEntry(TerramityModItems.DRAGON_BAND.get(), 200),

                new AbilityEntry(TerramityModItems.DEVILS_DICE.get(), 60),

                new AbilityEntry(TerramityModItems.CYBER_GLASSES.get(), 5)
        );

        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            MobEffectInstance inst = player.getEffect(TerramityModMobEffects.ABILITY_COOLDOWN.get());
            if (inst == null || itemStack.isEmpty()) return;

            float percentage = getPercentage(inst, maxTicks); // 1.0 = full, 0.0 = empty

            AbilityCooldownPosition pos = BrutalityClientConfig.ABILITY_COOLDOWN_METER_POSITION.get();
            int x = pos.getX(guiGraphics.guiWidth(), CommonConstants.itemWidth);
            int y = pos.getY(guiGraphics.guiHeight(), CommonConstants.itemHeight);

            if (pos == AbilityCooldownPosition.HOTBAR_LEFT) {
                if (player.hasItemInSlot(EquipmentSlot.OFFHAND)) {
                    x -= CommonConstants.offhandSlotSize + CommonConstants.bigPad; // Offhand slot + padding
                }
            }

            if (RageMeter.shouldRender(player)) {
                RageMeter.Style style = BrutalityClientConfig.RAGE_METER_STYLE.get();
                RageMeter.Position ragePos = BrutalityClientConfig.RAGE_METER_POSITION.get();
                if (style == RageMeter.Style.CLASSIC) {
                    int bgW = RageMeter.ClassicRageMeterRenderer.bgW;
                    if (pos == AbilityCooldownPosition.HOTBAR_LEFT && ragePos == RageMeter.Position.HOTBAR_LEFT) {
                        x -= bgW + CommonConstants.bigPad;
                    } else if (pos == AbilityCooldownPosition.HOTBAR_RIGHT && ragePos == RageMeter.Position.HOTBAR_RIGHT) {
                        x += bgW + CommonConstants.bigPad;
                    } else if (pos == AbilityCooldownPosition.BOTTOM_LEFT && ragePos == RageMeter.Position.BOTTOM_LEFT) {
                        x += bgW + CommonConstants.bigPad;
                    } else if (pos == AbilityCooldownPosition.BOTTOM_RIGHT && ragePos == RageMeter.Position.BOTTOM_RIGHT) {
                        x -= bgW + CommonConstants.bigPad;
                    }
                }
            }

            guiGraphics.pose().pushPose();
            guiGraphics.setColor(0.5F, 0.5F, 0.5F, 1F);
            guiGraphics.renderItem(itemStack, x, y);
            guiGraphics.setColor(1, 1, 1, 1);

            if (percentage > 0f) {
                int brightHeight = (int) (CommonConstants.itemHeight * (1 - percentage));  // how much is filled
                guiGraphics.enableScissor(x, y + (CommonConstants.itemHeight - brightHeight), x + CommonConstants.itemWidth, y + CommonConstants.itemHeight);

                guiGraphics.renderItem(itemStack, x, y);  // bright item

                guiGraphics.disableScissor();
            }

            RenderSystem.disableBlend();
            guiGraphics.pose().popPose();

        }

        private record AbilityEntry(Item item, int baseTicks) {
        }

        public static void updateActiveAbilityIcon(LocalPlayer player) {
            if (player.hasEffect(TerramityModMobEffects.ABILITY_COOLDOWN.get())) return;

            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                double multiplier = PrismaticRingMathProcedure.execute(player);

                for (AbilityEntry entry : ABILITIES) {
                    if (handler.isEquipped(entry.item)) {
                        maxTicks = (int) (entry.baseTicks * multiplier);
                        itemStack = entry.item.getDefaultInstance();
                        return;
                    }
                }
            });
        }

    }
}
