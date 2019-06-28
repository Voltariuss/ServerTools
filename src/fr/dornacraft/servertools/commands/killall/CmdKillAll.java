package fr.dornacraft.servertools.commands.killall;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.utils.ServerToolsConfig;
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
					String entityType = args[0].toLowerCase();

					try {
						KillAllManager.checkEntityTypeArgValidity(entityType);
						int radius = -1;
						String worldName = "";

						if (args.length >= 2) {
							String worldArg = args[1];
							KillAllManager.checkWorldArgValidity(worldArg);
							worldName = KillAllManager.getWorldName(player, worldArg);
							
							if (args.length == 3) {
								radius = Integer.parseInt(args[2]);
							}
						} else {
							worldName = KillAllManager.getWorldName(player, worldName);
						}
						KillAllManager.killEntities(player, entityType, radius, worldName);
					} catch (KillAllException e) {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, e.getMessage());
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
						new CommandArgument(
								CommandArgumentType.STRING.getCustomArgType(KillAllManager.WORLD_NAME_OPTIONS), false),
						ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_arg_target_world_desc"), executor,
						null),
				new CommandNode(
						new CommandArgument(CommandArgumentType.NUMBER.getCustomArgType(KillAllManager.RADIUS_OPTIONS),
								false),
						ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_arg_radius_desc"), executor,
						null));
	}
}
