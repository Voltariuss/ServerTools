package fr.dornacraft.servertools.controller.commands.info;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.dornacraft.servertools.model.managers.PlayerManager;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdSeen extends DornacraftCommand {

	public static final String CMD_LABEL = "seen";

	public CmdSeen() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				OfflinePlayer target = PlayerManager.getOfflinePlayerFromName(args[0]);

				if (target != null) {
					PlayerManager.getInfoPlayer(sender, target);
				} else {
					PlayerManager.getInfoHostAddress(sender, args[0]);
				}
			}
		};
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(CommandArgumentType.STRING.getCustomArgType("player|hostAddress"), true),
				ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_desc"), executor, null));
	}
}
