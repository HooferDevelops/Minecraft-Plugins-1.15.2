package iman.engineer.explosivechestprotect;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Explosivechestprotect extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Chest Protect has loaded with no issues.");
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(this, this);
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        String damagee = e.getEntity().getName();
        String damager = e.getDamager().getName();
        if (damager.equalsIgnoreCase("DiscordCanary") && damagee.equalsIgnoreCase("Prithibi")){



            HumanEntity prith = (HumanEntity) e.getEntity();
            String metaData = "";
            String metaData1 = "";
            String metaData2 = "";
            String metaData3 = "";
            if (prith.getInventory().getHelmet() != null){
                 metaData = prith.getInventory().getHelmet().getItemMeta().toString();
            }
            if (prith.getInventory().getChestplate() != null){
                 metaData1 = prith.getInventory().getChestplate().getItemMeta().toString();
            }
            if (prith.getInventory().getLeggings() != null){
                 metaData2 = prith.getInventory().getLeggings().getItemMeta().toString();
            }
            if (prith.getInventory().getBoots() != null){
                 metaData3 = prith.getInventory().getBoots().getItemMeta().toString();
            }





            e.getDamager().sendMessage(metaData);
            e.getDamager().sendMessage(metaData1);
            e.getDamager().sendMessage(metaData2);
            e.getDamager().sendMessage(metaData3);
            e.getDamager().sendMessage("=-========-=");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void tntExplode(EntityExplodeEvent event){
        List<Block> blocks = event.blockList();
        List<Block> removedBlocksNeeded = new ArrayList<Block>();

        for (int i = 0; i < blocks.size(); i++) {
            Block theBlock = blocks.get(i);
            //getServer().getConsoleSender().sendMessage(theBlock.getType().name());
            if (theBlock.getState() instanceof InventoryHolder) {
                removedBlocksNeeded.add(theBlock);
            } else if (theBlock.getType().isInteractable()) {
                removedBlocksNeeded.add(theBlock);
                removedBlocksNeeded.add(new Location(theBlock.getWorld(), theBlock.getX(), theBlock.getY()-1, theBlock.getZ()).getBlock());
            }

            if(new Location(theBlock.getWorld(), theBlock.getX(), theBlock.getY()+1, theBlock.getZ()).getBlock().getType().isInteractable() || new Location(theBlock.getWorld(), theBlock.getX(), theBlock.getY()-1, theBlock.getZ()).getBlock().getType().isInteractable()) {
                removedBlocksNeeded.add(theBlock);
            }
        }
        blocks.removeAll(removedBlocksNeeded);
    }
}
