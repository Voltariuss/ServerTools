package fr.dornacraft.servertools.model.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;

public class SpawnerCmdTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		ArrayList<String> result = new ArrayList<>();

		if (args.length == 1) {
			result = getPossibleArgs(args[0]);
		}
		return result;
	}

	private static ArrayList<String> getPossibleArgs(String start) {
		ArrayList<String> result = new ArrayList<>();
		for (EntityType ET : EntityType.values()) {
			if (ET.name().contains(start.toUpperCase()) && ET.isAlive() && ET != EntityType.PLAYER) {
				result.add(ET.name());
			}
		}
		return result;
	}

}
