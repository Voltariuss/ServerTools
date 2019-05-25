package fr.dornacraft.servertools.commands.utils;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					ItemStack item = player.getItemOnCursor();

					if (item != null && item.getType() != Material.AIR) {
						if (item.getAmount() < item.getMaxStackSize()) {
							item.setAmount(item.getMaxStackSize());
							player.setItemOnCursor(item);
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, "Votre stack est déjà plein.");
						}
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender,
								"Vous n'avez pas d'item dans votre main.");
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
	}
}
