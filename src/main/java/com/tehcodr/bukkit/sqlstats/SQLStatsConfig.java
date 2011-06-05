package com.tehcodr.bukkit.sqlstats;

import java.io.*;
import java.util.List;
import org.bukkit.util.config.Configuration;

public class SQLStatsConfig {
	
	@SuppressWarnings("unused")
	private SQLStatsMain plugin;
	
	String userName;
	String password;
	String ipAddr;
	String dbName;
	String portNum;
	boolean worldBlacklistEnabled;
	boolean worldWhitelistEnabled;
	List<String> worldBlacklist;
	List<String> worldWhitelist;
	boolean playerWhitelistEnabled;
	boolean playerBlacklistEnabled;
	List<String> playerBlacklist;
	List<String> playerWhitelist;
	boolean enableLogging;
	String logTime;
	
	File configFile;
	File playerBlacklistFile;
	File playerWhitelistFile;
	File worldBlacklistFile;
	File worldWhitelistFile;
	
	
	public SQLStatsConfig(SQLStatsMain plugin) {
		this.plugin = plugin;
        configFile = new File(plugin.getDataFolder().toString() + "config.yml");
        playerBlacklistFile = new File(plugin.getDataFolder().toString() + "playerblacklist.txt");
        playerWhitelistFile = new File(plugin.getDataFolder().toString() + "playerwhitelist.txt");
        worldBlacklistFile = new File(plugin.getDataFolder().toString() + "worldblacklist.txt");
        worldWhitelistFile = new File(plugin.getDataFolder().toString() + "worldwhitelist.txt");
        SQLStatsMain.createDefaultConfiguration(configFile, "config.yml");
        SQLStatsMain.createDefaultConfiguration(playerBlacklistFile, "playerblacklist.txt");
        SQLStatsMain.createDefaultConfiguration(playerWhitelistFile, "playerwhitelist.txt");
        SQLStatsMain.createDefaultConfiguration(worldBlacklistFile, "worldblacklist.txt");
        SQLStatsMain.createDefaultConfiguration(worldWhitelistFile, "worldwhitelist.txt");
        loadConfig();
	}
	
	public void loadConfig() {
		Configuration config = new Configuration(this.configFile);
        config.load();
        dbName = config.getString("sql.databasename", "minecraftsqlstats");
        userName = config.getString("sql.username", "root");
        password = config.getString("sql.password", "");
        ipAddr = config.getString("sql.ipaddress", "localhost");
        portNum = config.getString("sql.port", "3306");
        
        worldBlacklistEnabled = config.getBoolean("listing.worlds.blacklist.enabled", false);
        worldWhitelistEnabled = config.getBoolean("listing.worlds.whitelist.enabled", false);
        if (worldWhitelistEnabled && worldBlacklistEnabled) {
        	System.out.println("[SQLStats] Error: World whitelist and blacklsts are both activated. Using blacklist instead.");
        	worldWhitelistEnabled = false;
        }
        playerBlacklistEnabled = config.getBoolean("listing.players.blacklist.enabled", false);
        playerWhitelistEnabled = config.getBoolean("listing.players.whitelist.enabled", false);
        if(playerWhitelistEnabled && playerBlacklistEnabled) {
        	System.out.println("[SQLStats] Error: Player whitelist and blacklsts are both activated. Using blacklist instead.");
        	playerWhitelistEnabled = false;
        }
        
        
        if (worldBlacklistEnabled) {
        	try {
        		FileInputStream fstream = new FileInputStream(worldBlacklistFile);
        		DataInputStream in = new DataInputStream(fstream);
        		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        		String strLine;
        		while ((strLine = br.readLine()) != null)   {
        			worldBlacklist.add(strLine);
        		}
        		in.close();
        	}
        	catch (Exception e){
        		System.out.println("[SQLStats] Error: Failed to load world Blacklist");
        		worldBlacklistEnabled = false;
        	}
        }
        if (worldWhitelistEnabled) {
        	try {
        		FileInputStream fstream = new FileInputStream(worldWhitelistFile);
        		DataInputStream in = new DataInputStream(fstream);
        		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        		String strLine;
        		while ((strLine = br.readLine()) != null)   {
        			worldWhitelist.add(strLine);
        		}
        		in.close();
        	}
        	catch (Exception e){
        		System.out.println("[SQLStats] Error: Failed to load world Whitelist");
        		worldWhitelistEnabled = false;
        	}
        }
        if (playerBlacklistEnabled) {
        	try {
        		FileInputStream fstream = new FileInputStream(playerBlacklistFile);
        		DataInputStream in = new DataInputStream(fstream);
        		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        		String strLine;
        		while ((strLine = br.readLine()) != null)   {
        			playerBlacklist.add(strLine);
        		}
        		in.close();
        	}
        	catch (Exception e){
        		System.out.println("[SQLStats] Error: Failed to load player Blacklist");
        		playerBlacklistEnabled = false;
        	}
        }
        if (playerWhitelistEnabled) {
        	try {
        		FileInputStream fstream = new FileInputStream(playerWhitelistFile);
        		DataInputStream in = new DataInputStream(fstream);
        		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        		String strLine;
        		while ((strLine = br.readLine()) != null)   {
        			playerWhitelist.add(strLine);
        		}
        		in.close();
        	}
        	catch (Exception e){
        		System.out.println("[SQLStats] Error: Failed to load player Whitelist");
        		playerWhitelistEnabled = false;
        	}
        }
        
        
        enableLogging = config.getBoolean("history.enable", false);
        logTime = config.getString("history.time-to-keep", "7");
	}
	
}
