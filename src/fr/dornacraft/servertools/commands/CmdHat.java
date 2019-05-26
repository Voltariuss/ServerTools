package fr.dornacraft.servertools.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.dornacraft.servertools.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;
import fr.voltariuss.simpledevapi.items.InventoryFullException;
import fr.voltariuss.simpledevapi.items.ItemHandling;

public class CmdHat extends DornacraftCommand {

	public static final String CMD_LABEL = "hat";

	public CmdHat() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (args.length == 0) {
						if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
							ItemStack hand = player.getInventory().getItemInMainHand();
							ItemStack head = player.getInventory().getHelmet();
							player.getInventory().setItemInMainHand(head);
							player.getInventory().setHelmet(hand);
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
									ServerToolsConfig.getCommandMessage(CMD_LABEL, "success_message"));
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.WARNING, sender,
									ServerToolsConfig.getCommandMessage(CMD_LABEL, "warning_no_item_in_main_hand"));
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						try {
							ItemStack helmet = player.getInventory().getHelmet();
							
							if (helmet != null) {
								ItemHandling.addItem(player, player.getInventory().getHelmet());
								player.getInventory().setHelmet(new ItemStack(Material.AIR));
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
										ServerToolsConfig.getCommandMessage(CMD_LABEL, "info_remove_done"));								
							} else {
								UtilsAPI.sendSystemMessage(MessageLevel.WARNING, player,
										ServerToolsConfig.getCommandMessage(CMD_LABEL, "warning_remove_no_helmet"));
							}
						} catch (InventoryFullException e) {
							UtilsAPI.sendSystemMessage(MessageLevel.ERROR, player, UtilsAPI.INVENTORY_FULL);
						}
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument("remove", ""),
				ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_arg_remove_desc"), executor, null));
	}
}
