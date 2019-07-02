package fr.dornacraft.servertools.controller.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.managers.TeleportManager;
import fr.dornacraft.servertools.utils.Utils;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public final class CmdSpawn extends DornacraftCommand {

	public static final String CMD_LABEL = "spawn";

	private static final String DESC_ARG_PLAYER = "Le joueur à téléporter au spawn.";

	public CmdSpawn() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (args.length == 0 || sender instanceof Player && args[0].equalsIgnoreCase(sender.getName())) {
					if (sender instanceof Player) {
						TeleportManager.teleportPlayerTo(null, (Player) sender, Utils.SPAWN_LOCATION,
								Utils.TELEPORT_SPAWN);
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} else {
					Player player = Bukkit.getPlayer(args[0]);

					if (player != null) {
						TeleportManager.teleportPlayerTo(sender, player, Utils.SPAWN_LOCATION,
								Utils.TELEPORT_SPAWN);
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_UNKNOW);
					}
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, false),
				DESC_ARG_PLAYER, executor, TeleportManager.PERMISSION_TELEPORT_OTHERS));

	}
}
