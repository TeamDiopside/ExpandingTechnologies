package nl.teamdiopside.expandingtechnologies.behaviour;

import net.minecraft.world.phys.AABB;

public interface IBetterContraptionBounds {
    void expandingtechnologies$setBetterBounds(AABB betterBounds);
    AABB expandingtechnologies$getBetterBounds();
    void expandingtechnologies$calculateBetterBounds();
}
