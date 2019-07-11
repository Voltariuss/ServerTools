package fr.dornacraft.servertools.controller.commands.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.model.managers.PlayerManager;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandException;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

/**
 * Classe de gestion de la commande /adminexp
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class CmdAdminExp extends DornacraftCommand {

	public static final String CMD_LABEL = "adminexp";

	public static final String ARG_SET = "set";
	public static final String ARG_GIVE = "give";
	public static final String ARG_TAKE = "take";

	/**
	 * Constructeur de la commande /adminexp
	 */
	public CmdAdminExp() {
		super(CMD_LABEL);
		String cmdDesc = JavaPlugin.getPlugin(ServerTools.class).getCommand(CMD_LABEL).getDescription();
		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				// - /adminexp <set|give|take> <player> <number>
				Player player = Bukkit.getPlayer(args[1]);

				if (args[2].toLowerCase().charAt(args[2].length() - 1) == 'l') {
					int level = Integer.parseInt(args[2].substring(0, args[2].length() - 1));

					if (level < 0) {
						throw new DornacraftCommandException(UtilsAPI.NUMBER_MUST_BE_POSITIVE);
					}

					if (args[0].equalsIgnoreCase(ARG_SET)) {
						PlayerManager.setLevel(sender, player, level, false);
					} else if (args[0].equalsIgnoreCase(ARG_GIVE)) {
						PlayerManager.giveLevel(sender, player, level, false);
					} else {
						PlayerManager.takeLevel(sender, player, level, false);
					}
				} else {
					int exp = Integer.parseInt(args[2]);

					if (exp < 0) {
						throw new DornacraftCommandException(UtilsAPI.NUMBER_MUST_BE_POSITIVE);
					}

					if (args[0].equalsIgnoreCase(ARG_SET)) {
						PlayerManager.setExperience(sender, player, exp, false);
					} else if (args[0].equalsIgnoreCase(ARG_GIVE)) {
						PlayerManager.giveExperience(sender, player, exp, false);
					} else {
						PlayerManager.takeExperience(sender, player, exp, false);
					}
				}
			}
		};
		// /adminexp set <player> <number>
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(ARG_SET), cmdDesc),
				new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, true), cmdDesc),
				new CommandNode(new CommandArgument(CommandArgumentType.STRING, true), cmdDesc, executor, null));
		// /adminexp give <player> <number>
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(ARG_GIVE), cmdDesc),
				new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, true), cmdDesc),
				new CommandNode(new CommandArgument(CommandArgumentType.STRING, true), cmdDesc, executor, null));
		// /adminexp take <player> <number>
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(ARG_TAKE), cmdDesc),
				new CommandNode(new CommandArgument(CommandArgumentType.ONLINE_PLAYER, true), cmdDesc),
				new CommandNode(new CommandArgument(CommandArgumentType.STRING, true), cmdDesc, executor, null));
	}

}
