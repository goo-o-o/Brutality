package net.goo.brutality.common.item.seals;

import net.goo.brutality.util.item.SealUtils;

public class BombSealItem extends BaseSealItem{
    public BombSealItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SealUtils.SEAL_TYPE getSealType() {
        return SealUtils.SEAL_TYPE.BOMB;
    }
}
