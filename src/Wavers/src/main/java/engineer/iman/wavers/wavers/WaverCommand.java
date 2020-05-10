package engineer.iman.wavers.wavers;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WaverCommand implements CommandExecutor {

    static Wavers plugin;
    public void init(Wavers waver){
        plugin = waver;
    }

    public static boolean parseCheck(String input){
        try{
            Integer.valueOf(input);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length > 0 && args[0] != null){



                if (parseCheck(args[0]) && player.getInventory().getItemInMainHand().getType() == Material.PAPER && player.getInventory().getItemInMainHand().getItemMeta().getLore() == null){
                    Integer moneyRequested = Integer.valueOf(args[0]);
                    if (moneyRequested <= 0){
                        return false;
                    }
                    if (plugin.eco.getBalance(player) >= moneyRequested){
                        plugin.eco.withdrawPlayer(player, moneyRequested);
                        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
                        ItemStack waver = new ItemStack(Material.PAPER, 1);
                        ItemMeta meta = waver.getItemMeta();
                        meta.setDisplayName(ChatColor.RESET + "$" + moneyRequested.toString() + " Waver");
                        List<String> lore = new ArrayList<>();
                        String moneyString = moneyRequested.toString();
                        lore.add(ChatColor.RED + "$" + moneyString);
                        //lore.add("$" + moneyRequested.toString());
                        meta.setLore(lore);
                        waver.setItemMeta(meta);
                        player.getInventory().addItem(waver);
                        player.sendMessage(ChatColor.GREEN + "Successfully made waver for $" + moneyString);
                    } else {
                        player.sendMessage(ChatColor.RED + "You do not have enough money for this waver.");
                    }
                    return true;
                }

            }

        }
        return false;
    }
}
