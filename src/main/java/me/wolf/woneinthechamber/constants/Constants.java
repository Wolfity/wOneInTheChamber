package me.wolf.woneinthechamber.constants;

import me.wolf.woneinthechamber.utils.Utils;

public final class Constants {
    public static class Messages {
        public static final String RECRUITING = Utils.colorize(
                "&c=================================================\n" +
                        "&c= &6A game of OITC is now looking for players!   \n" +
                        "&c=      &6Join by doing /oitc join!               \n" +
                        "&c=================================================");

        public static final String TIMER_RAN_OUT = Utils.colorize(
                "&a==========================\n" +
                        "&c&lGame Over\n" +
                        "&aThe timer ran out! \n" +
                        "&a==========================");

        public static final String GAME_STARTED = Utils.colorize(
                "&a=============================================\n" +
                        "&aThe game has started\n" +
                        "The objective is to kill &2 {maxkills} players!\n" +
                        "&aGood luck!\n" +
                        "&a===========================================");

        public static final String COUNTDOWN_MESSAGE = Utils.colorize(
                "&bThe game will start in &3{countdown}&b seconds!");

        public static final String JOINED_WAITING_ROOM = Utils.colorize(
                "&aYou successfully joined the waiting room!\n" +
                        "The game will start once there are enough players");

        public static final String LEFT_WAITING_ROOM = Utils.colorize(
                "&aYou successfully left the waiting room!");

        public static final String ALREADY_INGAME = Utils.colorize(
                "&cFailed to join this game, you are already in-game!");

        public static final String NOT_INGAME = Utils.colorize(
                "&cFailed to leave the game, you are currently not in a game!");

        public static final String GAME_WON = Utils.colorize(
                "&a==========================================================\n" +
                        "&a=                    &6&lThe game has ended!\n" +
                        "&a=         &2{first} &a has won the game with {maxkills}\n" +
                        "&a=                 &6#1 {first} - {maxkills}     \n" +
                        "&a=                    &3#2 {second} - {secondkills} \n" +
                        "&a=                 &7#3 {third} - {thirdkills} \n" +
                        "&a==========================================================");

        public static final String GAME_IS_FULL = Utils.colorize(
                "&cFailed to join the game, the game is full!");

        public static final String HELP_MESSAGE = Utils.colorize(
                "&b======== OITC Help Message ========\n" +
                        "&b/oitc join &7- Join a game \n" +
                        "&b/oitc leave &7- Leave a game \n" +
                        "&b===================================");

        public static final String ADMIN_HELP_MESSAGE = Utils.colorize(
                "&b======== OITC &cAdmin &bHelp Message ========\n" +
                        "&b/oitc setspawnloc &7 - Set a (re)spawn location\n" +
                        "&b/oitc setworldspawn &7 - Set the world spawn\n" +
                        "&b/oitc setlobbyspawn &7 - Set the lobby spawn\n" +
                        "&b/oitc forcestart &7 - Force start a game\n" +
                        "&b/oitc forceend &7 - Force end a game\n" +
                        "&b=========================================");
        public static final String SPAWN_LOC_SET = Utils.colorize(
                "&aSuccessfully set a spawn location!");

        public static final String CANT_CREATE_SPAWN_POINT = Utils.colorize(
                "&cYou can not create spawn points outside of the arena coords!");

        public static final String LOBBY_SPAWN_SET = Utils.colorize(
                "&aSuccessfully set a lobby spawn!");

        public static final String WORLD_SPAWN_SET = Utils.colorize(
                "&aSuccessfully set a lobby spawn!");

        public static final String PLAYER_RESPAWNED = Utils.colorize(
                "&aYou respawned!");
    }


    private Constants() {

    }

}
