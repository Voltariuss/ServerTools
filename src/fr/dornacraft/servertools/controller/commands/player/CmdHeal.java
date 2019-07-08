package fr.dornacraft.servertools.controller.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.managers.PlayerManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;
import fr.voltariuss.simpledevapi.cmds.InvalidArgumentsCommandException;

public class CmdHeal extends DornacraftCommand {

	public static final String CMD_LABEL = "heal";
	private static final String DESC_ARG_PLAYER = "Le joueur à cibler.";

	public CmdHeal() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				CommandSender healer = sender;
				Player target = null;

				if (args.length == 1) {
					String playerName = args[0];
					target = Bukkit.getPlayer(playerName);

					if (target == null) {
						throw new InvalidArgumentsCommandException();
					}
				} else if (sender instanceof Player) {
					target = (Player) sender;
				} 
				
				if (sender instanceof Player || args.length == 1) {
					PlayerManager.heal(healer, target, false);
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};
		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, false),
				DESC_ARG_PLAYER, executor, "dornacraft.essentials.heal.other"));
	}
}
