package fr.dornacraft.servertools.utils;

import java.util.HashMap;

import org.apache.commons.lang.text.StrSubstitutor;

import fr.dornacraft.servertools.ServerTools;

public class ServerToolsConfig {

	public static String getString(String path) {
		return ServerTools.getInstance().getConfig().getString(path);
	}
	
	public static String getCommandMessage(String cmdLabel, String subPath) {
		return getString("messages.commands." + cmdLabel + '.' + subPath);
	}
	
	public static String getCommandMessage(String cmdLabel, String subPath, HashMap<String, String> values) {
		String configMessage = getCommandMessage(cmdLabel, subPath);
		return StrSubstitutor.replace(configMessage, values, "{", "}");
	}
}
