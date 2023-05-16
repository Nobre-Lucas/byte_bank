package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

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

	public Set<Conta> lista() {
		Set<Conta> contas = new HashSet<>();

		String sql = "SELECT * FROM conta";

		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				Integer numeroDaConta = resultSet.getInt(1);
				BigDecimal saldo = resultSet.getBigDecimal(2);
				String nomeDoCliente = resultSet.getString(3);
				String cpf = resultSet.getString(4);
				String email = resultSet.getString(5);

				DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nomeDoCliente, cpf, email);
				Cliente cliente = new Cliente(dadosCadastroCliente);
				Conta conta = new Conta(numeroDaConta, saldo, cliente);

				contas.add(conta);
			}
			prepareStatement.close();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return contas;
	}

	public Conta listarPorNumero(Integer numero) {
		String sql = "SELECT * FROM conta WHERE numero = ?";

		PreparedStatement preparedStatement;
		ResultSet resultSet;
		Conta conta = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, numero);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Integer numeroRecuperado = resultSet.getInt(1);
				BigDecimal saldo = resultSet.getBigDecimal(2);
				String nome = resultSet.getString(3);
				String cpf = resultSet.getString(4);
				String email = resultSet.getString(5);

				DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome, cpf, email);
				Cliente cliente = new Cliente(dadosCadastroCliente);

				conta = new Conta(numeroRecuperado, saldo, cliente);
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conta;
	}

	public void alteraValor(Integer numero, BigDecimal valor) {
		PreparedStatement ps;
		String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";

		try {
			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql);

			ps.setBigDecimal(1, valor);
			ps.setInt(2, numero);

			ps.execute();
			connection.commit();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			throw new RuntimeException(e);
		}
	}

}
