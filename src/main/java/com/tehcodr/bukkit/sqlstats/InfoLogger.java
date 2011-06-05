//InfoLogger.java
package com.tehcodr.bukkit.sqlstats;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import java.util.List;
import java.sql.*;

public class InfoLogger implements Runnable {
	private SQLStatsMain plugin = new SQLStatsMain();
	
	Server server;
	double playerHealth;
	String playerName;
	Location playerLocation;
	double playerX, playerZ, playerY; //y is up/down, so it's listed last
	World playerWorld;
	String playerWorldName;
	List<World> worldsList;
	Player connectedPlayers[];
	World worlds[];
	String connectionURL;
	Connection con;
	
	Statement statement;
	ResultSet rs;
	
	public void init(SQLStatsMain plugin) {
		this.plugin = plugin;
	}
	
	public void connect() {
		connectionURL = "jdbc:mysql://" + plugin.config.ipAddr + ":" + plugin.config.portNum + "/" + plugin.config.dbName;
		try {
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            con = DriverManager.getConnection (connectionURL, plugin.config.userName, plugin.config.password);
            statement = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            System.out.println ("[SQLStats] Database connection established");
        }
        catch (Exception e) {
            System.out.println ("[SQLStats] Cannot connect to database server:");
            e.printStackTrace();
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                    System.out.println ("[SQLStats] Database connection terminated");
                }
                catch (SQLException e) {
                	System.out.println("[SQLStats] Unable to terminate connection:");
                	e.printStackTrace();
                }
            }
        }
	}
	
	public void run() {
		connect();
		String playerWhitelist[] = plugin.config.playerWhitelist.toArray(new String[0]);
		String playerBlacklist[] = plugin.config.playerBlacklist.toArray(new String[0]);
		String worldWhitelist[] = plugin.config.worldWhitelist.toArray(new String[0]);
		String worldBlacklist[] = plugin.config.worldBlacklist.toArray(new String[0]);
		connectedPlayers = server.getOnlinePlayers();
		worldsList = server.getWorlds();
		worldsList.toArray(worlds);
		boolean onPlayerWhitelist = false;
		boolean onPlayerBlacklist = false;
		boolean onWorldWhitelist = false;
		boolean onWorldBlacklist = false;
		
		try {
			statement.executeUpdate("DROP TABLE *");
		}
		catch(SQLException e) {
			System.out.println("[SQLStats] Unable to remove data from database:");
			e.printStackTrace();
		}
		for(int i = 0; i < connectedPlayers.length; i++) {
			playerName = connectedPlayers[i].getName();
			playerWorld = connectedPlayers[i].getWorld();
			playerWorldName = playerWorld.getName();
			playerLocation = connectedPlayers[i].getLocation();
			playerHealth = connectedPlayers[i].getHealth()/2;
			playerX = playerLocation.getX();
			playerZ = playerLocation.getZ();
			playerY = playerLocation.getY();
			
			for(int n = 0; n <= worldWhitelist.length; n+= 1) {
				if(worldWhitelist[n] == playerWorldName) {
					onWorldWhitelist = true;
					continue;
				}
			}
			
			for(int n = 0; n <= worldBlacklist.length; n+= 1) {
				if(worldBlacklist[n] == playerWorldName) {
					onWorldBlacklist = true;
					continue;
				}
			}
			
			for(int n = 0; n <= playerWhitelist.length; n+= 1) {
				if(playerWhitelist[n] == playerName) {
					onPlayerWhitelist = true;
					continue;
				}
			}
			
			for(int n = 0; n <= playerBlacklist.length; n+= 1) {
				if(playerBlacklist[n] == playerName) {
					onPlayerBlacklist = true;
					continue;
				}
			}
			
			if ((plugin.config.playerWhitelistEnabled && onPlayerWhitelist)
					|| (plugin.config.playerBlacklistEnabled && !onPlayerBlacklist)
					|| (plugin.config.worldWhitelistEnabled && onWorldWhitelist)
					|| (plugin.config.worldBlacklistEnabled && !onWorldBlacklist)
					|| (!plugin.config.playerWhitelistEnabled && !plugin.config.worldWhitelistEnabled)
					|| (!plugin.config.playerBlacklistEnabled && !plugin.config.playerBlacklistEnabled)) {
				try {
					statement.execute("CREATE TABLE IF NOT EXISTS " + playerName);
				}
				catch(SQLException e) {
					System.out.println("[SQLStats] Unable to create database for " + playerName + ":");
					e.printStackTrace();
				}
			}
		}
	}
}