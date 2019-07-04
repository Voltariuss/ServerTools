package fr.dornacraft.servertools.controller.commands.info;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.managers.PlayerManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdWhois extends DornacraftCommand {

	public static final String CMD_LABEL = "whois";
	private static final String CMD_DESC = null;

	public CmdWhois() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				// - /whois <nickname|playername>
				if (args.length == 0
						|| sender instanceof Player && args[0].equalsIgnoreCase(((Player) sender).getDisplayName())) {
					if (sender instanceof Player) {
						PlayerManager.displayGlobalPlayerInformations(sender, (Player) sender);
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_OFFLINE);
					}
				} else {
					Player target = Bukkit.getPlayer(args[0]);

					if (target != null) {
						PlayerManager.displayGlobalPlayerInformations(sender, target);
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_UNKNOW);
					}
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.STRING.getCustomArgType("player"), false), CMD_DESC, executor, null));
	}
}