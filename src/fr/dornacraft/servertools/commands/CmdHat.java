package fr.dornacraft.servertools.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	private static final String DESC_ARG_REMOVE = "Retire votre chapeau.";

	public CmdHat() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (args.length == 0) {
						if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
							ItemStack hand = player.getItemOnCursor();
							ItemStack head = player.getInventory().getHelmet();
							player.setItemOnCursor(head);
							player.getInventory().setHelmet(hand);
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
									"§eVous avez changé votre §6chapeau §e!");
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender,
									"Vous n'avez pas de chapeau dans la main !");
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						try {
							ItemHandling.addItem(player, player.getInventory().getHelmet());
							player.getInventory().setHelmet(new ItemStack(Material.AIR));
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
									"§eVous avez retiré votre §6chapeau §e!");
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
		getCmdTreeExecutor()
				.addSubCommand(new CommandNode(new CommandArgument("remove", ""), DESC_ARG_REMOVE, executor, null));
	}
}
