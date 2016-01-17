package br.com.fences.vigilantebackend.config.jpa;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerProducer {

	 @Inject
	 private EntityManagerFactoryProducer emfp;
	
	 @Produces
	 public EntityManager produceEntityManager(InjectionPoint injectionPoint)
	 {
		 EntityManagerFactory emf = emfp.getEntityManagerFactory();
		 EntityManager em = emf.createEntityManager();
		 return em;
	 }

//	@PersistenceContext(unitName = "vigilante")
//	EntityManager em;

//	@Produces
//	public FullTextEntityManager getFTEM() {
//		return Search.getFullTextEntityManager(em);
//	}
}
