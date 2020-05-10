package engineer.iman.duels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class DuelCommand implements CommandExecutor {

    Plugin plugin;

    public String format(String info){

        return ChatColor.translateAlternateColorCodes('&', info);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player player = (Player) sender;


            if (args.length > 0 && args[0] != "" || args[0] != null){
                Player requestee = Bukkit.getPlayer(args[0]);
                if (requestee != null && requestee != sender){


                    List<MetadataValue> values = null;
                    if (player.getMetadata(requestee.getName()) != null){
                        values  = player.getMetadata(requestee.getName());
                    }

                        // Cancel the duel
                        if (values != null && values.size() > 0 && values.get(0).asBoolean() == true){
                            requestee.sendMessage(format("&3&l" + sender.getName() + "&r&c has cancelled the duel or duel request. PvP is now disabled."));
                            player.sendMessage(format("&cYou have cancelled your duel or duel request with &3&l" + requestee.getName()));
                            FixedMetadataValue meta = new FixedMetadataValue(Duel.getPlugin(Duel.class), false);
                            player.setMetadata(requestee.getName(), meta);
                            requestee.setMetadata(player.getName(), meta);
                            return true;
                        }


                        // Request a duel
                        sender.sendMessage(format("&cYou have sent a duel request to &3&l" + requestee.getName()));
                        FixedMetadataValue meta = new FixedMetadataValue(Duel.getPlugin(Duel.class), true);
                        player.setMetadata(requestee.getName(), meta);

                        List<MetadataValue> v = requestee.getMetadata(player.getName());
                        List<MetadataValue> v2 = player.getMetadata(requestee.getName());

                        if (v != null && v.size() > 0 && v.get(0).asBoolean() == true && v2 != null && v2.size() > 0 && v2.get(0).asBoolean() == true){
                            requestee.sendMessage(format("&cThe duel with &3&l" + sender.getName() + "&r&c has begun!"));
                            sender.sendMessage(format("&cThe duel with &3&l" + requestee.getName() + "&r&c has begun!"));
                        } else {
                            requestee.sendMessage(format("&3&l" + sender.getName() + "&r&c has requested to duel with you. Run &3&l\"/duel " + sender.getName() + "\"&r&c to accept."));
                        }


                }
            }


        }

        return true;
    }
}
