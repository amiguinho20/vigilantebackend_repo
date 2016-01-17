package br.com.fences.vigilantebackend.negocio;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilantebackend.dao.AlertaDAO;
import br.com.fences.vigilantebackend.rest.ColecaoJsonAdapter;
import br.com.fences.vigilanteentidade.negocio.Alerta;
import br.com.fences.vigilanteentidade.negocio.Usuario;
import br.com.fences.vigilanteentidade.negocio.Veiculo;

@RequestScoped
public class AlertaBO {

	@Inject
	private AlertaDAO alertaDAO;
	
	@Inject 
	private UsuarioBO usuarioBO;
	
	private Gson gson = new GsonBuilder()
			.registerTypeHierarchyAdapter(Collection.class, new ColecaoJsonAdapter())
			.create();
	
	public void emitir(String email, Alerta alerta)
	{
		if (alerta == null) 
		{
			throw new RuntimeException("O alerta está nulo.");	
		}
		else if (!Verificador.isValorado(email))
		{
			throw new RuntimeException("O email está vazio.");	
		}
		
		Usuario usuario = usuarioBO.consultar(email);
		
		Veiculo veiculo = clonarVeiculo(usuario.getVeiculo());
		
		alerta.setVeiculo(veiculo);
		alerta.setUsuarioCriacao(usuario);
		
		alertaDAO.adicionar(alerta);

	}
	
	public void recepcionar(String idAlerta, String email)
	{
		if (!Verificador.isValorado(idAlerta)) 
		{
			throw new RuntimeException("O alerta está nulo.");	
		}
		else if (!Verificador.isValorado(email))
		{
			throw new RuntimeException("O email está vazio.");	
		}
		
		Usuario usuario = usuarioBO.consultar(email);
		Alerta alerta = alertaDAO.consultar(idAlerta);
		
		alerta.setUsuarioRecepcao(usuario);
		alerta.setDataRecepcao(new Date());
		
		alertaDAO.atualizar(alerta);
		
	}
	
	
	public void atualizar(Alerta alerta)
	{
		alertaDAO.atualizar(alerta);
	}
	

	public List<Alerta> listarAlertas()
	{
		List<Alerta> alertas = alertaDAO.listarAlertas();
		return alertas;
	}
	
	public List<Alerta> listarAlertasPorIdUsuario(String id)
	{
		List<Alerta> alertas = alertaDAO.listarAlertasPorIdUsuario(id);
		retirarImagens(alertas, true, true, true);
		return alertas;
	}
	
	/**
	 * metodo para recuperar valores desvinculados da sessao do JPA
	 */
	private Veiculo clonarVeiculo(Veiculo veiculo)
	{
		String json = gson.toJson(veiculo);
		Veiculo veiculoClonado = gson.fromJson(json, Veiculo.class);
		return veiculoClonado;
	}

	public Alerta consultar(String id) {
		Alerta alerta = alertaDAO.consultar(id);
		retirarImagens(alerta, false, true, true);
		return alerta;
	}
	
	
	private void validar(Usuario usuario)
	{
//		//-- validacao
//		if (!Verificador.isValorado(usuario.getPassword()))
//		{
//			throw new RuntimeException("O password não pode ser vazio.");
//		}
//		if (!Verificador.isValorado(usuario.getEmail()))
//		{
//			throw new RuntimeException("O email não pode ser vazio.");
//		}
//		if (!Verificador.isValorado(usuario.getCpf()))
//		{
//			throw new RuntimeException("O cpf não pode ser vazio.");
//		}
//		if (!Verificador.isValorado(usuario.getRg()))
//		{
//			throw new RuntimeException("O rg não pode ser vazio.");
//		}
//		if (!Verificador.isValorado(usuario.getNome()))
//		{
//			throw new RuntimeException("O nome não pode ser vazio.");
//		}
//		if (!Verificador.isValorado(usuario.getCelular()))
//		{
//			throw new RuntimeException("O celular não pode ser vazio.");
//		}
//		if (usuario.getEndereco() != null)
//		{
//			Endereco endereco = usuario.getEndereco();
//			if (!Verificador.isValorado(endereco.getLogradouro()))
//			{
//				throw new RuntimeException("O logradouro não pode ser vazio.");
//			}
//			if (!Verificador.isValorado(endereco.getNumero()))
//			{
//				throw new RuntimeException("O numero não pode ser vazio.");
//			}	
//			if (!Verificador.isValorado(endereco.getBairro()))
//			{
//				throw new RuntimeException("O bairro não pode ser vazio.");
//			}
//			if (!Verificador.isValorado(endereco.getCep()))
//			{
//				throw new RuntimeException("O cep não pode ser vazio.");
//			}
//			if (!Verificador.isValorado(endereco.getCidade()))
//			{
//				throw new RuntimeException("A cidade não pode ser vazia.");
//			}
//			if (!Verificador.isValorado(endereco.getUf()))
//			{
//				throw new RuntimeException("A uf não pode ser vazia.");
//			}
//		}
	}

	public List<Alerta> pesquisarLazy(Object filtro, int primeiraPagina, int registrosPorPagina) {
		List<Alerta> alertas = alertaDAO.pesquisarLazy(filtro, primeiraPagina, registrosPorPagina);
		retirarImagens(alertas, true, true, true);
		return alertas;
	}
	
	
	public int pesquisarLazyContar(Object filtro) {
		int qtd = alertaDAO.pesquisarLazyContar(filtro);
		return qtd;
	}

	public int contarNaoRecepcionado(Object filtro) {
		int qtd = alertaDAO.contarNaoRecepcionado(filtro);
		return qtd;
	}

	
	/**
	 * limpa imagens para evitar sobrecarga de trafego
	 */
	private void retirarImagens(Alerta alerta, boolean imagemAlerta, boolean usuarioCriacao, boolean usuarioRecepcao)
	{
		if (alerta != null)
		{
			if (imagemAlerta && alerta.getVeiculo() != null)
			{
				if (Verificador.isValorado(alerta.getVeiculo().getImagens()))
				{
					alerta.getVeiculo().getImagens().clear();
				}
			}
			if (usuarioCriacao && alerta.getUsuarioCriacao() != null && alerta.getUsuarioCriacao().getVeiculo() != null)
			{
				if (Verificador.isValorado(alerta.getUsuarioCriacao().getVeiculo().getImagens()))
				{
					alerta.getUsuarioCriacao().getVeiculo().getImagens().clear();
				}
			}
			if (usuarioRecepcao && alerta.getUsuarioRecepcao() != null && alerta.getUsuarioRecepcao().getVeiculo() != null)
			{
				if (Verificador.isValorado(alerta.getUsuarioRecepcao().getVeiculo().getImagens()))
				{
					alerta.getUsuarioRecepcao().getVeiculo().getImagens().clear();
				}
			}
		}
	}
	
	private void retirarImagens(List<Alerta> alertas, boolean imagemAlerta, boolean usuarioCriacao, boolean usuarioRecepcao)
	{
		if (Verificador.isValorado(alertas))
		{
			for (Alerta alerta : alertas)
			{
				retirarImagens(alerta, imagemAlerta, usuarioCriacao, usuarioRecepcao);
			}
		}
	}

}
