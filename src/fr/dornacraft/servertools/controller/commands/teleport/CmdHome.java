package fr.dornacraft.servertools.controller.commands.teleport;

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

public class CmdHome extends DornacraftCommand {

	public static final String CMD_LABEL = "home";
	private static final String DESC_ARG_NAME = "Le nom de la résidence.";

	public CmdHome() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (sender instanceof Player) { // Commande exécutable par un joueur uniquement
					Player player = (Player) sender;

					if (args.length == 0) { // On affiche la liste des homes du joueur
						HomeManager.sendHomeList(player);
					} else if (args.length == 1) { // On téléporte le joueur vers le home spécifié
						HomeManager.teleportToHome(player, args[0]);
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);

		getCmdTreeExecutor().addSubCommand(new CommandNode(
				new CommandArgument(CommandArgumentType.STRING.getCustomArgType("nom_résidence"), false), DESC_ARG_NAME,
				executor, null));
	}
}
