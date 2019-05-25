package fr.dornacraft.servertools.commands.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdClear extends DornacraftCommand {

	public static final String CMD_LABEL = "clear";

	private static final String ARG_ENDERCHEST = "enderchest";
	public static final String ARG_INVENTORY = "inventory";

	private static final String DESC_ARG_INVENTORY = "Nettoie l'inventaire du joueur cible.";
	private static final String DESC_ARG_ENDERCHEST = "Nettoie l'enderchest du joueur cible.";

	public CmdClear() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				// - /clear [enderchest|inventory] [player]

				if (args.length == 2) {
					if (sender.hasPermission("dornacraft.essentials.clear.other")) {
						Player target = Bukkit.getPlayer(args[1]);

						if (target != null) {
							if (args[0].equalsIgnoreCase(ARG_ENDERCHEST)) {
								clearEnderchest(target);
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, target,
										"Votre §6ender chest §ea été supprimé par §b%s§e.", sender.getName());
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
										"Vous avez supprimé §6l'ender chest §edu joueur §b%s§e.", target.getName());
							} else if (args[0].equalsIgnoreCase(ARG_INVENTORY)) {
								clearInventory(target);
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, target,
										"Votre §6inventaire §ea été supprimé par §b%s§e.", sender.getName());
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
										"Vous avez supprimé §6l'inventaire §edu joueur §b%s§e.", target.getName());
							}
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PLAYER_UNKNOW);
						}
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
					}
				} else if (sender instanceof Player) {
					if (args.length == 0) {
						clearInventory((Player) sender);
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
								"Vous avez supprimé votre §6inventaire§e.");
					} else if (args.length == 1) {
						if (args[0].equalsIgnoreCase(ARG_ENDERCHEST)) {
							clearEnderchest((Player) sender);
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
									"Vous avez supprimé votre §6ender chest§e.");

						} else if (args[0].equalsIgnoreCase(ARG_INVENTORY)) {
							clearInventory((Player) sender);
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
									"Vous avez supprimé votre §6inventaire§e.");
						}

					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(ARG_ENDERCHEST, "ec"), DESC_ARG_ENDERCHEST, executor, null),
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, false), DESC_ARG_ENDERCHEST, executor,
						null));

		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(ARG_INVENTORY, "inv"), DESC_ARG_INVENTORY, executor, null),
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, false), DESC_ARG_INVENTORY, executor,
						null));

	}

	private void clearInventory(Player player) {
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
	}

	private void clearEnderchest(Player player) {
		player.getEnderChest().clear();
	}

}
