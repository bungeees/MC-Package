package dev.xvnukz.coins.withdraw;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import dev.xvnukz.coins.xCoins;

public class Item implements Listener{
	private RoseCoins plugin;
	
	public Item(xCoins plugin) {
		this.plugin = plugin;
	}

	//@EventHandler
	//public void onPlayerInteractWithThePaper(PlayerInteractEvent event) {
		//Player player = event.getPlayer();
		//FileConfiguration config = plugin.getConfig();
		//String ItemName = config.getString("Config.Withdraw.name");
		
		//if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			//if(player.getItemInHand().getItemMeta().getDisplayName().equals(config.getString(ItemName))) {
				//player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccesfully, you reclaim this paper and now you have 100 coins."));
			//}
		//}
	//}
}
