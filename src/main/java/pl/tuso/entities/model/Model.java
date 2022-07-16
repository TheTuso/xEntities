package pl.tuso.entities.model;

import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Model {
    public static final ItemStack CACTUS_TORSO = create(Material.PAPER, 1);
    public static final ItemStack CACTUS_LEFT_ARM = create(Material.PAPER, 2);
    public static final ItemStack CACTUS_RIGHT_ARM = create(Material.PAPER, 3);
    public static final ItemStack CACTUS_LEFT_LEG = create(Material.PAPER, 4);
    public static final ItemStack CACTUS_RIGHT_LEG = create(Material.PAPER, 5);

    private static ItemStack create(Material material, int modelData) {
        org.bukkit.inventory.ItemStack itemStack = new org.bukkit.inventory.ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(modelData);
        itemStack.setItemMeta(itemMeta);
        return CraftItemStack.asNMSCopy(itemStack);
    }
}
