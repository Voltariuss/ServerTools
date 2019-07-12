package fr.dornacraft.servertools.controller.commands.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.model.managers.PlayerManager;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandException;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdRepair extends DornacraftCommand {

	public static final String CMD_LABEL = "repair";

	public CmdRepair() {
		super(CMD_LABEL);
		String cmdDesc = JavaPlugin.getPlugin(ServerTools.class).getCommand(CMD_LABEL).getDescription();
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				// - /repair [hand|all]
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("hand")) {
						PlayerManager.repairItemInHand(player);
					} else if (args[0].equalsIgnoreCase("all")) {
						PlayerManager.repairAllItems(player);
					}
				} else {
					throw new DornacraftCommandException(UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument("hand"), cmdDesc, executor, null));
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument("all"), cmdDesc, executor, null));
	}
}
