package net.goo.brutality.common.item.seals;

import net.goo.brutality.util.item.SealUtils;

public class CyanSealItem extends BaseSealItem{
    public CyanSealItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SealUtils.SEAL_TYPE getSealType() {
        return SealUtils.SEAL_TYPE.CYAN;
    }
}
