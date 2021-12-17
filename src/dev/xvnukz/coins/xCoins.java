package dev.xvnukz.coins;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dev.xvnukz.coins.commands.Balance;
import dev.xvnukz.coins.commands.Commands;
import dev.xvnukz.coins.commands.Pay;
import dev.xvnukz.coins.commands.Top;
import dev.xvnukz.coins.withdraw.Item;
import dev.xvnukz.coins.withdraw.Withdraw;

public class xCoins extends JavaPlugin{

	public String rutaConfig;
	private FileConfiguration playerdata = null;
	FileConfiguration config = this.getConfig();
	private File playerdataFile = null;
	
	private Connection connection;
	public String host, database, username, password;
	public int port;
	
	//Eventos al activarse
	
	public void onEnable() {	
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&9xCoins &7| &fPlugin"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fStatus: &aEnabled"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------"));
		RegisterEvents();
		RegisterCommands();
		registerPlayerData();
		registerConfig();
		MySQLSetup();
	}
	
	
	//Eventos al desactivarse
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&9RoseCoins &7| &fPlugin"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fStatus: &cDisabled"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------"));
	}
	
	//MySQL
	
	public void MySQLSetup() {
		host = "localhost";
		port = 3306;
		database = "coinsdatabase";
		username = "root";
		password = "";
		
		try {
			synchronized (this) {
				if(getConnection() != null && !getConnection().isClosed()) {
					return;
				}
				
				Class.forName("com.mysql.jdbc.Driver");
				setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":"
				+ this.port + "/" + this.database, this.username, this.password));
				
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccesfully, The MySQL is now connected."));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	public Connection getConnection() {
		return connection;
	}


	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	

	//Registrar todos los eventos activos
	
	public void RegisterEvents() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new Join(this), this);
		pm.registerEvents(new Item(this), this);
	}
	
	//Registrar todos los comandos activos
	
	public void RegisterCommands() {
		this.getCommand("coins").setExecutor(new Commands(this));
		this.getCommand("pay").setExecutor(new Pay(this));
		this.getCommand("balance").setExecutor(new Balance(this));
		this.getCommand("top").setExecutor(new Top(this));
	}
	
	
	//Crear archivo de configuraci�n
	
	public void registerConfig(){
		File config = new File(this.getDataFolder(),"config.yml");
		rutaConfig = config.getPath();
		if(!config.exists()){
			this.getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}
	
	
	//Crear archivo de playerdata
	
	public FileConfiguration getPlayerData(){
		if(playerdata == null){
			reloadPlayerData();
		}
		return playerdata;
	}
 
	public void reloadPlayerData(){
		if(playerdata == null){
			playerdataFile = new File(getDataFolder(),"playerdata.yml");
		}
		playerdata = YamlConfiguration.loadConfiguration(playerdataFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("playerdata.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				playerdata.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
 
	public void savePlayerData(){
		try{
			playerdata.save(playerdataFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerPlayerData(){
		playerdataFile = new File(this.getDataFolder(),"playerdata.yml");
		if(!playerdataFile.exists()){
			this.getPlayerData().options().copyDefaults(true);
			savePlayerData();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
		if(!(sender instanceof Player)) {
			FileConfiguration config = this.getConfig();
			
			if((config.getString("Config.cancel-console-execute-commands") == "true")) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot execute commands in the console."));
				return false;
			}
		}else {
			if(args[0].equalsIgnoreCase("core")) {
	        	Player player = (Player) sender;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9xCoins &fAuthor by xVnukz and Luckash"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
			}
		}
		return false;
	}
}
