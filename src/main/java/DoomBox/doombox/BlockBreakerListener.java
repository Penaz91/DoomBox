package DoomBox.doombox;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreakerListener implements Listener{
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if (event.getBlock().getLocation().equals(doombox.carpetloc)){
			doombox.handleSpawn();
		}
	}
}
