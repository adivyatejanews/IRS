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

package org.hil.systemadministrator.systemusermanagement.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.hil.core.dao.SystemUserDao;
import org.hil.core.dao.GenericSystemUserDao;
import org.hil.core.dao.GenericSystemAccountRoleDao;
import org.hil.core.dao.GenericSystemRoleDao;
import org.hil.core.dao.GenericSystemAccountDao;
import org.hil.core.dao.SystemAccountRoleDao;
import org.hil.core.model.SystemUser;
import org.hil.core.model.SystemAccountRole;
import org.hil.core.model.SystemRole;
import org.hil.core.model.SystemAccount;
import org.hil.core.service.BaseManager;
import org.hil.core.util.MD5;
import org.hil.systemadministrator.systemusermanagement.service.SystemUserManager;

@Service("systemUserManager")
public class SystemUserManagerImpl extends BaseManager implements SystemUserManager {
	
	@Autowired
	private GenericSystemUserDao systemUserDao;
	
	@Autowired
	private GenericSystemAccountDao systemAccountDao;
	
	@Autowired
	private GenericSystemRoleDao systemRoleDao;
	
	@Autowired
	private GenericSystemAccountRoleDao systemAccountRoleDao;
	
	@Autowired
	@Qualifier("systemUserDaoExt")
    private SystemUserDao systemUserDaoExt;
	
	@Autowired
	@Qualifier("systemAccountRoleDaoExt")
    private SystemAccountRoleDao systemAccountRoleDaoExt;
	
	public List<SystemUser> getAllSystemUsers() {
		return systemUserDaoExt.getAllSystemUsers();
	}	

	public List<SystemUser> saveSystemUser(SystemUser systemUser, SystemAccount systemAccount, boolean newPw) {
		
		if (systemUser.getId() == null || systemUser.getId() == 0) {
			List<SystemAccount> tmp = systemAccountDao.findByAccountName(systemAccount.getAccountName());
			if (tmp != null && tmp.size() > 0)
				return null;
		}
		String md5Password = "";
		if (newPw) {
			try {
				md5Password = MD5.MD5(systemAccount.getPassword().toLowerCase());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			systemAccount.setPassword(md5Password);
		}		
		systemAccount = systemAccountDao.save(systemAccount);		
		if (systemUser.getId() == null || systemUser.getId() == 0)
			systemUser.setSystemAccount(systemAccount);		
		systemUserDao.save(systemUser);	
		return systemUserDao.getAll(null, null);
	}	

	public List<SystemUser> deleteSystemUser(SystemUser systemUser) {
		systemUserDao.remove(systemUser);
		SystemAccount systemAccount = systemUser.getSystemAccount();
		systemAccountDao.remove(systemAccount);		
		return systemUserDaoExt.getAllSystemUsers();
	}
	
	public List<SystemRole> getAllSystemRoles(){
		return systemRoleDao.getAll(null, null);
	}
	
	public List<SystemRole> getAllAccountRoles(SystemAccount systemAccount){		
		return systemAccountRoleDaoExt.findBySystemAccount(systemAccount);
	}
	
	// save account roles
	public List<SystemRole> saveAccountRoles(List<SystemRole> newAccountRoles, SystemAccount systemAccount){
		
		List<SystemRole> currentAccountRoles = systemAccountRoleDaoExt.findBySystemAccount(systemAccount);
		
		String strNewRoleIds = "(";
		for (int i=0; i< newAccountRoles.size()-1; i++) {
			strNewRoleIds += "'" + newAccountRoles.get(i).getId() + "',";
		}
		strNewRoleIds += "'" + newAccountRoles.get(newAccountRoles.size()-1).getId() + "')";
		log.debug(systemAccount.getId() + " | " + strNewRoleIds);
		
		for(int i = 0; i < currentAccountRoles.size(); i++ ) {
			for(int j = 0; j < newAccountRoles.size(); j++){
				String role1 = currentAccountRoles.get(i).getRoleName();
				String role2 = newAccountRoles.get(j).getRoleName();
				if(role1.equals(role2)){					 
					newAccountRoles.remove(newAccountRoles.get(j));
				}
			}
		}
		
		for(int i = 0; i < newAccountRoles.size(); i++){
			SystemAccountRole sar = new SystemAccountRole();
			sar.setSystemAccount(systemAccount);
			sar.setSystemRole(newAccountRoles.get(i));
			systemAccountRoleDao.save(sar);
		}
		
		systemAccountRoleDaoExt.deleteSystemAccountRoles(systemAccount, strNewRoleIds);
	
		return systemAccountRoleDaoExt.findBySystemAccount(systemAccount);
	}	
	
	public List<SystemAccount> getAllSystemAccounts(){
		return systemAccountDao.getAll(null, null);
	}
	
	public SystemAccount savePassword(SystemAccount sa, String newpassword) {
		String md5Password = "";
		try {
			md5Password = MD5.MD5(newpassword.toLowerCase());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sa.setPassword(md5Password);
		sa = systemAccountDao.save(sa);
		return sa;
	}
}