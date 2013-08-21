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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import org.hil.core.dao.SystemAccountRoleDao;
import org.hil.core.model.SystemAccountRole;
import org.hil.core.model.SystemRole;
import org.hil.core.model.SystemAccount;

@Repository("systemAccountRoleDaoExt")
public class SystemAccountRoleDaoHibernate extends
		GenericDaoHibernateSupportExt<SystemAccountRole> implements
		SystemAccountRoleDao {

	@SuppressWarnings("unchecked")
	public List<SystemRole> findBySystemAccount(SystemAccount account){
		List<SystemRole> result = new ArrayList<SystemRole>();
        String sql = "Select sysRole" +
        			" from SystemRole sysRole, SystemAccountRole accRole" +
        			" where accRole.systemRole = sysRole" +
        			" and accRole.systemAccount = :account ";
        Query qry = getSession().createQuery(sql);
        qry.setParameter("account", account);
        
        result = qry.list();      
        
        return result;	
	}
	
	public Integer deleteSystemAccountRoles(SystemAccount systemAccount, String strNewRoleIds) {
		String sql = "Delete from SystemAccountRole sar where sar.systemAccount =:systemAccount"
		+ " and sar.systemRole.id not in " + strNewRoleIds;
		Query qry = getSession().createQuery(sql);
        qry.setParameter("systemAccount", systemAccount);        
        return qry.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<SystemAccountRole> findByTaiKhoanSuDungAndNhomQuyenSuDung(SystemAccount aTaiKhoanSuDung, SystemRole aQuyenSuDung){
		List<SystemAccountRole> result = new ArrayList<SystemAccountRole>();
        String sql = "Select nqsd" +
        			" from  SystemAccountRole nqsd" +
        			" where nqsd.quyenSuDung = :quyenSuDung" +
        			" and nqsd.taiKhoanSuDung = :taiKhoanSuDung ";
        Query qry = getSession().createQuery(sql);
        qry.setParameter("taiKhoanSuDung", aTaiKhoanSuDung);
        qry.setParameter("quyenSuDung", aQuyenSuDung);
        result = qry.list();      
        
        return result;	
	}
	
}