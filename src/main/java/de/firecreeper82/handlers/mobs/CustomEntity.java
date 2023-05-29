package de.firecreeper82.handlers.mobs;

import de.firecreeper82.pathways.MobUsableAbility;
import jline.internal.Nullable;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public record CustomEntity(@NonNull String name, @NonNull String id, int rarity, @NonNull ItemStack drop,
                           @NonNull EntityType entityType, @Nullable Integer maxHealth,
                           @Nullable BeyonderMobs beyonderMobs, @Nullable EntityType spawnType, String particle,
                           boolean repeatingParticles, MobUsableAbility[] abilities) {
}
