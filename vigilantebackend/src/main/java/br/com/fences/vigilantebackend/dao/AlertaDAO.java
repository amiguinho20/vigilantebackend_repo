package br.com.fences.vigilantebackend.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilantebackend.config.jpa.TransactionCustom;
import br.com.fences.vigilanteentidade.negocio.Alerta;

@RequestScoped
public class AlertaDAO implements Serializable {

	private static final long serialVersionUID = -8319216589167182067L;
	
	@Inject
	private transient Logger logger;

	@Inject
	//@PersistenceContext(unitName = "vigilante")
	private EntityManager em;

	@TransactionCustom
	public void adicionar(Alerta alerta) {

		if (alerta == null) 
		{
			throw new RuntimeException("O alerta está nulo.");	
		}
		else if (alerta.getVeiculo() == null)
		{
			throw new RuntimeException("O alerta não possui veículo associado.");	
		}
		else if (alerta.getEndereco() == null || !Verificador.isValorado(alerta.getEndereco().getLogradouro()))
		{
			throw new RuntimeException("O alerta não possui endereço associado ou com logradouro.");
		}
		else if (alerta.getUsuarioCriacao() == null || !Verificador.isValorado(alerta.getUsuarioCriacao().getEmail()))
		{
			throw new RuntimeException("O alerta não possui usuário associado ou com email.");
		}
		else
		{
			alerta.setDataCriacao(new Date());
			em.persist(alerta);
		}
	}
	
	@TransactionCustom
	public void atualizar(Alerta alerta) {
		em.merge(alerta);
	}

	public List<Alerta> listarAlertas() {
		
		List<Alerta> alertas = em.createNamedQuery("Alerta.listarAlertas", Alerta.class)
               .getResultList();
		
		return alertas;
	}
	
	public List<Alerta> listarAlertasPorIdUsuario(String id)
	{
		//String query = "select a from Alerta a where a.usuarioCriacao.email = :email order by a.dataCriacao desc";
		//List<Alerta> alertas = em.createQuery(query, Alerta.class).setParameter("email", email).getResultList();
		String query = "{ $query : { usuarioCriacao_id : '" + id + "' }, $orderby : { dataCriacao : -1 } }";
		List<Alerta> alertas = em.createNativeQuery( query, Alerta.class ).getResultList();
		return alertas;
	}

	public Alerta consultar(String id) {
		Alerta alerta = em.find(Alerta.class, id);
		return alerta;
	}
	
	public List<Alerta> pesquisarLazy(Object filtro, int primeiraPagina, int registrosPorPagina) {
		
		List<Alerta> alertas = em.createNamedQuery("Alerta.listarAlertas", Alerta.class)
				.setFirstResult(primeiraPagina)
				.setMaxResults(registrosPorPagina)
				.getResultList();
		
		return alertas;
	}
	
	public int pesquisarLazyContar(Object filtro) {
		
		long count = (long) em.createNativeQuery( "db.Alerta.count()").getSingleResult();
		
		return Integer.parseInt(Long.toString(count));
	}
	
	public int contarNaoRecepcionado(Object filtro) {
		
		long count = (long) em.createNativeQuery( "db.Alerta.count({'usuarioRecepcao_id':{'$exists':false}})").getSingleResult();
		
		return Integer.parseInt(Long.toString(count));
	}

}
