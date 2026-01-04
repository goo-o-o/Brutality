package net.goo.brutality.event;

import net.goo.brutality.magic.IBrutalitySpell;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class ConsumeManaEvent extends Event implements IModBusEvent {
    private final Player player;
    private final float amount;
    private final int spellLevel;
    private final IBrutalitySpell spell;

    public ConsumeManaEvent(Player player, IBrutalitySpell spell, int spellLevel, float amount) {
        this.player = player;
        this.amount = amount;
        this.spellLevel = spellLevel;
        this.spell = spell;
    }

    public Player getPlayer() {
        return player;
    }

    public float getAmount() {
        return amount;
    }

    public int getSpellLevel() {
        return spellLevel;
    }

    public IBrutalitySpell getSpell() {
        return spell;
    }
}