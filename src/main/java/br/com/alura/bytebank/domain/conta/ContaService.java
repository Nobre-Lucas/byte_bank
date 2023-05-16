package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;

public class ContaService {

	private Set<Conta> contas = new HashSet<>();

	private ConnectionFactory connection;

	public ContaService() {
		this.connection = new ConnectionFactory();
	}

	public Set<Conta> listarContasAbertas() {
		Connection conn = connection.conecta();
		return new ContaDAO(conn).lista();
	}

	public BigDecimal consultarSaldo(Integer numeroDaConta) {
		var conta = buscarContaPorNumero(numeroDaConta);
		return conta.getSaldo();
	}

	public void abrir(DadosAberturaConta dadosDaConta) {
		Connection conn = connection.conecta();
		new ContaDAO(conn).salva(dadosDaConta);
	}

	public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
		var conta = buscarContaPorNumero(numeroDaConta);

		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
		}

		if (valor.compareTo(conta.getSaldo()) > 0) {
			throw new RegraDeNegocioException("Saldo insuficiente!");
		}

		BigDecimal novoValor = conta.getSaldo().subtract(valor);
		Connection conn = connection.conecta();
		new ContaDAO(conn).alteraValor(conta.getNumero(), novoValor);
	}

	public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
		var conta = buscarContaPorNumero(numeroDaConta);
		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
		}

		Connection conn = connection.conecta();
		new ContaDAO(conn).alteraValor(conta.getNumero(), valor);
	}

	public void encerrar(Integer numeroDaConta) {
		var conta = buscarContaPorNumero(numeroDaConta);
		if (conta.possuiSaldo()) {
			throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
		}

		contas.remove(conta);
	}

	private Conta buscarContaPorNumero(Integer numero) {
		Connection conn = connection.conecta();
		Conta conta = new ContaDAO(conn).listarPorNumero(numero);
		if (conta != null) {
			return conta;
		} else {
			throw new RegraDeNegocioException("Não existe conta cadastrada com esse número!");
		}
	}
}