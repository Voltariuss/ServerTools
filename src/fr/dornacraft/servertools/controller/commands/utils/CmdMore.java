package fr.dornacraft.servertools.controller.commands.utils;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdMore extends DornacraftCommand {

	public static final String CMD_LABEL = "more";

	public CmdMore() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					ItemStack item = player.getInventory().getItemInMainHand();

					if (item != null && item.getType() != Material.AIR) {
						if (item.getAmount() < 64) {
							item.setAmount(64);
							player.getInventory().setItemInMainHand(item);
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender,
									ServerToolsConfig.getCommandMessage(CMD_LABEL, "failure_slot_already_full"));
						}
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.WARNING, sender,
								ServerToolsConfig.getCommandMessage(CMD_LABEL, "warning_no_item_in_main_hand"));
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
	}
}
