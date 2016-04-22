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
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
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
	static int runs=0;
	static boolean triggered = false;
	//-------------------------------------------------------------
	@Override 
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new BlockBreakerListener(), this);
		getServer().getPluginManager().registerEvents(new MobDeathListener(), this);
		getServer().getPluginManager().registerEvents(new BossHitListener(), this);
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
    				loc.getBlock().setType(Material.CHEST);
    				triggered = false;
    				this.getServer().broadcastMessage(ChatColor.GOLD+settings.get("messages.summon_message").toString());
    			}
    		}else{
    			sender.sendMessage("You can't use this command in this world");
    		}
    		return true;
    	}else{
    		if (cmd.getName().equals("doombox")) {
    			if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("doombox.reload")){
    				settings=null;
    				this.reloadConfig();
    				settings=(HashMap<String, Object>) getConfig().getValues(true);
    				getLogger().info("DoomBox Settings Reloaded.");
    				sender.sendMessage("DoomBox has been reloaded");
    			}else{
    				if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("version")){
    					sender.sendMessage("DoomBox");
    					sender.sendMessage("Proudly brought to you by: Penaz");
    					sender.sendMessage("--------------------------------------");
    					sender.sendMessage("This plugin is Free (as in freedom) software,");
    					sender.sendMessage("feel free to fork the plugin and edit it, as long as");
    					sender.sendMessage("you quote its original author.");
    				}
    			}
    			return true;
    		}
    	}
    	return false;
	}
	public static void handleSpawn(){
		elist=new ArrayList<Entity>();
		boolean randomizer=settings.get("mob_settings.randomizer").toString().equalsIgnoreCase("true");
		if (randomizer){
			int nummobs=Integer.parseInt(settings.get("mob_settings.totalmobs").toString());
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
			}
		}else{
			int zombies=Integer.parseInt(settings.get("mob_settings.mobs.Zombies").toString());
			int skeletons=Integer.parseInt(settings.get("mob_settings.mobs.Skeletons").toString());;
			int pigmen=Integer.parseInt(settings.get("mob_settings.mobs.Pigmen").toString());;
			int ghasts=Integer.parseInt(settings.get("mob_settings.mobs.Ghasts").toString());;
			int witches=Integer.parseInt(settings.get("mob_settings.mobs.Witches").toString());;
			int creepers=Integer.parseInt(settings.get("mob_settings.mobs.Creepers").toString());;
			int spiders=Integer.parseInt(settings.get("mob_settings.mobs.Spiders").toString());;
			int endermen=Integer.parseInt(settings.get("mob_settings.mobs.Endermen").toString());;
			int blazes=Integer.parseInt(settings.get("mob_settings.mobs.Blazes").toString());;
			int slimes=Integer.parseInt(settings.get("mob_settings.mobs.Slimes").toString());;
			int cavespiders=Integer.parseInt(settings.get("mob_settings.mobs.CaveSpiders").toString());;
			int magma_cubes=Integer.parseInt(settings.get("mob_settings.mobs.MagmaCubes").toString());;
			int wither_skeletons=Integer.parseInt(settings.get("mob_settings.mobs.WitherSkeletons").toString());;
			//create Zombies
			for (int i=0;i<zombies;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.ZOMBIE));
			}
			//create Skeletons
			for (int i=0;i<skeletons;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.SKELETON));
			}
			//create Pigmen
			for (int i=0;i<pigmen;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.PIG_ZOMBIE));
			}
			//create Ghasts
			for (int i=0;i<ghasts;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.GHAST));
			}
			//create Witches
			for (int i=0;i<witches;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.WITCH));
			}
			//create Creepers
			for (int i=0;i<creepers;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.CREEPER));
			}
			//create Spider
			for (int i=0;i<spiders;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.SPIDER));
			}
			//create endermen
			for (int i=0;i<endermen;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.ENDERMAN));
			}
			//create blazes
			for (int i=0;i<blazes;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.BLAZE));
			}
			//create Slimes
			for (int i=0;i<slimes;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.SLIME));
			}
			//create CaveSpiders
			for (int i=0;i<cavespiders;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.CAVE_SPIDER));
			}
			//create magma cubes
			for (int i=0;i<magma_cubes;i++){
				elist.add(loc.getWorld().spawnEntity(loc,EntityType.MAGMA_CUBE));
			}
			//create wither skeletons
			ArrayList<Entity> with=new ArrayList<Entity>();
			for (int i=0;i<wither_skeletons;i++){
				with.add(loc.getWorld().spawnEntity(loc,EntityType.SKELETON));
				Skeleton skelly=(Skeleton) with.get(i);
				skelly.setSkeletonType(SkeletonType.WITHER);
			}
			elist.addAll(with);
		}
		//Armor entities and launch them
		ArrayList<EntityType> Armorable=new ArrayList<EntityType>(Arrays.asList(EntityType.ZOMBIE,EntityType.SKELETON,EntityType.PIG_ZOMBIE));
		for (int i=0;i<elist.size();i++){
			elist.get(i).setVelocity(new Vector(rnd.nextInt(2),1,rnd.nextInt(2)));
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
	public static void handleDeath(){
		System.out.println("A mob in the list died");
		if (elist.isEmpty()){
			boolean boss=settings.get("boss.enabled").toString().equalsIgnoreCase("true");
			if (boss){
				if (runs<Integer.parseInt(settings.get("boss.runs").toString())){
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN+settings.get("messages.end_message").toString());
					loc.getBlock().setType(Material.AIR);
					carpetloc.getBlock().setType(Material.AIR);
				}else{
					boss();
				}
			}else{
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN+settings.get("messages.end_message").toString());
				loc.getBlock().setType(Material.AIR);
				carpetloc.getBlock().setType(Material.AIR);
			}
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
		boolean random=settings.get("weapon_settings.randomizer").toString().equals("true");
		if (random){
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
		}else{
			if (sharp!=0){
				weapon.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharp);
			}
			if (smite!=0){
				weapon.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, smite);
			}
			if (BOA!=0){
				weapon.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, BOA);
			}
			if (fire!=0){
				weapon.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, fire);
			}
			if (knockback!=0){
				weapon.addUnsafeEnchantment(Enchantment.KNOCKBACK, knockback);
			}
		}
	}
	public static void AddBowEnchants(ItemStack weapon){
		int power=Integer.parseInt(settings.get("weapon_settings.weapons.bows.power").toString());
		int flame=Integer.parseInt(settings.get("weapon_settings.weapons.bows.flame").toString());
		int punch=Integer.parseInt(settings.get("weapon_settings.weapons.bows.punch").toString());
		boolean random=settings.get("weapon_settings.randomizer").toString().equals("true");
		if (random){
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
		}else{
			if (power!=0){
				weapon.addEnchantment(Enchantment.ARROW_DAMAGE, power);
			}
			if (flame!=0){
				weapon.addEnchantment(Enchantment.ARROW_FIRE, flame);
			}
			if (punch!=0){
				weapon.addEnchantment(Enchantment.ARROW_KNOCKBACK, punch);
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
		boolean random=settings.get("armor_settings.randomizer").toString().equalsIgnoreCase("true");
		if (random){
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
		}else{
			if (prot!=0){
				armorpiece.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, prot);
			}
			if (proj!=0){
				armorpiece.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, proj);
			}
			if (fire!=0){
				armorpiece.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, fire);
			}
			if (blast!=0){
				armorpiece.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, blast);
			}
		}
	}
	public static void startSpawn(){
		Bukkit.getServer().broadcastMessage(ChatColor.RED+settings.get("messages.open_message").toString());
		runs++;
		final int particles = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(doombox.getInstance(), new Runnable(){
			public void run(){
				loc.getWorld().playEffect(loc,Effect.MOBSPAWNER_FLAMES, 5000);
			}
		}, 40, 0);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
				public void run(){
					Bukkit.getServer().getScheduler().cancelTask(particles);
					loc.getWorld().createExplosion(loc,0.0F,false);
					loc.getBlock().setType(Material.AIR);
					handleSpawn();}
		},20*5);
	}
	private static Plugin getInstance() {
		return instance;
	}
	private static void boss(){
		final int particles = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(doombox.getInstance(), new Runnable(){
			public void run(){
				loc.getWorld().playEffect(loc,Effect.MOBSPAWNER_FLAMES, 5000);
			}
		}, 0, 10);
		Bukkit.getServer().broadcastMessage(parseFormat(settings.get("boss.messages.corrupted_end_message").toString()));
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
			public void run(){
				Bukkit.getServer().broadcastMessage(parseFormat(settings.get("boss.messages.corrupted_message1").toString()));
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
					public void run(){
						Bukkit.getServer().broadcastMessage(parseFormat(settings.get("boss.messages.corrupted_message2").toString()));
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
							public void run(){
								Bukkit.getServer().broadcastMessage(parseFormat(settings.get("boss.messages.corrupted_message3").toString()));
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
									public void run(){
										Bukkit.getServer().broadcastMessage(parseFormat(settings.get("boss.messages.corrupted_message4").toString()));
											loc.getWorld().playSound(loc, Sound.WITHER_IDLE, 10, 1);
											Bukkit.getServer().getScheduler().cancelTask(particles);
											StartBoss();
									}},20);
								}},30);
							}},40);
					}},100);
	}
	public static void StartBoss(){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
		public void run(){
			loc.getWorld().createExplosion(loc,0.0F,false);
			//more
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
				public void run(){
					loc.getWorld().strikeLightning(loc);
					//more
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
						public void run(){
							loc.getWorld().strikeLightning(loc);
							loc.getWorld().strikeLightning(loc);
							//more
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(doombox.getInstance(), new Runnable(){
								public void run(){
									runs=0;
									loc.getWorld().strikeLightning(loc);
									loc.getWorld().strikeLightning(loc);
									loc.getWorld().strikeLightning(loc);
									loc.getWorld().createExplosion(loc,0.0F,false);
									Bukkit.getServer().broadcastMessage(ChatColor.GOLD+settings.get("boss.name").toString()+": "+parseFormat(settings.get("boss.messages.boss_message").toString()));
									LivingEntity boss=(LivingEntity) loc.getWorld().spawnEntity(loc,EntityType.SKELETON);
									loc.getBlock().setType(Material.AIR);
									carpetloc.getBlock().setType(Material.AIR);
									Skeleton skelly=(Skeleton) boss;
									skelly.setSkeletonType(SkeletonType.WITHER);
									boss.setCustomName(settings.get("boss.name").toString());
									boss.setMaxHealth(Integer.parseInt(settings.get("boss.health").toString()));
									boss.setHealth(boss.getMaxHealth());
									int str=Integer.parseInt(settings.get("boss.effects.increase_damage").toString());
									int res=Integer.parseInt(settings.get("boss.effects.resistance").toString());
									int spd=Integer.parseInt(settings.get("boss.effects.speed").toString());
									if (str!=0){
										boss.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,999999,str));
									}
									if (res!=0){
										boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,999999,res));
									}
									if (spd!=0){
										boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,999999,spd));	
									}
									EntityEquipment ee=boss.getEquipment();
									ItemStack chest=new ItemStack(Material.DIAMOND_CHESTPLATE,1);
									ItemStack legs=new ItemStack(Material.DIAMOND_LEGGINGS,1);
									ItemStack hat=new ItemStack(Material.DIAMOND_HELMET,1);
									ItemStack boots=new ItemStack(Material.DIAMOND_BOOTS,1);
									ArrayList<ItemStack> gear=new ArrayList<ItemStack>();
									gear.add(chest);
									gear.add(legs);
									gear.add(hat);
									gear.add(boots);
									for (ItemStack item: gear){
										int prot=Integer.parseInt(settings.get("boss.armor.protection_all").toString());
										if (prot!=0){
											item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, prot);
										}
										int proj=Integer.parseInt(settings.get("boss.armor.protection_projectile").toString());
										if (proj!=0){
											item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, proj);
										}
										int fire=Integer.parseInt(settings.get("boss.armor.protection_fire").toString());
										if (fire!=0){
											item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, fire);
										}
										int blast=Integer.parseInt(settings.get("boss.armor.protection_explosions").toString());
										if (blast!=0){
											item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, blast);
										}
									}
									ee.setBoots(boots);
									ee.setLeggings(legs);
									ee.setChestplate(chest);
									ee.setHelmet(hat);
									ItemStack weapon=new ItemStack(Material.DIAMOND_AXE,1);
									int sharp=Integer.parseInt(settings.get("boss.weapon.sharpness").toString());
									int smite=Integer.parseInt(settings.get("boss.weapon.smite").toString());;
									int BOA=Integer.parseInt(settings.get("boss.weapon.BOA").toString());;
									int fire=Integer.parseInt(settings.get("boss.weapon.fire").toString());;
									int knockback=Integer.parseInt(settings.get("boss.weapon.knockback").toString());;
									if (sharp!=0){
										weapon.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharp);
									}
									if (smite!=0){
										weapon.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, smite);
									}
									if (BOA!=0){
										weapon.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, BOA);
									}
									if (fire!=0){
										weapon.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, fire);
									}
									if (knockback!=0){
										weapon.addUnsafeEnchantment(Enchantment.KNOCKBACK, knockback);
									}
									ee.setItemInHand(weapon);
									//more
								}},20);
							}},30);
						}},40);
				}},40);		
	}
	public static String parseFormat(String newline){
		newline = newline.replaceAll("&0", ChatColor.BLACK + "");
        newline = newline.replaceAll("&1", ChatColor.DARK_BLUE + "");
        newline = newline.replaceAll("&2", ChatColor.DARK_GREEN + "");
        newline = newline.replaceAll("&3", ChatColor.DARK_AQUA + "");
        newline = newline.replaceAll("&4", ChatColor.DARK_RED + "");
        newline = newline.replaceAll("&5", ChatColor.DARK_PURPLE + "");
        newline = newline.replaceAll("&6", ChatColor.GOLD + "");
        newline = newline.replaceAll("&7", ChatColor.GRAY + "");
        newline = newline.replaceAll("&8", ChatColor.DARK_GRAY+ "");
        newline = newline.replaceAll("&9", ChatColor.BLUE + "");
        newline = newline.replaceAll("&a", ChatColor.GREEN + "");
        newline = newline.replaceAll("&b", ChatColor.AQUA + "");
        newline = newline.replaceAll("&c", ChatColor.RED + "");
        newline = newline.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
        newline = newline.replaceAll("&e", ChatColor.YELLOW + "");
        newline = newline.replaceAll("&f", ChatColor.WHITE + "");
        newline = newline.replaceAll("&k", ChatColor.MAGIC + "");
        newline = newline.replaceAll("&l", ChatColor.BOLD + "");
        newline = newline.replaceAll("&o", ChatColor.ITALIC + "");
        newline = newline.replaceAll("&n", ChatColor.UNDERLINE + "");
        newline = newline.replaceAll("&m", ChatColor.STRIKETHROUGH + "");
        newline = newline.replaceAll("&r", ChatColor.RESET + "");
        return newline;
	}
	public static void BossPort(Entity boss, Entity Damager){
		if (settings.get("boss.anti-knockback").toString().equalsIgnoreCase("true")){
			if (rnd.nextInt(Integer.parseInt(settings.get("boss.chance").toString()))+1==1){
				if (Damager.getType()==EntityType.ARROW){
					Arrow arrow=(Arrow) Damager;
					if (arrow.getShooter() instanceof Player){
						Player source=(Player) arrow.getShooter();
						source.teleport(boss);
					}
				}
				boss.teleport(Damager);
			}
		}
	}
}