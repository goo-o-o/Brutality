package net.goo.brutality.util.magic;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.generic.SpellScroll;
import net.goo.brutality.common.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class SpellCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("spell")
                        .then(Commands.literal("add")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.argument("spell_name", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            for (RegistryObject<BrutalitySpell> spellRO : BrutalitySpells.SPELLS.getEntries()) {
                                                builder.suggest(spellRO.get().getSpellName());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .then(Commands.argument("level", IntegerArgumentType.integer(0))
                                                .executes(SpellCommands::addSpellToHeldItem)
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.argument("spell_name", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            for (RegistryObject<BrutalitySpell> spellRO : BrutalitySpells.SPELLS.getEntries()) {
                                                builder.suggest(spellRO.get().getSpellName());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .then(Commands.argument("spell_level", IntegerArgumentType.integer(1))
                                                .executes(SpellCommands::removeSpellFromHeldItem))
                                        .executes(SpellCommands::removeSpellFromHeldItem))
                        )
                        .then(Commands.literal("cooldowns")
                                .then(Commands.literal("reset")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(context -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                    SpellCooldownTracker.resetAllCooldowns(player);
                                                    context.getSource().sendSuccess(() -> Component.literal("Cooldowns reset for " + player.getName().getString()), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }

    private static int addSpellToHeldItem(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String spellName = StringArgumentType.getString(context, "spell_name");
        int level = IntegerArgumentType.getInteger(context, "level");
        Player player = context.getSource().getPlayerOrException();
        ItemStack heldItem = player.getMainHandItem();

        if (!(heldItem.getItem() instanceof BaseMagicTome || heldItem.getItem() instanceof SpellScroll)) {
            context.getSource().sendFailure(Component.translatable("message." + Brutality.MOD_ID + ".spell_command_fail_invalid_item"));
            return 0;
        }

        ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spellName.toLowerCase());
        BrutalitySpell spell = BrutalitySpells.getSpell(spellId);
        if (spell == null) {
            context.getSource().sendFailure(Component.translatable("message." + Brutality.MOD_ID + ".spell_command_fail_unknown", spellName));
            return 0;
        }

        if (SpellStorage.addSpell(heldItem, spell, level)) {
            context.getSource().sendSuccess(() -> Component.translatable("message." + Brutality.MOD_ID + ".spell_add_success", spellName, level), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.translatable("message." + Brutality.MOD_ID + ".insufficient_slots"));
        }
        context.getSource().sendFailure(Component.translatable("message." + Brutality.MOD_ID + ".spell_add_fail", spellName));
        return 0;
    }

    private static int removeSpellFromHeldItem(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String spellName = StringArgumentType.getString(context, "spell_name");
        Player player = context.getSource().getPlayerOrException();
        ItemStack heldItem = player.getMainHandItem();

        if (!(heldItem.getItem() instanceof BaseMagicTome || heldItem.getItem() instanceof SpellScroll)) {
            context.getSource().sendFailure(Component.translatable("message." + Brutality.MOD_ID + ".spell_command_fail_invalid_item"));
            return 0;
        }

        ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spellName.toLowerCase());
        BrutalitySpell spell = BrutalitySpells.getSpell(spellId);
        if (spell == null) {
            context.getSource().sendFailure(Component.translatable("message." + Brutality.MOD_ID + ".spell_command_fail_unknown", spellName));
            return 0;
        }

        Integer spellLevel = null;
        try {
            spellLevel = IntegerArgumentType.getInteger(context, "spell_level");
        } catch (IllegalArgumentException e) {
            // Argument not present, spellLevel remains null
        }

        if (SpellStorage.removeSpell(heldItem, spell, spellLevel)) {
            context.getSource().sendSuccess(() -> Component.translatable("message." + Brutality.MOD_ID + ".spell_remove_success", spellName), true);
            return 1;
        }
        context.getSource().sendFailure(Component.translatable("message." + Brutality.MOD_ID + ".spell_add_fail", spellName));
        return 0;
    }
}