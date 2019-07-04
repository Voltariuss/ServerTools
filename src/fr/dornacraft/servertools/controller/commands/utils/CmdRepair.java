package fr.dornacraft.servertools.controller.commands.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.dornacraft.servertools.model.utils.RepairType;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;
import fr.voltariuss.simpledevapi.items.ItemUtils;

public class CmdRepair extends DornacraftCommand {

	public static final String CMD_LABEL = "repair";
	private static final String DESC_HAND = "Répare l'objet dans votre main.";
	private static final String DESC_ALL = "Réparer tout les objets de votre inventaire.";

	public CmdRepair() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				// - /repair [hand|all]
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("hand")) {
						repairItems(player, Arrays.asList(player.getItemOnCursor()), RepairType.HAND);
					} else if (args[0].equalsIgnoreCase("all")) {
						ArrayList<ItemStack> inventoryItems = new ArrayList<>();
						PlayerInventory inv = player.getInventory();
						inventoryItems.addAll(Arrays.asList(inv.getContents()));
						inventoryItems.addAll(
								Arrays.asList(inv.getHelmet(), inv.getChestplate(), inv.getLeggings(), inv.getBoots()));
						repairItems(player, inventoryItems, RepairType.ALL);
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument("hand", ""), DESC_HAND, executor, null));
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument("all", ""), DESC_ALL, executor, null));

	}

	public void repairItems(Player player, List<ItemStack> items, RepairType repairType) {
		boolean hasRepairableItems = false;

		for (ItemStack item : items) {
			if (item != null && item.getType() != Material.AIR) {
				if (ItemUtils.isRepairable(item)) {
					if (item.getDurability() != 0) {
						item.setDurability((short) 0);

						if (repairType == RepairType.HAND) {
							UtilsAPI.sendSystemMessage(MessageLevel.SUCCESS, player,
									"L'objet dans votre main a été réparé.");
						} else {
							hasRepairableItems = true;
						}
					} else if (repairType == RepairType.HAND) {
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "l'objet dans votre main est intact.");
					}
				} else if (repairType == RepairType.HAND) {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, player,
							"L'objet dans votre main n'est pas réparable.");
				}
			} else if (repairType == RepairType.HAND) {
				UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, player, "Vous n'avez pas d'objet dans votre main.");
			}
		}

		if (repairType == RepairType.ALL) {
			if (hasRepairableItems) {
				UtilsAPI.sendSystemMessage(MessageLevel.SUCCESS, player,
						"Tous les objets de votre inventaire ont été réparés.");
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "Aucun objet à réparer.");
			}
		}
	}
}
