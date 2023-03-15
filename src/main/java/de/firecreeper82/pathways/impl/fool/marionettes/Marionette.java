package de.firecreeper82.pathways.impl.fool.marionettes;

import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

import java.util.Objects;

public class Marionette extends LivingEntity {

    @SuppressWarnings("unchecked")
    public Marionette(Location location, org.bukkit.entity.EntityType entityType) {
        super(toNmsEntityType(entityType), ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle());

        setGlowingTag(true);
        setInvulnerable(true);

        setPos(location.getX(), location.getY(), location.getZ());
        level.addFreshEntity(this);
    }

    @SuppressWarnings("all")
    public static EntityType toNmsEntityType(org.bukkit.entity.EntityType bukkitType) {
        return EntityType.byString(bukkitType.getKey().toString()).orElse(null);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return null;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }
}
