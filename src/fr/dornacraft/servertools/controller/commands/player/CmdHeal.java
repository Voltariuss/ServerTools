package fr.dornacraft.servertools.controller.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdHeal extends DornacraftCommand {

	public static final String CMD_LABEL = "heal";
	private static final String DESC_ARG_PLAYER = "Le joueur à cibler.";

	public CmdHeal() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (args.length == 0 || args[0].equalsIgnoreCase(sender.getName())) {
					if (sender instanceof Player || args.length > 0 && args[0].equalsIgnoreCase(sender.getName())) {
						Player player = (Player) sender;
						heal(player);
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "§eVous avez été soigné.");
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} else {
					Player target = Bukkit.getPlayer(args[0]);

					if (target != null) {
						heal(target);
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, target, "Vous avez été soigné par §b%s§e.",
								sender.getName());
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Vous avez soigné le joueur §b%s§e.",
								target.getDisplayName());
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_UNKNOW);
					}
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, false),
				DESC_ARG_PLAYER, executor, "dornacraft.essentials.heal.other"));
	}

	public static void heal(Player player) {
		player.setHealth(player.getHealthScale());
		player.setFireTicks(0);
		player.setRemainingAir(player.getMaximumAir());
		CmdFeed.feed(player);

		for (PotionEffect potionEffect : player.getActivePotionEffects()) {
			player.removePotionEffect(potionEffect.getType());
		}
	}
}
