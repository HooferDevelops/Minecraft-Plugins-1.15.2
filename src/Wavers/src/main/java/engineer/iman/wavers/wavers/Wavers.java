package engineer.iman.wavers.wavers;


import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;

public class Wavers extends JavaPlugin implements Listener {

    public Economy eco = null;

    public static void spawnFireworks(Location location, int amount){
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(new Location(location.getWorld(),0,0,0), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());



        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    @Override
    public void onEnable() {
        if (!setupEconomy()){
            getLogger().severe("Wavers failed to load, make sure vault is installed!");
        }
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Wavers has been loaded!");
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(this, this);
        WaverCommand wav = new WaverCommand();
        wav.init(this);
        this.getCommand("waver").setExecutor(wav);
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAPER && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore() != null){
            String lore = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(0);
            if (lore.contains("$")){
                lore = ChatColor.stripColor(lore);
                lore = lore.replace("$", "");
                Integer money = Integer.parseInt(lore);

                Player player = (Player) e.getPlayer();
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
                player.sendMessage(ChatColor.GREEN + "Redeemed waver for $" + money.toString());
                eco.depositPlayer(player, money);
                spawnFireworks(player.getLocation(), 3);
            }
        }
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
