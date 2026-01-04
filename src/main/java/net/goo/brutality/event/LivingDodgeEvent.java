package net.goo.brutality.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.fml.event.IModBusEvent;

/**
 * Called on the Mod Event Bus
 */
@Cancelable
public class LivingDodgeEvent extends LivingEvent implements IModBusEvent {
    private final DamageSource source;
    private float amount;

    public LivingDodgeEvent(LivingEntity entity, DamageSource source, float amount) {
        super(entity);
        this.source = source;
        this.amount = amount;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    /**
     * Gets called first on the Server, and sends a packet to the client, calling the Client version of the event, regardless of if the Server event is cancelled or not
     * If the Server event is cancelled, the dodge is annulled and damage will apply
     */
    @Cancelable
    public static class Server extends LivingDodgeEvent {
        public Server(LivingEntity entity, DamageSource source, float amount) {
            super(entity, source, amount);
        }
    }


    /**
     * Called on the client, no processing, not cancellable as nothing will happen after this event is called
     */
    public static class Client extends LivingDodgeEvent {
        public Client(LivingEntity entity, DamageSource source, float amount) {
            super(entity, source, amount);
        }
    }
}