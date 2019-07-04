package fr.dornacraft.servertools.controller.commands.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdInvsee extends DornacraftCommand {

	public static final String CMD_LABEL = "invsee";
	
	public CmdInvsee() {
		super(CMD_LABEL);
		// DornacraftCommandExecutor executor;
		new DornacraftCommandExecutor() {
			
			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				// - /invsee <playername>
				if (sender instanceof Player) {
					Player player = (Player) sender;
					
					if (args.length == 1) {
						Player target = Bukkit.getPlayer(args[0]);
						
						if (target != null) {
							player.openInventory(target.getInventory());
							/*Inventory inv = Bukkit.createInventory(null, 6 * 9, "Inventaire de " + target.getName());
							
							// Set d'armure
							for (int i = 0; i < 4; i++) {
								ItemStack armorItem = target.getInventory().getArmorContents()[i];
								
								if (armorItem != null && armorItem.getType() != Material.AIR) {
									inv.setItem(i, armorItem);
								}
							}
							
							// Item en main
							ItemStack itemHand = target.getItemInHand();
							
							if (itemHand == null || itemHand.getType() == Material.AIR) {
								itemHand = ItemUtils.generateItem(Material.GLASS, 1, (short) 0, "§cAucun item dans la main");
							}
							inv.setItem(6, target.getItemInHand());
							
							// Niveau
							int playerMcLevel = target.getLevel();
							ItemStack itemLevel = new ItemStack(Material.EXP_BOTTLE, playerMcLevel);
							ItemMeta meta = itemLevel.getItemMeta();
							meta.setDisplayName("§5Niveau du joueur : §d" + target.getLevel());
							itemLevel.setItemMeta(meta);
							inv.setItem(8, itemLevel);
							
							// Démarcation
							for (int i = 0; i < 9; i++) {
								inv.setItem(9 + i, new ItemStack(Material.STAINED_GLASS_PANE));
							}
							
							// Inventaire principal
							for (int i = 0; i < 4; i++) {
								for (int j = 0; j < 9; j++) {
									ItemStack item = target.getInventory().getItem(i * 9 + j);
									
									if (item != null && item.getType() != Material.AIR) {
										inv.setItem((i + 2) * 9 + j, target.getInventory().getItem(i * 9 + j));
									}
								}
							}
							player.openInventory(inv);*/
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.ERROR, player, UtilsAPI.PLAYER_UNKNOW);
						}
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};
	}
}
