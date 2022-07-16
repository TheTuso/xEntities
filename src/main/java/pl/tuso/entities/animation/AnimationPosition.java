package pl.tuso.entities.animation;

import net.minecraft.core.Rotations;
import org.bukkit.util.Vector;

public class AnimationPosition {
    private static final Rotations defaultRotation = new Rotations(0.0F, 0.0F, 0.0F);
    private final Vector animationLocation;
    private Rotations animationHeadPosition;
    private Rotations animationLeftArmPosition;
    private Rotations animationRightArmPosition;
    private float animationXRot;
    private float animationYRot;

    public AnimationPosition() {
        this.animationLocation = new Vector();
        this.loadDefault();
    }

    public void loadDefault() {
        this.animationLocation.setX(0.0F).setY(0.0F).setZ(0.0F);
        this.animationHeadPosition = defaultRotation;
        this.animationLeftArmPosition = defaultRotation;
        this.animationRightArmPosition = defaultRotation;
        this.animationXRot = 0.0F;
        this.animationYRot = 0.0F;
    }

    public Vector getAnimationLocation() {
        return this.animationLocation;
    }

    public Rotations getAnimationHeadPosition() {
        return this.animationHeadPosition;
    }

    public void setAnimationHeadPosition(Rotations animationHeadPosition) {
        this.animationHeadPosition = animationHeadPosition;
    }

    public Rotations getAnimationLeftArmPosition() {
        return this.animationLeftArmPosition;
    }

    public void setAnimationLeftArmPosition(Rotations animationLeftArmPosition) {
        this.animationLeftArmPosition = animationLeftArmPosition;
    }

    public Rotations getAnimationRightArmPosition() {
        return this.animationRightArmPosition;
    }

    public void setAnimationRightArmPosition(Rotations animationRightArmPosition) {
        this.animationRightArmPosition = animationRightArmPosition;
    }

    public float getAnimationXRot() {
        return this.animationXRot;
    }

    public void setAnimationXRot(float animationXRot) {
        this.animationXRot = animationXRot;
    }

    public float getAnimationYRot() {
        return this.animationYRot;
    }

    public void setAnimationYRot(float animationYRot) {
        this.animationYRot = animationYRot;
    }
}
