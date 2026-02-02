package net.goo.brutality.util.magic;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.goo.brutality.Brutality;
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
                                        .executes(SpellCommands::removeSpellFromHeldItem)
                                )
                        )
                        .then(Commands.literal("cooldowns")
                                .then(Commands.literal("reset")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .suggests((context, builder) -> {
                                                    for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
                                                        builder.suggest(player.getName().getString());
                                                    }
                                                    return builder.buildFuture();
                                                })
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

        if (!(heldItem.getItem() instanceof BaseMagicTome)) {
            context.getSource().sendFailure(Component.literal("You must be holding a magic tome!"));
            return 0;
        }

        ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spellName.toLowerCase());
        BrutalitySpell spell = BrutalitySpells.getSpell(spellId);
        if (spell == null) {
            context.getSource().sendFailure(Component.literal("Unknown spell: " + spellName));
            return 0;
        }

        if (SpellStorage.addSpell(heldItem, spell, level)) {
            context.getSource().sendSuccess(() -> Component.literal("Added spell " + spellName + " at level " + level), true);
            return 1;
        }
        context.getSource().sendFailure(Component.literal("Failed to add spell"));
        return 0;
    }

    private static int removeSpellFromHeldItem(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String spellName = StringArgumentType.getString(context, "spell_name");
        Player player = context.getSource().getPlayerOrException();
        ItemStack heldItem = player.getMainHandItem();

        if (!(heldItem.getItem() instanceof BaseMagicTome)) {
            context.getSource().sendFailure(Component.literal("You must be holding a magic tome!"));
            return 0;
        }

        ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spellName.toLowerCase());
        BrutalitySpell spell = BrutalitySpells.getSpell(spellId);
        if (spell == null) {
            context.getSource().sendFailure(Component.literal("Unknown spell: " + spellName));
            return 0;
        }

        if (SpellStorage.removeSpell(heldItem, spell)) {
            context.getSource().sendSuccess(() -> Component.literal("Removed spell " + spellName), true);
            return 1;
        }
        context.getSource().sendFailure(Component.literal("Failed to remove spell"));
        return 0;
    }
}