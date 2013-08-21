package org.hil.core.dao.hibernate;

import java.sql.Connection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.hil.core.dao.GenericDaoSupportExt;
import org.hil.core.model.util.PageableList;

public class GenericDaoHibernateSupportExt<T> implements GenericDaoSupportExt<T> {
    protected final Log log = LogFactory.getLog(getClass());
        @Autowired
        private SessionFactory sessionFactory;

        private Class<T> type;

        public Class<T> getType() {
            return type;
        }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void setType(Class type) {
        this.type = type;
    }
    
    public Session getSession() {
            return sessionFactory.getCurrentSession();
        }
    
    @SuppressWarnings("deprecation")
	public Connection getConnection()
    {
    	return getSession().connection();
    }
    
    @SuppressWarnings({ "rawtypes" })
	protected void processSortField(DetachedCriteria criteria, PageableList param)
    {
    	if (param.getOrderedField() != null) {
			String order = param.getOrderedField();
			// check if there is complex sort field, like
			// danhMucDanToc.tenDanToc
			String subOrder[] = order.split("\\.");
			log.debug("number of suborder:" + subOrder.length);

			DetachedCriteria subCriteria = criteria;
			for (int i = 0; i < subOrder.length - 1; i++) {
				subCriteria = criteria.createCriteria(subOrder[i]);
			}

			log.debug("sub order field:" + subOrder[subOrder.length - 1]);
			if (param.getDesc() == false) {
				subCriteria.addOrder(Order.asc(subOrder[subOrder.length - 1]));
			} else {
				subCriteria.addOrder(Order.desc(subOrder[subOrder.length - 1]));
			}

		}
    }
    
}
