package io.github.monterxto.naruto;

import java.io.File;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class Conexao {
	public static Connection con = null;
	
	public static void openConnection(){
		
		File file = new File(JavaPlugin.getPlugin(Main.class).getDataFolder(), "banco_de_dados.db");
		String url = "jdbc:sqlite:" + file;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			criarTabela();
		} catch (Exception e) {
			System.out.println("Não foi possivel se conectar ao banco de dados");
		}
	}
	
	public static void criarTabela() {
		PreparedStatement stm = null;
		Statement col = null;
		
		try {
			stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS player (nome VARCHAR,modo1 INT)");
			stm.execute();
			stm.close();
			col = con.createStatement();
			ResultSet colunas = col.executeQuery("pragma table_info(player)");
			int size = -1;
			while(colunas.next()) {
				size++;
				System.out.println(colunas.getString(2));
			}
			System.out.println(size);
			int quantidade = JavaPlugin.getPlugin(Main.class).getConfig().getInt("Modos.quantidade");
			if(size<quantidade) {
				for(int i = size+1;i<=quantidade;i++) {
					stm = con.prepareStatement("ALTER TABLE player ADD modo"+i+" INT");
					stm.execute();
				}
			}
			stm.close();
			col.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Não foi possivel criar as tabelas");
			JavaPlugin.getPlugin(Main.class).getPluginLoader().disablePlugin(JavaPlugin.getPlugin(Main.class));
		}
	}
	
	public static void fechar() {
		if(con != null) {
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
				System.out.println("Fechandooo");
			}
		}
	}
}
