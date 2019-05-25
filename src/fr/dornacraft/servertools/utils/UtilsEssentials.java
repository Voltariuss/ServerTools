package fr.dornacraft.servertools.utils;

import org.bukkit.Location;

import fr.voltariuss.simpledevapi.UtilsAPI;

public class UtilsEssentials {

	public static Location SPAWN_LOCATION;

	public static String getHeader(String title) {
		return "§6+§e------------ §a" + title + " §e------------§6+";
	}

	public static String getNewLine(String name, String string) {
		return String.format(" §6%s §8%s §e%s", name, UtilsAPI.CARAC_DOUBLE_QUOTE_END, string);
	}

	public static String getStrPosition(Location location) {
		return "§eX = §b" + location.getBlockX() + " §7/ §eY = §b" + location.getBlockY() + " §7/ §eZ = §b"
				+ location.getBlockZ();
	}

	public static String displayState(boolean bool) {
		return bool == true ? "§aOn" : "§cOff";
	}

	public static String convertTime(long milliseconds) {
		int seconds = (int) (milliseconds / 1000);

		int minutes = 0;
		while (seconds >= 60) {
			seconds -= 60;
			minutes++;
		}
		int hours = 0;
		while (minutes >= 60) {
			minutes -= 60;
			hours++;
		}
		int days = 0;
		while (hours >= 24) {
			hours -= 24;
			days++;
		}

		StringBuilder sb = new StringBuilder();
		if (days != 0)
			sb.append(days + " jour(s)");
		if (hours != 0 && days < 1)
			sb.append(days != 0 ? ", " : "" + hours + " heure(s)");
		if (minutes != 0 && days < 1 && hours < 1)
			sb.append((days != 0 || hours != 0) ? ", " : "" + minutes + " minute(s)");
		if (days < 1 && hours < 1 && minutes < 5)
			sb.append((days != 0 || hours != 0 || minutes != 0) ? ", " : "" + seconds + " seconde(s)");

		return sb.toString();
	}

	public static final String TELEPORT_SPAWN = "Téléportation au §cspawn§r.";
}
