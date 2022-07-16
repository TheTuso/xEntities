package pl.tuso.entities.type;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class EntityPartRig extends EntityRig {
    private final EntityRig parentEntity;
    private final Vector relativity;
    private float relativeYRot;

    public EntityPartRig(@NotNull EntityRig parentEntity, String name) {
        super((EntityType<? extends EntityRig>) parentEntity.getType(), parentEntity.getLevel());
        this.setPartName(name);
        this.setNoGravity(true);
        this.collides = false;
        this.persist = false;
        this.parentEntity = parentEntity;
        this.relativity = new Vector();
        this.parentEntity.addEntityPart(this);
        parentEntity.getLevel().addFreshEntity(this);
    }

    public Vector getRelativity() {
        return this.relativity;
    }

    public void positionRelatively() {
        Location reference = this.parentEntity.getBukkitEntity().getLocation();
        Vector direction = this.parentEntity.getModelDirection();
        Location pointInFront = reference.clone().add(direction.clone().multiply(this.relativity.getZ()));
        Vector up = new Vector(0.0D, 1.0D, 0.0D);
        Vector perpendcular = direction.clone().crossProduct(up).multiply(this.relativity.getX());
        Location location = pointInFront.clone().add(perpendcular);
        location.setY(location.getY() + this.parentEntity.getOffset() + this.relativity.getY());
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setYRot(this.parentEntity.getYRot() + this.relativeYRot);
    }

    public void setRelativeYRot(float relativeYRot) {
        this.relativeYRot = relativeYRot;
    }

    public float getRelativeYRot() {
        return this.relativeYRot;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) { // We want our entityPart to pass all damage to parentEntity
        return this.isInvulnerableTo(source) ? false : this.parentEntity.hurt(source, amount);
    }

    @Override
    public boolean isInWall() { // We don't want our parentEntity to get hurted when the entityPart goes into the wall
        return false;
    }
}
