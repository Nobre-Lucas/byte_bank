package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionFactory {

	public Connection conecta() {
		try {
			Dotenv dotenv = Dotenv.configure().load();
			String user = dotenv.get("SQL_USER");
			String password = dotenv.get("SQL_PASSWORD");
			return DriverManager
					.getConnection("jdbc:mysql://localhost:3306/byte_bank?user=" + user + "&password=" + password);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
