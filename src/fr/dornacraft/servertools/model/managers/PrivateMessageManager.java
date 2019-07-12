package fr.dornacraft.servertools.model.managers;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.controller.commands.utils.CmdMsg;
import fr.dornacraft.servertools.controller.commands.utils.CmdReply;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class PrivateMessageManager {

	private static final HashMap<CommandSender, CommandSender> senders = new HashMap<>();

	private static HashMap<CommandSender, CommandSender> getSenders() {
		return senders;
	}

	public static CommandSender getSender(CommandSender receiver) {
		return getSenders().get(receiver);
	}

	public static void addReceiver(CommandSender sender, CommandSender receiver) {
		getSenders().put(receiver, sender);
	}

	public static void removeReceiver(CommandSender receiver) {
		getSenders().remove(receiver);
	}

	public static boolean hasSender(CommandSender receiver) {
		return getSenders().containsKey(receiver);
	}

	public static void sendPrivateMessage(CommandSender sender, CommandSender receiver, String message) {
		if (receiver instanceof ConsoleCommandSender || ((Player) receiver).isOnline()) {
			if (!sender.equals(receiver)) {
				HashMap<String, String> values = new HashMap<>();
				String consoleName = ServerToolsConfig.getCommandMessage(CmdMsg.CMD_LABEL, "other_console_name");
				values.put("Sender", sender instanceof ConsoleCommandSender ? consoleName : sender.getName());
				values.put("Receiver", receiver.getName());
				values.put("Message", message);

				String messageSend = ServerToolsConfig.getCommandMessage(CmdMsg.CMD_LABEL, "normal_msg_send", values);
				UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, messageSend);
				String messageReceive = ServerToolsConfig.getCommandMessage(CmdMsg.CMD_LABEL, "normal_msg_receive",
						values);
				UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, receiver, messageReceive);

				if (receiver instanceof Player) {
					Player playerReceiver = (Player) receiver;
					playerReceiver.playSound(playerReceiver.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 2);
				}
				addReceiver(sender, receiver);
				addReceiver(receiver, sender);
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_NOT_YOURSELF);
			}
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_OFFLINE);

			if (hasSender(sender)) {
				removeReceiver(sender);
			}
		}
	}

	public static void reply(CommandSender receiver, String message) {
		if (hasSender(receiver)) {
			sendPrivateMessage(receiver, getSender(receiver), message);
		} else {
			String messageFailure = ServerToolsConfig.getCommandMessage(CmdReply.CMD_LABEL, "failure_no_receiver");
			UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, receiver, messageFailure);
		}
	}
}
