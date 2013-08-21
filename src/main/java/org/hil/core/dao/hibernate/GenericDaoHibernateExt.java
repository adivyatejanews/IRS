package org.hil.core.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.hil.core.dao.GenericDaoExt;
import org.hil.core.model.AbstractEntity;

public class GenericDaoHibernateExt<T extends AbstractEntity, PK extends Serializable>
		implements GenericDaoExt<T, PK>
{
	protected final Log log = LogFactory.getLog(getClass());
	@Autowired
	private SessionFactory sessionFactory;
	private Class<T> type;

	public Class<T> getType()
	{
		return type;
	}

	public void setType(Class<T> type)
	{
		this.type = type;
	}

	public Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll(Integer page, Integer pageSize)
	{
		Query q = getSession().createQuery("from " + type.getName());
		if (page != null && pageSize != null)
		{
			Integer firstResult = (page - 1) * pageSize;
			q.setFirstResult(firstResult);
			q.setMaxResults(pageSize);
		}
		// if(type.getAnnotation(JaxbClassContainer.class)!=null){
		// List<T> returnList= q.list();
		// for (T t : returnList) {
		// t.doUnmarshallObject();
		// }
		// return returnList;
		// }
		return q.list();
	}

	@SuppressWarnings("unchecked")
	public T get(PK id)
	{
		T entity = (T) getSession().get(type, id);
		if (entity == null)
		{
			log.warn("Uh oh, '" + this.type + "' object with id '" + id
					+ "' not found...");
			// throw new ObjectRetrievalFailureException(this.type, id);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public boolean exists(PK id)
	{
		T entity = (T) getSession().get(type, id);
		return entity != null;
	}

	@SuppressWarnings("unchecked")
	public T save(T object)
	{
		// Make sure uid column is not null before insert to database
		// AbstractEntity entity = (AbstractEntity) object;
		// if (entity.getUid()==null)
		// {
		// entity.setUid(UUID.randomUUID().toString());
		// }
		// entity.doMarshallObject();
		if (object.getUid() == null)
		{
			object.setUid(UUID.randomUUID().toString());
		}		
		T result = (T) getSession().merge(object);
		
		return result;
	}

	public void remove(T object)
	{
		getSession().delete(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hil.core.dao.GenericDaoExt#getAllCached()
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAllCached()
	{
		Query q = getSession().createQuery("from " + type.getName())
				.setCacheable(true);
		return q.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hil.core.dao.GenericDaoExt#saveAll(java.util.List)
	 */
	public void saveAll(List<T> list)
	{
		for (T object : list)
		{
			getSession().merge(object);
		}

	}
}
