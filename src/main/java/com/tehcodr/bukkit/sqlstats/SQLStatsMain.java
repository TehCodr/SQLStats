//SQLStatsMain.java
package com.tehcodr.bukkit.sqlstats;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.*;


public class SQLStatsMain extends JavaPlugin {
    private InfoLogger infoLogger = new InfoLogger();
    public SQLStatsConfig config = new SQLStatsConfig(this);
    
    public SQLStatsMain() {}
    
    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
    	System.out.println( "[" + pdfFile.getName() + "]" + " " + pdfFile.getVersion() + " Disabled" );
    }

    public void onEnable() {
    	infoLogger.init(this);
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( "[" + pdfFile.getName() + "]" + " " + pdfFile.getVersion() + " Enabled" );
        infoLogger.connect();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, infoLogger, 100, 1200);
    }
    // *stolen* from WorldGuard*
    public static void createDefaultConfiguration(File actual, String defaultName) {
    	
        File parent = actual.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (actual.exists()) {
            return;
        }
        InputStream input = SQLStatsMain.class.getResourceAsStream("/defaults/" + defaultName);
        
        if (input != null) {
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(actual);
                byte[] buf = new byte[8192];
                int length = 0;
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }
                
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                }
                catch (IOException e) {}

                try {
                    if (output != null) {
                        output.close();
                    }
                }
                catch (IOException e) {}
            }
        }
    }    
}