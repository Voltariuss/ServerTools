package fr.dornacraft.servertools.controller.commands.player;

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

public class CmdFeed extends DornacraftCommand {

	public static final String CMD_LABEL = "feed";
	private static final String CMD_DESC = "Régénère toute la barre de nourriture du joueur cible.";

	public CmdFeed() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (args.length == 0 || sender.getName().equalsIgnoreCase(args[0])) {
					if (sender instanceof Player || args.length > 0 && sender.getName().equalsIgnoreCase(args[0])) {
						feed((Player) sender);
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Vous avez été rassasié.");
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} else if (args.length == 1 && !sender.getName().equals(args[0])) {
					Player target = Bukkit.getPlayerExact(args[0]);

					if (target != null) {
						feed(target);
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, target, "Vous avez été rassasié par §b%s§e.",
								sender.getName());
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Vous avez rassasié §b%s§e.",
								target.getDisplayName());
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PLAYER_UNKNOW);
					}
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, false),
				CMD_DESC, executor, "dornacraft.essentials.feed.other"));
	}

	public static void feed(Player player) {
		player.setExhaustion(0);
		player.setSaturation(5);
		player.setFoodLevel(20);
	}
}
