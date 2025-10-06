package net.goo.brutality.item.seals;

import net.goo.brutality.util.SealUtils;

public class QuantiteSealItem extends BaseSealItem{
    public QuantiteSealItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SealUtils.SEAL_TYPE getSealType() {
        return SealUtils.SEAL_TYPE.QUANTITE;
    }
}
