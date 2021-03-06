package com.elikill58.negativity.sponge.timers;

import java.util.Collection;

import org.spongepowered.api.entity.living.player.Player;

import com.elikill58.negativity.sponge.SpongeNegativity;
import com.elikill58.negativity.sponge.SpongeNegativityPlayer;
import com.elikill58.negativity.sponge.listeners.PlayerCheatEvent;
import com.elikill58.negativity.sponge.utils.Utils;
import com.elikill58.negativity.universal.ReportType;

public class PendingAlertsTimer implements Runnable {

	@Override
	public void run() {
		Collection<Player> onlinePlayers = Utils.getOnlinePlayers();
		for (Player player : onlinePlayers) {
			SpongeNegativityPlayer nPlayer = SpongeNegativityPlayer.getNegativityPlayer(player);
			nPlayer.pendingAlerts.forEach((cheat, alerts) -> {
				if (alerts.isEmpty()) {
					return;
				}

				int ping = Utils.getPing(player);
				if (alerts.size() == 1) {
					PlayerCheatEvent.Alert alert = alerts.get(0);
					SpongeNegativity.sendAlertMessage(alert.getReportType(), player, cheat,
							alert.getReliability(), alert.getHoverProof(), nPlayer, ping, alert, 1, alert.getStatsSend());
				} else {
					PlayerCheatEvent.Alert referenceAlert = null;
					int reliabilitySum = 0;
					for (PlayerCheatEvent.Alert alert : alerts) {
						reliabilitySum += alert.getReliability();
						if (alert.getReportType() == ReportType.VIOLATION) {
							referenceAlert = alert;
						}
					}

					if (referenceAlert == null) {
						referenceAlert = alerts.get(0);
					}

					SpongeNegativity.sendAlertMessage(referenceAlert.getReportType(), player, cheat,
							reliabilitySum / alerts.size(), referenceAlert.getHoverProof(), nPlayer, ping, referenceAlert, alerts.size(), referenceAlert.getStatsSend());
				}
				alerts.clear();
			});
			nPlayer.pendingAlerts.clear();
		}
	}
}
