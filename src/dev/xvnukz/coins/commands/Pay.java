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

import dev.xvnukz.coins.xCoins;

public class Pay implements Listener, CommandExecutor{
	private xCoins plugin;
	
	public Pay(xCoins plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
		if(!(sender instanceof Player)) {
			FileConfiguration config = plugin.getConfig();
			
			if((config.getString("Config.General.cancel_console_execute_commands") == "true")) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot execute commands in the console."));
				return false;
			}
		}else {
	        if (args.length == 0) {
	        	Player player = (Player) sender;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /pay <player> <coins>"));
	        }else if(args.length == 1) {
	        	Player player = (Player) sender;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /pay <player> <coins>"));
	        }else if (args.length > 1) {
	        	
        		Pattern pattern = Pattern.compile("[a-zA-Z`~!@#$%^&*()_=+{}|;:,./<>?]");
        		Matcher m = pattern.matcher(args[1]);
        		if (m.find()) {
        			Player player = (Player) sender;
    				FileConfiguration config = plugin.getConfig();
        			String PayPermission = config.getString("Config.Commands.Pay.permission");
        			if(sender.hasPermission(PayPermission) || sender.hasPermission("xcoins.admin")) {
	    				String CharactersErrorMessage = config.getString("Messages.Coins.error_characters");
	    				String Prefix = config.getString("Config.General.prefix");
	        		    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+CharactersErrorMessage));
        			}else {
	    				String Prefix = config.getString("Config.General.prefix");
        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
        			}
        		}else {
        			FileConfiguration config = plugin.getConfig();
        			if(config.getString("Config.Commands.Pay.enabled") == "true") {
    	    			String PayPermission = config.getString("Config.Commands.Pay.permission");
    	    			if(sender.hasPermission(PayPermission) || sender.hasPermission("xcoins.admin")) {
            	        	Player player = (Player) sender;
                			Player target = Bukkit.getPlayer(args[0]);
                			FileConfiguration playerdata = plugin.getPlayerData();
                			int getCoins = Integer.valueOf(playerdata.getString("Players."+player.getUniqueId()+".coins"));
                			int getString = Integer.valueOf(args[1]);
                			int getCoinsTarget = Integer.valueOf(playerdata.getString("Players."+target.getUniqueId()+".coins"));
                			if((target == sender)) {
                				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHey, you cannot send coins to yourself."));
                			}else {
                    			if(getString < 1) {
                    				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHey, you need use a number greater than 1."));
                    			}else if(getString > 0){
                        			if(getCoins >= getString) {
                        				playerdata.set("Players."+player.getUniqueId()+".coins", (getCoins-getString));
                        				playerdata.set("Players."+target.getUniqueId()+".coins", (getCoinsTarget+getString));
                        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccesfully, You sent "+getString+" &acoins to the player "+target.getName()));
                        				target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCongratulations, you now have "+getString+" &acoins by the player "+player.getName()));
                        				
                        			}else {
                        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, you don't have enough coins."));
                    				}
                    			}
                			}
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
		}
		return false;
	}
}
