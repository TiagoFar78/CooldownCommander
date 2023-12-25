package net.tiagofar78.cooldowncommander;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.config.entities.CommandCooldown;

import net.ess3.api.events.TPARequestEvent;

public class Events implements Listener {
	
	@EventHandler
	public void playerCommand(TPARequestEvent e) {
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
			return;
		}
		
		Essentials ess = CooldownCommander.getEssentials();
		
		String label = "tpa";
		
		Pattern pattern = Pattern.compile(label + "( .*)?");
		
		User user = ess.getUser(player);
		
		for (CommandCooldown ccc : user.getCooldownsList()) {
			if (ccc.pattern().pattern().startsWith(label)) {
				pattern = ccc.pattern();
			}
		}
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, tpaCooldownSeconds);
		Date expiringDate = calendar.getTime();
		
		Date currentExpiringDate = user.getCommandCooldownExpiry(label);		
		if (currentExpiringDate == null || currentExpiringDate.before(expiringDate)) {
			return;
		}
		
		user.clearCommandCooldown(pattern);
		user.addCommandCooldown(pattern, expiringDate, true);
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
