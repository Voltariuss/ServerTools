package fr.dornacraft.servertools.controller.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class MobSpawnerPlaceListener implements Listener {

	private static int delay = -1;
	private static EntityType entityType;

	public static void setDelay(int newDelay) {
		delay = newDelay;
	}

	public static void setEntityType(EntityType newEntityType) {
		entityType = newEntityType;
	}

	@EventHandler
	public void onMobSpawnerPlacedEvent(BlockPlaceEvent e) {
		Block block = e.getBlockPlaced();
		if (block.getType() == Material.MOB_SPAWNER) {
			this.setSpawner(block, entityType, delay);
		}
	}

	private void setSpawner(Block block, EntityType ent, int delay) {
		BlockState blockState = block.getState();
		CreatureSpawner spawner = (CreatureSpawner) blockState;
		if (ent != null)
			spawner.setSpawnedType(ent);
		if (delay != -1) {
			spawner.setDelay(delay);
		}
		blockState.update();
	}
}
