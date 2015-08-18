package DoomBox.doombox;

import java.util.ArrayList;
import java.util.Random;

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
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class doombox extends JavaPlugin {
	static Random rnd=new Random();
	static ArrayList<Entity> elist=new ArrayList<Entity>();
	/* Settings area
	 * ------------------------------------------------------------
	 */
	static Location loc=null;
	static Location carpetloc=null;
	static String wld="world";
	static boolean EnhancedAttack=true;
	static int protectionLvl=0;
	static int AttackLvl=1;
	//-------------------------------------------------------------
	@Override 
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new BlockBreakerListener(), this);
		getServer().getPluginManager().registerEvents(new MobDeathListener(), this);
		getLogger().info("DoomBox Loaded Successfully");
	 }
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		getLogger().info("DoomBox Unloaded Successfully");
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("summonbox")) {
    		World world=getServer().getWorld(wld);
    		Player p= (Player) sender;
    		if (!(world==null)){
    			if (!(world.equals(p.getWorld()))){
    				sender.sendMessage("You can't use this command in this world");
    			}else{
    				loc = new Location(p.getWorld(),0,10,0);
    				loc.getBlock().setType(Material.CAULDRON);
    				carpetloc=loc.add(0,1,0);
    				carpetloc.getBlock().setType(Material.CARPET);
    			}
    		}else{
    			sender.sendMessage("You can't use this command in this world");
    		}
    		return true;
    	}
    	return false;
	}
	public static void handleSpawn(){
		elist=new ArrayList<Entity>();
		for (int i =0;i<20;i++){
			elist.add(loc.getWorld().spawnEntity(loc,EntityType.ZOMBIE));
			elist.get(i).setVelocity(new Vector(rnd.nextInt(2),1,rnd.nextInt(2)));
			int dice=rnd.nextInt(6);
			switch (dice){
				case(0):giveEmptyKit(elist.get(i));break;
				case(1):giveIronKit(elist.get(i));break;
				case(2):giveChainKit(elist.get(i));break;
				case(3):giveLeatherKit(elist.get(i));break;
				case(4):giveGoldKit(elist.get(i));break;
				case(5):giveDiamondKit(elist.get(i));break;
			}
		}
	}
	public static void handleDeath(){
		System.out.println("A mob in the list died");
		if (elist.isEmpty()){
			System.out.println("All mobs in the list died");
			//non funzionante
			loc.getBlock().setType(Material.AIR);
			carpetloc.getBlock().setType(Material.AIR);
		}
	}
	public static void giveDiamondKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		protectionLvl=4;
		ItemStack chest=new ItemStack(Material.DIAMOND_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.DIAMOND_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.DIAMOND_HELMET,1);
		ItemStack boots=new ItemStack(Material.DIAMOND_BOOTS,1);
		ItemStack hand=new ItemStack(Material.DIAMOND_SWORD,1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		hat.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		int level=rnd.nextInt(5);
		if (level!=0){
			hand.addEnchantment(Enchantment.DAMAGE_ALL, level);
		}
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		if (EnhancedAttack){
			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,5000,AttackLvl));
		}
	}
	public static void giveChainKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		protectionLvl=4;
		ItemStack chest=new ItemStack(Material.CHAINMAIL_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.CHAINMAIL_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.CHAINMAIL_HELMET,1);
		ItemStack boots=new ItemStack(Material.CHAINMAIL_BOOTS,1);
		ItemStack hand=new ItemStack(Material.IRON_SWORD,1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		hat.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		int level=rnd.nextInt(5);
		if (level!=0){
			hand.addEnchantment(Enchantment.DAMAGE_ALL, level);
		}
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		if (EnhancedAttack){
			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,5000,AttackLvl));
		}
	}
	public static void giveEmptyKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		protectionLvl=4;
		ItemStack hand=new ItemStack(Material.DIAMOND_SWORD,1);
		int level=rnd.nextInt(5);
		if (level!=0){
			hand.addEnchantment(Enchantment.DAMAGE_ALL, level);
		}
		ee.setItemInHand(hand);
		le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,5000,3));
		if (EnhancedAttack){
			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,5000,AttackLvl));
		}
	}
	public static void giveIronKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		protectionLvl=4;
		ItemStack chest=new ItemStack(Material.IRON_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.IRON_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.IRON_HELMET,1);
		ItemStack boots=new ItemStack(Material.IRON_BOOTS,1);
		ItemStack hand=new ItemStack(Material.IRON_SWORD,1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		hat.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		int level=rnd.nextInt(5);
		if (level!=0){
			hand.addEnchantment(Enchantment.DAMAGE_ALL, level);
		}
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		if (EnhancedAttack){
			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,5000,AttackLvl));
		}
	}
	public static void giveLeatherKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		protectionLvl=4;
		ItemStack chest=new ItemStack(Material.LEATHER_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.LEATHER_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.LEATHER_HELMET,1);
		ItemStack boots=new ItemStack(Material.LEATHER_BOOTS,1);
		ItemStack hand=new ItemStack(Material.IRON_SWORD,1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		hat.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		int level=rnd.nextInt(5);
		if (level!=0){
			hand.addEnchantment(Enchantment.DAMAGE_ALL, level);
		}
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		if (EnhancedAttack){
			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,5000,AttackLvl));
		}
	}
	public static void giveGoldKit(Entity e){
		LivingEntity le=((LivingEntity) e);
		EntityEquipment ee=le.getEquipment();
		protectionLvl=4;
		ItemStack chest=new ItemStack(Material.GOLD_CHESTPLATE,1);
		ItemStack legs=new ItemStack(Material.GOLD_LEGGINGS,1);
		ItemStack hat=new ItemStack(Material.GOLD_HELMET,1);
		ItemStack boots=new ItemStack(Material.GOLD_BOOTS,1);
		ItemStack hand=new ItemStack(Material.GOLD_SWORD,1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		hat.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLvl);
		int level=rnd.nextInt(5);
		if (level!=0){
			hand.addEnchantment(Enchantment.DAMAGE_ALL, level);
		}
		ee.setChestplate(chest);
		ee.setHelmet(hat);
		ee.setLeggings(legs);
		ee.setBoots(boots);
		ee.setItemInHand(hand);
		if (EnhancedAttack){
			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,5000,AttackLvl));
		}
	}
}
