package br.com.fences.vigilantebackend.config.jpa;

import java.lang.reflect.InvocationTargetException;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.transaction.TransactionManager;

import org.jboss.jandex.Main;

public class TransactionManagerProducer {
	
	private static final String JBOSS_TM_CLASS_NAME = "com.arjuna.ats.jta.TransactionManager";
	
	@Produces  
    public TransactionManager produceTransactionManager(InjectionPoint injectionPoint) {  
        TransactionManager  tm = null;
		try {
			Class<?> tmClass = Main.class.getClassLoader().loadClass(JBOSS_TM_CLASS_NAME);
			tm = (TransactionManager) tmClass.getMethod("transactionManager").invoke(null);
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			String msg = "Erro na criacao do TransactionManager: [" + e.getMessage() + "]";
			//logger.fatal(msg, e);
			throw new RuntimeException(msg, e);
		}
		return tm;
    }  
}
