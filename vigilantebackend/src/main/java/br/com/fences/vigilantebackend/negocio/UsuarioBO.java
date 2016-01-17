package br.com.fences.vigilantebackend.negocio;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilantebackend.dao.UsuarioDAO;
import br.com.fences.vigilanteentidade.negocio.Usuario;

@RequestScoped
public class UsuarioBO {

	@Inject
	//@EJB
	private UsuarioDAO usuarioDAO;
	
	public void adicionar(Usuario usuario)
	{
		validar(usuario);
		
		usuarioDAO.adicionar(usuario);
	}
	
	public void atualizar(Usuario usuario)
	{
		usuarioDAO.atualizar(usuario);
	}
	
	public boolean autenticar(Usuario usuario)
	{
		boolean autenticado = usuarioDAO.autenticar(usuario);
		return autenticado;
	}
	
	public boolean existeEmail(String email)
	{
		boolean existe = usuarioDAO.existeEmail(email);
		return existe;
	}
	
	public Usuario consultar(String email)
	{
		Usuario usuario = usuarioDAO.consultar(email);
		return usuario;
	}
	
	public Usuario consultarSemImagem(String email)
	{
		Usuario usuario = usuarioDAO.consultar(email);
		if (usuario != null)
		{
			if (usuario.getVeiculo() != null)
			{
				if (Verificador.isValorado(usuario.getVeiculo().getImagens()))
				{
					usuario.getVeiculo().getImagens().clear();
				}
			}
		}
		return usuario;
	}

	public List<Usuario> listarUsuarios()
	{
		List<Usuario> usuarios = usuarioDAO.listarUsuarios();
		return usuarios;
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
}
