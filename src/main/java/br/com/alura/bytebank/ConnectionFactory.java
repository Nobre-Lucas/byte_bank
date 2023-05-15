package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionFactory {

	Dotenv dotenv = Dotenv.configure().load();
	String user = dotenv.get("SQL_USER");
	String password = dotenv.get("SQL_PASSWORD");

	public Connection conecta() {
		try {

			return createDataSource().getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private HikariDataSource createDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
		config.setUsername(user);
		config.setPassword(password);
		config.setMaximumPoolSize(10);

		return new HikariDataSource(config);
	}

}
