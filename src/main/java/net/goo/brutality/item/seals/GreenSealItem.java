package net.goo.brutality.item.seals;

import net.goo.brutality.util.SealUtils;

public class GreenSealItem extends BaseSealItem{
    public GreenSealItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SealUtils.SEAL_TYPE getSealType() {
        return SealUtils.SEAL_TYPE.GREEN;
    }
}
