package fr.dornacraft.servertools.controller.commands.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.model.managers.PlayerManager;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;
import fr.voltariuss.simpledevapi.cmds.InvalidArgumentsCommandException;

public class CmdClear extends DornacraftCommand {

	public static final String CMD_LABEL = "clear";

	private static final String ARG_INVENTORY = "inventory";
	private static final String ARG_INVENTORY_CONTENT = "inventory_content";
	private static final String ARG_ARMOR = "armor";
	private static final String ARG_ENDERCHEST = "enderchest";
	private static final String ARG_ALL = "all";

	public static final String CLEAR_TYPE_USAGE = "inventory|inventory_content|armor|enderchest|all";

	public CmdClear() {
		super(CMD_LABEL);
		String cmdDesc = JavaPlugin.getPlugin(ServerTools.class).getCommand(CMD_LABEL).getDescription();
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				// - /clear [player] [inventory|inventory_content|armor|enderchest|all]
				Player player = null;

				if (args.length == 0) {
					if (sender instanceof ConsoleCommandSender) {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
					} else {
						player = (Player) sender;
						PlayerManager.clearInventory(sender, player, false);
					}
				} else {
					String playerName = args[0];
					player = Bukkit.getPlayer(playerName);

					if (player == null) {
						throw new InvalidArgumentsCommandException();
					}

					if (sender.getName().equalsIgnoreCase(playerName)
							|| sender.hasPermission("dornacraft.essentials.clear.other")) {
						if (args.length == 1) {
							PlayerManager.clearInventory(sender, player, false);
						} else {
							String clearType = args[1];

							switch (clearType) {
							case ARG_INVENTORY:
								PlayerManager.clearInventory(sender, player, false);
								break;
							case ARG_INVENTORY_CONTENT:
								PlayerManager.clearInventoryContentOnly(sender, player, false);
								break;
							case ARG_ARMOR:
								PlayerManager.clearArmor(sender, player, false);
								break;
							case ARG_ENDERCHEST:
								PlayerManager.clearEnderChest(sender, player, false);
								break;
							case ARG_ALL:
								PlayerManager.clearAll(sender, player, false);
								break;
							default:
								throw new InvalidArgumentsCommandException();
							}
						}
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PERMISSION_MISSING);
					}
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, false), cmdDesc, executor, null),
				new CommandNode(
						new CommandArgument(CommandArgumentType.STRING.getCustomArgType(CLEAR_TYPE_USAGE), false),
						cmdDesc, executor, null));

	}
}
