package pl.tuso.entities.type;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.tuso.entities.animation.Animation;
import pl.tuso.entities.animation.AnimationPosition;
import pl.tuso.entities.animation.Frame;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;

public class EntityRig extends PathfinderMob implements Rig, Animator {
    // LivingEntity
    private static final EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS = EntityDataSerializers.BYTE.createAccessor(8);
    private static final EntityDataAccessor<Float> DATA_HEALTH_ID = EntityDataSerializers.FLOAT.createAccessor(9);
    private static final EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID = EntityDataSerializers.INT.createAccessor(10);
    private static final EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID = EntityDataSerializers.BOOLEAN.createAccessor(11);
    private static final EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID = EntityDataSerializers.INT.createAccessor(12);
    private static final EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID = EntityDataSerializers.INT.createAccessor(13);
    private static final EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID = EntityDataSerializers.OPTIONAL_BLOCK_POS.createAccessor(14);
    // ArmorStand
    private static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS = EntityDataSerializers.BYTE.createAccessor(15);
    private static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = EntityDataSerializers.ROTATIONS.createAccessor(16);
    private static final EntityDataAccessor<Rotations> DATA_BODY_POSE = EntityDataSerializers.ROTATIONS.createAccessor(17);
    private static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE = EntityDataSerializers.ROTATIONS.createAccessor(18);
    private static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE = EntityDataSerializers.ROTATIONS.createAccessor(19);
    private static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE = EntityDataSerializers.ROTATIONS.createAccessor(20);
    private static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE = EntityDataSerializers.ROTATIONS.createAccessor(21);
    // Default poses
    private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    // Poses
    private Rotations headPose;
    private Rotations leftArmPose;
    private Rotations rightArmPose;
    // EntityParts
    private final HashSet<EntityPartRig> entityPartRigs;
    // Offset
    private float offset;
    // partName
    private String partName;
    // Animation
    private final AnimationPosition animationPosition;
    private Animation animation;
    private String animationName;
    private int frames;

    public EntityRig(EntityType<? extends EntityRig> type, Level world) {
        super(type, world);
        this.headPose = DEFAULT_HEAD_POSE;
        this.leftArmPose = DEFAULT_LEFT_ARM_POSE;
        this.rightArmPose = DEFAULT_RIGHT_ARM_POSE;
        this.entityPartRigs = new HashSet<>();
        this.partName = "root";
        this.animationPosition = new AnimationPosition();

        this.setPersistenceRequired();
        this.disableModelDrop();
        this.setShowArms(true);
        this.setNoBasePlate(true);
        this.setInvisible(true);
    }

    @Override
    public void defineSynchedData() {
        // LivingEntity
        this.entityData.define(DATA_LIVING_ENTITY_FLAGS, (byte) 0);
        this.entityData.define(DATA_EFFECT_COLOR_ID, 0);
        this.entityData.define(DATA_EFFECT_AMBIENCE_ID, false);
        this.entityData.define(DATA_ARROW_COUNT_ID, 0);
        this.entityData.define(DATA_STINGER_COUNT_ID, 0);
        this.entityData.define(DATA_HEALTH_ID, 1.0F);
        this.entityData.define(SLEEPING_POS_ID, Optional.empty());
        // ArmorStand
        this.entityData.define(DATA_CLIENT_FLAGS, (byte) 0);
        this.entityData.define(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
        this.entityData.define(DATA_BODY_POSE, DEFAULT_BODY_POSE);
        this.entityData.define(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
        this.entityData.define(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
        this.entityData.define(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
        this.entityData.define(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
    }

    @Override
    public void tick() {
        super.tick();

        if (animation != null) {
            if (this.frames == this.animation.getFrames()) this.frames = 0;
            for (EntityPartRig entityPartRig : this.entityPartRigs) {
                Frame frame = this.animation.getEntityPartFrames(entityPartRig.getPartName())[this.frames];
                if (frame != null) entityPartRig.loadFrame(frame);
            }
            Frame rootFrame = this.animation.getEntityPartFrames(this.partName)[this.frames];
            if (rootFrame != null) this.loadFrame(rootFrame);
            this.frames++;
        }

        this.entityPartRigs.forEach(entityPartRig -> entityPartRig.positionRelatively());
        Bukkit.getOnlinePlayers().forEach(player -> {
            ((CraftPlayer) player).getHandle().connection.send(this.getTeleportPacket());
            ((CraftPlayer) player).getHandle().connection.send(this.getSetDataPacket());
        });
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (this.entityPartRigs.size() > 0) {
            this.entityPartRigs.forEach(entityPartRig -> entityPartRig.remove(reason));
        }
        this.setRemoved(reason);
    }

    @Override
    public boolean isNoAi() {
        return false; // setSmall uses the same bit field
    }

    @Override
    public void setInvisible(boolean invisible) {
        super.setInvisible(invisible);
        this.persistentInvisibility = invisible;
    }

    @Override
    public boolean isBaby() {
        return this.isSmall();
    }

    @Override
    public void setSmall(boolean small) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 1, small));
    }

    @Override
    public boolean isSmall() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 1) != 0;
    }

