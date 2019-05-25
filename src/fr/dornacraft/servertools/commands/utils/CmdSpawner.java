package fr.dornacraft.servertools.commands.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftCreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdSpawner extends DornacraftCommand {

	public static final String CMD_LABEL = "spawner";

	public static final String DESC_TYPE = "Le type de mob à faire apparaître.";

	public CmdSpawner() {
		super(CMD_LABEL);
		DornacraftCommandExecutor dce = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				try {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						CraftCreatureSpawner spawner = getTarget(player);

						if (spawner != null) {
							if (args.length == 1 && !args[0].equalsIgnoreCase("help")) {
								EntityType entity = EntityType.valueOf(args[0].toUpperCase());

								if (entity != null && entity.isAlive() && entity != EntityType.PLAYER) {
									// on check si l'entity est un mob vivant et n'est pas un joueur
									if (!entity.equals(spawner.getSpawnedType())) {
										// on check si le spawner n'est pas déjà du type voulu
										UtilsAPI.sendSystemMessage(MessageLevel.SUCCESS, sender,
												"Vous avez changé le type de monstres du spawner par %s.", args[0]);
										spawner.setSpawnedType(entity);
										spawner.update();
									} else {
										UtilsAPI.sendSystemMessage(MessageLevel.SUCCESS, sender,
												"Le spawner est déjà un spawner à %s.", args[0]);
									}
								} else {
									UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender,
											"L'entité %s n'est pas supportée.", args[0]);
								}
							}
						} else {
							UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender,
									"Vous devez cibler un spawner pour effectuer cette commande.");
						}
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.CONSOLE_NOT_ALLOWED);
					}
				} catch (IllegalArgumentException e) {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, "'%s' n'existe pas.", args[0]);
				}
			}
		};

		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.STRING.getCustomArgType("mob_type"), true),
						DESC_TYPE, dce, null));
	}

	/**
	 * récupère le spawner que regarde le joueur en paramètre, peut retourner null
	 * si le joueur ne regarde rien.
	 * 
	 * @param player Le joueur ciblé, non null
	 * 
	 * @return Le spawner que regarde le joueur, null si le joueur ne regarde pas de
	 *         spawner
	 * 
	 */
	private CraftCreatureSpawner getTarget(Player player) {
		BlockIterator bi = new BlockIterator(player, 6);
		CraftCreatureSpawner result = null;

		while (result == null && bi.hasNext()) {
			Block lastBlock = bi.next();

			if (lastBlock.getType() == Material.MOB_SPAWNER) {
				result = (CraftCreatureSpawner) lastBlock.getState();
			}
		}
		return result;
	}

}
