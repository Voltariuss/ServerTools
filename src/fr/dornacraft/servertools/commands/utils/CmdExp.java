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

public class CmdExp extends DornacraftCommand {

	public static final String CMD_LABEL = "exp";
	private static final String DESC_CMD = "Affiche les niveaux d'expérience du joueur ciblé";

	public CmdExp() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				if (args.length == 0 || args[0].equalsIgnoreCase(sender.getName())) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
								"§6Votre niveau d'expérience : §e%s §8§o(§e§o%s§7§o/§e§o%s exp§8§o)", player.getLevel(),
								(int) (player.getExp() * player.getExpToLevel()), player.getExpToLevel());
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} else if (args.length == 1) {
					Player player = Bukkit.getPlayer(args[0]);

					if (player != null) {
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
								"§6Niveau d'expérience du joueur §b%s §e: §e%s §8§o(§e§o%s§7§o/§e§o%s exp§8§o)",
								player.getName(), player.getLevel(), (int) (player.getExp() * player.getExpToLevel()),
								player.getExpToLevel());
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, UtilsAPI.PLAYER_UNKNOW);
					}
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, false), DESC_CMD, executor, null));
	}
}
