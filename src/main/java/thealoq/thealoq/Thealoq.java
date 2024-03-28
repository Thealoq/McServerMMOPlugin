package thealoq.thealoq;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public final class Thealoq extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        String url = "";
        String username = "";
        String password = "";


        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Veritabanına bağlandı!");

            connection.close();
            System.out.println("Bağlantı kapatıldı.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material blockType = event.getBlockPlaced().getType();
        if (blockType == Material.END_CRYSTAL || blockType == Material.TNT_MINECART) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Bu bloğu yerleştiremezsiniz!");
        }
    }



    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.GREEN + " Sunucuya Hoş Geldin " + ChatColor.WHITE + event.getPlayer().getName());
        if (!event.getPlayer().hasPlayedBefore()) {
            event.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_AXE, 1));
            event.getPlayer().getInventory().addItem(new ItemStack(Material.COOKED_MUTTON, 16));
            event.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_PICKAXE, 1));
        }
    }

    @EventHandler
    public void onLeaveEvent(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.DARK_RED + " Görüşüz " + ChatColor.WHITE + event.getPlayer().getName());
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        long time = player.getWorld().getTime();
        if (time > 12999) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1000, 1));
        }
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        event.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
        event.getPlayer().removePotionEffect(PotionEffectType.SATURATION);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (event.getView().getTitle().equals("Update Menu") && event.getRawSlot() == 0 ) {
            event.getWhoClicked().closeInventory();
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("Tıklanan öğe: "+ event.getRawSlot());
        }
        if (event.getView().getTitle().equals("Update Menu") && event.getRawSlot() == 9 ) {
            event.getWhoClicked().closeInventory();
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("Tıklanan öğe: "+ event.getRawSlot());
        }
        if (event.getView().getTitle().equals("Update Menu") && event.getRawSlot() == 18 ) {
            event.getWhoClicked().closeInventory();
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("Tıklanan öğe: "+ event.getRawSlot());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bu komutu sadece oyuncular kullanabilir!");
            return false;
        }
        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("test")) {
            String path = System.getProperty("user.dir")+"/plugins/";
            String folderName = "Thealoq";
            String fileName = "Player.json";
            Path fullPath = Paths.get(path+folderName);
            if(!(Files.exists(fullPath) && Files.isDirectory(fullPath))) {
                try {
                    Files.createDirectory(fullPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                String json = new String(Files.readAllBytes(Paths.get(fullPath + "/" + fileName)), StandardCharsets.UTF_8);
                Gson gson = new Gson();

                PlayerData[] playerDataArray = gson.fromJson(json, PlayerData[].class);

                boolean playerExists = false;
                for (PlayerData playerData : playerDataArray) {
                    if (playerData.getPlayerUUID().equalsIgnoreCase(player.getUniqueId().toString())) {
                        playerExists = true;
                        break;
                    }
                }

                if (!playerExists) {
                    PlayerData newPlayerData = new PlayerData();
                    newPlayerData.setPlayerUUID(player.getUniqueId().toString());
                    newPlayerData.setSpeed(0);
                    newPlayerData.setAxe(0);
                    newPlayerData.setPickaxe(0);

                    List<PlayerData> newDataList = new ArrayList<>(Arrays.asList(playerDataArray));
                    newDataList.add(newPlayerData);

                    String updatedJson = gson.toJson(newDataList);
                    try (FileWriter file = new FileWriter(fullPath + "/" + fileName)) {
                        file.write(updatedJson);
                        System.out.println("Yeni veri başarıyla eklendi.");
                    }
                } else {
                    System.out.println("Oyuncu veri kümesinde zaten mevcut.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }






        }


        if (command.getName().equalsIgnoreCase("check")) {
            Inventory myInventory = Bukkit.createInventory(player, 27, "Update Menu");

            ItemStack woolItem = new ItemStack(Material.WHITE_WOOL);
            ItemMeta meta = woolItem.getItemMeta();
            meta.setDisplayName("Axe");
            woolItem.setItemMeta(meta);
            myInventory.setItem(0, woolItem);

            ItemStack woolItem2 = new ItemStack(Material.GREEN_WOOL);
            ItemMeta meta2 = woolItem.getItemMeta();
            meta2.setDisplayName("Pickaxe");
            woolItem2.setItemMeta(meta);
            myInventory.setItem(9, woolItem2);

            ItemStack woolItem3 = new ItemStack(Material.BLUE_WOOL);
            ItemMeta meta3 = woolItem.getItemMeta();
            meta3.setDisplayName("Speed");
            woolItem3.setItemMeta(meta);
            myInventory.setItem(18, woolItem3);


            player.openInventory(myInventory);
            return true;
        }

        if (command.getName().equalsIgnoreCase("start")) {
            ItemStack stoneAxe = new ItemStack(Material.STONE_AXE, 1);
            ItemMeta stoneAxeMeta = stoneAxe.getItemMeta();
            ((ItemMeta) stoneAxeMeta).setDisplayName("Level I");
            stoneAxeMeta.setUnbreakable(true);
            stoneAxe.setItemMeta(stoneAxeMeta);

            ItemStack stonePickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
            ItemMeta stonePickaxeMeta = stonePickaxe.getItemMeta();
            stonePickaxeMeta.setDisplayName("Level I");
            stonePickaxeMeta.setUnbreakable(true);
            stonePickaxe.setItemMeta(stonePickaxeMeta);

            ItemStack cookedMutton = new ItemStack(Material.COOKED_MUTTON, 16);
            ItemMeta cookedMuttonMeta = cookedMutton.getItemMeta();
            cookedMuttonMeta.setDisplayName("Pişmiş Koyun Eti");
            cookedMuttonMeta.setUnbreakable(true);
            cookedMutton.setItemMeta(cookedMuttonMeta);

            player.getInventory().addItem(stoneAxe);
            player.getInventory().addItem(stonePickaxe);
            player.getInventory().addItem(cookedMutton);
        }

        if (command.getName().equalsIgnoreCase("gm")) {
            if (!player.hasPermission("minecraft.command.gamemode")) {
                player.sendMessage(ChatColor.RED + "Yaratıcı moda geçmek için yeterli yetkiye sahip değilsiniz!");
                return false;
            }
            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Kullanım: /gm <mod>");
                return false;
            }

            int mode;
            try {
                mode = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Geçersiz mod numarası! Mod numarası 0 veya 1 olmalıdır.");
                return false;
            }

            switch (mode) {
                case 0:
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(ChatColor.WHITE + "Hayatta kalma " + ChatColor.GREEN + "moduna!");
                    break;
                case 1:
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(ChatColor.WHITE + "Yaratıcı "+ ChatColor.GREEN+ "modda!");
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Geçersiz mod numarası! Mod numarası 0 veya 1 olmalıdır.");
                    return false;
            }
            return true;
        }
        return true;
    }
}

class PlayerData {
    private UUID playerUUID;
    private int speed;
    private int axe;
    private int pickaxe;

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAxe() {
        return axe;
    }

    public void setAxe(int axe) {
        this.axe = axe;
    }

    public int getPickaxe() {
        return pickaxe;
    }

    public void setPickaxe(int pickaxe) {
        this.pickaxe = pickaxe;
    }
}