    @Override
    public void setShowArms(boolean showArms) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 4, showArms));
    }

    @Override
    public boolean isShowArms() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 4) != 0;
    }

    @Override
    public void setNoBasePlate(boolean hideBasePlate) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 8, hideBasePlate));
    }

    @Override
    public boolean isNoBasePlate() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 8) != 0;
    }

    @Override
    public void setMarker(boolean marker) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 16, marker));
    }

    @Override
    public boolean isMarker() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 16) != 0;
    }

    @Override
    public void setHeadPose(Rotations angle) {
        this.headPose = angle;
        this.entityData.set(DATA_HEAD_POSE, angle);
    }

    @Override
    public void setLeftArmPose(Rotations angle) {
        this.leftArmPose = angle;
        this.entityData.set(DATA_LEFT_ARM_POSE, angle);
    }

    @Override
    public void setRightArmPose(Rotations angle) {
        this.rightArmPose = angle;
        this.entityData.set(DATA_RIGHT_ARM_POSE, angle);
    }

    @Override
    public Rotations getHeadPose() {
        return this.headPose;
    }

    @Override
    public Rotations getLeftArmPose() {
        return this.leftArmPose;
    }

    @Override
    public Rotations getRightArmPose() {
        return this.rightArmPose;
    }

    @Override
    public HashSet<EntityPartRig> getEntityParts() {
        return this.entityPartRigs;
    }

    @Override
    public void addEntityPart(EntityPartRig entityPartRig) {
        this.entityPartRigs.add(entityPartRig);
    }

    @Override
    public void removeEntityPart(EntityPartRig entityPartRig) {
        this.entityPartRigs.remove(entityPartRig);
    }

    @Override
    public void setOffset(float offset) {
        this.offset = offset;
    }

    @Override
    public float getOffset() {
        return this.offset;
    }

    @Override
    public void setPartName(String partName) {
        this.partName = partName;
    }

    @Override
    public String getPartName() {
        return this.partName;
    }

    @Override
    public void playAnimation(String name) {
        this.frames = 0;
        this.animationName = name;
        this.animation = Animation.load(name);
        if (this.animation == null) {
            this.animationPosition.loadDefault();
            this.entityPartRigs.forEach(entityPartRig -> entityPartRig.getAnimationPosition().loadDefault());
        }
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public String getAnimationName() {
        return this.animationName;
    }

    @Override
    public AnimationPosition getAnimationPosition() {
        return this.animationPosition;
    }

    @Override
    public boolean isAnimationEnded() {
        return this.frames == this.animation.getFrames();
    }

    @Override
    public void loadFrame(@NotNull Frame frame) {
        this.animationPosition.getAnimationLocation().setX(frame.getX()).setY(frame.getY()).setZ(frame.getZ());
        this.animationPosition.setAnimationXRot(frame.getXRot());
        this.animationPosition.setAnimationYRot(frame.getYRot());
        this.animationPosition.setAnimationHeadPosition(frame.getHeadPose());
        this.animationPosition.setAnimationLeftArmPosition(frame.getLeftArmPose());
        this.animationPosition.setAnimationRightArmPosition(frame.getRightArmPose());
    }

    private void disableModelDrop() {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            this.setDropChance(equipmentSlot, 0.0F);
        }
    }

    public Vector getModelDirection() {
        Vector vector = new Vector();
        double rotX = this.getYRot();
        double rotY = this.getXRot();
        double xz = Math.cos(Math.toRadians(rotY));
        return vector.setX(-xz * Math.sin(Math.toRadians(rotX))).setY(-Math.sin(Math.toRadians(rotY))).setZ(xz * Math.cos(Math.toRadians(rotX)));
    }

    private @NotNull ClientboundTeleportEntityPacket getTeleportPacket() {
        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(this);
        try {
            final Field x = teleportEntityPacket.getClass().getDeclaredField("b");
            final Field y = teleportEntityPacket.getClass().getDeclaredField("c");
            final Field z = teleportEntityPacket.getClass().getDeclaredField("d");
            final Field yRot = teleportEntityPacket.getClass().getDeclaredField("e");
            final Field xRot = teleportEntityPacket.getClass().getDeclaredField("f");
            x.setAccessible(true);
            y.setAccessible(true);
            z.setAccessible(true);
            yRot.setAccessible(true);
            xRot.setAccessible(true);
            Vector relative = this.getRelativePosition();
            x.setDouble(teleportEntityPacket, relative.getX());
            y.setDouble(teleportEntityPacket, relative.getY());
            z.setDouble(teleportEntityPacket, relative.getZ());
            yRot.setByte(teleportEntityPacket, (byte)((int)((this.getYRot() + this.animationPosition.getAnimationYRot()) * 256.0F / 360.0F)));
            xRot.setByte(teleportEntityPacket, (byte)((int)((this.getXRot() + this.animationPosition.getAnimationXRot()) * 256.0F / 360.0F)));
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
        return teleportEntityPacket;
    }

    private @NotNull ClientboundSetEntityDataPacket getSetDataPacket() {
        ClientboundSetEntityDataPacket setEntityDataPacket = new ClientboundSetEntityDataPacket(this.getId(), this.entityData, true);
        setEntityDataPacket.getUnpackedData().set(16, new SynchedEntityData.DataItem<>(this.DATA_HEAD_POSE, this.addRotations(this.headPose, this.animationPosition.getAnimationHeadPosition())));
        setEntityDataPacket.getUnpackedData().set(18, new SynchedEntityData.DataItem<>(this.DATA_RIGHT_ARM_POSE, this.addRotations(this.rightArmPose, this.animationPosition.getAnimationRightArmPosition())));
        setEntityDataPacket.getUnpackedData().set(19, new SynchedEntityData.DataItem<>(this.DATA_LEFT_ARM_POSE, this.addRotations(this.leftArmPose, this.animationPosition.getAnimationLeftArmPosition())));
        return setEntityDataPacket;
    }

    private @NotNull Vector getRelativePosition() {
        Location reference = this.getBukkitEntity().getLocation();
        Vector direction = this.getModelDirection();
        Location pointInFront = reference.clone().add(direction.clone().multiply(this.animationPosition.getAnimationLocation().getZ()));
        Vector up = new Vector(0.0D, 1.0D, 0.0D);
        Vector perpendcular = direction.clone().crossProduct(up).multiply(this.animationPosition.getAnimationLocation().getX());
        Location location = pointInFront.clone().add(perpendcular);
        location.setY(location.getY() + this.getOffset() + this.animationPosition.getAnimationLocation().getY());
        return location.toVector();
    }

    @Contract("_, _ -> new")
    private @NotNull Rotations addRotations(@NotNull Rotations first, @NotNull Rotations second) {
        return new Rotations(first.getX() + second.getX(), first.getY() + second.getY(), first.getZ() + second.getZ());
    }

    private byte setBit(byte value, int bitField, boolean set) {
        return set ? (byte) (value | bitField) : (byte) (value & ~bitField);
    }
}
