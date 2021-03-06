package com.elikill58.negativity.spigot.protocols;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.elikill58.negativity.spigot.SpigotNegativity;
import com.elikill58.negativity.spigot.SpigotNegativityPlayer;
import com.elikill58.negativity.spigot.utils.Utils;
import com.elikill58.negativity.universal.Cheat;
import com.elikill58.negativity.universal.CheatKeys;
import com.elikill58.negativity.universal.ReportType;
import com.elikill58.negativity.universal.utils.UniversalUtils;

public class PhaseProtocol extends Cheat implements Listener {

	public PhaseProtocol() {
		super(CheatKeys.PHASE, false, Utils.getMaterialWith1_15_Compatibility("STAINED_GLASS", "WHITE_STAINED_GLASS"), CheatCategory.MOVEMENT, true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		SpigotNegativityPlayer np = SpigotNegativityPlayer.getNegativityPlayer(p);
		if (!np.ACTIVE_CHEAT.contains(this))
			return;
		if (!p.getGameMode().equals(GameMode.SURVIVAL) && !p.getGameMode().equals(GameMode.ADVENTURE))
			return;
		Location loc = p.getLocation();
		Location from = e.getFrom(), to = e.getTo();
		double y = to.getY() - from.getY();
		if (y > 0.1 && (!loc.clone().subtract(0, 1, 0).getBlock().getType().equals(Material.AIR)
				|| !np.hasOtherThan(loc.clone().subtract(0, 1, 0), Material.AIR)))
			np.isJumpingWithBlock = true;
		if (y < -0.1)
			np.isJumpingWithBlock = false;
		if (!loc.clone().subtract(0, 1, 0).getBlock().getType().equals(Material.AIR)
				|| !loc.clone().subtract(0, 2, 0).getBlock().getType().equals(Material.AIR)
				|| !loc.clone().subtract(0, 3, 0).getBlock().getType().equals(Material.AIR)
				|| !loc.clone().subtract(0, 4, 0).getBlock().getType().equals(Material.AIR))
			return;
		if (y < 0)
			return;
		if (np.hasOtherThan(loc.clone(), Material.AIR) || np.hasOtherThan(loc.clone().subtract(0, 1, 0), Material.AIR))
			return;
		if (!np.isJumpingWithBlock) {
			SpigotNegativity.alertMod(ReportType.VIOLATION, p, this, UniversalUtils.parseInPorcent((y * 200) + 20),
					"Player on air. No jumping. DistanceBetweenFromAndTo: " + y + " (ping: " + Utils.getPing(p)
							+ "). Warn: " + np.getWarn(this));
		}
	}
}
