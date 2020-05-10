package engineer.iman.jukeboxhopper;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Jukebox;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Jukeboxhopper extends JavaPlugin implements Listener {

    List<Material> discs = new ArrayList<>();
    List<Integer> lengthMS = new ArrayList<>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "JukeboxHopper has been loaded!");
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(this, this);
        discs.add(Material.MUSIC_DISC_MELLOHI);
        discs.add(Material.MUSIC_DISC_11);
        discs.add(Material.MUSIC_DISC_13);
        discs.add(Material.MUSIC_DISC_BLOCKS);
        discs.add(Material.MUSIC_DISC_CAT);
        discs.add(Material.MUSIC_DISC_CHIRP);
        discs.add(Material.MUSIC_DISC_FAR);
        discs.add(Material.MUSIC_DISC_MALL);
        discs.add(Material.MUSIC_DISC_STAL);
        discs.add(Material.MUSIC_DISC_STRAD);
        discs.add(Material.MUSIC_DISC_WAIT);
        discs.add(Material.MUSIC_DISC_WARD);

        lengthMS.add(81600);
        lengthMS.add(66600);
        lengthMS.add(154800);
        lengthMS.add(327000);
        lengthMS.add(183000);
        lengthMS.add(183000);
        lengthMS.add(152400);
        lengthMS.add(190200);
        lengthMS.add(138000);
        lengthMS.add(184800);
        lengthMS.add(214800);
        lengthMS.add(246600);






    }

    public int getLengthOfDisc(Material disc){
        int length = 2;
        for (int i=0;i<discs.size();i++){
            if (discs.get(i) == disc){
                length = lengthMS.get(i);
            }
        }
        return length;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void addDiscToJukeBox(Block jukeBlock, Jukebox juke, Block hopperBlock, ItemStack item){
        juke.setRecord(item);
        juke.setPlaying(item.getType());

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "scheduling disc playing");


                BlockFace bottom = BlockFace.DOWN;
                Block bottomBlock = jukeBlock.getRelative(bottom);
                if (bottomBlock.getType() == Material.HOPPER){
                    Hopper bottomBlockHopper = (Hopper) bottomBlock.getState();
                    bottomBlockHopper.getInventory().addItem(juke.getRecord());
                }

                juke.setRecord(null);
                juke.setPlaying(null);
                juke.update();
                checkHopperBlock(hopperBlock, null);

            }
        }, (getLengthOfDisc(item.getType())/50) + 40);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "bye bye disc");
                Hopper hopper = (Hopper) hopperBlock.getState();
                hopper.getInventory().remove(item.getType());
            }
        }, 1);
        juke.update();
    }

    public void checkHopperBlock(Block hopperBlock, ItemStack item){
        if (item == null){
            Hopper hopper = (Hopper) hopperBlock.getState();
            ItemStack discCheck = hopper.getInventory().getItem(0);
            if (discCheck != null && discCheck.getType() != null && discCheck.getType().toString().contains("DISC")) {
                item = discCheck;
            } else {
                return;
            }
        }
        Directional data = (Directional) hopperBlock.getBlockData();

        BlockFace face = data.getFacing();
        Block subject = hopperBlock.getRelative(face);
        getServer().getConsoleSender().sendMessage(face.toString());
        getServer().getConsoleSender().sendMessage(subject.getType().toString());
        if (subject.getType() == Material.JUKEBOX){
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "i'm connected to a jukebox!");
            Jukebox juke = (Jukebox) subject.getState();
            if (juke.isPlaying()){

            } else {
                addDiscToJukeBox(subject, juke, hopperBlock, item);
            }

        }
    }
    @EventHandler
    public void BlockPhysicsEvent(BlockPhysicsEvent e){
        getServer().getConsoleSender().sendMessage(e.getSourceBlock().getType().toString());
    }

    @EventHandler
    public void blockUpdate(InventoryMoveItemEvent e){
        if (e.getDestination().getType() == InventoryType.HOPPER) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "i'm a hopper");
            Block hopperBlock = e.getDestination().getLocation().getBlock();
            ItemStack item = e.getItem();
            getServer().getConsoleSender().sendMessage( item.getType().toString());
            if (item.getType().toString().contains("DISC")) {
                checkHopperBlock(hopperBlock,item);
            }
        }
    }
    @EventHandler
    public void itemEnterHopper(InventoryPickupItemEvent e){
        if (e.getInventory().getType() == InventoryType.HOPPER){
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "i'm a hopper");
            Block hopperBlock = e.getInventory().getLocation().getBlock();
            getServer().getConsoleSender().sendMessage(hopperBlock.toString());
            ItemStack item = e.getItem().getItemStack();
            getServer().getConsoleSender().sendMessage( item.getType().toString());
            if (item.getType().toString().contains("DISC")){
                checkHopperBlock(hopperBlock,item);
            }

        }
    }
}
