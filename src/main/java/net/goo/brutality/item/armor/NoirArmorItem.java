package net.goo.brutality.item.armor;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.base.BrutalityArmorItem;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class NoirArmorItem extends BrutalityArmorItem {


    public NoirArmorItem(ArmorMaterial pMaterial, Type pType, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pMaterial, pType, rarity, descriptionComponents);
    }

    @SubscribeEvent
    public static void onRenderNametag(RenderNameTagEvent event) {
        if (event.getEntity() instanceof Player player)
            if (ModUtils.hasFullArmorSet(player, BrutalityArmorMaterials.NOIR)) {
                event.setResult(Event.Result.DENY);
            }
    }


    


}
