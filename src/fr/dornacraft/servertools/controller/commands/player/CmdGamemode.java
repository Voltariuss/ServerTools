package fr.dornacraft.servertools.controller.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.managers.PlayerManager;
import fr.dornacraft.servertools.model.utils.GameModeType;
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

public class CmdGamemode extends DornacraftCommand {

	public static final String CMD_LABEL = "gamemode";

	public CmdGamemode() {
		super(CMD_LABEL);
		String cmdDesc = ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_desc");
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				// - /gamemode <mode> [player]
				GameModeType gameModeType = null;

				try {
					int index = Integer.parseInt(args[0]);
					gameModeType = GameModeType.getFromIndex(index);
				} catch (NumberFormatException e) {
					String gameModeName = args[0];
					gameModeType = GameModeType.valueOf(gameModeName.toUpperCase());
				}
				GameMode gameMode = GameMode.valueOf(gameModeType.name());
				Player player = null;

				if (args.length == 1) {
					if (sender instanceof Player) {
						player = (Player) sender;
						PlayerManager.setGameMode(sender, player, gameMode);
					} else {
						throw new DornacraftCommandException(UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} else if (args.length == 2) {
					player = Bukkit.getPlayer(args[1]);
					PlayerManager.setGameMode(sender, player, gameMode);
				}
			}
		};

		CommandArgumentChecker checker = new CommandArgumentChecker() {

			@Override
			public void check(String str) throws InvalidArgumentsCommandException {
				try {
					int index = Integer.parseInt(str);
					GameModeType gameModeType = GameModeType.getFromIndex(index);

					if (gameModeType == null) {
						throw new InvalidArgumentsCommandException();
					}
				} catch (NumberFormatException e1) {
					try {
						GameModeType.valueOf(str.toUpperCase());
					} catch (IllegalArgumentException e2) {
						throw new InvalidArgumentsCommandException();
					}
				}
			}
		};
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(new CommandArgumentType("mode", checker), true), cmdDesc, executor,
						null),
				new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, false), cmdDesc, executor,
						"dornacraft.essentials.gamemode.other"));
	}
}
