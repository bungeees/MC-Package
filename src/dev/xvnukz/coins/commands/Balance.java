package dev.xvnukz.coins.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import dev.xvnukz.coins.RoseCoins;

public class Balance implements Listener, CommandExecutor{
	private xCoins plugin;
	
	public Balance(xCoins plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
		if(!(sender instanceof Player)) {
			FileConfiguration config = plugin.getConfig();
			
			if((config.getString("Config.General.cancel_console_execute_commands") == "true")) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot execute commands in the console."));
				return false;
			}
		}else {
	        if (args.length == 0) {
				FileConfiguration config = plugin.getConfig();
				if(config.getString("Config.Commands.Balance.enabled") == "true") {
	    			String BalancePermission = config.getString("Config.Commands.Balance.permission");
	    			if(sender.hasPermission(BalancePermission) || sender.hasPermission("rosecoins.admin")) {
	    	        	Player player = (Player) sender;
	    				String Prefix = config.getString("Config.General.prefix");
	    				String BalanceMessage = config.getString("Messages.Coins.balance_check");
	            		FileConfiguration playerdata = plugin.getPlayerData();
	    				int getCoins = Integer.valueOf(playerdata.getString("Players."+player.getUniqueId()+".coins"));
	    				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+BalanceMessage)	
	    				.replaceAll("%coins%", getCoins+""));
	    			}else {
	    				Player player = (Player) sender;
	    				String Prefix = config.getString("Config.General.prefix");
        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
        			}
    			}else {
    	        	Player player = (Player) sender;
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, this command is disabled for a admin!"));
				}
	        }
		}
		return false;
	}
}
