package pl.tuso.entities.type;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import pl.tuso.entities.creatures.cactus.Cactus;
import pl.tuso.entities.creatures.cactusnpc.CactusNpc;

import java.util.HashSet;

public final class Types {
    public static final HashSet<EntityType> VALUES = new HashSet<>();
    public static EntityType<Cactus> CACTUS;
    public static EntityType<CactusNpc> CACTUS_NPC;

    public static void register() {
        CACTUS = Registration.register("cactus", Cactus::new, MobCategory.CREATURE, EntityType.ARMOR_STAND, 0.75F, 1.0F);
        CACTUS_NPC = Registration.register("cactus_npc", CactusNpc::new, MobCategory.CREATURE, EntityType.ARMOR_STAND, 0.75F, 1.0F);
    }
}
