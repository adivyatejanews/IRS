/*
 * Children Immunization Registry System (IRS). Copyright (C) 2011 PATH (www.path.org)
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Author: Tran Trung Hieu
 * Email: htran282@gmail.com
 */

package org.hil.dao;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author 	Trung Hieu
 * @version	Oct 2, 2008
 */
public abstract class BaseDaoTestCase extends AbstractTransactionalDataSourceSpringContextTests  {
	protected static final Logger log = Logger.getLogger(BaseDaoTestCase.class);
	protected ResourceBundle rb;

	//"classpath*:org/parancoe/persistence/dao/generic/genericDao.xml",
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_TYPE);
		return new String[]{
		"/applicationContext-resources.xml",
		"classpath*:/applicationContext-dao.xml",
		"/applicationContext.xml",
		};
	}
	
	public BaseDaoTestCase() {
	    // Since a ResourceBundle is not required for each class, just
	    // do a simple check to see if one exists
	    String className = this.getClass().getName();
	
	    try {
	        rb = ResourceBundle.getBundle(className);
	    } catch (MissingResourceException mre) {
	        //log.warn("No resource bundle found for: " + className);
	    }
	}
	
	/**
	 * Utility method to populate a javabean-style object with values
	 * from a Properties file
	 * @param obj
	 * @return Object populated object
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object populate(Object obj) throws Exception {
	    // loop through all the beans methods and set its properties from
	    // its .properties file
	    Map map = new HashMap();
	
	    for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
	        String key = (String) keys.nextElement();
	        map.put(key, rb.getString(key));
	    }
	
	    BeanUtils.copyProperties(obj, map);
	
	    return obj;
	}

}