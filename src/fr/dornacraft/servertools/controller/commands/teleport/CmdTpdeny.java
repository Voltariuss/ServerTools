package fr.dornacraft.servertools.controller.commands.teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.managers.TeleportManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdTpdeny extends DornacraftCommand {

	public static final String CMD_LABEL = "tpdeny";

	public CmdTpdeny() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				if (sender instanceof Player) {
					TeleportManager.denyRequest((Player) sender);
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
	}
}
