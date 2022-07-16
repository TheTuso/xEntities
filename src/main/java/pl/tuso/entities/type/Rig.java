package pl.tuso.entities.type;

import net.minecraft.core.Rotations;

import java.util.HashSet;

public interface Rig {
    void setInvisible(boolean invisible);

    boolean isBaby();

    void setSmall(boolean small);

    boolean isSmall();

    void setShowArms(boolean showArms);

    boolean isShowArms();

    void setNoBasePlate(boolean hideBasePlate);

    boolean isNoBasePlate();

    void setMarker(boolean marker);

    boolean isMarker();

    void setHeadPose(Rotations angle);

    void setLeftArmPose(Rotations angle);

    void setRightArmPose(Rotations angle);

    Rotations getHeadPose();

    Rotations getLeftArmPose();

    Rotations getRightArmPose();

    HashSet<EntityPartRig> getEntityParts();

    void addEntityPart(EntityPartRig entityPartRig);

    void removeEntityPart(EntityPartRig entityPartRig);

    void setOffset(float offset);

    float getOffset();

    void setPartName(String partName);

    String getPartName();
}
