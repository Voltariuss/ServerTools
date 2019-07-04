package fr.dornacraft.servertools.model.managers;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
				sender.sendMessage(String.format("§8>> Vous chuchotez à §7%s §8%s §7%s", receiver.getName(),
						UtilsAPI.CARAC_DOUBLE_QUOTE_END, message));
				receiver.sendMessage(String.format("§8>> §7%s §8vous chuchote %s §7%s",
						sender instanceof ConsoleCommandSender ? "§c@Console" : sender.getName(),
						UtilsAPI.CARAC_DOUBLE_QUOTE_END, message));

				if (receiver instanceof Player) {
					((Player) receiver).playSound(((Player) receiver).getLocation(), Sound.BLOCK_NOTE_PLING, 1, 2);
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
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, receiver, "Vous n'avez personne à qui répondre.");
		}
	}
}
