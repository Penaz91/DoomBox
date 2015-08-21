package DoomBox.doombox;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class doombox extends JavaPlugin {
	static Random rnd=new Random();
	static ArrayList<Entity> elist=new ArrayList<Entity>();
	static /* Settings area
	 * ------------------------------------------------------------
	 */
	HashMap<String,Object> settings=null;
	//--------------------------------------
	static Location loc=null;
	static Location carpetloc=null;
	static doombox instance=null;
	//-------------------------------------------------------------
	@Override 
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new BlockBreakerListener(), this);
		getServer().getPluginManager().registerEvents(new MobDeathListener(), this);
		File f = getDataFolder();
		if (!f.exists()){
			f.mkdir();
			saveResource("config.yml", false);
			saveResource("plugin.yml", false);
		}
		settings=(HashMap<String, Object>) getConfig().getValues(true);
		instance = this;
		getLogger().info("DoomBox Loaded Successfully");
	 }
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		getLogger().info("DoomBox Unloaded Successfully");
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("summonbox")) {
    		World world=getServer().getWorld(settings.get("world.name").toString());
    		Player p= (Player) sender;
    		if (!(world==null)){
    			if (!(world.equals(p.getWorld()))){
    				sender.sendMessage("You can't use this command in this world");
    			}else{
    				loc = new Location(p.getWorld(),Integer.parseInt(settings.get("world.x").toString()),Integer.parseInt(settings.get("world.y").toString()),Integer.parseInt(settings.get("world.z").toString()));
    				loc.getBlock().setType(Material.CAULDRON);
    				carpetloc = new Location(p.getWorld(),Integer.parseInt(settings.get("world.x").toString()),Integer.parseInt(settings.get("world.y").toString())+1,Integer.parseInt(settings.get("world.z").toString()));
    				carpetloc.getBlock().setType(Material.CARPET);
    				this.getServer().broadcastMessage(ChatColor.GOLD+settings.get("messages.summon_message").toString());
    			}
    		}else{
    			sender.sendMessage("You can't use this command in this world");
    		}
    		return true;
    	}else{
    		if (cmd.getName().equals("doombox")) {
    			if (args[0].equals("reload")){
    				settings=null;
    				this.reloadConfig();
    				settings=(HashMap<String, Object>) getConfig().getValues(true);
    				getLogger().info("DoomBox Settings Reloaded.");
    				sender.sendMessage("DoomBox has been reloaded");
    			}
    			return true;
    		}
    	}
    	return false;
	}
	public static void handleSpawn(){
		elist=new ArrayList<Entity>();
		loc.getBlock().setType(Material.AIR);
		carpetloc.getBlock().setType(Material.AIR);
		String bool=settings.get("mob_settings.randomizer").toString();
		boolean randomizer=false;
		if (bool.equalsIgnoreCase("true")){
			randomizer=true;
		}
		System.out.println(randomizer);
		System.out.println(bool);
		System.out.println(settings.get("mob_settings.randomizer").toString());
		if (randomizer){
			int nummobs=Integer.parseInt(settings.get("mob_settings.totalmobs").toString());
			System.out.println(nummobs);
			for (int i =0;i<nummobs;i++){
				switch(rnd.nextInt(13)){
					case 0: elist.add(loc.getWorld().spawnEntity(loc,EntityType.ZOMBIE));break;
					case 1: elist.add(loc.getWorld().spawnEntity(loc,EntityType.SKELETON));break;
					case 2: elist.add(loc.getWorld().spawnEntity(loc,EntityType.PIG_ZOMBIE));break;
					case 3: elist.add(loc.getWorld().spawnEntity(loc,EntityType.GHAST));break;
					case 4: elist.add(loc.getWorld().spawnEntity(loc,EntityType.WITCH));break;
					case 5: elist.add(loc.getWorld().spawnEntity(loc,EntityType.CREEPER));break;
					case 6: elist.add(loc.getWorld().spawnEntity(loc,EntityType.SPIDER));break;
					case 7: elist.add(loc.getWorld().spawnEntity(loc,EntityType.ENDERMAN));break;
					case 8: elist.add(loc.getWorld().spawnEntity(loc,EntityType.BLAZE));break;
					case 9: elist.add(loc.getWorld().spawnEntity(loc,EntityType.SLIME));break;
					case 10: elist.add(loc.getWorld().spawnEntity(loc,EntityType.CAVE_SPIDER));break;
					case 11: elist.add(loc.getWorld().spawnEntity(loc,EntityType.MAGMA_CUBE));break;
					case 12: {
						elist.add(loc.getWorld().spawnEntity(loc,EntityType.SKELETON));
						Skeleton skelly=(Skeleton) elist.get(i);
						skelly.setSkeletonType(SkeletonType.WITHER);
						break;
					}
				}
				elist.get(i).setVelocity(new Vector(rnd.nextInt(2),1,rnd.nextInt(2)));
				ArrayList<EntityType> Armorable=new ArrayList<EntityType>(Arrays.asList(EntityType.ZOMBIE,EntityType.SKELETON,EntityType.PIG_ZOMBIE));
				if (Armorable.contains(elist.get(i).getType())){
					boolean sets=settings.get("armor_settings.sets").toString().equalsIgnoreCase("true");
					if (sets){
						int dice=rnd.nextInt(6);
						switch (dice){
							case(0):giveEmptyKit(elist.get(i));break;
							case(1):giveIronKit(elist.get(i));break;
							case(2):giveChainKit(elist.get(i));break;
							case(3):giveLeatherKit(elist.get(i));break;
							case(4):giveGoldKit(elist.get(i));break;
							case(5):giveDiamondKit(elist.get(i));break;
						}	
					}else{
						giveRandomKit(elist.get(i));
					}
				}
			}
		}
	}
	public static void handleDeath(){
		System.out.println("A mob in the list died");
		if (elist.isEmpty()){
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN+settings.get("messages.end_message").toString());
		}
	}
	public static void giveDiamondKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		ItemStack chest=new ItemStack(Material.DIAMOND_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.DIAMOND_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.DIAMOND_HELMET,1);
		ItemStack boots=new ItemStack(Material.DIAMOND_BOOTS,1);
		ItemStack hand=giveWeapon(e,1);
		AddArmorEnchant(chest);
		AddArmorEnchant(legs);
		AddArmorEnchant(hat);
		AddArmorEnchant(boots);
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		givePotionEffects(le);
	}
	public static void giveChainKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		ItemStack chest=new ItemStack(Material.CHAINMAIL_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.CHAINMAIL_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.CHAINMAIL_HELMET,1);
		ItemStack boots=new ItemStack(Material.CHAINMAIL_BOOTS,1);
		ItemStack hand=giveWeapon(e,2);
		AddArmorEnchant(chest);
		AddArmorEnchant(legs);
		AddArmorEnchant(hat);
		AddArmorEnchant(boots);
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		givePotionEffects(le);
	}
	public static void giveEmptyKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		ItemStack hand=giveWeapon(e,0);
		ee.setItemInHand(hand);
		le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,5000,3));
		givePotionEffects(le);
	}
	public static void giveIronKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		ItemStack chest=new ItemStack(Material.IRON_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.IRON_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.IRON_HELMET,1);
		ItemStack boots=new ItemStack(Material.IRON_BOOTS,1);
		AddArmorEnchant(chest);
		AddArmorEnchant(legs);
		AddArmorEnchant(hat);
		AddArmorEnchant(boots);
		ItemStack hand=giveWeapon(e,2);
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		givePotionEffects(le);
	}
	public static void giveLeatherKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		ItemStack chest=new ItemStack(Material.LEATHER_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.LEATHER_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.LEATHER_HELMET,1);
		ItemStack boots=new ItemStack(Material.LEATHER_BOOTS,1);
		ItemStack hand=giveWeapon(e,2);
		AddArmorEnchant(chest);
		AddArmorEnchant(legs);
		AddArmorEnchant(hat);
		AddArmorEnchant(boots);
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		givePotionEffects(le);
	}
	public static void giveGoldKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		ItemStack chest=new ItemStack(Material.GOLD_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.GOLD_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.GOLD_HELMET,1);
		ItemStack boots=new ItemStack(Material.GOLD_BOOTS,1);
		ItemStack hand=giveWeapon(e,3);
		AddArmorEnchant(chest);
		AddArmorEnchant(legs);
		AddArmorEnchant(hat);
		AddArmorEnchant(boots);
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		givePotionEffects(le);
	}
	public static void giveRandomKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		ItemStack chest=null;
		ItemStack legs=null;
		ItemStack hat=null;
		ItemStack boots=null;
		switch (rnd.nextInt(6)){
		case 0: chest=new ItemStack(Material.DIAMOND_CHESTPLATE,1);AddArmorEnchant(chest);break;
		case 1: chest=new ItemStack(Material.IRON_CHESTPLATE,1);AddArmorEnchant(chest);break;
		case 2: chest=new ItemStack(Material.CHAINMAIL_CHESTPLATE,1);AddArmorEnchant(chest);break;
		case 3: chest=new ItemStack(Material.GOLD_CHESTPLATE,1);AddArmorEnchant(chest);break;
		case 4: chest=new ItemStack(Material.LEATHER_CHESTPLATE,1);AddArmorEnchant(chest);break;
		case 5: break;
		}
		switch (rnd.nextInt(6)){
		case 0: legs=new ItemStack(Material.DIAMOND_LEGGINGS,1);AddArmorEnchant(legs);break;
		case 1: legs=new ItemStack(Material.IRON_LEGGINGS,1);AddArmorEnchant(legs);break;
		case 2: legs=new ItemStack(Material.CHAINMAIL_LEGGINGS,1);AddArmorEnchant(legs);break;
		case 3: legs=new ItemStack(Material.GOLD_LEGGINGS,1);AddArmorEnchant(legs);break;
		case 4: legs=new ItemStack(Material.LEATHER_LEGGINGS,1);AddArmorEnchant(legs);break;
		case 5: break;
		}
		switch (rnd.nextInt(6)){
		case 0: hat=new ItemStack(Material.DIAMOND_HELMET,1);AddArmorEnchant(hat);break;
		case 1: hat=new ItemStack(Material.IRON_HELMET,1);AddArmorEnchant(hat);break;
		case 2: hat=new ItemStack(Material.CHAINMAIL_HELMET,1);AddArmorEnchant(hat);break;
		case 3: hat=new ItemStack(Material.GOLD_HELMET,1);AddArmorEnchant(hat);break;
		case 4: hat=new ItemStack(Material.LEATHER_HELMET,1);AddArmorEnchant(hat);break;
		case 5: break;
		}
		switch (rnd.nextInt(6)){
		case 0: boots=new ItemStack(Material.DIAMOND_BOOTS,1);AddArmorEnchant(boots);break;
		case 1: boots=new ItemStack(Material.IRON_BOOTS,1);AddArmorEnchant(boots);break;
		case 2: boots=new ItemStack(Material.CHAINMAIL_BOOTS,1);AddArmorEnchant(boots);break;
		case 3: boots=new ItemStack(Material.GOLD_BOOTS,1);AddArmorEnchant(boots);break;
		case 4: boots=new ItemStack(Material.LEATHER_BOOTS,1);AddArmorEnchant(boots);break;
		case 5: break;
		}
		ItemStack hand=giveWeapon(e,rnd.nextInt(4));
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		givePotionEffects(le);
	}
	public static void givePotionEffects (LivingEntity le){
		int dam=Integer.parseInt(settings.get("potions.increase_damage").toString());
		int speed=Integer.parseInt(settings.get("potions.speed").toString());
		int res=Integer.parseInt(settings.get("potions.resistance").toString());
		int health=Integer.parseInt(settings.get("potions.health").toString());
		if (dam!=0){
			int damage=rnd.nextInt(dam+1);
			if (damage!=0){
				le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,999999,damage));
			}
		}
		if (speed!=0){
			int spd=rnd.nextInt(speed+1);
			if (spd!=0){
				le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,999999,spd));
			}
		}
		if (res!=0){
			int resistance=rnd.nextInt(res+1);
			if (resistance!=0){
				le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,999999,resistance));
			}
		}
		if (health!=0){
			int hlt=rnd.nextInt(health+1);
			if (hlt!=0){
				le.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST,999999,hlt));
				le.setHealth(le.getMaxHealth());
			}
		}
	}
	public static ItemStack giveWeapon (Entity e, int type){
		/*
		 * 0=random
		 * 1=diamond
		 * 2=iron
		 * 3=gold
		 */
		ItemStack weapon=null;
		if (e.getType()==EntityType.SKELETON){
			if (rnd.nextBoolean()){
				weapon=GiveBow();
			}else{
				switch (type){
					case 0:{
						//give a random weapon type/tier
						switch (rnd.nextInt(3)){
							case 0: GiveDiamondWeapon();break;
							case 1: GiveIronWeapon();break;
							case 2: GiveGoldWeapon();break;
						}
					}
					case 1:{
						//give a random diamond weapon
						weapon=GiveDiamondWeapon();
						break;
					}
					case 2:{
						//give a random iron weapon
						weapon=GiveIronWeapon();
						break;
					}
					case 3:{
						//give a random gold weapon
						weapon=GiveGoldWeapon();
						break;
					}
				}
			}
		}else{
			switch (type){
				case 0:{
					//give a random weapon type/tier
					switch (rnd.nextInt(3)){
						case 0: GiveDiamondWeapon();break;
						case 1: GiveIronWeapon();break;
						case 2: GiveGoldWeapon();break;
					}
				}
				case 1:{
					//give a random diamond weapon
					weapon=GiveDiamondWeapon();
					break;
				}
				case 2:{
					//give a random iron weapon
					weapon=GiveIronWeapon();
					break;
				}
				case 3:{
					//give a random gold weapon
					weapon=GiveGoldWeapon();
					break;
				}
			}
		}
		return weapon;
	}
	public static ItemStack GiveBow(){
		ItemStack weapon=new ItemStack(Material.BOW,1);
		AddBowEnchants(weapon);
		return weapon;
	}
	public static ItemStack GiveDiamondWeapon(){
		ItemStack weapon=null;
		switch (rnd.nextInt(3)){
			case 0:weapon=new ItemStack(Material.DIAMOND_SWORD,1);break;
			case 1:weapon=new ItemStack(Material.DIAMOND_AXE,1);break;
			case 2:weapon=new ItemStack(Material.DIAMOND_HOE,1);break;
			case 3:weapon=new ItemStack(Material.DIAMOND_PICKAXE);break;
		}
		AddWeaponEnchants(weapon);
		return weapon;
	}
	public static ItemStack GiveIronWeapon(){
		ItemStack weapon=null;
		switch (rnd.nextInt(3)){
			case 0:weapon=new ItemStack(Material.IRON_SWORD,1);break;
			case 1:weapon=new ItemStack(Material.IRON_AXE,1);break;
			case 2:weapon=new ItemStack(Material.IRON_HOE,1);break;
			case 3:weapon=new ItemStack(Material.IRON_PICKAXE);break;
		}
		AddWeaponEnchants(weapon);
		return weapon;
	}
	public static ItemStack GiveGoldWeapon(){
		ItemStack weapon=null;
		switch (rnd.nextInt(3)){
			case 0:weapon=new ItemStack(Material.GOLD_SWORD,1);break;
			case 1:weapon=new ItemStack(Material.GOLD_AXE,1);break;
			case 2:weapon=new ItemStack(Material.GOLD_HOE,1);break;
			case 3:weapon=new ItemStack(Material.GOLD_PICKAXE);break;
		}
		AddWeaponEnchants(weapon);
		return weapon;
	}
	public static void AddWeaponEnchants(ItemStack weapon){
		int sharp=0;
		int smite=0;
		int BOA=0;
		int fire=0;
		int knockback=0;
		if ((weapon.getType()==Material.DIAMOND_SWORD)||(weapon.getType()==Material.GOLD_SWORD)||(weapon.getType()==Material.IRON_SWORD)){
			sharp=Integer.parseInt(settings.get("weapon_settings.weapons.swords.sharpness").toString());
			smite=Integer.parseInt(settings.get("weapon_settings.weapons.swords.smite").toString());
			BOA=smite=Integer.parseInt(settings.get("weapon_settings.weapons.swords.BOA").toString());
			fire=Integer.parseInt(settings.get("weapon_settings.weapons.swords.fire").toString());
			knockback=Integer.parseInt(settings.get("weapon_settings.weapons.swords.knockback").toString());
		}else{
			if ((weapon.getType()==Material.DIAMOND_AXE)||(weapon.getType()==Material.GOLD_AXE)||(weapon.getType()==Material.IRON_AXE)){
				sharp=Integer.parseInt(settings.get("weapon_settings.weapons.axes.sharpness").toString());
				smite=Integer.parseInt(settings.get("weapon_settings.weapons.axes.smite").toString());
				BOA=smite=Integer.parseInt(settings.get("weapon_settings.weapons.axes.BOA").toString());
				fire=Integer.parseInt(settings.get("weapon_settings.weapons.axes.fire").toString());
				knockback=Integer.parseInt(settings.get("weapon_settings.weapons.axes.knockback").toString());
			}else{
				if ((weapon.getType()==Material.DIAMOND_HOE)||(weapon.getType()==Material.GOLD_HOE)||(weapon.getType()==Material.IRON_HOE)){
					sharp=Integer.parseInt(settings.get("weapon_settings.weapons.hoes.sharpness").toString());
					smite=Integer.parseInt(settings.get("weapon_settings.weapons.hoes.smite").toString());
					BOA=smite=Integer.parseInt(settings.get("weapon_settings.weapons.hoes.BOA").toString());
					fire=Integer.parseInt(settings.get("weapon_settings.weapons.hoes.fire").toString());
					knockback=Integer.parseInt(settings.get("weapon_settings.weapons.hoes.knockback").toString());
				}else{
					if ((weapon.getType()==Material.DIAMOND_PICKAXE)||(weapon.getType()==Material.GOLD_PICKAXE)||(weapon.getType()==Material.IRON_PICKAXE)){
						sharp=Integer.parseInt(settings.get("weapon_settings.weapons.picks.sharpness").toString());
						smite=Integer.parseInt(settings.get("weapon_settings.weapons.picks.smite").toString());
						BOA=smite=Integer.parseInt(settings.get("weapon_settings.weapons.picks.BOA").toString());
						fire=Integer.parseInt(settings.get("weapon_settings.weapons.picks.fire").toString());
						knockback=Integer.parseInt(settings.get("weapon_settings.weapons.picks.knockback").toString());
					}
				}
			}
		}
		if (sharp!=0){
			int sh=rnd.nextInt(sharp+1);
			if (sh!=0){
				weapon.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sh);
			}
		}
		if (smite!=0){
			int sm=rnd.nextInt(smite+1);
			if (sm!=0){
				weapon.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, sm);
			}
		}
		if (BOA!=0){
			int bane=rnd.nextInt(BOA+1);
			if (bane!=0){
				weapon.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, bane);
			}
		}
		if (fire!=0){
			int f=rnd.nextInt(fire+1);
			if (f!=0){
				weapon.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, f);
			}
		}
		if (knockback!=0){
			int kn=rnd.nextInt(knockback+1);
			if (kn!=0){
				weapon.addUnsafeEnchantment(Enchantment.KNOCKBACK, kn);
			}
		}
	}
	public static void AddBowEnchants(ItemStack weapon){
		int power=Integer.parseInt(settings.get("weapon_settings.weapons.bows.power").toString());
		int flame=Integer.parseInt(settings.get("weapon_settings.weapons.bows.flame").toString());
		int punch=Integer.parseInt(settings.get("weapon_settings.weapons.bows.punch").toString());
		if (power!=0){
			int pwr=rnd.nextInt(power+1);
			if (pwr!=0){
				weapon.addEnchantment(Enchantment.ARROW_DAMAGE, pwr);
			}
		}
		if (flame!=0){
			int flm=rnd.nextInt(flame+1);
			if (flm!=0){
				weapon.addEnchantment(Enchantment.ARROW_FIRE, flm);
			}
		}
		if (punch!=0){
			int pnc=rnd.nextInt(punch+1);
			if (pnc!=0){
				weapon.addEnchantment(Enchantment.ARROW_KNOCKBACK, pnc);
			}
		}
	}
	public static void AddArmorEnchant(ItemStack armorpiece){
		int prot=0;
		int proj=0;
		int fire=0;
		int blast=0;
		if ((armorpiece.getType()==Material.DIAMOND_BOOTS)||(armorpiece.getType()==Material.DIAMOND_LEGGINGS)||(armorpiece.getType()==Material.DIAMOND_CHESTPLATE)||(armorpiece.getType()==Material.DIAMOND_HELMET)){
			prot=Integer.parseInt(settings.get("armor_settings.armors.diamond.protection_all").toString());
			proj=Integer.parseInt(settings.get("armor_settings.armors.diamond.protection_projectile").toString());
			fire=Integer.parseInt(settings.get("armor_settings.armors.diamond.protection_fire").toString());
			blast=Integer.parseInt(settings.get("armor_settings.armors.diamond.protection_explosions").toString());
		}else{
			if ((armorpiece.getType()==Material.IRON_BOOTS)||(armorpiece.getType()==Material.IRON_LEGGINGS)||(armorpiece.getType()==Material.IRON_CHESTPLATE)||(armorpiece.getType()==Material.IRON_HELMET)){
				prot=Integer.parseInt(settings.get("armor_settings.armors.iron.protection_all").toString());
				proj=Integer.parseInt(settings.get("armor_settings.armors.iron.protection_projectile").toString());
				fire=Integer.parseInt(settings.get("armor_settings.armors.iron.protection_fire").toString());
				blast=Integer.parseInt(settings.get("armor_settings.armors.iron.protection_explosions").toString());
			}else{
				if ((armorpiece.getType()==Material.GOLD_BOOTS)||(armorpiece.getType()==Material.GOLD_LEGGINGS)||(armorpiece.getType()==Material.GOLD_CHESTPLATE)||(armorpiece.getType()==Material.GOLD_HELMET)){
					prot=Integer.parseInt(settings.get("armor_settings.armors.gold.protection_all").toString());
					proj=Integer.parseInt(settings.get("armor_settings.armors.gold.protection_projectile").toString());
					fire=Integer.parseInt(settings.get("armor_settings.armors.gold.protection_fire").toString());
					blast=Integer.parseInt(settings.get("armor_settings.armors.gold.protection_explosions").toString());
				}else{
					if ((armorpiece.getType()==Material.CHAINMAIL_BOOTS)||(armorpiece.getType()==Material.CHAINMAIL_LEGGINGS)||(armorpiece.getType()==Material.CHAINMAIL_CHESTPLATE)||(armorpiece.getType()==Material.CHAINMAIL_HELMET)){
						prot=Integer.parseInt(settings.get("armor_settings.armors.chain.protection_all").toString());
						proj=Integer.parseInt(settings.get("armor_settings.armors.chain.protection_projectile").toString());
						fire=Integer.parseInt(settings.get("armor_settings.armors.chain.protection_fire").toString());
						blast=Integer.parseInt(settings.get("armor_settings.armors.chain.protection_explosions").toString());
					}else{
						if ((armorpiece.getType()==Material.LEATHER_BOOTS)||(armorpiece.getType()==Material.LEATHER_LEGGINGS)||(armorpiece.getType()==Material.LEATHER_CHESTPLATE)||(armorpiece.getType()==Material.LEATHER_HELMET)){
							prot=Integer.parseInt(settings.get("armor_settings.armors.leather.protection_all").toString());
							proj=Integer.parseInt(settings.get("armor_settings.armors.leather.protection_projectile").toString());
							fire=Integer.parseInt(settings.get("armor_settings.armors.leather.protection_fire").toString());
							blast=Integer.parseInt(settings.get("armor_settings.armors.leather.protection_explosions").toString());
						}
					}
				}
			}
		}
		if (prot!=0){
			int prt=rnd.nextInt(prot+1);
			if (prt!=0){
				armorpiece.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, prt);
			}
		}
		if (proj!=0){
			int pr=rnd.nextInt(proj+1);
			if (pr!=0){
				armorpiece.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, pr);
			}
		}
		if (fire!=0){
			int f=rnd.nextInt(fire+1);
			if (f!=0){
				armorpiece.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, f);
			}
		}
		if (blast!=0){
			int bl=rnd.nextInt(blast+1);
			if (bl!=0){
				armorpiece.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, bl);
			}
		}
	}
	public static void startSpawn(){
		Bukkit.getServer().broadcastMessage(ChatColor.RED+settings.get("messages.open_message").toString());
		final int particles = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(doombox.getInstance(), new Runnable(){
			public void run(){
				loc.getWorld().playEffect(loc,Effect.MOBSPAWNER_FLAMES, 5000);
			}
		}, 40, 0);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
				public void run(){
					Bukkit.getServer().getScheduler().cancelTask(particles);
					loc.getWorld().createExplosion(loc,0.0F,false);
					handleSpawn();}
		},20*5);
	}
	private static Plugin getInstance() {
		return instance;
	}
}
