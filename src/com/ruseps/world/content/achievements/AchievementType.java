package com.ruseps.world.content.achievements;

import org.apache.commons.lang3.text.WordUtils;

public enum AchievementType {
    EASY,
    MEDIUM,
    HARD,
    EXPERT,
    ;

    @Override
    public String toString() {
        return WordUtils.capitalize(this.name().toLowerCase().replaceAll("_", " "));
    }
}
