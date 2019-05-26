package fr.dornacraft.servertools.commands.info;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdPing extends DornacraftCommand {

	public static final String CMD_LABEL = "ping";
	private static final String CMD_DESC = ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_desc");

	public CmdPing() {
		super(CMD_LABEL);
		DornacraftCommandExecutor executor = (new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				// - /ping [player]
				if (args.length == 0
						|| sender instanceof Player && args[0].equalsIgnoreCase(((Player) sender).getName())) {
					if (sender instanceof Player) {
						String ping = Integer.toString(getPing((Player) sender));

						HashMap<String, String> values = new HashMap<>();
						values.put("Ping", ping);

						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
								ServerToolsConfig.getCommandMessage(CMD_LABEL, "info_ping_himself", values));
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, UtilsAPI.PLAYER_OFFLINE);
					}
				} else {
					Player target = Bukkit.getPlayer(args[0]);

					if (target != null) {
						String ping = Integer.toString(getPing(target));

						HashMap<String, String> values = new HashMap<>();
						values.put("Ping", ping);
						values.put("Player", target.getDisplayName());

						UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender,
								ServerToolsConfig.getCommandMessage(CMD_LABEL, "info_ping_target", values));
					} else {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_UNKNOW);
					}
				}
			}
		});

		getCmdTreeExecutor().getRoot().setExecutor(executor);
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, false), CMD_DESC, executor, null));
	}

	private int getPing(Player player) {
		return ((CraftPlayer) player).getHandle().ping;
	}
}
