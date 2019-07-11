package fr.dornacraft.servertools.model.utils;

import org.bukkit.GameMode;

import fr.dornacraft.servertools.controller.commands.player.CmdGamemode;
import fr.dornacraft.servertools.utils.ServerToolsConfig;

public enum GameModeType {

    SURVIVAL(GameMode.SURVIVAL, 0, "normal_gamemode_survival"),
    CREATIVE(GameMode.CREATIVE, 1, "normal_gamemode_creative"),
    ADVENTURE(GameMode.ADVENTURE, 2, "normal_gamemode_adventure"),
    SPECTATOR(GameMode.SPECTATOR, 3, "normal_gamemode_spectator");

    private GameMode gameMode;
    private int index;
    private String name;

    private GameModeType(GameMode gameMode, int index, String nameId) {
        setGameMode(gameMode);
        setIndex(index);
        setName(ServerToolsConfig.getCommandMessage(CmdGamemode.CMD_LABEL, nameId));
    }

    public static GameModeType getFromIndex(int index) {
        GameModeType gameModeType = null;

        for (GameModeType gmt : values()) {
            if (gmt.getIndex() == index) {
                gameModeType = gmt;
            }
        }
        return gameModeType;
    }

    public static GameModeType getFromGameMode(GameMode gameMode) {
        GameModeType gameModeType = null;

        for (GameModeType gmt : values()) {
            if (gmt.getGameMode() == gameMode) {
                gameModeType = gmt;
            }
        }
        return gameModeType;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    private void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public int getIndex() {
        return index;
    }

    private void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}