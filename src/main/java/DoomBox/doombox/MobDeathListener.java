package DoomBox.doombox;

import org.bukkit.Bukkit;
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
		try{
			if (event.getEntity().getCustomName().contains(doombox.settings.get("boss.name").toString())){
				Bukkit.getServer().broadcastMessage(doombox.parseFormat(doombox.settings.get("boss.messages.end_message").toString()));
			}
		}catch (NullPointerException xe){
			System.out.print("");
		}
	}
}
