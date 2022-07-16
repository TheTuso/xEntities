package pl.tuso.entities.animation;

import net.minecraft.core.Rotations;

public class Frame {
    private final Rotations headPose;
    private final Rotations leftArmPose;
    private final Rotations rightArmPose;
    private final double x;
    private final double y;
    private final double z;
    private final float xRot;
    private final float yRot;

    public Frame(Rotations headPose, Rotations leftArmPose, Rotations rightArmPose, double x, double y, double z, float xRot, float yRot) {
        this.headPose = headPose;
        this.leftArmPose = leftArmPose;
        this.rightArmPose = rightArmPose;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public Rotations getHeadPose() {
        return this.headPose;
    }

    public Rotations getLeftArmPose() {
        return this.leftArmPose;
    }

    public Rotations getRightArmPose() {
        return this.rightArmPose;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
    public float getXRot() {
        return this.xRot;
    }

    public float getYRot() {
        return this.yRot;
    }
}
