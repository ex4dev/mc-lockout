package dev.tswanson.lockout;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            if (Lockout.getInstance().isIngame() && (args.length < 2 || !args[1].equals("-f"))) {
                sender.sendMessage(ChatColor.RED + "A game is currently active! Use -f to reset anyway.");
                return true;
            }
            Lockout.getInstance().getTeamManager().clear();
            Lockout.getInstance().getBoardGenerator().resetBoard();
            Lockout.getInstance().getMenu().clear();
            return true;
        }

        if (args[0].equals("assign")) {
            if (Lockout.getInstance().isIngame() && (args.length < 2 || !args[1].equals("-f"))) {
                sender.sendMessage(ChatColor.RED + "The game has already started! Use -f to reassign teams anyway.");
                return true;
            }
            TeamManager teamManager = Lockout.getInstance().getTeamManager();
            if (teamManager.getTeams().isEmpty()) teamManager.createTeams(2);
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
            if (Lockout.getInstance().isIngame() && (args.length < 2 || !args[1].equals("-f"))) {
                sender.sendMessage(ChatColor.RED + "The game has already started! Use -f to start anyway.");
                return true;
            }
            sender.sendMessage("Starting game");
            Lockout.getInstance().startGame();
            for (Player player : Bukkit.getOnlinePlayers()) {
                Location loc = SpawnHelper.getRandomSpawnpoint(player.getWorld());
                player.getInventory().clear();
                player.setHealth(player.getMaxHealth());
                player.setSaturation(20.0f);
                player.setFoodLevel(20);
                player.setRespawnLocation(loc); // TODO this doesn't work ("home bed was missing/obstructed")
                player.teleport(loc);
            }
            sender.sendMessage("Done. Teams: " + String.join(", ", Lockout.getInstance().getTeamManager().getTeams().toString()));
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
