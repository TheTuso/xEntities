package pl.tuso.entities.creatures.cactus;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class CactusWaterAvoidingRandomStrollGoal extends WaterAvoidingRandomStrollGoal {
    public CactusWaterAvoidingRandomStrollGoal(PathfinderMob mob, double speed) {
        super(mob, speed);
    }

    @Override
    public void start() {
        super.start();
        if (this.mob instanceof Cactus cactus) {
            cactus.playAnimation("cactus_walk");
            cactus.moving = true;
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (this.mob instanceof Cactus cactus) {
            cactus.playAnimation("cactus_rest");
            cactus.moving = false;
        }
    }
}
