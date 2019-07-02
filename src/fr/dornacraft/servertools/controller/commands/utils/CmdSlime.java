package fr.dornacraft.servertools.controller.commands.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdSlime extends DornacraftCommand {

	public static final String CMD_LABEL = "slime";

	public static final String ARG_MAP = "map";

	public static final String DESC_ARG_MAP = "Vous affiche la map des chunks à slime aux alentours.";

	public CmdSlime() {
		super(CMD_LABEL);
		DornacraftCommandExecutor dce = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String Label, String[] args) throws Exception {
				if (sender instanceof Player) {
					String map = listToString(buildChunkList((Player) sender, 5));
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, "Carte des chunks à slimes :\n%s", map);
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(dce);
		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(ARG_MAP, ""), DESC_ARG_MAP, dce, null));
	}

	/***
	 * @param totest le chunk a vérifier
	 * @return true: le chunk est un chunk à slimes <br>
	 *         false: le chunk n'est PAS un chunk à slimes
	 */
	private Boolean isSlimeChunk(Chunk totest) {
		Random value = new Random(totest.getWorld().getSeed() + (long) (totest.getX() * totest.getX() * 0x4c1906)
				+ (long) (totest.getX() * 0x5ac0db) + (long) (totest.getZ() * totest.getZ()) * 0x4307a7L
				+ (long) (totest.getZ() * 0x5f24f) ^ 0x3ad8025f);

		if (value.nextInt(10) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * 
	 * @param player Le joueur
	 * @param rayon  Le rayon
	 * @return Une représentation des chunks aux alentours du joueur dans un rayon
	 *         spécifié
	 */
	private List<String> buildChunkList(Player player, Integer rayon) {
		World world = player.getWorld();
		Chunk center = player.getLocation().getChunk();
		List<String> resList = new ArrayList<>();
		String line = new String();

		for (int Z = -rayon; Z < rayon + 1; Z++) {
			for (int X = -rayon; X < rayon + 1; X++) {

				Chunk chunk = world.getChunkAt(center.getX() + X, center.getZ() + Z);
				String filler = getFiller(chunk, player);
				line += " " + filler;
			}

			line = "§-----§r" + line;
			resList.add(line);
			line = "";
		}

		return resList;
	}

	private String listToString(List<String> list) {
		String result = "";

		for (String l : list) {
			result += l + "\n";
		}

		return result;
	}

	/***
	 * Renvoie caractère pour direction du joueur
	 * 
	 * @param player le joueur à récupérer la direction
	 * @return caractère "fleche" qui indique la direction du joueur sur la map
	 */
	private String getDir(Player player) {
		float yaw = player.getLocation().getYaw();

		if (yaw < 0) {
			yaw += 360;
		}

		if (yaw >= 315 || yaw < 45) {
			return "\u25AA\u25BC"; // direction sud u25BC
		} else if (yaw < 135) {
			return "\u25AA\u25C4"; // direction ouest u25C4
		} else if (yaw < 225) {
			return "\u25AA\u25B2"; // direction nord u25B2
		} else if (yaw < 315) {
			return "\u25AA\u25BA"; // direction est u25BA
		}
		return "c";
	}

	/***
	 * Renvoie un motif pour le chunk ciblé.
	 * 
	 * @param chunk  le chunk cible
	 * @param player le joueur qui execute
	 * @return Le motif du chunk
	 */
	private String getFiller(Chunk chunk, Player player) {
		String color = "";
		if (isSlimeChunk(chunk)) {
			color = "§2";
		} else {
			color = "§7";
		}

		if (chunk.equals(player.getLocation().getChunk())) { // direction du joueur
			return color + getDir(player); // u229E || u2592 || u2630
		} else { // chunks
			return color + '\u2630';
		}
	}
}
