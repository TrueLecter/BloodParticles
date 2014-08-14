package truelecter.blood;

import java.util.logging.Logger;

import static com.sk89q.worldguard.bukkit.BukkitUtil.*;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class Blood extends JavaPlugin implements Listener {
	public static Blood plugin;
	public Logger log;
	private WorldGuardPlugin wg;
	private String ANSI_RED = "\u001B[31m";
	private String ANSI_RESET = "\u001B[0m";

	public boolean pvpAllowed(Location loc, String entity) {
		Vector v = toVector(loc);
		RegionManager manager = wg.getRegionManager(loc.getWorld());
		ApplicableRegionSet set = manager.getApplicableRegions(v);
		if (set.allows(DefaultFlag.PVP))
			return true;
		return false;
		
	}

	public void onEnable() {
		plugin = this;
		this.log = getLogger();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		wg = (WorldGuardPlugin) this.getServer().getPluginManager().getPlugin("WorldGuard");
		WorldEditPlugin we = (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
		if (wg == null) {
			System.out.println("[Blood]" + ANSI_RED + " WorldGuard not found." + ANSI_RESET);
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		if (we == null) {
			System.out.println("[Blood]" + ANSI_RED + " WorldEdit not found." + ANSI_RESET);
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	@EventHandler
	public void BloodEvent(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof Player)) {
			if (pvpAllowed(e.getEntity().getLocation(), "victim")
					&& pvpAllowed(e.getDamager().getLocation(), "damager")) {
				e.getEntity().getLocation().getWorld()
						.playEffect(e.getEntity().getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 55);
			}
		}
	}
}
