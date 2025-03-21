package fr.zadiho.hepickstudio.olympiade.tasks;

import fr.zadiho.hepickstudio.olympiade.Olympiade;
import fr.zadiho.hepickstudio.olympiade.game.EGames;
import fr.zadiho.hepickstudio.olympiade.game.Game;
import fr.zadiho.hepickstudio.olympiade.game.GameSettings;
import fr.zadiho.hepickstudio.olympiade.game.pve.EPVE;
import fr.zadiho.hepickstudio.olympiade.utils.Cuboid;
import fr.zadiho.hepickstudio.olympiade.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PVETask extends BukkitRunnable implements Listener {

    private static int counter = 16;
    public static boolean played = false;
    private static Cuboid arene = new Cuboid(new Location(Bukkit.getWorld("OlympiadeS3"), -971.5, 55, -935), new Location(Bukkit.getWorld("OlympiadeS3"), -1183.5, -6, -731.5));
    public static int time = 0;
    private static List<Player> inPVE = new ArrayList<>();
    private static List<Player> dead = new ArrayList<>();
    public static int round = 0;

    public static ArrayList<Player> alives = new ArrayList<>();
    public static int entities;

    public static void resetPVE() {
        setPlayed(false);
        counter = 16;
        alives.clear();
        time = 0;
        entities = EPVE.ROUND1.getEntities();
        EPVE.setCurrentRound(EPVE.ROUND1);
        GameSettings.getPvePodium().clear();
        getInPVE().clear();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        if(EGames.getCurrentState().equals(EGames.PVE)){
            Player player = event.getPlayer();
            getInPVE().remove(player);
            alives.remove(player);
        }
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event){
        if(EGames.getCurrentState().equals(EGames.PVE)){
            Player player = event.getPlayer();
            player.getInventory().clear();
            getInPVE().add(player);
            alives.remove(player);
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(new Location(Bukkit.getWorld("OlympiadeS3"), -1092.5, 86, -724.5, -180, 0));
            if(!(GameSettings.getPvePodium().containsKey(player))){
                GameSettings.getPvePodium().put(player, 0);
            }
        }
    }

    public static boolean isAirLocation(Location location) {
        World world = location.getWorld();
        if (world == null) {
            return false;
        }

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    Block block = world.getBlockAt(x + dx, y + dy, z + dz);
                    if (block.getType() != Material.AIR) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static Location randomLoc() {
        return arene.getRandomLocation();
    }

    @EventHandler
    public void onEat(EntityChangeBlockEvent event) {
        if (EGames.getCurrentState().equals(EGames.PVE)) {
            if (event.getEntity().getType() == EntityType.ENDER_DRAGON || event.getEntity().getType() == EntityType.WITHER_SKULL || event.getEntity().getType() == EntityType.WITHER || event.getEntity().getType() == EntityType.FIREBALL) {
                event.setCancelled(true);
            }
        }
    }

    public static void startRound() {
        removeEntities();
        if (EPVE.getCurrentRound().equals(EPVE.ROUND1)) {
            reviveDeadPlayer();
            for (int i = 180; i > 0; i--) {
                Location loc = randomLoc();
                while (!isAirLocation(loc)) {
                    loc = randomLoc();
                }
                Frog frog = (Frog) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.FROG);
                Skeleton skeleton = (Skeleton) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.SKELETON);
                Spider spider = (Spider) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.SPIDER);
                Zombie zombie = (Zombie) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.ZOMBIE);
                zombie.setCustomName("§cZombie");
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
                zombie.setCustomNameVisible(true);
                zombie.setAdult();

                skeleton.setCustomName("§cSquelette");
                skeleton.setCustomNameVisible(true);

                spider.setCustomName("§cAraignée");
                spider.setCustomNameVisible(true);

                frog.setCustomName("§cGrenouille");
                frog.setCustomNameVisible(true);

            }
        }

        if (EPVE.getCurrentRound().equals(EPVE.ROUND2)) {
            reviveDeadPlayer();
            Location loca = randomLoc();
            while (!isAirLocation(loca)) {
                loca = randomLoc();
            }
            for (int i = 120; i > 0; i--) {
                Location loc = randomLoc();
                while (!isAirLocation(loc)) {
                    loc = randomLoc();
                }
                Pillager pillager = (Pillager) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.PILLAGER);
                Vindicator vindicator = (Vindicator) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.VINDICATOR);
                Vex vex = (Vex) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.VEX);
                Allay allay = (Allay) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.ALLAY);


                pillager.setCustomName("§cPillager");
                pillager.setCustomNameVisible(true);

                vindicator.setCustomName("§cVindicateur");
                vindicator.setCustomNameVisible(true);

                vex.setCustomName("§cVex");
                vex.setCustomNameVisible(true);

                allay.setCustomName("§cAllay");
                allay.setCustomNameVisible(true);
            }
            Wither wither = (Wither) Bukkit.getWorld("OlympiadeS3").spawnEntity(loca, EntityType.WITHER);
            wither.setCustomName("§cWither");
            wither.setCustomNameVisible(true);
        }
        if (EPVE.getCurrentRound().equals(EPVE.ROUND3)) {
            reviveDeadPlayer();
            for (int i = 120; i > 0; i--) {
                Location loc = randomLoc();
                while (!isAirLocation(loc)) {
                    loc = randomLoc();
                }
                Witch witch = (Witch) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.WITCH);
                Slime slime = (Slime) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.SLIME);
                Phantom phantom = (Phantom) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.PHANTOM);
                WitherSkeleton witherSkeleton = (WitherSkeleton) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.WITHER_SKELETON);

                witch.setCustomName("§cSorcière");
                witch.setCustomNameVisible(true);

                slime.setCustomName("§cSlime");
                slime.setCustomNameVisible(true);

                phantom.setCustomName("§cFantôme");
                phantom.setCustomNameVisible(true);
                ;
                witherSkeleton.setCustomName("§cWither squelette");
                witherSkeleton.setCustomNameVisible(true);
            }
        }
        if (EPVE.getCurrentRound().equals(EPVE.ROUND4)) {
            reviveDeadPlayer();
            for (int i = 120; i > 0; i--) {
                Location loc = randomLoc();
                while (!isAirLocation(loc)) {
                    loc = randomLoc();
                }
                WitherSkeleton witherSkeleton = (WitherSkeleton) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.WITHER_SKELETON);
                Blaze blaze = (Blaze) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.BLAZE);
                Piglin piglin = (Piglin) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.PIGLIN);
                PiglinBrute piglinBrute = (PiglinBrute) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.PIGLIN_BRUTE);
                Enderman enderman = (Enderman) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.ENDERMAN);
                enderman.setCustomName("§cEnderman");
                enderman.setCustomNameVisible(true);

                witherSkeleton.setCustomName("§cWither squelette");
                witherSkeleton.setCustomNameVisible(true);

                blaze.setCustomName("§cBlaze");
                blaze.setCustomNameVisible(true);

                piglinBrute.setCustomName("§cPiglin brute");
                piglinBrute.setCustomNameVisible(true);

                piglin.setCustomName("§cpiglin");
                piglin.setCustomNameVisible(true);
            }
        }
        if (EPVE.getCurrentRound().equals(EPVE.ROUND5)) {
            reviveDeadPlayer();
            for (int i = 25; i > 0; i--) {
                Location loc = randomLoc();
                while (!isAirLocation(loc)) {
                    loc = randomLoc();
                }
                Warden warden = (Warden) Bukkit.getWorld("OlympiadeS3").spawnEntity(loc, EntityType.WARDEN);

                warden.setCustomName("§cWarden");
                warden.setCustomNameVisible(true);
            }
        }

    }

    public static boolean isPlayed() {
        return played;
    }

    public static void setPlayed(boolean played) {
        TNTTask.played = played;
    }

    private static void removeEntityTypes(EntityType type) {
        for (Entity ent : Bukkit.getWorld("OlympiadeS3").getEntitiesByClass(type.getEntityClass())) {
            ent.remove();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (EGames.getCurrentState().equals(EGames.PVE)) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                event.setCancelled(true);
            }
            if (counter > -10) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (EGames.getCurrentState().equals(EGames.PVE)) {
            event.setDroppedExp(0);
            event.getDrops().clear();
            if (EPVE.getCurrentRound().equals(EPVE.ROUND1)) {
                if (event.getEntity().getType() == EntityType.SKELETON) {

                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.ZOMBIE) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.SPIDER) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.FROG) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
            }
            if (EPVE.getCurrentRound().equals(EPVE.ROUND2)) {
                if (event.getEntity().getType() == EntityType.PILLAGER) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.VINDICATOR) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.VEX) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.ALLAY) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.RAVAGER) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
            }
            if (EPVE.getCurrentRound().equals(EPVE.ROUND3)) {
                if (event.getEntity().getType() == EntityType.SLIME) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.WITCH) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.PHANTOM) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.WITHER_SKELETON) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
            }
            if (EPVE.getCurrentRound().equals(EPVE.ROUND4)) {
                if (event.getEntity().getType() == EntityType.ENDERMAN) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.WITHER) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.BLAZE) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.PIGLIN) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
                if (event.getEntity().getType() == EntityType.PIGLIN_BRUTE) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
            }
            if (EPVE.getCurrentRound().equals(EPVE.ROUND5)) {
                if (event.getEntity().getType() == EntityType.WARDEN) {
                    if (event.getEntity().getKiller() != null) {
                        Player killer = event.getEntity().getKiller();
                        GameSettings.getPvePodium().put(killer, GameSettings.getPvePodium().get(killer) + 1);
                    }
                    entities -= 1;
                }
            }
        }
    }

    private static void removeEntities() {
        removeEntityTypes(EntityType.ZOMBIE);
        removeEntityTypes(EntityType.SKELETON);
        removeEntityTypes(EntityType.SPIDER);
        removeEntityTypes(EntityType.FROG);
        removeEntityTypes(EntityType.PILLAGER);
        removeEntityTypes(EntityType.VINDICATOR);
        removeEntityTypes(EntityType.VEX);
        removeEntityTypes(EntityType.ALLAY);
        removeEntityTypes(EntityType.WITHER);
        removeEntityTypes(EntityType.WITCH);
        removeEntityTypes(EntityType.SLIME);
        removeEntityTypes(EntityType.PHANTOM);
        removeEntityTypes(EntityType.WITHER_SKELETON);
        removeEntityTypes(EntityType.ENDERMAN);
        removeEntityTypes(EntityType.WITHER_SKELETON);
        removeEntityTypes(EntityType.BLAZE);
        removeEntityTypes(EntityType.PIGLIN_BRUTE);
        removeEntityTypes(EntityType.PIGLIN);
        removeEntityTypes(EntityType.WARDEN);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (EGames.getCurrentState().equals(EGames.PVE)) {
            event.setDeathMessage(null);
            event.getDrops().clear();
            alives.remove(event.getEntity().getPlayer());
            if (event.getEntity().getPlayer() != null) {
                Player player = event.getEntity().getPlayer();
                player.setGameMode(GameMode.SPECTATOR);
                player.playSound(player.getLocation(), Sound.ENTITY_MOOSHROOM_CONVERT, 1, 1);
                dead.add(player);
                if(alives.size() > 1){
                    Bukkit.getScheduler().runTaskLater(Olympiade.getInstance(), () -> {
                        player.teleport(alives.get(0));
                    }, 10);
                }
            }
        }
    }

    private static void reviveDeadPlayer() {
        if (dead.size() > 0) {
            Iterator<Player> iterator = dead.iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();
                equipPlayer(player);
                iterator.remove();
                player.teleport(new Location(Bukkit.getWorld("OlympiadeS3"), -1080.5, 50, -844.5, -180, 0));
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(20);
            }
        }
    }

    public static void equipPlayer(Player player) {
        ItemStack[] armorContents = new ItemStack[4];
        armorContents[3] = new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.DURABILITY, 10).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack();
        armorContents[2] = new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.DURABILITY, 10).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack();
        armorContents[1] = new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.DURABILITY, 10).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack();
        armorContents[0] = new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.DURABILITY, 10).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack();

        player.getInventory().setArmorContents(armorContents);
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).addEnchant(Enchantment.DURABILITY, 10).addEnchant(Enchantment.DAMAGE_ALL, 3).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).addEnchant(Enchantment.DURABILITY, 10).addEnchant(Enchantment.ARROW_DAMAGE, 2).toItemStack());
        player.getInventory().addItem(new ItemStack(Material.SHIELD));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 32));
        player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 3));
    }

    @Override
    public void run() {
        if (counter == 16) {
            EPVE.setCurrentRound(EPVE.ROUND1);
            Bukkit.getWorld("OlympiadeS3").setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            Bukkit.getWorld("OlympiadeS3").setPVP(false);
            removeEntities();
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.stopAllSounds();
                players.showPlayer(Olympiade.getInstance(), players);
                players.setGlowing(false);
                players.playSound(players.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1, 1);
                players.setGameMode(GameMode.SURVIVAL);
                alives.add(players);
                getInPVE().add(players);
                players.teleport(new Location(Bukkit.getWorld("OlympiadeS3"), -1092.5, 86, -724.5, -180, 0));
                players.stopAllSounds();
                players.playSound(players.getLocation(), Sound.MUSIC_DISC_FAR, 1, 1);
                GameSettings.getPvePodium().put(players, 0);
            }
        }
        if (counter == 10) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendTitle("§cAttention !", "§6Placez vous devant la ligne de départ !", 10, 20, 10);
            }
        }
        if (counter == 5) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendTitle("§95", "§bTenez vous prêt !", 10, 20, 10);
                players.playNote(players.getLocation(), Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.E));
            }
        }
        if (counter == 4) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendTitle("§24", "§aPas de faux départs !", 10, 20, 10);
                players.playNote(players.getLocation(), Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.D));
            }
        }
        if (counter == 3) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendTitle("§e3", "§6A vos marques", 10, 20, 10);
                players.playNote(players.getLocation(), Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.C));
            }
        }
        if (counter == 2) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendTitle("§62", "§ePrêt ?", 10, 20, 10);
                players.playNote(players.getLocation(), Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.B));
            }
        }
        if (counter == 1) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendTitle("§c1", "§4C'est partie !", 10, 20, 10);
                players.playNote(players.getLocation(), Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.A));
            }
        }
        if (counter == 0) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                EPVE.setCurrentRound(EPVE.ROUND1);
                entities = 200;
                players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                players.teleport(new Location(Bukkit.getWorld("OlympiadeS3"), -1080.5, 50, -844.5, -180, 0));
                equipPlayer(players);
                startRound();
            }
        }
        if (counter == -10) {
            Bukkit.broadcastMessage("§6Invincibilité désactivée !");
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.playSound(players.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
            }
        }
        if (counter < 0) {
            for (Player players : GameSettings.getGamePlayers()) {
                if (players.isVisualFire()) {
                    players.setVisualFire(false);
                }
            }
            if (entities <= 0) {
                if (EPVE.getCurrentRound().equals(EPVE.ROUND1)) {
                    removeEntities();
                    Bukkit.broadcastMessage("§cFin de la vague 1, préparez vous à la vague 2");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                    EPVE.setCurrentRound(EPVE.ROUND2);
                    entities = EPVE.ROUND2.getEntities();
                    Bukkit.getScheduler().runTaskLater(Olympiade.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
                            }
                            startRound();
                            Bukkit.broadcastMessage("C'est parti pour la vague 2 !");
                        }
                    }, 20 * 10);
                }
                if (EPVE.getCurrentRound().equals(EPVE.ROUND2) && entities <= 0) {
                    removeEntities();
                    Bukkit.broadcastMessage("§cFin de la vague 2, préparez vous à la vague 3");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                    EPVE.setCurrentRound(EPVE.ROUND3);
                    entities = EPVE.ROUND3.getEntities();
                    Bukkit.getScheduler().runTaskLater(Olympiade.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
                            }
                            startRound();
                            Bukkit.broadcastMessage("C'est parti pour la vague 3 !");
                        }
                    }, 20 * 10);
                }
                if (EPVE.getCurrentRound().equals(EPVE.ROUND3) && entities <= 0) {
                    removeEntities();
                    Bukkit.broadcastMessage("§cFin de la vague 3, préparez vous à la vague 4");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                    EPVE.setCurrentRound(EPVE.ROUND4);
                    entities = EPVE.ROUND4.getEntities();
                    Bukkit.getScheduler().runTaskLater(Olympiade.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
                            }
                            startRound();
                            Bukkit.broadcastMessage("C'est parti pour la vague 4 !");
                        }
                    }, 20 * 10);
                }
                if (EPVE.getCurrentRound().equals(EPVE.ROUND4) && entities <= 0) {
                    removeEntities();
                    Bukkit.broadcastMessage("§cFin de la vague 4, préparez vous à la vague 5");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                    EPVE.setCurrentRound(EPVE.ROUND5);
                    entities = EPVE.ROUND5.getEntities();
                    Bukkit.getScheduler().runTaskLater(Olympiade.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
                            }
                            startRound();
                            Bukkit.broadcastMessage("C'est parti pour la vague 5 !");
                        }
                    }, 20 * 10);
                }
                if (EPVE.getCurrentRound().equals(EPVE.ROUND5) && entities <= 0) {
                    removeEntities();
                    Bukkit.broadcastMessage("Fin de la vague 5 !");
                    for (Player players : GameSettings.getGamePlayers()) {
                        players.sendTitle("§cPVE Terminé !", "§6Toutes les vagues ont été terminées !", 10, 100, 10);
                        players.playSound(players.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
                        players.getInventory().clear();
                        players.setGameMode(GameMode.ADVENTURE);
                    }
                    EGames.setState(EGames.WAITING);
                    setPlayed(true);
                    Game.teleportPodium(GameSettings.pvePodiumList());
                    Game.givePoints(GameSettings.pvePodiumList());
                    cancel();
                }
            }

            if (alives.size() == 0) {
                removeEntities();
                if (EPVE.getCurrentRound().equals(EPVE.ROUND1)) {
                    removeEntities();
                    for (Player players : GameSettings.getGamePlayers()) {
                        players.sendTitle("§cDéfaite!", "§6Vous n'avez pas remporté cette vague...", 10, 100, 10);
                        players.playSound(players.getLocation(), Sound.ENTITY_CAT_HISS, 1, 1);
                        players.setGameMode(GameMode.SURVIVAL);
                        players.teleport(new Location(Bukkit.getWorld("OlympiadeS3"), -1080.5, 50, -844.5, -180, 0));
                        alives.add(players);
                    }
                    Bukkit.broadcastMessage("Fin de la vague 1, préparez vous à la vague 2");
                    entities = EPVE.ROUND2.getEntities();
                    Bukkit.getScheduler().runTaskLater(Olympiade.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            removeEntities();
                            EPVE.setCurrentRound(EPVE.ROUND2);
                            startRound();
                            Bukkit.broadcastMessage("C'est parti pour la vague 2 !");
                        }
                    }, 20 * 10);
                }
                if (EPVE.getCurrentRound().equals(EPVE.ROUND2)) {
                    removeEntities();

                    for (Player players : GameSettings.getGamePlayers()) {
                        players.sendTitle("§cDéfaite!", "§6Vous n'avez pas remporté cette vague...", 10, 100, 10);
                        players.playSound(players.getLocation(), Sound.ENTITY_CAT_HISS, 1, 1);
                        players.setGameMode(GameMode.SURVIVAL);
                        players.teleport(new Location(Bukkit.getWorld("OlympiadeS3"), -1080.5, 50, -844.5, -180, 0));
                        alives.add(players);
                    }
                    Bukkit.broadcastMessage("Fin de la vague 2, préparez vous à la vague 3");
                    entities = EPVE.ROUND3.getEntities();
                    Bukkit.getScheduler().runTaskLater(Olympiade.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            removeEntities();
                            EPVE.setCurrentRound(EPVE.ROUND3);
                            startRound();
                            Bukkit.broadcastMessage("C'est parti pour la vague 3 !");
                        }
                    }, 20 * 10);
                }
                if (EPVE.getCurrentRound().equals(EPVE.ROUND3)) {
                    removeEntities();
                    for (Player players : GameSettings.getGamePlayers()) {
                        players.sendTitle("§cDéfaite!", "§6Vous n'avez pas remporté cette vague...", 10, 100, 10);
                        players.playSound(players.getLocation(), Sound.ENTITY_CAT_HISS, 1, 1);
                        players.setGameMode(GameMode.SURVIVAL);
                        players.teleport(new Location(Bukkit.getWorld("OlympiadeS3"), -1080.5, 50, -844.5, -180, 0));
                        alives.add(players);
                    }
                    Bukkit.broadcastMessage("Fin de la vague 3, préparez vous à la vague 4");
                    entities = EPVE.ROUND4.getEntities();
                    Bukkit.getScheduler().runTaskLater(Olympiade.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            removeEntities();
                            EPVE.setCurrentRound(EPVE.ROUND4);
                            startRound();
                            Bukkit.broadcastMessage("C'est parti pour la vague 4 !");
                        }
                    }, 20 * 10);
                }
                if (EPVE.getCurrentRound().equals(EPVE.ROUND4)) {
                    removeEntities();
                    for (Player players : GameSettings.getGamePlayers()) {
                        players.sendTitle("§cDéfaite!", "§6Vous n'avez pas remporté cette vague...", 10, 100, 10);
                        players.playSound(players.getLocation(), Sound.ENTITY_CAT_HISS, 1, 1);
                        players.setGameMode(GameMode.SURVIVAL);
                        players.teleport(new Location(Bukkit.getWorld("OlympiadeS3"), -1080.5, 50, -844.5, -180, 0));
                        alives.add(players);
                    }
                    Bukkit.broadcastMessage("Fin de la vague 4, préparez vous à la vague 5");
                    entities = EPVE.ROUND5.getEntities();
                    Bukkit.getScheduler().runTaskLater(Olympiade.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            removeEntities();
                            EPVE.setCurrentRound(EPVE.ROUND5);
                            startRound();
                            Bukkit.broadcastMessage("C'est parti pour la vague 5 !");
                        }
                    }, 20 * 10);
                }
                if (EPVE.getCurrentRound().equals(EPVE.ROUND5)) {
                    removeEntities();
                    removeEntities();
                    for (Player players : GameSettings.getGamePlayers()) {
                        players.sendTitle("§cPVE Terminé !", "§6Toutes les vagues ont été terminées !", 10, 100, 10);
                        players.playSound(players.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
                        players.getInventory().clear();
                        players.setGameMode(GameMode.ADVENTURE);
                        alives.add(players);
                    }
                    EGames.setState(EGames.WAITING);
                    setPlayed(true);
                    Game.teleportPodium(GameSettings.pvePodiumList());
                    Game.givePoints(GameSettings.pvePodiumList());
                    cancel();
                }
            }
            if (time / 60 >= EGames.PVE.getDuration()) {
                for (Player players : GameSettings.getGamePlayers()) {
                    players.sendTitle("§cTemps écoulé !", "§6Le parcours est terminé !", 10, 100, 10);
                    players.playSound(players.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
                    players.getInventory().clear();
                    players.setGameMode(GameMode.ADVENTURE);
                }
                EGames.setState(EGames.WAITING);
                setPlayed(true);
                Game.teleportPodium(GameSettings.pvePodiumList());
                Game.givePoints(GameSettings.pvePodiumList());
                cancel();
            }
            time++;
        }
        counter--;
    }

    public static List<Player> getInPVE() {
        return inPVE;
    }
}

