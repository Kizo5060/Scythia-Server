package com.ruse.world.content;

public enum EffectTimer {

	OVERLOAD(15332),
    X2_DDR_1HR(15356),
    X2_DMG_1HR(600),
    X2_DR_30MIN(15358),
    X2_DMG_30MIN(15359),
    T1_INF_OVERLOAD(23124),
    T2_INF_OVERLOAD(23125),
    T3_INF_OVERLOAD(23126),
    ;

    EffectTimer(int clientSprite) {
        this.clientSprite = clientSprite;
    }

    private int clientSprite;

    public int getClientSprite() {
        return clientSprite;
    }

    public void setClientSprite(int sprite) {
        this.clientSprite = sprite;
    }
}