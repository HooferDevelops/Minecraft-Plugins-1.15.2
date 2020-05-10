package engineer.iman.headdrop.main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Head Drops has been loaded!");
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(this, this);
    }
    public static ItemStack getHead(Player player, String deathReason) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(ChatColor.YELLOW + player.getDisplayName() + ChatColor.YELLOW + "'s Head");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Belonged to " + player.getDisplayName());
        if (player.getKiller() != null){
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Killed by " + player.getKiller().getName());
        } else {
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + deathReason);
        }

        skull.setLore(lore);
        skull.setOwningPlayer(player);
        item.setItemMeta(skull);
        return item;
    }
    @EventHandler
    public void death(PlayerDeathEvent e){

        e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), getHead(e.getEntity().getPlayer(),e.getDeathMessage()));
    }

}
