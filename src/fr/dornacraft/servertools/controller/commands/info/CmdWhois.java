package fr.dornacraft.servertools.controller.commands.info;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.controller.listeners.GodPlayerDamageListener;
import fr.dornacraft.servertools.utils.Utils;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdWhois extends DornacraftCommand {

	public static final String CMD_LABEL = "whois";
	private static final String CMD_DESC = null;

	public CmdWhois() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				// - /whois <nickname|playername>
				if (args.length == 0
						|| sender instanceof Player && args[0].equalsIgnoreCase(((Player) sender).getDisplayName())) {
					if (sender instanceof Player) {
						displayWhoisPlayer(sender, (Player) sender);
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_OFFLINE);
					}
				} else {
					Player target = Bukkit.getPlayer(args[0]);

					if (target != null) {
						displayWhoisPlayer(sender, target);
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_UNKNOW);
					}
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, false), CMD_DESC, executor, null));
	}

	public static void displayWhoisPlayer(CommandSender sender, Player target) {
		sender.sendMessage(Utils.getHeader("Informations du joueur"));
		sender.sendMessage(Utils.getNewLine("Pseudo", target.getDisplayName()));
		sender.sendMessage(
				Utils.getNewLine("Vie", (int) target.getHealth() + "§7/§e" + (int) target.getHealthScale()));
		sender.sendMessage(Utils.getNewLine("Faim", target.getFoodLevel() + "§7/§e20"));
		sender.sendMessage(Utils.getNewLine("Niveau", Integer.toString(target.getLevel())));
		sender.sendMessage(Utils.getNewLine("Monde", target.getWorld().getName()));
		sender.sendMessage(
				Utils.getNewLine("Position", Utils.getStrPosition(target.getLocation())));
		sender.sendMessage(Utils.getNewLine("Adresse IP", target.getAddress().getAddress().getHostAddress()));
		sender.sendMessage(Utils.getNewLine("Gamemode", target.getGameMode().name()));
		sender.sendMessage(Utils.getNewLine("Godmode",
				Utils.displayState(GodPlayerDamageListener.isGod(target))));
		sender.sendMessage(
				Utils.getNewLine("Flymode", Utils.displayState(target.getAllowFlight())));
		sender.sendMessage(Utils.getNewLine("Op", Utils.displayState(target.isOp())));
	}
}