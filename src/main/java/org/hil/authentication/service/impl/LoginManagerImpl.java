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

package org.hil.authentication.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.hil.authentication.service.LoginManager;
import org.hil.core.dao.GenericSystemAccountDao;
import org.hil.core.dao.GenericSystemUserDao;
import org.hil.core.model.SystemUser;
import org.hil.core.model.SystemAccount;
import org.hil.core.model.vo.UserAuthentication;
import org.hil.core.service.BaseManager;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service("loginManager")
public class LoginManagerImpl extends BaseManager implements LoginManager {

	@Autowired
	private GenericSystemAccountDao systemAccountDao;
	
	@Autowired
	private GenericSystemUserDao systemUserDao;
	
	public UserAuthentication getPrincipal() {
		GrantedAuthority[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		int numAuthorities = authorities.length;
		Set<String> uRoles = new HashSet<String>();

		for (int counter = 0; counter < numAuthorities; counter++) {
			uRoles.add(authorities[counter].getAuthority());
		}
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		
		SystemAccount account = new SystemAccount();
		account = systemAccountDao.findByAccountName(name).get(0);
		Boolean lock = account.getLocked();
		Boolean expired = account.getExpired();
		
		SystemUser user = null;
		
		List<SystemUser> users = systemUserDao.findBySystemAccount(account);
		
		if (users != null && users.size() > 0)
			user = users.get(0);		

		UserAuthentication userAuthentication = new UserAuthentication(uRoles, name, user, lock, expired);
		
		return userAuthentication;
	}
	
}