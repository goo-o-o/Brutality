package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.screen.table_of_wizardry.TableOfWizardryBookSection;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.goo.brutality.util.magic.SpellStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ServerboundTableOfWizardryUpdatePacket {
    private final BlockPos pos;
    private final String section, state;
    private final @Nullable ResourceLocation spellId;
    private final @Nullable SpellStorage.SpellEntry expungeEntry;
    // --- ADD THIS ---
    private final ItemStack craftingItem;

    public ServerboundTableOfWizardryUpdatePacket(BlockPos pos, String section, String state,
                                                  @Nullable ResourceLocation spellId,
                                                  @Nullable SpellStorage.SpellEntry expungeSpellEntry,
                                                  ItemStack craftingItem) {
        this.pos = pos;
        this.section = section;
        this.state = state;
        this.spellId = spellId;
        this.expungeEntry = expungeSpellEntry;
        this.craftingItem = craftingItem;
    }

    public ServerboundTableOfWizardryUpdatePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.section = buf.readUtf();
        this.state = buf.readUtf();
        this.spellId = buf.readNullable(FriendlyByteBuf::readResourceLocation);

        // --- READ ITEMSTACK ---
        this.craftingItem = buf.readNullable(FriendlyByteBuf::readItem);

        boolean hasExpungeEntry = buf.readBoolean();
        if (hasExpungeEntry) {
            ResourceLocation id = buf.readResourceLocation();
            int level = buf.readInt();
            this.expungeEntry = new SpellStorage.SpellEntry(BrutalitySpells.getSpell(id), level);
        } else {
            this.expungeEntry = null;
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeUtf(this.section);
        buf.writeUtf(this.state);
        buf.writeNullable(this.spellId, FriendlyByteBuf::writeResourceLocation);

        // --- WRITE ITEMSTACK ---
        buf.writeNullable(this.craftingItem, FriendlyByteBuf::writeItem);

        if (this.expungeEntry != null) {
            buf.writeBoolean(true);
            buf.writeResourceLocation(BrutalitySpells.getIdFromSpell(this.expungeEntry.spell()));
            buf.writeInt(this.expungeEntry.level());
        } else {
            buf.writeBoolean(false);
        }
    }

    public static void handle(ServerboundTableOfWizardryUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            if (player.level().getBlockEntity(packet.pos) instanceof TableOfWizardryBlockEntity be) {
                try {
                    be.currentSection = TableOfWizardryBookSection.valueOf(packet.section);
                    be.currentState = TableOfWizardryBlockEntity.GuiState.valueOf(packet.state);

                    // --- UPDATE ITEMSTACK ---
                    be.craftingItem = packet.craftingItem;

                    if (packet.expungeEntry != null) {
                        be.expungeEntry = packet.expungeEntry;
                        be.tryStartCrafting(player, be.craftingItem);
                    }

                    be.currentSpell = packet.spellId != null ? BrutalitySpells.getSpell(packet.spellId) : null;

                    be.setChanged();
                    player.level().sendBlockUpdated(packet.pos, be.getBlockState(), be.getBlockState(), 3);
                } catch (IllegalArgumentException e) {
                    Brutality.LOGGER.error("Received invalid TableOfWizardry state from player: " + player.getName().getString());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}