package net.tiagofar78.cooldowncommander;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

public class CooldownCommander extends JavaPlugin {
	
	public static final String TPA_COOLDOWN_PERMISSION_START = "essentials.tpa.cooldown.";
	
	private static Essentials ess;
	
	@Override
	public void onEnable() {		
		getCommand("tpa").setExecutor(new TpaCommand());
		
		ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
		
		if (ess == null) {
			Bukkit.getPluginManager().disablePlugin(this);
			
			System.err.println("No essentials plugin was found! Disabling TF_CooldownCommander..");			
		}
	}
	
	public static Essentials getEssentials() {
		return ess;
	}
    
}
