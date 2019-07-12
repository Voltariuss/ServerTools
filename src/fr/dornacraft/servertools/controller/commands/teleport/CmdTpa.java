package fr.dornacraft.servertools.controller.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.model.managers.TeleportManager;
import fr.dornacraft.servertools.model.utils.TypeRequest;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandException;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdTpa extends DornacraftCommand {

	public static final String CMD_LABEL = "tpa";

	public CmdTpa() {
		super(CMD_LABEL);
		String cmdDesc = JavaPlugin.getPlugin(ServerTools.class).getCommand(CMD_LABEL).getDescription();
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (sender instanceof Player) {
					if (!args[0].equalsIgnoreCase(sender.getName())) {
						Player receiver = Bukkit.getPlayer(args[0]);

						if (receiver != null) {
							TeleportManager.sendRequest((Player) sender, receiver,
									TypeRequest.TRANSMITTER_TELEPORT_TO_RECEIVER);
						} else {
							throw new DornacraftCommandException(UtilsAPI.PLAYER_NOT_FOUND);
						}
					} else {
						throw new DornacraftCommandException(UtilsAPI.PLAYER_NOT_YOURSELF);
					}
				} else {
					throw new DornacraftCommandException(UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, true), cmdDesc, executor, null));
	}
}
