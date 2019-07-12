package fr.dornacraft.servertools.model.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.controller.commands.utils.CmdList;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class PlayerListManager {

    private static PlayerListManager instance = new PlayerListManager();

    private int numberPlayerPerPage;

    private PlayerListManager() {
        setNumberPlayerPerPage(
                JavaPlugin.getPlugin(ServerTools.class).getConfig().getInt("player_list.number_player_per_page"));
    }

    public static PlayerListManager getInstance() {
        return instance;
    }

    public void displayPlayerList(CommandSender sender, int page) {
        List<Player> playerList = getOnlinePlayers(sender);
        int numberPlayer = playerList.size();

        if (numberPlayer > 0) {
            int numberPage = (int) Math.ceil(numberPlayer / (double) getNumberPlayerPerPage());

            if (page > numberPage) {
                page = numberPage;
            } else if (page <= 0) {
                page = 1;
            }
            HashMap<String, String> values = new HashMap<>();
            values.put("Number_Online_Player", Integer.toString(numberPlayer));
            String pageIndicator = "";

            if (numberPage > 1) {
                HashMap<String, String> valuesPage = new HashMap<>();
                valuesPage.put("Current_Page_Number", Integer.toString(page));
                valuesPage.put("Total_Number_Page", Integer.toString(numberPage));
                pageIndicator = ServerToolsConfig.getCommandMessage(CmdList.CMD_LABEL, "normal_player_list_header_page",
                        valuesPage);
            }
            values.put("Page", pageIndicator);
            String header = ServerToolsConfig.getCommandMessage(CmdList.CMD_LABEL, "normal_player_list_header", values);
            UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, header);

            StringJoiner playerListJoiner = new StringJoiner("§7, §r");
            int startIndexPage = getNumberPlayerPerPage() * (page - 1);
            int startIndexNextPage = getNumberPlayerPerPage() * page;

            for (int i = startIndexPage; i < numberPlayer && i < startIndexNextPage; i++) {
                Player player = playerList.get(i);
                playerListJoiner.add(player.getName());
            }
            UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, playerListJoiner.toString());

            if (numberPage > page) {
                HashMap<String, String> valuesNextPage = new HashMap<>();
                valuesNextPage.put("Page", Integer.toString(page + 1));
                String messageInfoNextPage = ServerToolsConfig.getCommandMessage(CmdList.CMD_LABEL,
                        "info_player_list_next_page", valuesNextPage);
                UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, messageInfoNextPage);
            }
        } else {
            String messageNoPlayer = ServerToolsConfig.getCommandMessage(CmdList.CMD_LABEL, "warning_no_player_online");
            UtilsAPI.sendSystemMessage(MessageLevel.WARNING, sender, messageNoPlayer);
        }
    }

    public void displayPlayerList(CommandSender sender) {
        displayPlayerList(sender, 1);
    }

    public List<Player> getOnlinePlayers(CommandSender sender) {
        List<Player> playerList = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (sender == null || sender.hasPermission("dornacraft.essentials.vanish.see")
                    || ((Player) sender).canSee(player)) {
                playerList.add(player);
            }
        }
        return playerList;
    }

    public List<Player> getOnlinePlayers() {
        return getOnlinePlayers(null);
    }

    public int getNumberPlayerPerPage() {
        return numberPlayerPerPage;
    }

    public void setNumberPlayerPerPage(int numberPlayerPerPage) {
        this.numberPlayerPerPage = numberPlayerPerPage;
    }
}