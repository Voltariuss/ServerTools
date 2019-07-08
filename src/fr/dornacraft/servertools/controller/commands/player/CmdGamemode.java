package fr.dornacraft.servertools.controller.commands.player;

import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentChecker;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdGamemode extends DornacraftCommand {

	public static final String CMD_LABEL = "gamemode";

	private static final String DESC_ARG_MODE = "Le nouveau mode de jeu du joueur cible.";
	private static final String DESC_ARG_PLAYER = "Le joueur à cibler";

	private TreeMap<String, GameMode> argsList = new TreeMap<String, GameMode>(String.CASE_INSENSITIVE_ORDER) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 837003470282756681L;

		{
			put("survival", GameMode.SURVIVAL);
			put("0", GameMode.SURVIVAL);
			put("creative", GameMode.CREATIVE);
			put("1", GameMode.CREATIVE);
			put("adventure", GameMode.ADVENTURE);
			put("2", GameMode.ADVENTURE);
			put("spectator", GameMode.SPECTATOR);
			put("3", GameMode.SPECTATOR);
		}
	};

	public CmdGamemode() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {

				if (args.length == 1) {
					if (sender instanceof Player) {
						setGamemode(sender, (Player) sender, argsList.get(args[0]));
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} else if (args.length == 2) {
					setGamemode(sender, Bukkit.getPlayer(args[1]), argsList.get(args[0]));
				}
			}
		};

		CommandArgumentChecker checker = new CommandArgumentChecker() {

			@Override
			public boolean check(String str) {
				if (argsList.containsKey(str)) {
					return true;
				} else {
					return false;
				}
			}
		};

		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(new CommandArgumentType("mode", checker), true), DESC_ARG_MODE,
						executor, null),
				new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, false), DESC_ARG_PLAYER, executor,
						"dornacraft.essentials.gamemode.other"));
	}

	private static void setGamemode(CommandSender sender, Player target, GameMode gamemode) {

		if (target.getGameMode() != gamemode) {
			if (sender != target) {
				if (sender.hasPermission("dornacraft.essentials.gamemode.other")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, target,
							"Votre mode de jeu a été défini sur §6%s§e par §b%s§e.", gamemode.toString().toLowerCase(),
							sender.getName());
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
							"Vous avez défini le mode de jeu du joueur §b%s§e sur §6%s§e.", target.getDisplayName(),
							gamemode.toString().toLowerCase());
					target.setGameMode(gamemode);
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, target, "Vous avez défini votre mode de jeu sur §6%s§e.",
						gamemode.toString().toLowerCase());
				target.setGameMode(gamemode);
			}
		} else {
			if (sender != target) {
				if (sender.hasPermission("dornacraft.essentials.gamemode.other")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "§b%s §eest déjà en §6%s§e.",
							target.getName(), gamemode.toString().toLowerCase());
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Vous êtes déjà en §6%s§e.",
						gamemode.toString().toLowerCase());
			}
		}
	}
}
