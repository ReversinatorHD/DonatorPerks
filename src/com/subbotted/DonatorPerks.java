package com.subbotted;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.subbotted.utils.PotionUtils;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;

/**
 * The MIT License
 * Copyright (c) 2016-2017 subbotted
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

public class DonatorPerks extends JavaPlugin implements Listener{


	@Override
	public void onEnable(){
		if(!setupPermissions()){
			getLogger().severe(ChatColor.RED + "You must have Vault installed to use this plugin.");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		enableBroadcastTask();
	}

	@Override
	public void onDisable(){
		getLogger().info(ChatColor.YELLOW + "Disabling DonatorPerks v1.0");
	}

	@SuppressWarnings("deprecation")
	public void doBroadcast(){
		ArrayList<String> onlineDonators = new ArrayList<String>();
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(permission.getPrimaryGroup(player) == getConfig().getString("donator_broadcast_group")){
				onlineDonators.add(player.getName());
			}
		}
		String rankName = getConfig().getString("donator_rank_name");
		Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("donator_broadcast_message").replaceAll("<rankname>", rankName).replaceAll("<donators>", onlineDonators.size() > 0 ? Strings.join(onlineDonators, ",") : "None").replaceAll("<newline>", "\n").replaceAll("<storeurl>", getConfig().getString("donation_store_url"))));
	}

	public void enableBroadcastTask(){
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			public void run(){
				doBroadcast();
			}

		}, 0L, getConfig().getLong("donator_broadcast_delay") * 20L);
	}

	public static Permission permission = null;

	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}



	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		if(cmd.getName().equalsIgnoreCase("donatorperks")){

			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
				return true;
			}

			Player player = (Player) sender;
			if(args.length == 0){
				sender.sendMessage(ChatColor.RED + "This is an open source plugin by subbotted. Usage of this command is " + ChatColor.ITALIC + '/' + label + " [speed/strength/haste/nightvision]");
			}else{

				PotionUtils potion = new PotionUtils(player);
				switch(args[0].toLowerCase()){ //switch instead of using if(args[0].equalsIgnoreCase(string)){} , cleaner code :)

				case "speed":
					if(!player.hasPermission("donatorperk.speed")){
						player.sendMessage(ChatColor.RED + "You do not have permission to use this perk.");
						return true;
					}
					if(!player.hasPotionEffect(PotionEffectType.SPEED)){
						potion.addEffect(PotionEffectType.SPEED);
					}else{
						potion.removeEffect(PotionEffectType.SPEED);
					}
					sender.sendMessage((player.hasPotionEffect(PotionEffectType.SPEED) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + ChatColor.YELLOW + " donator perk " + ChatColor.GREEN + ChatColor.BOLD + "Speed" + ChatColor.YELLOW + '.');
					break;
				case "strength":
					if(!player.hasPermission("donatorperk.strength")){
						player.sendMessage(ChatColor.RED + "You do not have permission to use this perk.");
						return true;
					}
					if(!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
						potion.addEffect(PotionEffectType.INCREASE_DAMAGE);
					}else{
						potion.removeEffect(PotionEffectType.INCREASE_DAMAGE);
					}
					sender.sendMessage((player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + ChatColor.YELLOW + " donator perk " + ChatColor.RED + ChatColor.BOLD + "Strength" + ChatColor.YELLOW + '.');
					break;
				case "haste":
					if(!player.hasPermission("donatorperk.haste")){
						player.sendMessage(ChatColor.RED + "You do not have permission to use this perk.");
						return true;
					}
					if(!player.hasPotionEffect(PotionEffectType.FAST_DIGGING)){
						potion.addEffect(PotionEffectType.FAST_DIGGING);
					}else{
						potion.removeEffect(PotionEffectType.FAST_DIGGING);
					}
					sender.sendMessage((player.hasPotionEffect(PotionEffectType.FAST_DIGGING) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + ChatColor.YELLOW + " donator perk " + ChatColor.GOLD + ChatColor.BOLD + "Haste" + ChatColor.YELLOW + '.');
					break;
				case "nightvision":
					if(!player.hasPermission("donatorperk.nightvision")){
						player.sendMessage(ChatColor.RED + "You do not have permission to use this perk.");
						return true;
					}
					if(!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
						potion.addEffect(PotionEffectType.NIGHT_VISION);
					}else{
						potion.removeEffect(PotionEffectType.NIGHT_VISION);
					}
					sender.sendMessage((player.hasPotionEffect(PotionEffectType.NIGHT_VISION) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + ChatColor.YELLOW + " donator perk " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Night Vision" + ChatColor.YELLOW + '.');
					break;
				default:
					sender.sendMessage(ChatColor.YELLOW + "Could not find donator perk " + ChatColor.DARK_RED + ChatColor.BOLD + args[0] + ChatColor.YELLOW + '.');
					break;

				}

			}

		}

		return true;
	}
}
