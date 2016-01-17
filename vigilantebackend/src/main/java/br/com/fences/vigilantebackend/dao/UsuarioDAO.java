package br.com.fences.vigilantebackend.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import br.com.fences.vigilantebackend.config.jpa.TransactionCustom;
import br.com.fences.vigilanteentidade.negocio.Usuario;

@RequestScoped
public class UsuarioDAO implements Serializable {

	private static final long serialVersionUID = -8319216589167182067L;
	
	@Inject
	private transient Logger logger;

	@Inject
	//@PersistenceContext(unitName = "vigilante")
	private EntityManager em;

	@TransactionCustom
	public void adicionar(Usuario usuario) {

		if (usuario != null) {
			if (!existeEmail(usuario.getEmail()))
			{
				usuario.setAtivo(true);
				usuario.setDataCriacao(new Date());
				usuario.setDataAtualizacao(new Date());
				em.persist(usuario);
			}
			else
			{
				throw new RuntimeException("O email[" + usuario.getEmail() + "] já existe.");	
			}
		}
	}
	
	@TransactionCustom
	public void atualizar(Usuario usuario) {
		usuario.setDataAtualizacao(new Date());
		em.merge(usuario);
	}

	public boolean autenticar(Usuario usuario) {
		boolean autenticado = false;

		Usuario usuarioAux = em.createNamedQuery("Usuario.consultar", Usuario.class)
                .setParameter("email", usuario.getEmail())
                .setParameter("password", usuario.getPassword())
                .getSingleResult(); 
		
		if (usuarioAux != null) {
			autenticado = true;
		}
		return autenticado;
	}

	public boolean existeEmail(String email) {
		boolean existe = false;
		
		Usuario usuario = consultar(email);
		
		if (usuario != null) {
			existe = true;
		}
		return existe;
	}

	public Usuario consultar(String email) {
		
		Usuario usuario = null;
		try {
			usuario = em.createNamedQuery("Usuario.consultar", Usuario.class)
	                .setParameter("email", email)
	                .getSingleResult(); 
		} catch (NoResultException e){
			//-- retornar objeto nulo.
		}
		return usuario;
	}

	public List<Usuario> listarUsuarios() {
		
		List<Usuario> usuarios = em.createNamedQuery("Usuario.listarUsuarios", Usuario.class)
               .getResultList();
		
		return usuarios;
	}

}
