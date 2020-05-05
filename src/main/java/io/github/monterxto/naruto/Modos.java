package io.github.monterxto.naruto;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Modos implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("modos")) {
			Plugin pl = JavaPlugin.getPlugin(Main.class);
			String titulo = pl.getConfig().getString("Modos.titulo_inventario");
			Inventory inv = Bukkit.createInventory(null, 6 * 9, ChatColor.translateAlternateColorCodes('&', titulo));
			player.openInventory(inv);
			for(int i = 1;i<=pl.getConfig().getInt("Modos.quantidade");i++) {
				String nome = pl.getConfig().getString("Modos.modo"+i+".nome");
				String lore = pl.getConfig().getString("Modos.modo"+i+".lore");
				int posicao = pl.getConfig().getInt("Modos.modo"+i+".posicao");
				int bloco = pl.getConfig().getInt("Modos.modo"+i+".bloco");
				ItemStack item_modo1 = new ItemStack(bloco);
				ItemMeta meta_modo1 = item_modo1.getItemMeta();
				meta_modo1.setDisplayName(ChatColor.translateAlternateColorCodes('&', nome));
				ArrayList<String> lore_modo1 = new ArrayList<String>();
				lore_modo1.add(ChatColor.translateAlternateColorCodes('&', lore));
				meta_modo1.setLore(lore_modo1);
				item_modo1.setItemMeta(meta_modo1);
				inv.setItem(posicao, item_modo1);
			}
			String nome_sair = pl.getConfig().getString("Modos.sair_modo.nome");
			String lore_sair = pl.getConfig().getString("Modos.sair_modo.lore");
			int posicao_sair = pl.getConfig().getInt("Modos.sair_modo.posicao");
			int bloco_sair = pl.getConfig().getInt("Modos.sair_modo.bloco");
			ItemStack sair_modo = new ItemStack(bloco_sair);
			ItemMeta meta_sair_modo = sair_modo.getItemMeta();
			meta_sair_modo.setDisplayName(ChatColor.translateAlternateColorCodes('&', nome_sair));
			ArrayList<String> sair_lore_modo = new ArrayList<String>();
			sair_lore_modo.add(ChatColor.translateAlternateColorCodes('&', lore_sair));
			meta_sair_modo.setLore(sair_lore_modo);
			sair_modo.setItemMeta(meta_sair_modo);
			inv.setItem(posicao_sair, sair_modo);
			return true;
		}
		return false; 
	}
}
