package fr.dornacraft.servertools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.commands.CmdBroadcast;
import fr.dornacraft.servertools.commands.CmdHat;
import fr.dornacraft.servertools.commands.CmdKillAll;
import fr.dornacraft.servertools.commands.CmdSuicide;
import fr.dornacraft.servertools.commands.info.CmdGc;
import fr.dornacraft.servertools.commands.info.CmdPing;
import fr.dornacraft.servertools.commands.info.CmdSeen;
import fr.dornacraft.servertools.commands.info.CmdWhois;
import fr.dornacraft.servertools.commands.player.CmdDelhome;
import fr.dornacraft.servertools.commands.player.CmdFeed;
import fr.dornacraft.servertools.commands.player.CmdFly;
import fr.dornacraft.servertools.commands.player.CmdGamemode;
import fr.dornacraft.servertools.commands.player.CmdGod;
import fr.dornacraft.servertools.commands.player.CmdHeal;
import fr.dornacraft.servertools.commands.player.CmdSethome;
import fr.dornacraft.servertools.commands.teleport.CmdBack;
import fr.dornacraft.servertools.commands.teleport.CmdHome;
import fr.dornacraft.servertools.commands.teleport.CmdSpawn;
import fr.dornacraft.servertools.commands.teleport.CmdTpa;
import fr.dornacraft.servertools.commands.teleport.CmdTpaall;
import fr.dornacraft.servertools.commands.teleport.CmdTpaccept;
import fr.dornacraft.servertools.commands.teleport.CmdTpahere;
import fr.dornacraft.servertools.commands.teleport.CmdTpdeny;
import fr.dornacraft.servertools.commands.utils.CmdAdminExp;
import fr.dornacraft.servertools.commands.utils.CmdClear;
import fr.dornacraft.servertools.commands.utils.CmdCraft;
import fr.dornacraft.servertools.commands.utils.CmdEnderchest;
import fr.dornacraft.servertools.commands.utils.CmdExp;
import fr.dornacraft.servertools.commands.utils.CmdInvsee;
import fr.dornacraft.servertools.commands.utils.CmdList;
import fr.dornacraft.servertools.commands.utils.CmdMore;
import fr.dornacraft.servertools.commands.utils.CmdMsg;
import fr.dornacraft.servertools.commands.utils.CmdRepair;
import fr.dornacraft.servertools.commands.utils.CmdReply;
import fr.dornacraft.servertools.commands.utils.CmdSlime;
import fr.dornacraft.servertools.commands.utils.CmdSpawner;
import fr.dornacraft.servertools.commands.utils.SpawnerCmdTabCompleter;
import fr.dornacraft.servertools.listeners.BackListener;
import fr.dornacraft.servertools.listeners.FlyingPlayerJoinListener;
import fr.dornacraft.servertools.listeners.GodPlayerDamageListener;
import fr.dornacraft.servertools.listeners.PlayerMoveListener;
import fr.dornacraft.servertools.listeners.PlayerRespawnListener;
import fr.dornacraft.servertools.utils.Lag;
import fr.dornacraft.servertools.utils.MobSpawnerPlaceEvent;
import fr.dornacraft.servertools.utils.Utils;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class ServerTools extends JavaPlugin {

	private static JavaPlugin instance;

	public static JavaPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();

		World world = Bukkit.getWorld(getConfig().getString("spawn_location.world"));
		double x = getConfig().getDouble("spawn_location.x");
		double y = getConfig().getDouble("spawn_location.y");
		double z = getConfig().getDouble("spawn_location.z");
		float pitch = (float) getConfig().getDouble("spawn_location.pitch");
		float yaw = (float) getConfig().getDouble("spawn_location.yaw");
		Utils.SPAWN_LOCATION = new Location(world, x, y, z, pitch, yaw);

		Bukkit.getPluginManager().registerEvents(new BackListener(), this);
		Bukkit.getPluginManager().registerEvents(new GodPlayerDamageListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
		Bukkit.getPluginManager().registerEvents(new FlyingPlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new MobSpawnerPlaceEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), this);

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);

		getCommand(CmdBroadcast.CMD_LABEL).setExecutor(new CmdBroadcast());
		getCommand(CmdHat.CMD_LABEL).setExecutor(new CmdHat());
		getCommand(CmdKillAll.CMD_LABEL).setExecutor(new CmdKillAll());
		// TODO commande fonctionnelle mais non stable
		getCommand(CmdSuicide.CMD_LABEL).setExecutor(new CmdSuicide());

		getCommand(CmdGc.CMD_LABEL).setExecutor(new CmdGc());
		getCommand(CmdPing.CMD_LABEL).setExecutor(new CmdPing());
		getCommand(CmdSeen.CMD_LABEL).setExecutor(new CmdSeen());
		getCommand(CmdWhois.CMD_LABEL).setExecutor(new CmdWhois());

		getCommand(CmdDelhome.CMD_LABEL).setExecutor(new CmdDelhome());
		getCommand(CmdFeed.CMD_LABEL).setExecutor(new CmdFeed());
		getCommand(CmdFly.CMD_LABEL).setExecutor(new CmdFly());
		getCommand(CmdGamemode.CMD_LABEL).setExecutor(new CmdGamemode());
		getCommand(CmdGod.CMD_LABEL).setExecutor(new CmdGod());
		getCommand(CmdHeal.CMD_LABEL).setExecutor(new CmdHeal());
		getCommand(CmdSethome.CMD_LABEL).setExecutor(new CmdSethome());

		getCommand(CmdClear.CMD_LABEL).setExecutor(new CmdClear());
		getCommand(CmdAdminExp.CMD_LABEL).setExecutor(new CmdAdminExp());
		// TODO commande NE fonctionne PAS
		getCommand(CmdCraft.CMD_LABEL).setExecutor(new CmdCraft());
		getCommand(CmdEnderchest.CMD_LABEL).setExecutor(new CmdEnderchest());
		getCommand(CmdExp.CMD_LABEL).setExecutor(new CmdExp());
		getCommand(CmdInvsee.CMD_LABEL).setExecutor(new CmdInvsee());
		// TODO faire la commande
		getCommand(CmdMore.CMD_LABEL).setExecutor(new CmdMore());
		getCommand(CmdMsg.CMD_LABEL).setExecutor(new CmdMsg());
		getCommand(CmdRepair.CMD_LABEL).setExecutor(new CmdRepair());
		getCommand(CmdReply.CMD_LABEL).setExecutor(new CmdReply());

		getCommand(CmdSlime.CMD_LABEL).setExecutor(new CmdSlime());
		getCommand(CmdList.CMD_LABEL).setExecutor(new CmdList());
		getCommand(CmdSpawner.CMD_LABEL).setExecutor(new CmdSpawner());
		getCommand(CmdSpawner.CMD_LABEL).setTabCompleter(new SpawnerCmdTabCompleter());

		getCommand(CmdBack.CMD_LABEL).setExecutor(new CmdBack());
		getCommand(CmdHome.CMD_LABEL).setExecutor(new CmdHome());
		getCommand(CmdSpawn.CMD_LABEL).setExecutor(new CmdSpawn());
		getCommand(CmdSpawn.CMD_LABEL).setExecutor(new CmdSpawn());
		getCommand(CmdTpa.CMD_LABEL).setExecutor(new CmdTpa());
		getCommand(CmdTpahere.CMD_LABEL).setExecutor(new CmdTpahere());
		getCommand(CmdTpaall.CMD_LABEL).setExecutor(new CmdTpaall());
		getCommand(CmdTpaccept.CMD_LABEL).setExecutor(new CmdTpaccept());
		getCommand(CmdTpdeny.CMD_LABEL).setExecutor(new CmdTpdeny());

		UtilsAPI.sendActivationMessage(this.getClass(), true);
	}

	@Override
	public void onDisable() {
		UtilsAPI.sendActivationMessage(this.getClass(), false);
	}

}
