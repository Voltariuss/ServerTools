package fr.dornacraft.servertools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdSuicide extends DornacraftCommand {

	public static final String CMD_LABEL = "suicide";

	public CmdSuicide() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				if (sender instanceof Player) {
					((Player) sender).setHealth(0);
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender,
							ServerToolsConfig.getCommandMessage(CMD_LABEL, "failure_message"));
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
	}
}
