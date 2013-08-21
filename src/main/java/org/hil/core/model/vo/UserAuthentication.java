/*
 * Children Immunization Registry System (IRS). Copyright (C) 2011 PATH (www.path.org)
 * 
 * IRS is based on HIL (http://code.google.com/p/hil)
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
 * Email: hieutt24@gmail.com
 */

package org.hil.core.model.vo;

import java.util.HashSet;
import java.util.Set;

import org.hil.core.model.SystemUser;

public class UserAuthentication
{
	private String account;
	private Set<String> roles = new HashSet<String>();
	private Boolean lock;
	private Boolean expired;
	private SystemUser user;	

	
	public UserAuthentication(Set<String> p_roles, String account,
			SystemUser user, Boolean lock, Boolean expired) {
		this.account = account;
		this.user = user;
		this.roles = p_roles;
		this.lock = lock;
		this.expired = expired;
	}	

	/**
	 * @return the expired
	 */
	public Boolean getExpired()	{
		return expired;
	}

	/**
	 * @param expired
	 *            the expired to set
	 */
	public void setExpired(Boolean expired)	{
		this.expired = expired;
	}

	/**
	 * @return the lock
	 */
	public Boolean getLock() {
		return lock;
	}

	/**
	 * @param lock
	 *            the lock to set
	 */
	public void setLock(Boolean lock) {
		this.lock = lock;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public SystemUser getUser() {
		return user;
	}

	public void setUser(SystemUser user) {
		this.user = user;
	}	

}