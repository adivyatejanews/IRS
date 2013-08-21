package org.hil.core.dao.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.apache.log4j.Logger;


/**
 * Based on  net.chrisrichardson.arid.dao.hibernate.CriteriaQueryFactory
 */
public class CriteriaQueryFactory {
    private String methodName = null;
    @SuppressWarnings({ "rawtypes" })
	private Class entityClass = null;

    
    
    private Integer firstResult;
    public Integer getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	private Integer maxResults;
    
    
    private static final Logger log = Logger.getLogger(CriteriaQueryFactory.class);


    @SuppressWarnings({ "rawtypes" })
	public CriteriaQueryFactory(String methodName, Class entityClass) {
        this.methodName = methodName;
        this.entityClass = entityClass;
    }

    public DetachedCriteria makeCriteriaQuery(Object[] args) {
        CriteriaBuilder builder = makeCriteriaBuilder(args);
        int index = 0;
        int lastIndex = methodName.length();
        int orderIndex;
        String queryParams = null;
        String orderParams = null;

        orderIndex = methodName.indexOf("OrderBy");
        if (orderIndex >= 0) {
            lastIndex = orderIndex;
            orderParams = methodName.substring(orderIndex + "OrderBy".length());
        }
        if (methodName.startsWith("findBy")) {
            queryParams = methodName.substring("findBy".length(), lastIndex);
        } else if (methodName.startsWith("findPagedBy")) {
            queryParams = methodName.substring("findPagedBy".length(), lastIndex);
            firstResult = (Integer) builder.getArgument();
            maxResults= (Integer) builder.getArgument();

        }

        log.debug("Query param:" + queryParams);
        log.debug("Order:" + orderParams);


        while (index != queryParams.length()) {
            int keywordStart = -1;
            String theKeyword = null;

            for (String keyword : CriteriaBuilder.getKeywords()) {

                keywordStart = queryParams.indexOf(keyword, index);
                if (keywordStart != -1) {
                    theKeyword = keyword;
                    break;
                }
            }
            if (keywordStart != -1) {
                String propertyName = makePropertyName(queryParams
                        .substring(index, keywordStart));
                builder.processKeyword(theKeyword, propertyName);
                index = keywordStart + theKeyword.length();
            } else {
                String propertyName = queryParams
                        .substring(index, index + 1).toLowerCase()
                        + queryParams.substring(index + 1, queryParams
                        .length());
                builder.handleEq(propertyName);
                index = queryParams.length();
            }

        }

        orderIndex = 0;

        while (orderParams != null && orderIndex != orderParams.length()) {
            int keywordStart = -1;
            String theKeyword = null;

            for (String keyword : CriteriaBuilder.getOrderByKeywords()) {
//                log.debug("order Keywork:" + keyword);
                keywordStart = orderParams.indexOf(keyword, orderIndex);
                if (keywordStart != -1) {
                    theKeyword = keyword;
                    break;
                }
            }
//            log.debug("the keywork:" + theKeyword);
            if (keywordStart != -1) {
                String propertyName = makePropertyName(orderParams
                        .substring(orderIndex, keywordStart));
                builder.processOrderByKeyword(theKeyword, propertyName);
                orderIndex = keywordStart + theKeyword.length();
            } else {
                String propertyName = orderParams
                        .substring(orderIndex, orderIndex + 1).toLowerCase()
                        + orderParams.substring(orderIndex + 1, orderParams
                        .length());
                builder.handleOrderByAsc(propertyName);
                orderIndex = orderParams.length();
            }

        }


        return builder.getCriteriaQuery();
    }


    protected CriteriaBuilder makeCriteriaBuilder(Object[] args) {
        return new CriteriaBuilder(entityClass, args);
    }

    private String makePropertyName(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }


    public String makeQueryName(Object[] args) {
        int index = 0;
        String queryName;
        if (methodName.startsWith("findNamedQuery")) {
            index = "findNamedQuery".length();
        }
        else if (methodName.startsWith("findPagedNamedQuery"))
        {
           index = "findPagedNamedQuery".length();
            firstResult = (Integer) args[0];
            maxResults=(Integer) args[1];
        }
        queryName = entityClass.getSimpleName() + "." + methodName.substring(index);
        log.debug("named query:" + queryName);
        return queryName;
    }
}
