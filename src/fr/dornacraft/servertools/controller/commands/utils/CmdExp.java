package fr.dornacraft.servertools.controller.commands.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.model.managers.PlayerManager;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandException;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdExp extends DornacraftCommand {

	public static final String CMD_LABEL = "exp";

	public CmdExp() {
		super(CMD_LABEL);
		String cmdDesc = JavaPlugin.getPlugin(ServerTools.class).getCommand(CMD_LABEL).getDescription();
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String msg, String[] args) throws Exception {
				// - /exp [player]
				Player player = null;

				if (args.length == 1) {
					player = Bukkit.getPlayer(args[0]);
				} else if (sender instanceof Player) {
					player = (Player) sender;
				} else {
					throw new DornacraftCommandException(UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
				PlayerManager.displayExperience(sender, player);
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(new CommandNode(
				new CommandArgument(CommandArgumentType.ONLINE_PLAYER, false), cmdDesc, executor, null));
	}
}
