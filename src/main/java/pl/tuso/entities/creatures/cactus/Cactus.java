package pl.tuso.entities.creatures.cactus;

import net.minecraft.core.Rotations;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import pl.tuso.entities.Entities;
import pl.tuso.entities.model.Model;
import pl.tuso.entities.type.EntityPartRig;
import pl.tuso.entities.type.EntityRig;

public class Cactus extends EntityRig { // Cactus is an example entity
    private final EntityPartRig arms;
    public boolean moving;

    public Cactus(EntityType<? extends EntityRig> type, Level world) {
        super(type, world);

        this.setOffset(-1.25F);
        this.setItemSlot(EquipmentSlot.HEAD, Model.CACTUS_TORSO);
        this.setItemSlot(EquipmentSlot.OFFHAND, Model.CACTUS_LEFT_LEG);
        this.setItemSlot(EquipmentSlot.MAINHAND, Model.CACTUS_RIGHT_LEG);

        this.arms = new EntityPartRig(this, "arms");
        this.arms.getRelativity().setY(0.25F);
        this.arms.setItemSlot(EquipmentSlot.OFFHAND, Model.CACTUS_LEFT_ARM);
        this.arms.setItemSlot(EquipmentSlot.MAINHAND, Model.CACTUS_RIGHT_ARM);
        this.arms.setLeftArmPose(new Rotations(0.0F, 0.0F, -15.0F));
        this.arms.setRightArmPose(new Rotations(0.0F, 0.0F, 15.0F));

        this.playAnimation("cactus_rest");

        ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25F);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getHealth() < amount) {
            this.playAnimation("cactus_death");
        } else {
            this.playAnimation("cactus_hurt");
        }
        Bukkit.getScheduler().runTaskLater(Entities.getPlugin(Entities.class), () -> {
            if (!this.getAnimationName().equals("cactus_death")) {
                if (this.moving) {
                    this.playAnimation("cactus_walk");
                } else {
                    this.playAnimation("cactus_rest");
                }
            }
        }, 20);
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        this.setYRot(this.getYHeadRot());
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CactusWaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }
}
