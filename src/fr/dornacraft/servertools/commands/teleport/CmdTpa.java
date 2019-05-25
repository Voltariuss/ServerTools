package fr.dornacraft.servertools.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdTpa extends DornacraftCommand {

	public static final String CMD_LABEL = "tpa";
	private static final String DESC_ARG_PLAYER = "Vous téléporte au joueur ciblé.";

	public CmdTpa() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (sender instanceof Player) {
					if (!args[0].equalsIgnoreCase(sender.getName())) {
						Player receiver = Bukkit.getPlayer(args[0]);

						if (receiver != null) {
							TeleportManager.sendRequest((Player) sender, receiver,
									TypeRequest.TRANSMITTER_TELEPORT_TO_RECEIVER);
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_UNKNOW);
						}
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_NOT_YOURSELF);
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};

		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, true),
				DESC_ARG_PLAYER, executor, null));
	}
}
