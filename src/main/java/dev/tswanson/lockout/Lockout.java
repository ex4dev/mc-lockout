package dev.tswanson.lockout;

import dev.tswanson.lockout.challenge.*;
import dev.tswanson.lockout.challenge.item.ObtainItemsChallenge;
import dev.tswanson.lockout.challenge.mob.BreedMobChallenge;
import dev.tswanson.lockout.challenge.mob.KillColoredSheepChallenge;
import dev.tswanson.lockout.challenge.mob.TameMobChallenge;
import dev.tswanson.lockout.challenge.opponent.*;
import dev.tswanson.lockout.gui.StaticIcon;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;

// TODO implement compass
// TODO implement random spawning in radius around center

public final class Lockout extends JavaPlugin {
    private static Lockout INSTANCE;

    public static Lockout getInstance() {
        return INSTANCE;
    }

    public Lockout() {
        super();
        INSTANCE = this;
    }

    private boolean ingame;

    public void startGame() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
            p.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "GO!", null, 0, 40, 30);
        }
        this.ingame = true;
        this.compassHandler.initialize();
    }

    private TeamManager teamManager;
    private BoardGenerator boardGenerator = new BoardGenerator();
    private ChallengeBoardMenu menu;
    private CompassHandler compassHandler;

    public void endGame() {
        this.ingame = false;
        this.compassHandler.stop();
    }

    public boolean isIngame() { return ingame; }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public BoardGenerator getBoardGenerator() {
        return boardGenerator;
    }

    public ChallengeBoardMenu getMenu() {
        return menu;
    }

    public void setMenu(ChallengeBoardMenu menu) {
        this.menu = menu;
    }

    private Collection<Challenge> challenges = List.of(
            new MineBlockChallenge(StaticIcon.of(Material.DIAMOND_ORE), "Mine diamond ore"),
            new MineBlockChallenge(StaticIcon.of(Material.EMERALD_ORE), "Mine emerald ore"),
            new MineBlockChallenge(StaticIcon.of(Material.SPAWNER), "Mine mob spawner"),
            new MineBlockChallenge(StaticIcon.of(Material.TURTLE_EGG), "Mine turtle egg"),

            new EnterDimensionChallenge(StaticIcon.of(Material.NETHERRACK), "Enter the nether", World.Environment.NETHER),
            new EnterDimensionChallenge(StaticIcon.of(Material.END_PORTAL_FRAME), "Enter the end", World.Environment.THE_END),

            new KillMobChallenge(StaticIcon.of(Material.DRAGON_EGG), "Kill the Ender Dragon", EntityType.ENDER_DRAGON),
            new KillMobChallenge(StaticIcon.of(Material.WITCH_SPAWN_EGG), "Kill a Witch", EntityType.WITCH),
            new KillMobChallenge(StaticIcon.of(Material.ZOMBIE_VILLAGER_SPAWN_EGG), "Kill a Zombie Villager", EntityType.ZOMBIE_VILLAGER),
            new KillMobChallenge(StaticIcon.of(Material.STRAY_SPAWN_EGG), "Kill a Stray", EntityType.STRAY),
            new KillMobChallenge(StaticIcon.of(Material.ZOGLIN_SPAWN_EGG), "Kill a Zoglin", EntityType.ZOGLIN),
            new KillMobChallenge(StaticIcon.of(Material.SILVERFISH_SPAWN_EGG), "Kill a Silverfish", EntityType.SILVERFISH),
            new KillMobChallenge(StaticIcon.of(Material.GUARDIAN_SPAWN_EGG), "Kill a Guardian", EntityType.GUARDIAN),
            new KillMobChallenge(StaticIcon.of(Material.GHAST_SPAWN_EGG), "Kill a Ghast", EntityType.GHAST),
            new KillMobChallenge(StaticIcon.of(Material.SNOW_GOLEM_SPAWN_EGG), "Kill a Snow Golem", EntityType.SNOWMAN),
            new KillMobChallenge(StaticIcon.of(Material.ELDER_GUARDIAN_SPAWN_EGG), "Kill an Elder Guardian", EntityType.ELDER_GUARDIAN),
            new KillMobChallenge(StaticIcon.of(Material.ENDERMITE_SPAWN_EGG), "Kill an Endermite", EntityType.ENDERMITE),
            new KillColoredSheepChallenge(),

            new ObtainItemsChallenge(StaticIcon.of(Material.WOODEN_AXE), "Obtain all wooden tools", Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_SWORD),
            new ObtainItemsChallenge(StaticIcon.of(Material.STONE_AXE), "Obtain all stone tools", Material.STONE_AXE, Material.STONE_HOE, Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_SWORD),
            new ObtainItemsChallenge(StaticIcon.of(Material.GOLDEN_AXE), "Obtain all golden tools", Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_SWORD),
            new ObtainItemsChallenge(StaticIcon.of(Material.IRON_AXE), "Obtain all iron tools", Material.IRON_AXE, Material.IRON_HOE, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_SWORD),
            new ObtainItemsChallenge(StaticIcon.of(Material.DIAMOND_AXE), "Obtain all diamond tools", Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_SWORD),

            new ObtainItemsChallenge(StaticIcon.of(Material.NETHERITE_SHOVEL), "Obtain every type of shovel", Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL),
            new ObtainItemsChallenge(StaticIcon.of(Material.NETHERITE_HOE), "Obtain every type of hoe", Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE),
            new ObtainItemsChallenge(StaticIcon.of(Material.NETHERITE_AXE), "Obtain every type of axe", Material.DIAMOND_AXE, Material.DIAMOND_AXE, Material.DIAMOND_AXE, Material.DIAMOND_AXE, Material.DIAMOND_AXE),
            new ObtainItemsChallenge(StaticIcon.of(Material.NETHERITE_PICKAXE), "Obtain every type of pickaxe", Material.DIAMOND_PICKAXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_PICKAXE),
            new ObtainItemsChallenge(StaticIcon.of(Material.NETHERITE_SWORD), "Obtain every type of sword", Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD),

            new ObtainItemsChallenge(StaticIcon.of(Material.LEATHER_HELMET), "Obtain a full leather armor set", Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_HELMET),
            new ObtainItemsChallenge(StaticIcon.of(Material.GOLDEN_HELMET), "Obtain a full gold armor set", Material.GOLDEN_BOOTS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_HELMET),
            new ObtainItemsChallenge(StaticIcon.of(Material.IRON_HELMET), "Obtain a full iron armor set", Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_HELMET),
            new ObtainItemsChallenge(StaticIcon.of(Material.DIAMOND_HELMET), "Obtain a full diamond armor set", Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_HELMET),

            new ObtainItemsChallenge(StaticIcon.of(Material.NETHERITE_INGOT), "Obtain a piece of netherite armor", ObtainItemsChallenge.GoalType.ANY, Material.NETHERITE_BOOTS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_HELMET),
            new ObtainItemsChallenge(StaticIcon.of(Material.CHAIN), "Obtain a piece of chain armor", ObtainItemsChallenge.GoalType.ANY, Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_HELMET),

            new TameMobChallenge(StaticIcon.of(Material.CAT_SPAWN_EGG), "Tame a cat", EntityType.CAT),
            new TameMobChallenge(StaticIcon.of(Material.HORSE_SPAWN_EGG), "Tame a horse", EntityType.HORSE),
            new TameMobChallenge(StaticIcon.of(Material.PARROT_SPAWN_EGG), "Tame a parrot", EntityType.PARROT),
            new TameMobChallenge(StaticIcon.of(Material.WOLF_SPAWN_EGG), "Tame a wolf", EntityType.WOLF),

            new ObtainItemsChallenge(StaticIcon.of(Material.RED_NETHER_BRICK_STAIRS), "Obtain red nether brick stairs", Material.RED_NETHER_BRICK_STAIRS),
            new ObtainItemsChallenge(StaticIcon.of(Material.TROPICAL_FISH_BUCKET), "Obtain a bucket of tropical fish", Material.TROPICAL_FISH_BUCKET),
            new ObtainItemsChallenge(StaticIcon.of(Material.SEA_LANTERN), "Obtain a sea lantern", Material.SEA_LANTERN),
            new ObtainItemsChallenge(StaticIcon.of(Material.BOOKSHELF), "Obtain a bookshelf", Material.BOOKSHELF),
            new ObtainItemsChallenge(StaticIcon.of(Material.MOSSY_STONE_BRICK_WALL), "Obtain a mossy stone brick wall", Material.MOSSY_STONE_BRICK_WALL),
            new ObtainItemsChallenge(StaticIcon.of(Material.FLOWERING_AZALEA), "Obtain a flowering azalea", Material.FLOWERING_AZALEA),
            new ObtainItemsChallenge(StaticIcon.of(Material.SCAFFOLDING), "Obtain a scaffolding block", Material.SCAFFOLDING),
            new ObtainItemsChallenge(StaticIcon.of(Material.END_CRYSTAL), "Obtain an endcrystal", Material.END_CRYSTAL),
            new ObtainItemsChallenge(StaticIcon.of(Material.BELL), "Obtain a bell", Material.BELL),
            new ObtainItemsChallenge(StaticIcon.of(Material.EXPERIENCE_BOTTLE), "Obtain a bottle o' enchanting", Material.EXPERIENCE_BOTTLE),
            new ObtainItemsChallenge(StaticIcon.of(Material.SLIME_BLOCK), "Obtain a slime block", Material.SLIME_BLOCK),
            new ObtainItemsChallenge(StaticIcon.of(Material.POWDER_SNOW_BUCKET), "Obtain a powder snow bucket", Material.POWDER_SNOW_BUCKET),
            new ObtainItemsChallenge(StaticIcon.of(Material.SOUL_LANTERN), "Obtain a soul lantern", Material.SOUL_LANTERN),
            new ObtainItemsChallenge(StaticIcon.of(Material.HONEY_BOTTLE), "Obtain a honey bottle", Material.HONEY_BOTTLE),
            new ObtainItemsChallenge(StaticIcon.of(Material.ANCIENT_DEBRIS), "Obtain ancient debris", Material.ANCIENT_DEBRIS),
            new ObtainItemsChallenge(StaticIcon.of(Material.CAKE), "Obtain cake", Material.CAKE),
            new ObtainItemsChallenge(StaticIcon.of(Material.ENDER_CHEST), "Obtain an ender chest", Material.ENDER_CHEST),
            new ObtainItemsChallenge(StaticIcon.of(Material.HEART_OF_THE_SEA), "Obtain a heart of the sea", Material.HEART_OF_THE_SEA),
            new ObtainItemsChallenge(StaticIcon.of(Material.WITHER_SKELETON_SKULL), "Obtain a wither skeleton skull", Material.WITHER_SKELETON_SKULL),
            new ObtainItemsChallenge(StaticIcon.of(Material.LODESTONE), "Obtain a lodestone", Material.LODESTONE),
            new ObtainItemsChallenge(StaticIcon.of(Material.END_ROD), "Obtain an end rod", Material.END_ROD),
            new ObtainItemsChallenge(StaticIcon.of(Material.SPONGE), "Obtain a sponge", Material.SPONGE),
            new ObtainItemsChallenge(StaticIcon.of(Material.MUSHROOM_STEM), "Obtain a mushroom stem", Material.MUSHROOM_STEM),
            new ObtainItemsChallenge(StaticIcon.of(Material.DRAGON_EGG), "Obtain a dragon_egg", Material.DRAGON_EGG),
            new ObtainItemsChallenge(StaticIcon.of(Material.TNT), "Obtain TNT", Material.TNT),
            new ObtainItemsChallenge(StaticIcon.of(Material.COBWEB), "Obtain a cobweb", Material.COBWEB),
            new ObtainItemsChallenge(StaticIcon.of(Material.GOAT_HORN), "Obtain a goat horn", Material.GOAT_HORN),
            new ObtainItemsChallenge(StaticIcon.of(Material.MUD_BRICK_WALL), "Obtain a mud brick wall", Material.MUD_BRICK_WALL),
            new ObtainItemsChallenge(StaticIcon.of(Material.DAYLIGHT_DETECTOR), "Obtain a daylight sensor", Material.DAYLIGHT_DETECTOR),
            new ObtainItemsChallenge(StaticIcon.of(Material.REPEATER), "Obtain a redstone repeater", Material.REPEATER),
            new ObtainItemsChallenge(StaticIcon.of(Material.COMPARATOR), "Obtain a comparator", Material.COMPARATOR),
            new ObtainItemsChallenge(StaticIcon.of(Material.OBSERVER), "Obtain an observer", Material.OBSERVER),
            new ObtainItemsChallenge(StaticIcon.of(Material.ACTIVATOR_RAIL), "Obtain an activator rail", Material.ACTIVATOR_RAIL),
            new ObtainItemsChallenge(StaticIcon.of(Material.DETECTOR_RAIL), "Obtain a detector rail", Material.DETECTOR_RAIL),
            new ObtainItemsChallenge(StaticIcon.of(Material.POWERED_RAIL), "Obtain a powered rail", Material.POWERED_RAIL),
            new ObtainItemsChallenge(StaticIcon.of(Material.DISPENSER), "Obtain a dispenser", Material.DISPENSER),
            new ObtainItemsChallenge(StaticIcon.of(Material.PISTON), "Obtain a piston", Material.PISTON),
            new ObtainItemsChallenge(StaticIcon.of(Material.RAW_IRON), "Obtain every type of raw ore", Material.RAW_COPPER, Material.RAW_GOLD, Material.RAW_IRON),
            new ObtainItemsChallenge(StaticIcon.of(Material.CHERRY_SAPLING), "Obtain every type of sapling", Material.ACACIA_SAPLING, Material.CHERRY_SAPLING, Material.BIRCH_SAPLING, Material.OAK_SAPLING, Material.SPRUCE_SAPLING, Material.DARK_OAK_SAPLING, Material.JUNGLE_SAPLING, Material.OAK_SAPLING),
            new ObtainItemsChallenge(StaticIcon.of(Material.DIAMOND_HORSE_ARMOR), "Obtain every type horse armor", Material.DIAMOND_HORSE_ARMOR, Material.GOLDEN_HORSE_ARMOR, Material.IRON_HORSE_ARMOR, Material.LEATHER_HORSE_ARMOR),
            new ObtainItemsChallenge(StaticIcon.of(Material.WHEAT_SEEDS), "Obtain every type of seeds", Material.BEETROOT_SEEDS, Material.MELON_SEEDS, Material.PUMPKIN_SEEDS, Material.WHEAT_SEEDS /* excluding Torchflower seeds  */),
            new ObtainItemsChallenge(StaticIcon.of(Material.BLACK_GLAZED_TERRACOTTA), "Obtain glazed terracotta",
                    ObtainItemsChallenge.GoalType.ANY,
                    Material.WHITE_GLAZED_TERRACOTTA,
                    Material.ORANGE_GLAZED_TERRACOTTA,
                    Material.MAGENTA_GLAZED_TERRACOTTA,
                    Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
                    Material.YELLOW_GLAZED_TERRACOTTA,
                    Material.LIME_GLAZED_TERRACOTTA,
                    Material.PINK_GLAZED_TERRACOTTA,
                    Material.GRAY_GLAZED_TERRACOTTA,
                    Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
                    Material.CYAN_GLAZED_TERRACOTTA,
                    Material.PURPLE_GLAZED_TERRACOTTA,
                    Material.BLUE_GLAZED_TERRACOTTA,
                    Material.BROWN_GLAZED_TERRACOTTA,
                    Material.GREEN_GLAZED_TERRACOTTA,
                    Material.RED_GLAZED_TERRACOTTA,
                    Material.BLACK_GLAZED_TERRACOTTA
                    ),
//            new ObtainItemsChallenge(StaticIcon.of(Material.6_UNIQUE_FLOWERS), "Obtain 6_unique_flowers", Material.6_UNIQUE_FLOWERS),
//            new ObtainItemsChallenge(StaticIcon.of(Material.64_OF_ONE_ITEM_BLOCK), "Obtain 64_of_one_item_block", Material.64_OF_ONE_ITEM_BLOCK),
//            new ObtainItemsChallenge(StaticIcon.of(Material.64_X_WOOL), "Obtain 64_x_wool", Material.64_X_WOOL),
//            new ObtainItemsChallenge(StaticIcon.of(Material.64_X_CONCRETE), "Obtain 64_x_concrete", Material.64_X_CONCRETE),

            new BreedMobChallenge(StaticIcon.of(Material.COW_SPAWN_EGG), "Breed cows", EntityType.COW),
            new BreedMobChallenge(StaticIcon.of(Material.SHEEP_SPAWN_EGG), "Breed sheep", EntityType.SHEEP),
            new BreedMobChallenge(StaticIcon.of(Material.CHICKEN_SPAWN_EGG), "Breed chickens", EntityType.CHICKEN),
            new BreedMobChallenge(StaticIcon.of(Material.PIG_SPAWN_EGG), "Breed pigs", EntityType.PIG),
            new BreedMobChallenge(StaticIcon.of(Material.HORSE_SPAWN_EGG), "Breed horses", EntityType.HORSE),
            new BreedMobChallenge(StaticIcon.of(Material.HOGLIN_SPAWN_EGG), "Breed hoglins", EntityType.HOGLIN),
            new BreedMobChallenge(StaticIcon.of(Material.OCELOT_SPAWN_EGG), "Breed ocelots", EntityType.OCELOT),
            new BreedMobChallenge(StaticIcon.of(Material.RABBIT_SPAWN_EGG), "Breed rabbits", EntityType.RABBIT),
            new BreedMobChallenge(StaticIcon.of(Material.FOX_SPAWN_EGG), "Breed foxes", EntityType.FOX),
            new BreedMobChallenge(StaticIcon.of(Material.STRIDER_SPAWN_EGG), "Breed striders", EntityType.STRIDER),
            new BreedMobChallenge(StaticIcon.of(Material.GOAT_SPAWN_EGG), "Breed goats", EntityType.GOAT),

            new GainEffectChallenge(StaticIcon.of(Material.POTION), "Gain nausea", PotionEffectType.CONFUSION),
            new GainEffectChallenge(StaticIcon.of(Material.POTION), "Gain jump boost", PotionEffectType.JUMP),
            new GainEffectChallenge(StaticIcon.of(Material.POTION), "Gain absorption", PotionEffectType.ABSORPTION),
            new GainEffectChallenge(StaticIcon.of(Material.POTION), "Gain levitation", PotionEffectType.LEVITATION),
            new GainEffectChallenge(StaticIcon.of(Material.POTION), "Gain glowing", PotionEffectType.GLOWING),
            new GainEffectChallenge(StaticIcon.of(Material.POTION), "Gain mining fatigue", PotionEffectType.SLOW_DIGGING),
            new GainEffectChallenge(StaticIcon.of(Material.POTION), "Gain bad omen", PotionEffectType.BAD_OMEN),
            new GainEffectChallenge(StaticIcon.of(Material.POTION), "Gain weakness", PotionEffectType.WEAKNESS),
            new GainEffectChallenge(StaticIcon.of(Material.POTION), "Gain poison", PotionEffectType.POISON),

            new EnterVehicleChallenge(StaticIcon.of(Material.MINECART), "Enter a minecart", EntityType.MINECART),
            new EnterVehicleChallenge(StaticIcon.of(Material.STRIDER_SPAWN_EGG), "Ride a strider", EntityType.STRIDER),
            new EnterVehicleChallenge(StaticIcon.of(Material.PIG_SPAWN_EGG), "Ride a pig with a saddle", EntityType.PIG),

            new ConsumeItemChallenge(StaticIcon.of(Material.PUMPKIN_PIE), "Eat a pumpkin pie", Material.PUMPKIN_PIE),
            new ConsumeItemChallenge(StaticIcon.of(Material.GLOW_BERRIES), "Eat a glow berry", Material.GLOW_BERRIES),
            new ConsumeItemChallenge(StaticIcon.of(Material.RABBIT_STEW), "Eat rabbit stew", Material.RABBIT_STEW),
            new ConsumeItemChallenge(StaticIcon.of(Material.SUSPICIOUS_STEW), "Eat suspicious stew", Material.SUSPICIOUS_STEW),
            new ConsumeItemChallenge(StaticIcon.of(Material.COOKIE), "Eat a cookie", Material.COOKIE),
            new ConsumeItemChallenge(StaticIcon.of(Material.CHORUS_FRUIT), "Eat a chorus fruit", Material.CHORUS_FRUIT),
            new ConsumeItemChallenge(StaticIcon.of(Material.POISONOUS_POTATO), "Eat a poisonous potato", Material.POISONOUS_POTATO),

            new EnterBiomeChallenge(StaticIcon.of(Material.PACKED_ICE), "Enter an ice spikes biome", Biome.ICE_SPIKES),
            new EnterBiomeChallenge(StaticIcon.of(Material.BROWN_MUSHROOM), "Enter a mushroom fields biome", Biome.MUSHROOM_FIELDS),
            new EnterBiomeChallenge(StaticIcon.of(Material.ORANGE_TERRACOTTA), "Enter a badlands biome", Biome.BADLANDS),
            new EnterBiomeChallenge(StaticIcon.of(Material.BAMBOO), "Enter a bamboo jungle", Biome.BAMBOO_JUNGLE),
            new EnterBiomeChallenge(StaticIcon.of(Material.CHERRY_FENCE_GATE), "Enter a cherry grove", Biome.CHERRY_GROVE),

            new DieChallenge(StaticIcon.of(Material.LAVA_BUCKET), "Die in lava", DamageType.LAVA),
            new DieChallenge(StaticIcon.of(Material.BUCKET), "Die by suffocating", DamageType.IN_WALL),
            new DieChallenge(StaticIcon.of(Material.BEEHIVE), "Die by bee sting", DamageType.STING),
            new DieChallenge(StaticIcon.of(Material.CACTUS), "Die to a cactus", DamageType.CACTUS),
            new DieChallenge(StaticIcon.of(Material.SWEET_BERRIES), "Die to a sweet berry bush", DamageType.SWEET_BERRY_BUSH),
            new DieChallenge(StaticIcon.of(Material.ANVIL), "Die by anvil", DamageType.FALLING_ANVIL),
            new DieChallenge(StaticIcon.of(Material.RED_BED), "Die to intentional game design", DamageType.BAD_RESPAWN_POINT),

            new KillEnemyPlayerChallenge(StaticIcon.of(Material.NETHERITE_SWORD), "Kill an enemy player"),

            new OpponentDamageChallenge(StaticIcon.of(Material.FEATHER), "Be the last team to take fall damage", EntityDamageEvent.DamageCause.FALL),
            new OpponentMineChallenge(StaticIcon.of(Material.DIRT), "Be the last team to break a dirt block", Material.DIRT),
            new OpponentMineChallenge(StaticIcon.of(Material.SHORT_GRASS), "Be the last team to break short grass", Material.SHORT_GRASS),
            new OpponentBuildChallenge(StaticIcon.of(Material.COBBLESTONE), "Be the last team to place cobblestone", Material.COBBLESTONE),
            new OpponentOpenChestChallenge(StaticIcon.of(Material.CHEST), "Be the last team to open a chest"),
            new OpponentDieChallenge(StaticIcon.of(Material.SKELETON_SKULL), "Be the last team to have someone die")


            /*
        Delete an entry from here once it's implemented and added to the list:

        Avoid fatal fall damage with a water bucket

        fill_armor_stand,
        wear_carved_pumpkin,
        wear_colored_leather_armor,
        breed_4_unique,
        breed_6_unique,
        breed_8_unique,

        kill_7_unique_hostile,
        kill_10_unique_hostile,
        kill_15_unique_hostile,
        kill_30_undead,
        kill_20_arthropods,

        write_book,
        fill_inventory_with_unique_items,
        ride_horse_with_saddle,
        use_a_brewing_stand,
        brew_potion_of_healing,
        brew_potion_of_leaping,
        brew_potion_of_swiftness,
        brew_potion_of_invisibility,
        brew_potion_of_water_breathing,
        brew_potion_of_lingering,
        eat_5_unique_food,
        eat_10_unique_food,
        eat_20_unique_food,

        get_3_status_effects_at_once,
        get_6_status_effects_at_once,
        remove_status_effect_with_milk,
        die_by_llama,
        die_by_firework_rocket,
        die_to_falling_stalactite,
        die_by_magic,
        die_to_iron_golem,
        die_by_falling_off_vines,
        find_fortress,
        find_bastion,
        find_stronghold,
        find_end_city,
        use_smithing_table,
        use_enchanting_table,
        use_anvil,
        use_composter,
        use_cauldron_to_wash,
        use_loom_to_design_banner,
        use_jukebox_to_play_music,
        opponent_dies,
        opponent_dies_3_times,
        opponent_catches_fire,
        opponent_obtains_crafting_table,
        oponnent_obtains_obsidian,
        oponnent_obtains_seeds,
        opponent_jumps,
        opponent_touches_water,
        opponent_takes_fall_damage,
        opponent_takes_100_total_damage,
        opponent_is_hit_by_snowball,
        opponent_hits_you,
        sleep_alone_in_overworld,
        spawn_a_chicken_with_an_egg,
        reach_level_15,
        reach_level_30,
        use_glow_ink_on_crimson_sign,
        empty_huger_bar,
        reach_bedrock,
        reach_sky_limit,
        detonate_tnt_minecart,
        enrage_zombie_piglin,
        take_200_damage,
        kill_100_mobs,
        deal_400_damage,
        sprint_1k,
        get_any_spyglass_advancement,
        trade_with_villager,
        distract_piglin,
        visit_all_nether_biomes,
        get_sniper_duel,
        get_bullseye,
        charge_respawn_anchor_to_max,
        get_15_advancements,
        get_25_advancements,
        get_35_advancements,
        obtain_more_dried_kelp_than_opponent,
        obtain_more_hoppers_than_opponent,
        have_more_levels_than_opponent
             */
    );

    public Collection<Challenge> getChallenges() {
        return challenges;
    }

    @Override
    public void onEnable() {
        this.getCommand("lockout").setExecutor(new LockoutCommand());
        challenges.forEach(challenge -> {
            Bukkit.getPluginManager().registerEvents(challenge, this);
            challenge.initialize();
        });
        teamManager = new TeamManager();
        menu = new ChallengeBoardMenu();
        compassHandler = new CompassHandler();

        Bukkit.getPluginManager().registerEvents(menu, this);
        Bukkit.getPluginManager().registerEvents(compassHandler, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
