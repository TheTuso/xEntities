package pl.tuso.entities.type;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.jetbrains.annotations.NotNull;
import pl.tuso.entities.util.Reflect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public final class Registration {
    public static @NotNull EntityType register(String name, EntityType.EntityFactory entityFactory, MobCategory mobCategory, EntityType<?> model, float width, float height) {
        unfreezeRegistry();
        EntityType entityType = Registry.ENTITY_TYPE.registerMapping(Registry.ENTITY_TYPE.getId(model),
                ResourceKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("minecraft", name)),
                new EntityType<>(entityFactory, mobCategory, true, model.canSummon(), model.fireImmune(),
                        model.canSpawnFarFromPlayer(), ImmutableSet.of(), EntityDimensions.scalable(width, height), // EntityDimensions - server side hitbox
                        model.clientTrackingRange(), model.updateInterval()), Lifecycle.stable()).value();
        setDefaultAttributes(entityType);
        Types.VALUES.add(entityType);
        return entityType;
    }

    private static void unfreezeRegistry()  {
        Class<MappedRegistry> registryClass = MappedRegistry.class;
        try {
            final Field intrusiveHolderCache = registryClass.getDeclaredField("cc");
            intrusiveHolderCache.setAccessible(true);
            intrusiveHolderCache.set(Registry.ENTITY_TYPE, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
            final Field frozen = registryClass.getDeclaredField("ca");
            frozen.setAccessible(true);
            frozen.set(Registry.ENTITY_TYPE, false);
        } catch (IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
    }

    private static void setDefaultAttributes(EntityType entityType) {
        try {
            final Field suppliers = DefaultAttributes.class.getDeclaredField("b");
            suppliers.setAccessible(true);
            Map<EntityType<?>, AttributeSupplier> attributeSupplierMap = new HashMap<>((Map<EntityType<?>, AttributeSupplier>) suppliers.get(null));
            attributeSupplierMap.put(entityType, Mob.createMobAttributes().build());
            Reflect.setField(suppliers, null, attributeSupplierMap);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }
}
