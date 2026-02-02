package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ServerboundTableOfWizardryUpdatePacket {
    private final BlockPos pos;
    private final String section, state;
    private final ResourceLocation spellId;

    public ServerboundTableOfWizardryUpdatePacket(BlockPos pos, String section, String state, @Nullable ResourceLocation spellId) {
        this.pos = pos;
        this.section = section;
        this.state = state;
        this.spellId = spellId;
    }

    public ServerboundTableOfWizardryUpdatePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.section = buf.readUtf();
        this.state = buf.readUtf();
        // Use readNullable to keep the code clean
        this.spellId = buf.readNullable(FriendlyByteBuf::readResourceLocation);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeUtf(this.section);
        buf.writeUtf(this.state);
        // writeNullable handles the boolean flag and the data automatically
        buf.writeNullable(this.spellId, FriendlyByteBuf::writeResourceLocation);
    }

    public static void handle(ServerboundTableOfWizardryUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            // Security check: Is the player close enough to this block to actually modify it?
            if (player.level().getBlockEntity(packet.pos) instanceof TableOfWizardryBlockEntity be) {
                try {
                    be.currentSection = TableOfWizardryScreen.TableOfWizardryBookSection.valueOf(packet.section);
                    be.currentState = TableOfWizardryBlockEntity.GuiState.valueOf(packet.state);

                    // If spellId is null, we should probably clear the current spell
                    be.currentSpell = packet.spellId != null ? BrutalitySpells.getSpell(packet.spellId) : null;

                    be.setChanged();
                    player.level().sendBlockUpdated(packet.pos, be.getBlockState(), be.getBlockState(), 3);
                } catch (IllegalArgumentException e) {
                    // Safety: If someone sends a fake packet with a wrong Enum name, don't crash the server
                    Brutality.LOGGER.error("Received invalid TableOfWizardry state from player: " + player.getName().getString());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}