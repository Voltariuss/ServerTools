package fr.dornacraft.servertools.commands.info;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.dornacraft.servertools.utils.UtilsEssentials;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdSeen extends DornacraftCommand {

	public static final String CMD_LABEL = "seen";
	private static final String CMD_DESC = "Affiche des information sur l'ip du joueur cible";

	public CmdSeen() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				OfflinePlayer target = InfoManager.getOfflinePlayerFromPseudo(args[0]);

				if (target != null) {
					getInfoPlayer(sender, target);
				} else {
					getInfoIp(sender, args[0]);
				}
			}
		};

		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, true), CMD_DESC, executor, null));
	}

	private void getInfoPlayer(CommandSender sender, OfflinePlayer target) throws Exception {
		String ip = InfoManager.getIp(target);
		sender.sendMessage(UtilsEssentials.getHeader("Informations sur l'IP"));
		sender.sendMessage(UtilsEssentials.getNewLine("Pseudo", target.getName()));
		sender.sendMessage(UtilsEssentials.getNewLine("Dernière IP connue", ip));
		sender.sendMessage(UtilsEssentials.getNewLine("Localisation",
				InfoManager.getCountry(target.isOnline() ? target.getPlayer().getAddress() : null)));
		sender.sendMessage(UtilsEssentials.getNewLine("Dernière connexion", target.isOnline() ? "§aConnecté"
				: "Il y a " + UtilsEssentials.convertTime(System.currentTimeMillis() - target.getLastPlayed())));
		sender.sendMessage("§6Liste des joueurs utilisant la même adresse IP :");

		for (String player : InfoManager.getPlayers(ip)) {
			sender.sendMessage(" §e- §b" + player);
		}
	}

	private void getInfoIp(CommandSender sender, String ip) throws SQLException {
		ArrayList<String> players = InfoManager.getPlayers(ip);

		if (!players.isEmpty()) {
			sender.sendMessage(UtilsEssentials.getHeader("Informations sur l'IP"));
			sender.sendMessage(UtilsEssentials.getNewLine("IP", ip));
			sender.sendMessage("§6Liste des joueurs utilisant la même adresse IP :");

			for (String player : players) {
				sender.sendMessage(" §e- §b" + player);
			}
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, "Saisie incorrecte ou joueur/ip inconnu(e).");
		}
	}
}
