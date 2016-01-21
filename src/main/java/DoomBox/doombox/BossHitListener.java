package DoomBox.doombox;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BossHitListener implements Listener{
	@EventHandler
	public void OnHit(EntityDamageByEntityEvent e){
		try{
			if (e.getEntity().getCustomName().contains(doombox.settings.get("boss.name").toString())){
				doombox.BossPort(e.getEntity(),e.getDamager());
			}
		}catch (NullPointerException xe){
			System.out.print("");
		}
	}
}
