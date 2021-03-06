package fr.dornacraft.servertools;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.controller.commands.CmdBroadcast;
import fr.dornacraft.servertools.controller.commands.CmdHat;
import fr.dornacraft.servertools.controller.commands.CmdSuicide;
import fr.dornacraft.servertools.controller.commands.info.CmdGc;
import fr.dornacraft.servertools.controller.commands.info.CmdPing;
import fr.dornacraft.servertools.controller.commands.info.CmdSeen;
import fr.dornacraft.servertools.controller.commands.info.CmdWhois;
import fr.dornacraft.servertools.controller.commands.player.CmdDelhome;
import fr.dornacraft.servertools.controller.commands.player.CmdFeed;
import fr.dornacraft.servertools.controller.commands.player.CmdFly;
import fr.dornacraft.servertools.controller.commands.player.CmdGamemode;
import fr.dornacraft.servertools.controller.commands.player.CmdGod;
import fr.dornacraft.servertools.controller.commands.player.CmdHeal;
import fr.dornacraft.servertools.controller.commands.player.CmdSethome;
import fr.dornacraft.servertools.controller.commands.teleport.CmdBack;
import fr.dornacraft.servertools.controller.commands.teleport.CmdHome;
import fr.dornacraft.servertools.controller.commands.teleport.CmdSpawn;
import fr.dornacraft.servertools.controller.commands.teleport.CmdTpa;
import fr.dornacraft.servertools.controller.commands.teleport.CmdTpaall;
import fr.dornacraft.servertools.controller.commands.teleport.CmdTpaccept;
import fr.dornacraft.servertools.controller.commands.teleport.CmdTpahere;
import fr.dornacraft.servertools.controller.commands.teleport.CmdTpdeny;
import fr.dornacraft.servertools.controller.commands.utils.CmdAdminExp;
import fr.dornacraft.servertools.controller.commands.utils.CmdClear;
import fr.dornacraft.servertools.controller.commands.utils.CmdCraft;
import fr.dornacraft.servertools.controller.commands.utils.CmdEnderchest;
import fr.dornacraft.servertools.controller.commands.utils.CmdExp;
import fr.dornacraft.servertools.controller.commands.utils.CmdInvsee;
import fr.dornacraft.servertools.controller.commands.utils.CmdKillAll;
import fr.dornacraft.servertools.controller.commands.utils.CmdList;
import fr.dornacraft.servertools.controller.commands.utils.CmdMore;
import fr.dornacraft.servertools.controller.commands.utils.CmdMsg;
import fr.dornacraft.servertools.controller.commands.utils.CmdRepair;
import fr.dornacraft.servertools.controller.commands.utils.CmdReply;
import fr.dornacraft.servertools.controller.commands.utils.CmdSlime;
import fr.dornacraft.servertools.controller.commands.utils.CmdSpawner;
import fr.dornacraft.servertools.controller.listeners.BackListener;
import fr.dornacraft.servertools.controller.listeners.FlyingPlayerJoinListener;
import fr.dornacraft.servertools.controller.listeners.GodPlayerDamageListener;
import fr.dornacraft.servertools.controller.listeners.MobSpawnerPlaceListener;
import fr.dornacraft.servertools.controller.listeners.PlayerJoinListener;
import fr.dornacraft.servertools.controller.listeners.PlayerMoveListener;
import fr.dornacraft.servertools.controller.listeners.PlayerRespawnListener;
import fr.dornacraft.servertools.model.managers.GlobalManager;
import fr.dornacraft.servertools.model.managers.SQLManager;
import fr.dornacraft.servertools.model.utils.SpawnerCmdTabCompleter;
import fr.dornacraft.servertools.utils.Lag;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class ServerTools extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();
		SQLManager.checkDatabase();
		GlobalManager.setSpawnLocation(this);
		startSchedulers();
		activateListeners();
		activateCommands();
		UtilsAPI.sendActivationMessage(this.getClass(), true);

	}

	@Override
	public void onDisable() {
		UtilsAPI.sendActivationMessage(this.getClass(), false);
	}

	public void startSchedulers() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
	}

	public void activateListeners() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new BackListener(), this);
		Bukkit.getPluginManager().registerEvents(new GodPlayerDamageListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
		Bukkit.getPluginManager().registerEvents(new FlyingPlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new MobSpawnerPlaceListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), this);
	}

	public void activateCommands() {
		getCommand(CmdBroadcast.CMD_LABEL).setExecutor(new CmdBroadcast());
		getCommand(CmdHat.CMD_LABEL).setExecutor(new CmdHat());
		getCommand(CmdKillAll.CMD_LABEL).setExecutor(new CmdKillAll());
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
	}
}
