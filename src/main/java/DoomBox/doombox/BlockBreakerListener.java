package DoomBox.doombox;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class BlockBreakerListener implements Listener{
	@EventHandler
	public void onBlockBreak(PlayerInteractEvent event){
		if (event.hasBlock() && event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if (!(doombox.triggered) && event.getClickedBlock().getLocation().equals(doombox.loc)){
				event.setCancelled(true);
				event.getPlayer().sendMessage("Some mysterious force prevents you from opening this chest");
				doombox.triggered = true;
				doombox.startSpawn();
			}
		}
	}
}
