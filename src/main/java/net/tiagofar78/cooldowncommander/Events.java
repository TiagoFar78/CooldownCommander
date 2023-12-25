package net.tiagofar78.cooldowncommander;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import net.ess3.api.events.TPARequestEvent;

public class Events implements Listener {
	
	public void playerCommand(TPARequestEvent e) {
		System.out.println("Event fireou banger");
		
		Essentials ess = CooldownCommander.getEssentials();
		
		String label = "tpa";
		
		Player player = e.getRequester().getPlayer();
		
		int tpaCooldownSeconds = -1;
		for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
			String permissionName = permission.getPermission();
			
			if (permissionName.startsWith(CooldownCommander.TPA_COOLDOWN_PERMISSION_START)) {
				tpaCooldownSeconds = stringToInt(permissionName.substring(CooldownCommander.TPA_COOLDOWN_PERMISSION_START.length()));
				break;
			}
		}
		
		if (tpaCooldownSeconds == -1) {
			System.out.println("segundos -1");
			return;
		}
		
		Pattern pattern = Pattern.compile(label);
		
		User user = ess.getUser(player);
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, tpaCooldownSeconds);
		Date expiringDate = calendar.getTime();
		
		if (user.getCommandCooldownExpiry(label).before(expiringDate)) {
			System.out.println("Cooldown antigo tava mais pequeno");
			return;
		}
		
		user.clearCommandCooldown(pattern);
		user.addCommandCooldown(pattern, expiringDate, true);
		
		System.out.println("Meteu uma menor");
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
