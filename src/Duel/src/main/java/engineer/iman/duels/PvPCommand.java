package engineer.iman.duels;


import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class PvPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player player = (Player) sender;

            List<MetadataValue> values = null;
            if (player.getMetadata(player.getName()+"PVP") != null){
                values  = player.getMetadata(player.getName()+"PVP");
            }

            // PvP Toggle
            if (values != null && values.size() > 0 && values.get(0).asBoolean() == true){
                player.sendMessage(ChatColor.RED + "You have disabled PvP.");
                FixedMetadataValue meta = new FixedMetadataValue(Duel.getPlugin(Duel.class), false);
                player.setMetadata(player.getName()+"PVP", meta);
                return true;
            } else {
                FixedMetadataValue meta = new FixedMetadataValue(Duel.getPlugin(Duel.class), true);
                player.setMetadata(player.getName()+"PVP", meta);
                player.sendMessage(ChatColor.RED + "You have enabled PvP.");
                return true;
            }






        }
        IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
        Player player = (Player) sender;
        User user = new User(player, (net.ess3.api.IEssentials) essentials);

        return true;
    }
}
