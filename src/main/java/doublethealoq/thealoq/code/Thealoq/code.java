package doublethealoq.thealoq.code.Thealoq;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public final class code extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }
    @Override
    public void onDisable() {
    }
    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event){
        event.setJoinMessage(ChatColor.GREEN+" Sunucuya Hoş Geldin " +event.getPlayer().getName());
        if(!event.getPlayer().hasPlayedBefore()){
            event.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_AXE, 1));
            event.getPlayer().getInventory().addItem(new ItemStack(Material.COOKED_MUTTON, 16));
            event.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_PICKAXE, 1));
        }
    }
    @EventHandler
    public void onLeaveEvent(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.DARK_RED +" Görüşüz " +event.getPlayer().getName());
    }

    @EventHandler
    public void OnBed(PlayerBedEnterEvent event) {
        Player p = event.getPlayer();
        long Time = p.getWorld().getTime();
        if (Time > 12999) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,1000, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,1000, 1));
        }
    }


    @EventHandler
    public void BedLeave(PlayerBedLeaveEvent event) {
        event.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
        event.getPlayer().removePotionEffect(PotionEffectType.SATURATION);
    }


   @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(command.getName().equalsIgnoreCase("test")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;

            }

        }
        return true;
   }


}
