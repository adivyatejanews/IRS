package org.hil.core.dao;

import org.hibernate.Session;

public interface GenericDaoSupportExt<T> {
    public Session getSession();
    public Class<T> getType();
    public void setType(Class<T> type);
}