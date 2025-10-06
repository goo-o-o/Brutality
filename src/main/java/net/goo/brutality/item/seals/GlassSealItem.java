package net.goo.brutality.item.seals;

import net.goo.brutality.util.SealUtils;

public class GlassSealItem extends BaseSealItem{
    public GlassSealItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SealUtils.SEAL_TYPE getSealType() {
        return SealUtils.SEAL_TYPE.GLASS;
    }
}
