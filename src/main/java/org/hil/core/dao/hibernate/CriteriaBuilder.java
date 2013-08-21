package org.hil.core.dao.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Based on package net.chrisrichardson.arid.dao.hibernate.CriteriaBuilder;
 */
public class CriteriaBuilder {
	
	private final Object[] args;

    private DetachedCriteria criteria;

    private int argIndex = 0;
    
    public int getArgIndex() {
        return argIndex;
    }

    public void setArgIndex(int argIndex) {
        this.argIndex = argIndex;
    }

    @SuppressWarnings({ "rawtypes" })
	public CriteriaBuilder(Class entityClass, Object[] args) {
        this.args = args;
        this.criteria = DetachedCriteria.forClass(entityClass);
    }

    public DetachedCriteria getCriteriaQuery() {
        if (argIndex != args.length)
            throw new RuntimeException("leftover arguments: " + (args.length - argIndex));
        return criteria;
    }

    public static List<String> getKeywords() {
        List<String> result = getHandleMethods();
        Collections.sort(result, new Comparator<String>(){

            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }});

        return result;
    }

    private static List<String> getHandleMethods() {
        List<String> result = new LinkedList<String>();
        for (Method m : CriteriaBuilder.class.getDeclaredMethods()){
            if (m.getName().startsWith("handle") &&!m.getName().startsWith("handleOrderBy")) {
                String name = m.getName().substring("handle".length());
                result.add(name);
            }
        }
        return result;
    }

    public void processKeyword(String theKeyword, String propertyName) {
        try {
            Method m = getClass().getMethod("handle" + theKeyword,
                    new Class[] { String.class });
            m.invoke(this, propertyName);
        } catch (SecurityException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        } catch (NoSuchMethodException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        } catch (InvocationTargetException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        }
    }

    public Object getArgument() {
        return args[argIndex++];
    }

    public void handleGreaterThan(String propertyName) {
        criteria.add(Restrictions.ge(propertyName, getArgument()));
    }

    public void handleAnd(String propertyName) {
        criteria.add(Restrictions.eq(propertyName, getArgument()));
    }

    public void handleBetween(String propertyName) {
        criteria.add(Restrictions.ge(propertyName, getArgument()));
        criteria.add(Restrictions.le(propertyName, getArgument()));
    }

    public void handleEq(String propertyName) {
        criteria.add(Restrictions.eq(propertyName, getArgument()));
    }

    public void handleLessThan(String propertyName) {
        criteria.add(Restrictions.lt(propertyName, getArgument()));
    }

    public void handleLessThanEquals(String propertyName) {
        criteria.add(Restrictions.le(propertyName, getArgument()));
    }

    public void handleGreaterThanEquals(String propertyName) {
        criteria.add(Restrictions.ge(propertyName, getArgument()));
    }

    public void handleLike(String propertyName) {
        criteria.add(Restrictions.like(propertyName, getArgument()));
    }

    public void handleIlike(String propertyName) {
        criteria.add(Restrictions.ilike(propertyName, getArgument()));
    }

    public void handleIsNotNull(String propertyName) {
        criteria.add(Restrictions.isNotNull(propertyName));
    }

    public void handleIsNull(String propertyName) {
        criteria.add(Restrictions.isNull(propertyName));
    }

//	public void handleNot(String propertyName) {
//		criteria.add(Restrictions.eq(propertyName, getArgument()));
//	}

    public void handleEqual(String propertyName) {
        criteria.add(Restrictions.eq(propertyName, getArgument()));
    }

    public void handleNotEqual(String propertyName) {
        criteria.add(Restrictions.ne(propertyName, getArgument()));
    }

//	public void handleOr(String propertyName) {
//		criteria.add(Restrictions.eq(propertyName, getArgument()));
//	}


    

    public static List<String> getOrderByKeywords() {
        List<String> result = getHandleOrderByMethods();
                Collections.sort(result, new Comparator<String>(){

                    public int compare(String o1, String o2) {
                        return o1.length() - o2.length();
                    }});

                return result;
        
    }

    private static List<String> getHandleOrderByMethods() {
        List<String> result = new LinkedList<String>();
        for (Method m : CriteriaBuilder.class.getDeclaredMethods()){
            if (m.getName().startsWith("handleOrderBy")) {
                String name = m.getName().substring("handleOrderBy".length());
                result.add(name);
            }
        }
        return result;
    }

    public void processOrderByKeyword(String theKeyword, String propertyName) {
        try {
            Method m = getClass().getMethod("handleOrderBy" + theKeyword,
                    new Class[] { String.class });
            m.invoke(this, propertyName);
        } catch (SecurityException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        } catch (NoSuchMethodException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        } catch (InvocationTargetException e) {
            throw new UnsupportedOperationException(
                    "Don't know what to do with: " + theKeyword, e);
        }
    }
    

    public void handleOrderByAsc(String propertyName) {
        criteria.addOrder(Order.asc(propertyName));
    }
    public void handleOrderByDesc(String propertyName) {
        criteria.addOrder(Order.desc(propertyName));
    }
}