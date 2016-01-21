package DoomBox.doombox;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class BlockBreakerListener implements Listener{
	@EventHandler
	public void onBlockBreak(PlayerInteractEvent event){
		if (event.getClickedBlock().getLocation().equals(doombox.loc)){
			event.setCancelled(true);
			event.getPlayer().sendMessage("Some mysterious force prevents you from opening this chest");
			doombox.startSpawn();
		}
	}
}
