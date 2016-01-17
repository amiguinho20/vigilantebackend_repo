package br.com.fences.vigilantebackend.config.jpa;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;

@TransactionCustom
@Interceptor
public class TransactionCustomInterceptor {

	@Inject
	private EntityManager em;

	@Inject
	private TransactionManager tm;
	
	@AroundInvoke
	public Object transacao(InvocationContext ctx) throws Exception {

		Object obj = null;

		try {
			tm.begin();
			obj = ctx.proceed();
			em.flush();
			tm.commit();
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		
		return obj;
	}
	
}
