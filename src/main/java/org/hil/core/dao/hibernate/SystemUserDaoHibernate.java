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

package org.hil.core.dao.hibernate;

import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.hil.core.dao.SystemUserDao;
import org.hil.core.model.SystemUser;
import org.hil.core.model.SystemAccount;

@Repository("systemUserDaoExt")
public class SystemUserDaoHibernate extends GenericDaoHibernateSupportExt<SystemUser> implements SystemUserDao
{	
	@SuppressWarnings("unchecked")
	public List<SystemUser> getAllSystemUsers() {
		String sql = "SELECT user FROM " + SystemUser.class.getName()
				+ " user, " + SystemAccount.class.getName()
				+ " acc WHERE user.systemAccount = acc "
				+ " AND acc.expired = false ";
		Query qry = getSession().createQuery(sql);		
		List<SystemUser> resultList = qry.list();
		log.debug(resultList.size());
		return resultList;
	}
	
}