package fr.dornacraft.servertools.controller.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.managers.TeleportManager;
import fr.dornacraft.servertools.model.utils.TypeRequest;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdTpahere extends DornacraftCommand {

	public static final String CMD_LABEL = "tpahere";
	private static final String DESC_ARG_PLAYER = "Téléporte le joueur cible à vous.";

	public CmdTpahere() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				if (sender instanceof Player) {
					if (!args[0].equalsIgnoreCase(sender.getName())) {
						Player receiver = Bukkit.getPlayer(args[0]);

						if (receiver != null) {
							TeleportManager.sendRequest((Player) sender, receiver,
									TypeRequest.RECEIVER_TELEPORT_TO_TRANSMITTER);
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

		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, true),
				DESC_ARG_PLAYER, executor, null));
	}
}
