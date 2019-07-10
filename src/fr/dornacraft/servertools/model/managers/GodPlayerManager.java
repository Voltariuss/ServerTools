package fr.dornacraft.servertools.model.managers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.controller.commands.player.CmdGod;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class GodPlayerManager {

    private static GodPlayerManager instance = new GodPlayerManager();

    private HashMap<UUID, Player> godPlayers = new HashMap<>();

    private GodPlayerManager() {
    }

    public static GodPlayerManager getInstance() {
        return instance;
    }

    /**
     * @return La liste des joueurs en god mode, non null
     */
    private HashMap<UUID, Player> getGodPlayers() {
        return godPlayers;
    }

    /**
     * Supprime le joueur de la liste des joueurs en god mode.
     * 
     * @param player Le joueur ciblé, non null
     */
    public void removeGod(Player player) {
        getGodPlayers().remove(player.getUniqueId());
    }

    /**
     * Ajoute le joueur à la liste des joueurs en god mode.
     * 
     * @param player Le joueur ciblé, non null
     */
    public void addGod(Player player) {
        getGodPlayers().put(player.getUniqueId(), player);
    }

    /**
     * @param player Le joueur ciblé, non null
     * @return Vrai si le joueur est en god mode, faux sinon
     */
    public boolean isGod(Player player) {
        return getGodPlayers().containsKey(player.getUniqueId());
    }

    public static void setGod(CommandSender sender, Player player, Boolean isGod) {
        HashMap<String, String> values = new HashMap<>();
        values.put("Sender", sender.getName());

        String state = null;

        if (isGod) {
            state = ServerToolsConfig.getCommandMessage(CmdGod.CMD_LABEL, "normal_god_mode_state_enabled");
        } else {
            state = ServerToolsConfig.getCommandMessage(CmdGod.CMD_LABEL, "normal_god_mode_state_disabled");
        }
        values.put("State", state);

        String messageSenderId = null;
        MessageLevel messageSenderLevel = MessageLevel.INFO;

        if (getInstance().isGod(player) != isGod) {
            if (sender != player) {
                if (sender.hasPermission("dornacraft.essentials.god.other")) {
                    if (isGod) {
                        GodPlayerManager.getInstance().addGod(player);
                    } else {
                        GodPlayerManager.getInstance().removeGod(player);
                    }
                    values.put("Target", player.getName());
                    String messageTargetId = null;

                    if (sender instanceof ConsoleCommandSender) {
                        messageTargetId = "info_god_mode_activate";
                    } else {
                        messageTargetId = "info_god_mode_activate_by_other";
                    }
                    String messageTarget = ServerToolsConfig.getCommandMessage(CmdGod.CMD_LABEL, messageTargetId,
                            values);
                    UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, messageTarget);
                    messageSenderId = "info_god_mode_activate_target";
                } else {
                    UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PERMISSION_MISSING);
                }
            } else {
                if (isGod) {
                    GodPlayerManager.getInstance().addGod(player);
                } else {
                    GodPlayerManager.getInstance().removeGod(player);
                }
                messageSenderId = "info_god_mode_activate_by_himself";
            }
        } else {
            if (sender != player) {
                if (sender.hasPermission("dornacraft.essentials.god.other")) {
                    messageSenderLevel = MessageLevel.FAILURE;
                    messageSenderId = "failure_god_mode_already_in_this_state";
                } else {
                    UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
                }
            } else {
                messageSenderLevel = MessageLevel.FAILURE;
                messageSenderId = "failure_god_mode_already_in_this_state_himself";
            }
        }

        if (messageSenderId != null) {
            String messageSender = ServerToolsConfig.getCommandMessage(CmdGod.CMD_LABEL, messageSenderId, values);
            UtilsAPI.sendSystemMessage(messageSenderLevel, sender, messageSender);
        }
    }

    public static void toggleGodMode(CommandSender sender, Player player) {
        setGod(sender, player, !getInstance().isGod(player));
    }
}