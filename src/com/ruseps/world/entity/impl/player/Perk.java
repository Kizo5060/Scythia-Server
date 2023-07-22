package com.ruseps.world.entity.impl.player;

public interface Perk {
    void apply(Attributes attributes);
    String getDescription();
}