package org.hil.core.dao.hibernate;

import org.hil.core.dao.GenericDaoSupportExt;
import org.hil.core.model.util.PageableList;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;


/**
 * Based on net.chrisrichardson.arid.dao.hibernate.CriteriaQueryFactory and
 * org.parancoe.persistence.dao.generic.HibernateDaoInstrumentation
 */

@Component("genericDaoAspect")
@Aspect
public class GenericDaoHibernateAspect {

    private static final Logger log = Logger.getLogger(GenericDaoHibernateAspect.class);

    //        @Around(value = "execution(* org.hil..dao.*.find*By*(..)) && target(org.hil.basedao.GenericDaoSupportExt)")
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Around(value = "execution(* org.hil..dao.*.find*By*(..)) && bean(*Dao)")
//    @Around(value = "execution(* find*By*(..)) && @annotation(org.parancoe.persistence.dao.generic.Dao)")
//    @Around(value = "execution(* find*By*(..)) && target(org.hil.basedao.hibernate.GenericDaoHibernateExt)")
    public Object executeFinder(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        final GenericDaoSupportExt target = (GenericDaoSupportExt) pjp.getTarget();
        final Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        String methodName = method.getName();
        Class entityClass = target.getType();
        final Object[] args = pjp.getArgs();
        @SuppressWarnings("unused")
		final Class<?>[] parameterTypes = method.getParameterTypes();
        @SuppressWarnings("unused")
		final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Integer rowCount = null;
        Integer firstResult;
        Integer maxResults;
        CriteriaQueryFactory cQueryFactory = new CriteriaQueryFactory(methodName, entityClass);
        log.debug("target:" + target);
        log.debug("entityClass:" + entityClass);
        log.debug("method: " + methodName);
        log.debug("args: " + args);

        DetachedCriteria criteria = cQueryFactory.makeCriteriaQuery(args);

        if (cQueryFactory.getFirstResult() != null && cQueryFactory.getMaxResults() != null) {

            criteria.setProjection(Projections.rowCount());
            rowCount = (Integer) criteria.getExecutableCriteria(target.getSession()).list().get(0);
            criteria.setProjection(null);
            criteria.setResultTransformer(Criteria.ROOT_ENTITY);

            Criteria executableCriteria = criteria.getExecutableCriteria(target.getSession());
            firstResult = cQueryFactory.getFirstResult();
            maxResults = cQueryFactory.getMaxResults();

            executableCriteria.setFirstResult(firstResult);
            executableCriteria.setMaxResults(maxResults);
        }
        List values = criteria.getExecutableCriteria(target.getSession()).list();
        log.debug("row count: " + values.size());
        if (rowCount != null) {
            PageableList pagedList = new PageableList(values, cQueryFactory.getFirstResult(), cQueryFactory.getMaxResults(), rowCount);
            result = pagedList;
        } else {
            result = values;
        }
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Around(value = "execution(* find*NamedQuery*(..)) && target(org.hil.core.dao.GenericDaoSupportExt)")
    public Object executeNamedQueryFinder(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        final GenericDaoSupportExt target = (GenericDaoSupportExt) pjp.getTarget();
        final Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        String methodName = method.getName();
        Class entityClass = target.getType();
        final Object[] args = pjp.getArgs();
        @SuppressWarnings("unused")
		final Class<?>[] parameterTypes = method.getParameterTypes();
        @SuppressWarnings("unused")
		final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Integer rowCount = null;
        Integer firstResult;
        Integer maxResults;
        CriteriaQueryFactory cQueryFactory = new CriteriaQueryFactory(methodName, entityClass);
        log.debug("execute:" + entityClass);
        log.debug("method: " + methodName);
        log.debug("args: " + args);
        String queryName = cQueryFactory.makeQueryName(args);
        Query namedQuery = target.getSession().getNamedQuery(queryName);
        if (namedQuery != null) {
            int index = 0;
            if (cQueryFactory.getFirstResult() != null && cQueryFactory.getMaxResults() != null) {

                index = 2;

            }
            for (int i = index; i < args.length; i++) {

                Object arg = args[i];
                namedQuery.setParameter(i - index, arg);

            }

            if (cQueryFactory.getFirstResult() != null && cQueryFactory.getMaxResults() != null) {
                ScrollableResults sc = namedQuery.scroll();
                sc.last();
                rowCount = sc.getRowNumber() + 1;
                sc.close();
                firstResult = cQueryFactory.getFirstResult();
                maxResults = cQueryFactory.getMaxResults();
                namedQuery.setFirstResult(firstResult);
                namedQuery.setMaxResults(maxResults);
                log.debug("row count:" + rowCount);

            }

        }
        List values = namedQuery.list();
        if (rowCount != null) {
            PageableList pagedList = new PageableList(values, cQueryFactory.getFirstResult(), cQueryFactory.getMaxResults(), rowCount);
            result = pagedList;
        } else {
            result = values;
        }

        return result;
    }

}
