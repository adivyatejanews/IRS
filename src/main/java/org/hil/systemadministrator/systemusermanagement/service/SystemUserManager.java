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

package org.hil.systemadministrator.systemusermanagement.service;

import java.util.List;

import org.hil.core.model.SystemUser;

import org.hil.core.model.SystemRole;
import org.hil.core.model.SystemAccount;

public interface SystemUserManager {
	List<SystemUser> getAllSystemUsers();	
	List<SystemUser> saveSystemUser(SystemUser systemUser, SystemAccount systemAccount, boolean newPw);
	List<SystemUser> deleteSystemUser(SystemUser systemUser);
	List<SystemRole> getAllSystemRoles();
	List<SystemRole> getAllAccountRoles(SystemAccount systemAccount);
	List<SystemRole> saveAccountRoles(List<SystemRole> tmpAccountRoles, SystemAccount systemAccount);
	List<SystemAccount> getAllSystemAccounts();
	SystemAccount savePassword(SystemAccount sa, String newpassword);
}