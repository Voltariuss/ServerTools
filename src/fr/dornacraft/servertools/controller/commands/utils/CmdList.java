package fr.dornacraft.servertools.controller.commands.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.model.managers.PlayerListManager;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdList extends DornacraftCommand {

	public static final String CMD_LABEL = "list";

	public CmdList() {
		super(CMD_LABEL);
		String cmdDesc = JavaPlugin.getPlugin(ServerTools.class).getCommand(CMD_LABEL).getDescription();
		DornacraftCommandExecutor dce = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				if (args.length == 0) {
					PlayerListManager.getInstance().displayPlayerList(sender);
				} else {
					int page = Integer.parseInt(args[0]);
					PlayerListManager.getInstance().displayPlayerList(sender, page);
				}
			}
		};

		getCmdTreeExecutor().getRoot().setExecutor(dce);
		getCmdTreeExecutor().addSubCommand(
				new CommandNode(new CommandArgument(CommandArgumentType.NUMBER.getCustomArgType("page"), false),
						cmdDesc, dce, null));

	}
}
