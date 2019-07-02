package fr.dornacraft.servertools.controller.commands.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.managers.HomeManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdSethome extends DornacraftCommand {

	public static final String CMD_LABEL = "sethome";
	private static final String DESC_ARG_NAME = null;

	public CmdSethome() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (args.length == 0) {
						HomeManager.setHome(player, "home");
					} else {
						HomeManager.setHome(player, args[0]);
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(new CommandNode(
				new CommandArgument(CommandArgumentType.STRING.getCustomArgType("nom_de_la_résidence"), false),
				DESC_ARG_NAME, executor, null));

	}

}
