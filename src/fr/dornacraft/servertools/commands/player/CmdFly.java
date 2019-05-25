package fr.dornacraft.servertools.commands.player;

import org.bukkit.Bukkit;
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

public class CmdFly extends DornacraftCommand {

	public static final String CMD_LABEL = "fly";

	private static final String DESC_ARG_PLAYER = "Le joueur cible";

	private static final String ARG_ON = "on";
	private static final String ARG_OFF = "off";

	private static final String DESC_ARG_TOGGLE = "Active/Désactive le mode fly pour le joueur cible.";

	public CmdFly() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (args.length == 0) {
					if (sender instanceof Player) {
						toggleFly(sender, (Player) sender);
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} else if (args.length == 1) {
					toggleFly(sender, Bukkit.getPlayer(args[0]));

				} else if (args.length == 2) {
					if (args[1].equalsIgnoreCase(ARG_ON)) {
						setFly(sender, Bukkit.getPlayer(args[0]), true);

					} else if (args[1].equalsIgnoreCase(ARG_OFF)) {
						setFly(sender, Bukkit.getPlayer(args[0]), false);
					}
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);

		CommandArgumentChecker checker = new CommandArgumentChecker() {

			@Override
			public boolean check(String str) {
				if (str.equalsIgnoreCase("on") || str.equalsIgnoreCase("off")) {
					return true;
				} else {
					return false;
				}
			}
		};
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, false), DESC_ARG_PLAYER, executor,
						null),
				new CommandNode(new CommandArgument(new CommandArgumentType("on/off", checker), false), DESC_ARG_TOGGLE,
						executor, "dornacraft.essentials.fly.other"));

	}

	private static void setFly(CommandSender sender, Player target, Boolean isAllowedFlight) {
		String action = isAllowedFlight ? "§a§lactivé" : "§c§ldésactivé";

		if (target.getAllowFlight() != isAllowedFlight) {
			if (sender != target) {
				if (sender.hasPermission("dornacraft.essentials.fly.other")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, target, "Mode §6Fly %s §epar §b%s§e.", action,
							sender.getName());
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
							"Vous avez %s §ele mode §6Fly §eau joueur §b%s§e.", action, target.getDisplayName());
					target.setAllowFlight(isAllowedFlight);
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Vous avez %s §evotre mode §6Fly§e.", action);
				target.setAllowFlight(isAllowedFlight);
			}
		} else {
			if (sender != target) {
				if (sender.hasPermission("dornacraft.essentials.fly.other")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Mode §6Fly §edéjà %s §epour §b%s§e.", action,
							target.getName());
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Votre mode §6Fly §eest déjà %s§e.", action);
			}
		}
	}

	private static void toggleFly(CommandSender sender, Player target) {
		setFly(sender, target, !target.getAllowFlight());
	}

}