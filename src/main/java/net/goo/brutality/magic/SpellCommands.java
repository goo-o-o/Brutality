package net.goo.brutality.magic;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.weapon.tome.BaseMagicTome;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpellCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("spell")
                        .then(Commands.literal("add")
                                .requires(source -> source.hasPermission(2)) // Requires OP permission
                                .then(Commands.argument("spell_name", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            // Add spell name suggestions
                                            for (ResourceLocation spellId : SpellRegistry.SPELLS.keySet()) {
                                                IBrutalitySpell spell = SpellRegistry.getSpell(spellId);

                                                builder.suggest(spell.getSpellName());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .then(Commands.argument("level", IntegerArgumentType.integer(0))
                                                .executes(SpellCommands::addSpellToHeldItem)
                                        )
                                )
                        ));

        dispatcher.register(
                Commands.literal("spell")
                        .then(Commands.literal("remove")
                                .requires(source -> source.hasPermission(2)) // Requires OP permission
                                .then(Commands.argument("spell_name", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            // Add spell name suggestions
                                            for (ResourceLocation spellId : SpellRegistry.SPELLS.keySet()) {
                                                IBrutalitySpell spell = SpellRegistry.getSpell(spellId);

                                                builder.suggest(spell.getSpellName());
                                            }
                                            return builder.buildFuture();
                                        }).executes(SpellCommands::removeSpellFromHeldItem)
                                )
                        ));

        dispatcher.register(
                Commands.literal("spell")
                        .then(Commands.literal("cooldowns")
                                .then(Commands.literal("reset")
                                        .requires(source -> source.hasPermission(2)) // Requires OP permission
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .suggests((context, builder) -> {
                                                    // Suggest online player names
                                                    for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
                                                        builder.suggest(player.getName().getString());
                                                    }
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                    SpellCooldownTracker.resetCooldowns(player);
                                                    context.getSource().sendSuccess(() -> Component.literal("Cooldowns reset for " + player.getName().getString()), true);
                                                    return 1;
                                                })
                                        )
                                )));
    }

    private static int removeSpellFromHeldItem(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String spellName = StringArgumentType.getString(context, "spell_name");

        Player player = context.getSource().getPlayerOrException();
        ItemStack heldItem = player.getMainHandItem();

        if (!(heldItem.getItem() instanceof BaseMagicTome)) {
            context.getSource().sendFailure(Component.literal("You must be holding a magic item in your main hand!"));
            return 0;
        }

        ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spellName.toLowerCase());
        IBrutalitySpell spell = SpellRegistry.getSpell(spellId);

        if (spell == null) {
            context.getSource().sendFailure(Component.literal("Unknown spell: " + spellName));
            return 0;
        }

        boolean success = SpellStorage.removeSpell(heldItem, spell);

        if (success) {
            context.getSource().sendSuccess(() -> Component.literal("Removed spell " + spellName + "from your held item"), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Failed to remove spell from item"));
            return 0;
        }
    }

    private static int addSpellToHeldItem(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String spellName = StringArgumentType.getString(context, "spell_name");
        int level = IntegerArgumentType.getInteger(context, "level");

        Player player = context.getSource().getPlayerOrException();
        ItemStack heldItem = player.getMainHandItem();

        if (!(heldItem.getItem() instanceof BaseMagicTome)) {
            context.getSource().sendFailure(Component.literal("You must be holding a magic item in your main hand!"));
            return 0;
        }

        ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spellName.toLowerCase());
        IBrutalitySpell spell = SpellRegistry.getSpell(spellId);

        if (spell == null) {
            context.getSource().sendFailure(Component.literal("Unknown spell: " + spellName));
            return 0;
        }

        boolean success = SpellStorage.addSpell(heldItem, spell, level);

        if (success) {
            context.getSource().sendSuccess(() -> Component.literal("Added spell " + spellName + " at level " + level + " to your held item"), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Failed to add spell to item"));
            return 0;
        }
    }

}
