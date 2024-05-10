package dev.tswanson.lockout;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengeBoardMenu implements Listener {

    private final Inventory gui = Bukkit.getServer().createInventory(null, 54, "Challenges");
    private final Map<Player, Challenge> trackedChallenges = new HashMap<>();

    public ChallengeBoardMenu() {
        Bukkit.getScheduler().runTaskTimer(Lockout.getInstance(), () -> {
            this.update();

            if (Lockout.getInstance().isIngame()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Challenge tracked = trackedChallenges.get(player);
                    if (tracked == null) {
                        continue;
                    }
                    if (Lockout.getInstance().getTeamManager().isCompleted(tracked)) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder().append("Tracked Challenge: " + tracked.getName() + " COMPLETE").build());
                        trackedChallenges.remove(player);
                    } else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder().append("Tracked Challenge: " + tracked.getName()).build());
                    }
                }
            }
        }, 0L, 20L);
    }

    private final Map<ChatColor, Material> teamColorToBlock = Map.of(
            ChatColor.RED, Material.RED_CONCRETE,
            ChatColor.BLUE, Material.BLUE_CONCRETE,
            ChatColor.GREEN, Material.GREEN_CONCRETE,
            ChatColor.YELLOW, Material.YELLOW_CONCRETE,
            ChatColor.LIGHT_PURPLE, Material.MAGENTA_CONCRETE,
            ChatColor.WHITE, Material.WHITE_CONCRETE
    );

    public void update() {

        Challenge[][] board = Lockout.getInstance().getBoardGenerator().getBoard();

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                Challenge challenge = board[x][y];
                Material material = challenge.getIcon().getMaterial();
                List<String> lore = List.of();

                Team completedBy = Lockout.getInstance().getTeamManager().getCompletingTeam(challenge);
                if (completedBy != null) {
                    material = teamColorToBlock.get(completedBy.getColor());
                    lore = List.of(ChatColor.RESET + "" + ChatColor.YELLOW + "Completed by " + completedBy.getDisplayName());

                }

                ItemStack icon = new ItemStack(material);
                ItemMeta meta = icon.getItemMeta();
                meta.setDisplayName(ChatColor.RESET + challenge.getName());
                meta.setLore(lore);
                if (completedBy != null) {
                    meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                icon.setItemMeta(meta);
                this.gui.setItem(y * 9 + x + 2, icon);
            }
        }
    }

    public void open(Player player) {
        player.openInventory(gui);
    }

    public void clear() {
        new ArrayList<>(this.gui.getViewers()).forEach(HumanEntity::closeInventory);
        this.update();
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (this.gui.equals(event.getClickedInventory())) {
            event.setCancelled(true);

            Challenge[][] board = Lockout.getInstance().getBoardGenerator().getBoard();

            int index = event.getSlot();
            int x = (index - 2) % 9;
            int y = (index - 2) / 9;

            if (event.getWhoClicked() instanceof Player player) {
                trackedChallenges.put(player, board[x][y]);
            }
        }
    }
}
