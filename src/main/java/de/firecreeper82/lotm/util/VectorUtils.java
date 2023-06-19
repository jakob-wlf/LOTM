package de.firecreeper82.lotm.util;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class VectorUtils {
    public static void rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        v.setY(y).setZ(z);
    }

    public static Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static Vector getBackVector(Location loc) {
        final float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 90))));
        final float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 90))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }

    private static final double EPSILON = Math.ulp(1.0d) * 2d;
    private static boolean isSignificant(double value) {
        return Math.abs(value) >= EPSILON;
    }

    public static Location getRelativeLocation(Entity entity, double forward, double right, double up) {
        Location ret;
        if (entity instanceof LivingEntity) {
            ret = ((LivingEntity) entity).getEyeLocation();
        } else {
            ret = entity.getLocation();
        }
        Vector direction = null;
        if (isSignificant(forward)) {
            direction = ret.getDirection();
            ret.add(direction.clone().multiply(forward));
        }
        boolean hasUp = isSignificant(up);
        if (hasUp && direction == null) direction = ret.getDirection();
        if (isSignificant(right) || hasUp) {
            Vector rightDirection;
            if (direction != null && isSignificant(Math.abs(direction.getY()) - 1)) {
                rightDirection = direction.clone();
                double factor = Math.sqrt(1 - Math.pow(rightDirection.getY(), 2)); // a shortcut that lets us not normalize which is slow
                double nx = -rightDirection.getZ() / factor;
                double nz = rightDirection.getX() / factor;
                rightDirection.setX(nx);
                rightDirection.setY(0d);
                rightDirection.setZ(nz);
            } else {
                float yaw = ret.getYaw() + 90f;
                double yawRad = yaw * (Math.PI / 180d);
                double z = Math.cos(yawRad);
                double x = -Math.sin(yawRad);
                rightDirection = new Vector(x, 0d, z);
            }
            ret.add(rightDirection.clone().multiply(right));
            if (hasUp) {
                Vector upDirection = rightDirection.crossProduct(direction);
                ret.add(upDirection.clone().multiply(up));
            }
        }
        return ret;
    }
}