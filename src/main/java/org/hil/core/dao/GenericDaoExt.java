package org.hil.core.dao;


import java.io.Serializable;
import java.util.List;

/**
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="org.appfuse.dao.hibernate.GenericDaoJpaHibernate"&gt;
 *          &lt;constructor-arg value="org.appfuse.model.Foo"/&gt;
 *          &lt;property name="sessionFactory" ref="sessionFactory"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public interface GenericDaoExt<T, PK extends Serializable> extends GenericDaoSupportExt<T> {
    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     *
     * @return List of populated objects
     */

    List<T> getAll(Integer page, Integer pageSize);
    
    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table. The result will be cached until value is modified
     *
     * @return List of populated objects
     */
    
    List<T> getAllCached();
    

//    PageableList getAll(PageableList pageableList);

    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    T get(PK id);

    /**
     * Checks for existence of an object of type T using the id arg.
     *
     * @param id the id of the entity
     * @return - true if it exists, false if it doesn't
     */
    boolean exists(PK id);

    /**
     * Generic method to save an object - handles both update and insert.
     *
     * @param object the object to save
     * @return the persisted object
     */
    T save(T object);

    /**
     * Generic method to delete an object based on class and id
     *
     * @param id the identifier (primary key) of the object to remove
     */
    void remove(T object);
    void saveAll(List<T> list);

}