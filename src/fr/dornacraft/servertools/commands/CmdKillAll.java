package fr.dornacraft.servertools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdKillAll extends DornacraftCommand {

	public static final String CMD_LABEL = "killall";

	public CmdKillAll() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (args.length == 1) {
						KillAllManager.killEntities(player, args[0]);
					} else {
						KillAllManager.killEntities(player, args[0], args[1]);
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};

		getCmdTreeExecutor().addSubCommand(
				new CommandNode(
						new CommandArgument(CommandArgumentType.STRING.getCustomArgType(KillAllManager.ENTITY_OPTIONS),
								true),
						ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_arg_entities_desc"), executor, null),
				new CommandNode(
						new CommandArgument(CommandArgumentType.STRING.getCustomArgType(KillAllManager.SCOPE_OPTIONS),
								false),
						ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_arg_entities_location_desc"), executor,
						null));
	}
}
