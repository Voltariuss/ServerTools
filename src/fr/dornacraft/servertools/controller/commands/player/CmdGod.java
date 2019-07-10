package fr.dornacraft.servertools.controller.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.managers.GodPlayerManager;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentChecker;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandException;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;
import fr.voltariuss.simpledevapi.cmds.InvalidArgumentsCommandException;

public class CmdGod extends DornacraftCommand {

	public static final String CMD_LABEL = "god";

	public CmdGod() {
		super(CMD_LABEL);
		String cmdDesc = ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_desc");

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				// - /god [player] [selector]
				if (args.length == 0) {
					if (sender instanceof Player) {
						GodPlayerManager.toggleGodMode(sender, (Player) sender);
					} else {
						throw new DornacraftCommandException(UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} else if (args.length == 1) {
					if (sender.hasPermission("dornacraft.essentials.god.other")) {
						GodPlayerManager.toggleGodMode(sender, Bukkit.getPlayer(args[0]));
					} else {
						throw new DornacraftCommandException(UtilsAPI.PERMISSION_MISSING);
					}
				} else if (args.length == 2) {
					if (sender.hasPermission("dornacraft.essentials.god.other")) {
						if (args[1].equalsIgnoreCase("on")) {
							GodPlayerManager.setGod(sender, Bukkit.getPlayer(args[0]), true);

						} else if (args[1].equalsIgnoreCase("off")) {
							GodPlayerManager.setGod(sender, Bukkit.getPlayer(args[0]), false);
						}
					}
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);

		CommandArgumentChecker checker = new CommandArgumentChecker() {

			@Override
			public void check(String str) throws InvalidArgumentsCommandException {
				if (!str.equalsIgnoreCase("on") && !str.equalsIgnoreCase("off")) {
					throw new InvalidArgumentsCommandException();
				}
			}
		};
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, false), cmdDesc, executor, null),
				new CommandNode(new CommandArgument(new CommandArgumentType("on/off", checker), false), cmdDesc,
						executor, "dornacraft.essentials.god.other"));
	}
}