package pl.tuso.entities.creatures.cactusnpc;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.tuso.entities.creatures.cactus.Cactus;
import pl.tuso.entities.type.EntityRig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CactusNpc extends Cactus {
    private final ArrayList<String> animations;
    private final Random random;
    private int time;
    private int tick;

    public CactusNpc(EntityType<? extends EntityRig> type, Level world) {
        super(type, world);

        this.animations = new ArrayList<>();
        this.random = new Random(this.getId());
        this.time = 20 * (4 + this.random.nextInt(4));

        this.animations.add("cactus_backflip");
        this.animations.add("cactus_jumps");
        this.collides = false;
    }

    @Override
    public void tick() {
        super.tick();
        List<Entity> entities = this.getBukkitEntity().getNearbyEntities(8.0D, 8.0D, 8.0D);
        Location location = this.getBukkitEntity().getLocation();
        Player result = null;
        double distance = Double.MAX_VALUE;
        for (Entity entity : entities) {
            if (entity instanceof Player player) {
                if (!Bukkit.getOnlinePlayers().contains(player)) continue; // We don't want npc here
                if (location.distance(player.getLocation()) < distance) {
                    distance = location.distance(player.getLocation());
                    result = player;
                }
            }
        }
        if (result != null) {
            this.lookAt(((CraftPlayer) result).getHandle(), 360.0F, 360.0F);
        }
        if (this.tick == this.time) {
            this.playAnimation(this.animations.get(this.random.nextInt(this.animations.size())));
        }
        if (!this.getAnimationName().equals("cactus_rest") && this.isAnimationEnded()) {
            this.playAnimation("cactus_rest");
            this.time = 20 * (8 + this.random.nextInt(16));
            this.tick = 0;
        }
        this.tick++;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source.equals(DamageSource.OUT_OF_WORLD)) super.hurt(source, amount);
        return false;
    }

    @Override
    public void registerGoals() {
        // Nothing
    }
}
