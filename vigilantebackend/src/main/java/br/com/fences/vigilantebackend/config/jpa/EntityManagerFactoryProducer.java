package br.com.fences.vigilantebackend.config.jpa;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

/**
 * Deve ter apenas um EntityManagerFactory por aplicacao (base de
 * dados/classloader).
 *
 */
@ApplicationScoped
public class EntityManagerFactoryProducer {

	private transient Logger logger;
	
	private EntityManagerFactory emf;
	
	@PostConstruct
	private void init()
	{
		try
		{
		emf = Persistence.createEntityManagerFactory("vigilante");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@PreDestroy
	private void destroy()
	{
		if (emf != null)
		{
			if (emf.isOpen())
			{
				emf.close();
				logger.info("EntityManagerFactory foi fechado.");
			}
			else
			{
				logger.info("EntityManagerFactory já estava fechado.");
			}
		}
		else
		{
			logger.info("EntityManagerFactory está nulo.");
		}
	}
	
	public EntityManagerFactory getEntityManagerFactory()
	{
		return emf;
	}
}
