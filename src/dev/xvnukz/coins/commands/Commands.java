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

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.Packet;
import dev.xvnukz.coins.xCoins;

public class Commands implements Listener, CommandExecutor{
	private xCoins plugin;
	
	public Commands(xCoins plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("unused")
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
				FileConfiguration playerdata = plugin.getPlayerData();
				
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9/coins check <player> &7- &fCheck the coins of a player."));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9/coins add <player> <coins> &7- &fAdd coins to a player."));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9/coins remove <player> <coins> &7- &fRemove coins to a player."));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9/coins set <player> <coins> &7- &fSet coins to a player."));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9/coins reset <player> &7- &fReset coins to a player."));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9/coins reload &7- &fReloaded config file."));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
				
	        }else if(args.length == 1) {
	        	Player player = (Player) sender;
	        	if(args[0].equalsIgnoreCase("set")) {
	        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /coins set <player> <coins>"));
	        	}else if(args[0].equalsIgnoreCase("reset")) {
	        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /coins reset <player>"));
	        	}else if(args[0].equalsIgnoreCase("check")) {
	        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /coins check <player>"));
	        	}else if(args[0].equalsIgnoreCase("reload")) {
        			if(sender.hasPermission("rosecoins.command.reload") || sender.hasPermission("rosecoins.admin")) {
        				plugin.reloadConfig();
	        			
	    				FileConfiguration config = plugin.getConfig();
	    				String ReloadFile = config.getString("Messages.File.file_reloaded");
	    				String Prefix = config.getString("Config.General.prefix");
	    				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+ReloadFile));
	        			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+ReloadFile));
        			}else {
        				FileConfiguration config = plugin.getConfig();
	    				String Prefix = config.getString("Config.General.prefix");
        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
        			}
	        	}else if(args[0].equalsIgnoreCase("add")) {
	        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /coins add <player> <coins>"));
	        	}else if(args[0].equalsIgnoreCase("remove")) {
	        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /coins remove <player> <coins>"));
	        	}
	        }else if(args.length > 1) {
				Player player = (Player) sender;
				FileConfiguration config = plugin.getConfig();
				if(args[0].equalsIgnoreCase("set") && args.length > 1) {
	        		String SetPermission = config.getString("Config.Commands.Set.permission");
					if(config.getString("Config.Commands.Set.enabled") == "true") {
						if((args[1].equalsIgnoreCase(null))) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /coins set <player> <coins>"));
						}else {
							Player target = Bukkit.getPlayer(args[1]);
			        		FileConfiguration playerdata = plugin.getPlayerData();
			        		
			        		Pattern pattern = Pattern.compile("[a-zA-Z`~!@#$%^&*()_=+{}|;:,./<>?]");
			        		Matcher m = pattern.matcher(args[2]);
			        		if (m.find()) {
			        			if(sender.hasPermission(SetPermission) && sender.hasPermission("rosecoins.admin")) {
				    				String CharactersErrorMessage = config.getString("Messages.Coins.error_characters");
				    				String Prefix = config.getString("Config.General.prefix");
				        		    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+CharactersErrorMessage));
			        			}else {
				    				String Prefix = config.getString("Config.General.prefix");
			        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
			        			}
			        		}else {
			        			if(sender.hasPermission(SetPermission) || sender.hasPermission("xcoins.admin")) {
			        				playerdata.set("Players."+target.getUniqueId()+".coins", args[2]);
				        			
				    				String SetedCoins = config.getString("Messages.Coins.coins_seted");
				    				String Prefix = config.getString("Config.General.prefix");
				    				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', SetedCoins)
						        			.replaceAll("%coins%", args[2] + "")
						        			.replaceAll("%player%", target.getName() + ""));
				        			player.sendMessage(ChatColor.translateAlternateColorCodes('&', SetedCoins)
				        			.replaceAll("%coins%", args[2] + "")
				        			.replaceAll("%player%", target.getName() + ""));
					        		plugin.savePlayerData();
			        			}else {
				    				String Prefix = config.getString("Config.General.prefix");
			        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
			        			}
			        		}
						}
					}else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, this command is disabled for a admin!"));
					}
					
	        	}else if(args[0].equalsIgnoreCase("check") && args.length > 1) {
	        		String CheckPermission = config.getString("Config.Commands.Check.permission");
					if(config.getString("Config.Commands.Check.enabled") == "true") {
		        		if(sender.hasPermission(CheckPermission) || sender.hasPermission("xcoins.admin")) {
		        			Player target = Bukkit.getPlayer(args[1]);
			        		FileConfiguration playerdata = plugin.getPlayerData();
				        	int getTargetCoins = Integer.valueOf(playerdata.getString("Players."+target.getUniqueId()+".coins"));
		    				String CheckCoins = config.getString("Messages.Coins.coins_check");
		    				String Prefix = config.getString("Config.General.prefix");
		    				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+CheckCoins).replaceAll("%coins%", getTargetCoins + ""));
		        			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+CheckCoins).replaceAll("%coins%", getTargetCoins + ""));
		        		}else {
		    				String Prefix = config.getString("Config.General.prefix");
	        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
	        			}
					}else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, this command is disabled for a admin!"));
					}
	        	}else if(args[0].equalsIgnoreCase("reset") && args.length > 1) {
	        		String ResetPermission = config.getString("Config.Commands.Reset.permission");
					if(config.getString("Config.Commands.Reset.enabled") == "true") {
		        		if(sender.hasPermission(ResetPermission) || sender.hasPermission("rosecoins.admin")) {
		        			Player target = Bukkit.getPlayer(args[1]);
		        			FileConfiguration playerdata = plugin.getPlayerData();
		    				String ConfigCoins = config.getString("Config.General.default_coins");
		        		
		        			playerdata.set("Players."+target.getUniqueId()+".coins", ConfigCoins);
		        			
		        			
		    				String ResetedCoins = config.getString("Messages.Coins.coins_reseted");
		    				String Prefix = config.getString("Config.General.prefix");
		    				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', ResetedCoins).replaceAll("%player%", target.getName() + ""));
		        			player.sendMessage(ChatColor.translateAlternateColorCodes('&', ResetedCoins).replaceAll("%player%", target.getName() + ""));
		        			plugin.savePlayerData();
		        		}else {
		    				String Prefix = config.getString("Config.General.prefix");
	        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
	        			}
					}else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, this command is disabled for a admin!"));
					}
	        	}else if(args[0].equalsIgnoreCase("add") && args.length > 1) {
	        		
	        		Pattern pattern = Pattern.compile("[a-zA-Z`~!@#$%^&*()_=+{}|;:,./<>?]");
	        		Matcher m = pattern.matcher(args[2]);
	        		String AddPermission = config.getString("Config.Commands.Add.permission");
	        		if (m.find()) {
	        			if(sender.hasPermission(AddPermission) && sender.hasPermission("xcoins.admin")) {
		    				String CharactersErrorMessage = config.getString("Messages.Coins.error_characters");
		    				String Prefix = config.getString("Config.General.prefix");
		        		    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+CharactersErrorMessage));
	        			}else {
		    				String Prefix = config.getString("Config.General.prefix");
	        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
	        			}
	        		}else {
						if(config.getString("Config.Commands.Add.enabled") == "true") {
		        			if(sender.hasPermission(AddPermission) || sender.hasPermission("rosecoins.admin")) {
			        			Player target = Bukkit.getPlayer(args[1]);
			        			FileConfiguration playerdata = plugin.getPlayerData();
			        			int getCoins = Integer.valueOf(playerdata.getString("Players."+player.getUniqueId()+".coins"));
			        			int getString = Integer.valueOf(args[2]);
			        			int getCoinsTarget = Integer.valueOf(playerdata.getString("Players."+target.getUniqueId()+".coins"));
			        			
		        				playerdata.set("Players."+target.getUniqueId()+".coins", (getCoinsTarget+getString));
		        				
			    				String AddedCoins = config.getString("Messages.Coins.coins_added");
			    				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', AddedCoins));
			        			player.sendMessage(ChatColor.translateAlternateColorCodes('&', AddedCoins)
			        			.replaceAll("%coins%", getString + "")
			        			.replaceAll("%player%", target.getName() + ""));
				        		plugin.savePlayerData();
		        			}else {
			    				String Prefix = config.getString("Config.General.prefix");
		        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
		        			}
						}else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, this command is disabled for a admin!"));
						}
	        		}

	        	}else if(args[0].equalsIgnoreCase("remove") && args.length > 1) {
	        		
	        		Pattern pattern = Pattern.compile("[a-zA-Z`~!@#$%^&*()_=+{}|;:,./<>?]");
	        		Matcher m = pattern.matcher(args[2]);
	        		String RemovePermission = config.getString("Config.Commands.Remove.permission");
	        		if (m.find()) {
	        			if(sender.hasPermission(RemovePermission) && sender.hasPermission("rosecoins.admin")) {
		    				String CharactersErrorMessage = config.getString("Messages.Coins.error_characters");
		    				String Prefix = config.getString("Config.General.prefix");
		        		    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+CharactersErrorMessage));
	        			}else {
		    				String Prefix = config.getString("Config.General.prefix");
	        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
	        			}
	        		}else {
						if(config.getString("Config.Commands.Remove.enabled") == "true") {
		        			if(sender.hasPermission(RemovePermission) || sender.hasPermission("xcoins.admin")) {
			        			Player target = Bukkit.getPlayer(args[1]);
			        			FileConfiguration playerdata = plugin.getPlayerData();
			        			int getCoins = Integer.valueOf(playerdata.getString("Players."+player.getUniqueId()+".coins"));
			        			int getString = Integer.valueOf(args[2]);
			        			int getCoinsTarget = Integer.valueOf(playerdata.getString("Players."+target.getUniqueId()+".coins"));
			        			
		        				playerdata.set("Players."+target.getUniqueId()+".coins", (getCoinsTarget-getString));
		        				
			        			
			    				String RemovedCoins = config.getString("Messages.Coins.coins_added");
			    				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', RemovedCoins));
			        			player.sendMessage(ChatColor.translateAlternateColorCodes('&', RemovedCoins)
			        			.replaceAll("%coins%", getString + "")
			        			.replaceAll("%player%", target.getName() + ""));
				        		plugin.savePlayerData();
		        			}else {
			    				String Prefix = config.getString("Config.General.prefix");
		        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
		        			}
						}else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, this command is disabled for a admin!"));
						}
	        		}
	        	}
	        }
		}
		
		return false;
	}
	
	
}
