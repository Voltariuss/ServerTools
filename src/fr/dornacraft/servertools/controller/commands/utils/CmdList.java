package fr.dornacraft.servertools.controller.commands.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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

public class CmdList extends DornacraftCommand {

	public static final String CMD_LABEL = "list";

	public static final String DESC_ARG_INDEX = "Permet de choisir la page à afficher.";
	public static final String INFO_NO_ONLINE_PLAYERS = "Il n'y a pas de joueur en ligne.";

	public CmdList() {
		super(CMD_LABEL);
		DornacraftCommandExecutor dce = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				HashMap<Integer, ArrayList<String>> playersByPages = constructPlayerList(sender);
				// les joueurs que peut voir l'executeur de la commande
				Integer maxPages = playersByPages.keySet().size(); // le nombre de pages max
				Integer nbPlayers = getNbPlayers(playersByPages.values()); // le nombre de joueurs
				Integer index = 1; // la page à afficher

				if (args.length == 1) { // le CommandSender précise la page
					index = Integer.parseInt(args[0]);

					if (index > 0 && index < maxPages + 1) {
						// valeur de l'argument supérieure à 0 et inférieure au nombre de pages max
						if (nbPlayers > 0) {
							UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender,
									getCommandResultDisplay(index, maxPages, playersByPages, nbPlayers));
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, INFO_NO_ONLINE_PLAYERS);
						}
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Page min : 1, page max : %s.", maxPages);
					}
				} else { // pas de pages de précisées
					if (nbPlayers > 0) {
						UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender,
								getCommandResultDisplay(index, maxPages, playersByPages, nbPlayers));
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, INFO_NO_ONLINE_PLAYERS);
					}
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(dce);
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.NUMBER.getCustomArgType("page"), false),
						DESC_ARG_INDEX, dce, null));

	}

	/***
	 * Retourne le nombre de joueur que voit voir le joueur ( -> exclu les joueurs
	 * vanish)
	 */
	private Integer getNbPlayers(Collection<ArrayList<String>> collection) {
		Integer result = 0;

		for (ArrayList<String> array : collection) {
			result += array.size();
		}

		return result;
	}

	/**
	 * 
	 * @param index          La page à afficher
	 * @param maxPages       Le nombre de page maximal
	 * @param playersByPages la list des joueurs par tranches de 20 (construit avec
	 *                       : constructPlayerList())
	 * @return L'affichage des joueurs en list
	 */
	private String getCommandResultDisplay(Integer index, Integer maxPages,
			HashMap<Integer, ArrayList<String>> playersByPages, Integer nbPlayers) {
		String header = String.format("§6===== [§a%s joueurs en ligne : page %d/%d§6] =====", nbPlayers, index,
				maxPages); // le header
		String info_nextpage = String.format("Faites /list %s pour voir la page suivante.", index + 1);
		// informations pour la page suivante
		String list = header + playersByPages.get(index).toString().replace("[", "").replace("]", "");

		if (index < maxPages) { // si il y a des pages suivantes, ajouter "info_nextpage"
			list = header + playersByPages.get(index).toString().replace("[", "").replace("]", "") + info_nextpage;
		}

		return list;
	}

	/**
	 * @return La liste des joueurs par tranches de 20
	 */
	private HashMap<Integer, ArrayList<String>> constructPlayerList(CommandSender sender) {
		Player player = null;

		if (sender instanceof Player) {
			player = (Player) sender;
		}
		HashMap<Integer, ArrayList<String>> result = new HashMap<>();
		// le resultat (map <page - noms des joueurs sur cette pages>
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers()); // tout les joueurs
		ArrayList<String> page = new ArrayList<>(); // les noms des joueurs sur une page
		Integer index = 1; // la page actuelle à remplir

		for (int p = 0; p < onlinePlayers.size(); p++) {
			if (p != 0 && (p % 21) == 0) { // tous les 10 joueurs, passé à la page suivante
				page = new ArrayList<>();
				index++;
			}

			if (player != null) { // le sender est un joueur
				if (player.canSee(onlinePlayers.get(p))) { // check non vanish
					page.add("§r" + onlinePlayers.get(p).getDisplayName());
				}
			} else {
				page.add("§r" + onlinePlayers.get(p).getDisplayName());
			}
			result.put(index, page);
		}

		return result;
	}
}
