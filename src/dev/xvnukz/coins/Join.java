package dev.xvnukz.coins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

public class Join implements Listener{
	
	private RoseCoins plugin;
	
	public Join(xCoins plugin) {
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void Join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		FileConfiguration playerdata = plugin.getPlayerData();
		
		if(player.hasPlayedBefore()) {
			if(!(playerdata.getString("Players."+player.getUniqueId()) == null)) {
				int getCoins = Integer.valueOf(playerdata.getString("Players."+player.getUniqueId()+".coins"));
				FileConfiguration config = plugin.getConfig();
				String JoinMessage = config.getString("Messages.Join.join_message");
				String Prefix = config.getString("Config.General.prefix");
				if(config.getString("Config.Join.send_message_join") == "true") {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+JoinMessage).replaceAll("%coins%", getCoins + ""));	
				}
			}else {
				FileConfiguration config = plugin.getConfig();
				String CreatingMessage = config.getString("Messages.File.file_creating");
				String Prefix = config.getString("Config.General.prefix");
				String ConfigCoins = config.getString("Config.General.default_coins");
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+CreatingMessage));
				
				playerdata.set("Players."+player.getUniqueId()+".name", player.getName());
				playerdata.set("Players."+player.getUniqueId()+".coins", ConfigCoins);
				plugin.savePlayerData();
				
				String CreatedMessage = config.getString("Messages.File.file_created");
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+CreatedMessage));
			}
		}
		FileConfiguration config = plugin.getConfig();
		if(config.getString("Config.Join.change_level_on_join") == "true") {
			player.setLevel(0);
			int getCoins = Integer.valueOf(playerdata.getString("Players."+player.getUniqueId()+".coins"));
			player.setLevel(getCoins);	
		}
	}
}
