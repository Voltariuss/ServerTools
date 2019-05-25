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

	private static final String DESC_ARG_PLAYER = "Le joueur cible.";
	private static final String DESC_ARG_SET = "Définit la quantité d'xp du joueur.";
	private static final String DESC_ARG_QTY = "La quantité d'expérience à ajouter/retirer.";
	private static final String DESC_ARG_GIVE = "Génère de l'xp au joueur.";
	private static final String DESC_ARG_TAKE = "Retire de l'xp au joueur.";

	/**
	 * Constructeur de la commande /adminexp
	 */
	public CmdAdminExp() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				Player target = Bukkit.getPlayer(args[1]);

				if (args[2].toLowerCase().charAt(args[2].length() - 1) == 'l') {
					int level = Integer.parseInt(args[2].substring(0, args[2].length() - 2));

					if (level >= 0) {
						if (args[0].equalsIgnoreCase(ARG_SET)) {
							target.setTotalExperience(level);
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
									"Le joueur §b%s &ea désormais §6%s §eniveaux d'expérience.", target.getName(),
									target.getLevel());
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, target,
									"§b%s §ea définit votre niveau d'expérience à §6%s§e.", sender.getName(),
									target.getTotalExperience());
						} else if (level != 0) {
							if (args[0].equalsIgnoreCase(ARG_GIVE)) {
								target.setLevel(target.getLevel() + level);
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
										"Vous avez donné §6%s §eniveaux d'expérience au joueur §b%s§e.", level,
										target.getName());
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, target,
										"§b%s §avous a donné §6%s §eniveaux d'expérience§a.", sender.getName(), level);
							} else {
								target.setLevel(target.getLevel() - level);
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
										"Vous avez retiré §6%s §eniveaux d'expérience au joueur §b%s§e.", level,
										target.getName());
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, target,
										"§b%s §cvous a retiré §6%s §eniveaux d'expérience§c.", sender.getName(), level);
							}
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.NUMBER_MUST_BE_POSITIVE);
						}
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.NUMBER_MUST_BE_POSITIVE);
					}
				} else {
					int exp = Integer.parseInt(args[2]);

					if (exp >= 0) {
						if (args[0].equalsIgnoreCase(ARG_SET)) {
							target.setTotalExperience(exp);
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
									"Le joueur §b%s §ea désormais un total de §6%s §eexpériences.", target.getName(),
									target.getTotalExperience());
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, target,
									"§b%s §ea définit votre quantité totale d'expérience à §6%s§e.", sender.getName(),
									target.getTotalExperience());
						} else if (exp != 0) {
							if (args[0].equalsIgnoreCase(ARG_GIVE)) {
								target.giveExp(exp);
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
										"Vous avez donné §6%s §eexpériences au joueur §b%s§e.", exp, target.getName());
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, target,
										"§b%s §avous a donné §6%s §eexpériences§a.", sender.getName(), exp);
							} else {
								target.giveExp(-exp);
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
										"Vous avez retiré §6%s §eexpériences au joueur §b%s§e.", exp, target.getName());
								UtilsAPI.sendSystemMessage(MessageLevel.INFO, target,
										"§b%s §cvous a retiré §6%s §eexpériences§c.", sender.getName(), exp);
							}
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.NUMBER_MUST_BE_POSITIVE);
						}
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.NUMBER_MUST_BE_POSITIVE);
					}
				}
			}
		};

		DornacraftCommandExecutor executor_set = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command arg1, String arg2, String[] args) throws Exception {
				// TODO Auto-generated method stub

			}
		};

		// /adminexp set <player> <number>
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(ARG_SET, ""), DESC_ARG_SET, executor_set, null),
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, true), DESC_ARG_PLAYER, executor, null),
				new CommandNode(new CommandArgument(CommandArgumentType.NUMBER, true), DESC_ARG_QTY, executor, null));

		// /adminexp give <player> <number>
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(ARG_GIVE, ""), DESC_ARG_GIVE, executor, null),
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, true), DESC_ARG_PLAYER, executor, null),
				new CommandNode(new CommandArgument(CommandArgumentType.NUMBER, true), DESC_ARG_QTY, executor, null));

		// /adminexp take <player> <number>
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(ARG_TAKE, ""), DESC_ARG_TAKE, executor, null),
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, true), DESC_ARG_PLAYER, executor, null),
				new CommandNode(new CommandArgument(CommandArgumentType.NUMBER, true), DESC_ARG_QTY, executor, null));
	}

}
