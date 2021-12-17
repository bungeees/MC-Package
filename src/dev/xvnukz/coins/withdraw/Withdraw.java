package dev.xvnukz.coins.withdraw;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.avaje.ebeaninternal.server.ldap.LdapOrmQueryEngine;

import dev.xvnukz.coins.xCoins;

public class Withdraw implements Listener, CommandExecutor{
	private xCoins plugin;
	
	public Withdraw(xCoins plugin) {
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
	        	Player player = (Player) sender;
	        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /withdraw <coins>"));
	        }else if(args.length == 1){
    			FileConfiguration playerdata = plugin.getPlayerData();
    			Player player = (Player) sender;
    			int getCoins = Integer.valueOf(playerdata.getString("Players."+player.getUniqueId()+".coins"));
    			int getString = Integer.valueOf(args[0]);
    			if(getString < 1) {
    				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHey, you need use a number greater than 1."));
    			}else if(getString > 0){
        			if(getCoins >= getString) {
        	    		if(sender.hasPermission("rosecoins.command.withdraw") || sender.hasPermission("rosecoins.admin")) {
        	    			
        	        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccesfully, withdraw 100 coins and now you have a paper."));
        	    		}else {
        	    			FileConfiguration config = plugin.getConfig();
        	    			String Prefix = config.getString("Config.General.prefix");
        	    			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefix+"&cSorry, You no have permissions to use this command."));
        	    		}
        				
        			}else {
    					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, you don't have enough coins."));
    				}
    			}
			}
		}
		return false;
	}
}
