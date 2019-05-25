package fr.dornacraft.servertools.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Classe d'écoute des événements de connection d'un joueur volant
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class FlyingPlayerJoinListener implements Listener {

	@EventHandler
	public void onFlyingPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		// on vérifie d'abord si le joueur possède la permission de voler
		if (player.hasPermission("dornacraft.essentials.fly")) {
			// on vérifie si les trois blocs sous le joueur correspondent à des
			// blocs d'air
			Location firstBlocLoc = player.getLocation().subtract(0.0d, 1.0d, 0.0d);
			Location secondBlocLoc = player.getLocation().subtract(0.0d, 2.0d, 0.0d);
			Location thirdBlocLoc = player.getLocation().subtract(0.0d, 3.0d, 0.0d);

			if (player.getWorld().getBlockAt(firstBlocLoc).getType() == Material.AIR
					&& player.getWorld().getBlockAt(secondBlocLoc).getType() == Material.AIR
					&& player.getWorld().getBlockAt(thirdBlocLoc).getType() == Material.AIR) {
				// on considère alors que le joueur était en mode fly
				// et on le lui réactive
				player.setAllowFlight(true);
				player.setFlying(true);
			}
		}
	}
}
