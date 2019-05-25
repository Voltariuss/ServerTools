package fr.dornacraft.servertools.commands;

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

public class CmdKillAll extends DornacraftCommand {

	public static final String CMD_LABEL = "killall";
	private static final String DESC_ARG1 = "Spécifie quels mobs doivent être tués";
	private static final String DESC_ARG2 = "Spécifie la zone dans laquelle tuer les mobs.";

	public CmdKillAll() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (args.length == 1) {
						KillAllManager.killEntities(player, args[0]);
					} else if (args.length == 2) {
						KillAllManager.killEntities(player, args[0], args[1]);
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "Usage : §6/killall  []");
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};

		getCmdTreeExecutor()
				.addSubCommand(
						new CommandNode(
								new CommandArgument(CommandArgumentType.STRING.getCustomArgType(
										"all|monsters|animals|ambient|drops|xp|arrows|mobs:[MobType]"), true),
								DESC_ARG1, executor, null),
						new CommandNode(
								new CommandArgument(CommandArgumentType.STRING
										.getCustomArgType("radius=100|none:world=name|current"), false),
								DESC_ARG2, executor, null));
	}
}
