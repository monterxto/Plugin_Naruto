package io.github.monterxto.naruto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Eventos implements Listener{
	private int valor;
	private ArrayList atri_pontos;
	private int contador = 0;

	@EventHandler
	public void aoClicar(InventoryClickEvent evento) {
		if(evento.getCurrentItem() == null || evento.getCurrentItem().getItemMeta() == null || evento.getCurrentItem().getItemMeta().getDisplayName() == null){
			return;
		}
		Player p = (Player)evento.getWhoClicked();
		if(evento.getWhoClicked() instanceof Player) {
			Plugin pl = JavaPlugin.getPlugin(Main.class);
			String bloco_sair = pl.getConfig().getString("Modos.sair_modo.nome");
			if(evento.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', bloco_sair))) {
				evento.setCancelled(true);
				PreparedStatement setar = null;
				Statement usando = null;
				atri_pontos = new ArrayList();
				atri_pontos.add(0, "Strength");
				atri_pontos.add(1, "Dexterity");
				atri_pontos.add(2, "Constitution");
				atri_pontos.add(3, "Willpower");
				atri_pontos.add(4, "Mind");
				atri_pontos.add(5, "Concentration");
				for(int k = 1;k<=pl.getConfig().getInt("Modos.quantidade");k++) {
					try {
						usando = Conexao.con.createStatement();
						ResultSet esta_modo = usando.executeQuery("SELECT modo"+k+" FROM player WHERE nome = '"+p.getName()+"'");
						setar = Conexao.con.prepareStatement("UPDATE player SET modo"+k+" = 0 WHERE nome = '"+p.getName()+"'");
						setar.execute();
						if(esta_modo.getInt(1) == 1) {
							contador = 1;
							ArrayList atributos = (ArrayList) pl.getConfig().getStringList("Modos.modo"+k+".atributos");
							ArrayList valor = (ArrayList) pl.getConfig().getStringList("Modos.modo"+k+".valor");
							for(int i = 0; i<atributos.size();i++) {
								for(int j = 0; j < 6; j++) {
									if(atri_pontos.get(j).equals(atributos.get(i))) {
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "jrmca Add "+ atributos.get(i).toString() + " -" + valor.get(i).toString() + " " + p.getName());
										break;
									}
								}
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("Não consegui tirar o player do modo");
					}
				}
				try {
					usando.close();
					setar.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if(contador==1) {
					contador = 0;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Mensagens.sair_modos")));
				}else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Mensagens.sem_modos")));					
				}
				
				p.closeInventory();
			}
			for(int i = 1;i<=pl.getConfig().getInt("Modos.quantidade");i++) {
				String nome = pl.getConfig().getString("Modos.modo"+i+".nome");
				if(evento.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', nome))) {
						evento.setCancelled(true);
						if(p.hasPermission("monter.modo"+i)) {
							Statement usando = null;
							PreparedStatement setar = null;
							try {
								usando = Conexao.con.createStatement();
								ResultSet exist = usando.executeQuery("SELECT count(nome) FROM player WHERE nome = '"+p.getName()+"'");
								if(exist.getInt(1) != 1) {
									setar = Conexao.con.prepareStatement("INSERT INTO player (nome) VALUES ('"+p.getName()+"')");
									setar.execute();
									for(int k = 1;k<=pl.getConfig().getInt("Modos.quantidade");k++) {
										setar = Conexao.con.prepareStatement("UPDATE player SET modo"+k+" = 0 WHERE nome = '"+p.getName()+"'");
										setar.execute();
									}
									setar.close();
								}
								ResultSet result = usando.executeQuery("SELECT modo"+i+" FROM player WHERE nome = '"+ p.getName()+"'");
								valor = result.getInt(1);
								usando.close();
							} catch (SQLException e) {
								e.printStackTrace();
								System.out.println("Nao Consegui Buscar Se o Player já esta usando o modo");
							}
							if(valor == 0) {
								ArrayList atributos = new ArrayList();
								atributos = (ArrayList) pl.getConfig().getStringList("Modos.modo"+i+".atributos");
								ArrayList valor = (ArrayList) pl.getConfig().getStringList("Modos.modo"+i+".valor");
								String mensagem = pl.getConfig().getString("Mensagens.escolhido").replace("{modo}", pl.getConfig().getString("Modos.modo"+i+".nome"));
								for(int j = 0;j<atributos.size();j++) {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "jrmca Add "+ atributos.get(j).toString() + " " + valor.get(j).toString() + " " + p.getName());
								}
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensagem));
								
								try {
									setar = Conexao.con.prepareStatement("UPDATE player SET modo"+i+" = 1 WHERE nome = '"+p.getName()+"'");
									setar.execute();
									setar.close();
								} catch (SQLException e) {
									e.printStackTrace();
									System.out.println("Nao Consegui Buscar Se o Player já esta usando o modo");
								}
							}else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Mensagens.usando")));
							}
							p.closeInventory();
						}else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Mensagens.sem_permissao")));
						}
				}
			}
		}
	}
}
