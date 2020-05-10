package engineer.iman.duels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Duel extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Dueling has been loaded!");
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(this, this);
        this.getCommand("duel").setExecutor(new DuelCommand());
        this.getCommand("pvp").setExecutor(new PvPCommand());
        this.getCommand("bounty").setExecutor(new DuelCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){

        Entity damageE = e.getDamager();

        if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
            if (e.getDamager() instanceof Arrow){
                Arrow arrow = (Arrow) e.getDamager();
                damageE = (Entity) arrow.getShooter();
            } else if (e.getDamager() instanceof Trident){
                Trident trident = (Trident) e.getDamager();
                damageE = (Entity) trident.getShooter();
            }
        }

        if (e.getEntity() instanceof Player && damageE instanceof Player){
            Player damagee = (Player) e.getEntity();
            Player damager = (Player) damageE;

            if (damagee != null && damager != null){
                List<MetadataValue> duel1 = damagee.getMetadata(damager.getName());
                List<MetadataValue> duel2 = damager.getMetadata(damagee.getName());

                List<MetadataValue> pvp1 = damagee.getMetadata(damagee.getName()+"PVP");
                List<MetadataValue> pvp2 = damager.getMetadata(damager.getName()+"PVP");

                if ((duel1 != null && duel1.size() > 0 && duel1.get(0).asBoolean() == true && duel2 != null && duel2.size() > 0 && duel2.get(0).asBoolean() == true) || (pvp1 != null && pvp1.size() > 0 && pvp1.get(0).asBoolean() == true && pvp2 != null && pvp2.size() > 0 && pvp2.get(0).asBoolean() == true)){
                    // oooo he dead broo he gonna dieeeee
                } else {
                    e.setCancelled(true);
                }

            }
        }

    }

    @EventHandler
    public void death(PlayerDeathEvent e){
        if (e.getEntity() instanceof Player){
            Player damagee = (Player) e.getEntity();
            if (damagee.getKiller() != null && damagee.getKiller() instanceof Player){

                Player damager = (Player) damagee.getKiller();

                if (damagee != null && damager != null){
                    List<MetadataValue> values = damagee.getMetadata(damager.getName());
                    List<MetadataValue> values2 = damager.getMetadata(damagee.getName());

                    if (values != null && values.size() > 0 && values.get(0).asBoolean() == true && values2 != null && values2.size() > 0 && values2.get(0).asBoolean() == true){
                        damager.sendMessage(ChatColor.RED + "You have won the duel, congratulations!");
                        damagee.sendMessage(ChatColor.RED + "You have lost the duel, oh how sad.");
                        Bukkit.broadcastMessage(ChatColor.GOLD + damager.getDisplayName() + " has won a duel against: " + damagee.getDisplayName() + "!");
                        FixedMetadataValue meta = new FixedMetadataValue(Duel.getPlugin(Duel.class), false);
                        damager.setMetadata(damagee.getName(), meta);
                        damagee.setMetadata(damager.getName(), meta);
                    }

                }

            }

        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){

    }



}

