package DoomBox.doombox;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDeathListener implements Listener{
	@EventHandler
	public void OnMobKilled(EntityDeathEvent event){
		if (doombox.elist.contains(event.getEntity())){
			doombox.elist.remove(event.getEntity());
			doombox.handleDeath();
		}
	}
}
