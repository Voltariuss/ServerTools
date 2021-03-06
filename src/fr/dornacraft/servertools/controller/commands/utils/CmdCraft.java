package fr.dornacraft.servertools.controller.commands.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdCraft extends DornacraftCommand {

	public static final String CMD_LABEL = "craft";

	public CmdCraft() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (sender instanceof Player) {
					((Player) sender).openWorkbench(null, true);
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
							ServerToolsConfig.getCommandMessage(CMD_LABEL, "info_open_workbench"));
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
	}
}
