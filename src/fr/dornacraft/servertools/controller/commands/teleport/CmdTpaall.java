package fr.dornacraft.servertools.controller.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.managers.TeleportManager;
import fr.dornacraft.servertools.model.utils.TypeRequest;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdTpaall extends DornacraftCommand {

	public static final String CMD_LABEL = "tpaall";

	public CmdTpaall() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (sender instanceof Player) {
					for (Player player : Bukkit.getOnlinePlayers()) {
						if (!player.getName().equalsIgnoreCase(sender.getName())) {
							TeleportManager.sendRequest((Player) sender, player,
									TypeRequest.ALL_TELEPORT_TO_TRANSMITTER);
						}
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);
	}
}
