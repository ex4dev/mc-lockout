package dev.tswanson.lockout;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// TODO add permissions to subcommands
public class LockoutCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            printHelp(sender);
            return true;
        }

        if (args[0].equals("reset")) {
            // TODO end game here
            Lockout.getInstance().getTeamManager().clear();
            Lockout.getInstance().getBoardGenerator().resetBoard();
            Lockout.getInstance().getMenu().clear();
            return true;
        }

        if (args[0].equals("assign")) {
            TeamManager teamManager = Lockout.getInstance().getTeamManager();
            if (teamManager.getTeams().isEmpty()) teamManager.createTeams(3);
            teamManager.assignRandomTeams();
            return true;
        }

        if (args[0].equals("board")) {
            if (sender instanceof Player player) {
                Lockout.getInstance().getMenu().open(player);
            }

            return true;
        }

        if (args[0].equals("start")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Location loc = SpawnHelper.getRandomSpawnpoint(player.getWorld());
                player.setRespawnLocation(loc); // TODO this doesn't work ("home bed was missing/obstructed")
                player.teleport(loc);
            }
            Lockout.getInstance().startGame();
            return true;
        }

        printHelp(sender);
        return true;
    }



    private void printHelp(CommandSender sender) {
        sender.sendMessage("/lockout help : Displays this message.");
        sender.sendMessage("/lockout reset : Ends the game and resets the scoreboard.");
        sender.sendMessage("/lockout assign : Assigns all online players to a random team.");
        sender.sendMessage("/lockout board : Opens the board GUI.");
        sender.sendMessage("/lockout start : Starts the game.");
    }
}
