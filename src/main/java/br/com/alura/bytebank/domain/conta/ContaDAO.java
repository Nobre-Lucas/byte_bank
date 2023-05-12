package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.Cliente;

public class ContaDAO {

	private Connection connection;

	ContaDAO(Connection connection) {
		this.connection = connection;
	}

	public void salva(DadosAberturaConta dadosDaConta) {
		var cliente = new Cliente(dadosDaConta.dadosCliente());
		var conta = new Conta(dadosDaConta.numero(), cliente);

		String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email) VALUES (?, ?, ?, ?, ?)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, conta.getNumero());
			preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
			preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
			preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
			preparedStatement.setString(5, dadosDaConta.dadosCliente().email());

			preparedStatement.execute();

			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
