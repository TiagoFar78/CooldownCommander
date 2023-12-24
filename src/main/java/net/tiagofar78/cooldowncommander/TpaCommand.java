package net.tiagofar78.cooldowncommander;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

public class TpaCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			System.out.println("Exclusive command for players");
			return false;
		}
		
		Player player = (Player) sender;
		
		int tpaCooldownSeconds = -1;
		for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
			String permissionName = permission.getPermission();
			
			if (permissionName.startsWith(CooldownCommander.TPA_COOLDOWN_PERMISSION_START)) {
				tpaCooldownSeconds = stringToInt(permissionName.substring(CooldownCommander.TPA_COOLDOWN_PERMISSION_START.length()));
			}
		}
		
		if (tpaCooldownSeconds == -1) {
			return false;
		}
		
		Essentials ess = CooldownCommander.getEssentials();
		
		Pattern pattern = Pattern.compile(label);
		
		User user = ess.getUser(player);
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, tpaCooldownSeconds);
		Date expiringDate = calendar.getTime();
		
		if (user.getCommandCooldownExpiry(label).before(expiringDate)) {
			return false;
		}
		
		user.clearCommandCooldown(pattern);
		user.addCommandCooldown(pattern, expiringDate, true);
		
		return false;
	}

	private static int stringToInt(String s) {
		try {
			return Integer.parseInt(s);
		} 
		catch (Exception e) {
			return -1;
		}
	}

}
