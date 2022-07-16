package pl.tuso.entities;

import org.bukkit.plugin.java.JavaPlugin;
import pl.tuso.entities.command.EntityCommand;
import pl.tuso.entities.type.Types;

public class Entities extends JavaPlugin {
    @Override
    public void onEnable() {
        Types.register();
        new EntityCommand().register(this.getCommand("entity"));
    }

    @Override
    public void onDisable() {

    }
}
