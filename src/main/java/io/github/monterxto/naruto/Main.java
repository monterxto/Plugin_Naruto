package io.github.monterxto.naruto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new Eventos(), this);
		getCommand("modos").setExecutor(new Modos());
		if(!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		Conexao.openConnection();
	}
	
	@Override
	public void onDisable() {
		Conexao.fechar();
		getLogger().info("onDisable foi invocado!");
	}
}
