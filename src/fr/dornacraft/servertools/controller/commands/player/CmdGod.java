package fr.dornacraft.servertools.controller.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.controller.listeners.GodPlayerDamageListener;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentChecker;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdGod extends DornacraftCommand {

	public static final String CMD_LABEL = "god";
	private static final String DESC_ARG_PLAYER = "Le joueur à cibler.";
	private static final String DESC_ARG_TOGGLE = "Active/Désactive le mode dieu pour le joueur ciblé.";
	protected static final String ARG_ON = "on";
	protected static final String ARG_OFF = "off";

	public CmdGod() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (args.length == 0) {
					if (sender instanceof Player) {
						toggleGod(sender, (Player) sender);
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} else if (args.length == 1) {
					if (sender.hasPermission("dornacraft.essentials.god.other")) {
						toggleGod(sender, Bukkit.getPlayer(args[0]));
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
					}

				} else if (args.length == 2) {
					if (sender.hasPermission("dornacraft.essentials.god.other")) {
						if (args[1].equalsIgnoreCase(ARG_ON)) {
							setGod(sender, Bukkit.getPlayer(args[0]), true);

						} else if (args[1].equalsIgnoreCase(ARG_OFF)) {
							setGod(sender, Bukkit.getPlayer(args[0]), false);
						}
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
						executor, "dornacraft.essentials.god.other"));
	}

	private static void setGod(CommandSender sender, Player target, Boolean isGod) {
		String action = isGod ? "§a§lactivé" : "§c§ldésactivé";

		if (GodPlayerDamageListener.isGod(target) != isGod) {
			if (sender != target) {
				if (sender.hasPermission("dornacraft.essentials.god.other")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, target, "Mode §6God %s §epar §b%s§e.", action,
							sender.getName());
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
							"Vous avez %s §ele mode §6God §eau joueur §b%s§e.", action, target.getDisplayName());
					if (GodPlayerDamageListener.isGod(target)) {
						GodPlayerDamageListener.removeGod(target);
					} else {
						GodPlayerDamageListener.addGod(target);
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Vous avez %s §evotre mode §6God§e.", action);
				if (GodPlayerDamageListener.isGod(target)) {
					GodPlayerDamageListener.removeGod(target);
				} else {
					GodPlayerDamageListener.addGod(target);
				}
			}
		} else {
			if (sender != target) {
				if (sender.hasPermission("dornacraft.essentials.god.other")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Mode §6God §edéjà %s §epour §b%s§e.", action,
							target.getName());
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Votre mode §6God §eest déjà %s§e.", action);
			}
		}
	}

	private static void toggleGod(CommandSender sender, Player target) {
		setGod(sender, target, !GodPlayerDamageListener.isGod(target));
	}

}