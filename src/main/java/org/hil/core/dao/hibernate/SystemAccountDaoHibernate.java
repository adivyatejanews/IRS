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

import org.springframework.stereotype.Repository;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.security.userdetails.UserDetailsService;
import org.hil.core.dao.SystemAccountDao;
import org.hil.core.model.SystemAccount;
import java.util.List;

@Repository("systemAccountDaoExt")
public class SystemAccountDaoHibernate extends
        GenericDaoHibernateSupportExt<SystemAccount> implements
        SystemAccountDao, UserDetailsService {

    /**
     * Gets users information based on login name.
     *
     * @param username the user's username
     * @return userDetails populated userDetails object
     * @throws org.springframework.security.userdetails.UsernameNotFoundException
     *          thrown when user not found in database
     */
    @SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SystemAccount> list = getSession().createQuery("from SystemAccount where accountName=:username").setParameter("username", username).list();

        if (list == null || list.isEmpty()) {

            throw new UsernameNotFoundException("user '" + username + "' not found...");

        } else {
            return (UserDetails) list.get(0);

        }

    }

    /**
     * Retrieves the password in DB for a user
     *
     * @param username the user's username
     * @return the password in DB, if the user is already persisted
     */
    public String getUserPassword(String username) {
        String password = getSession().createQuery("select password from SystemAccount where accountName=:username").setParameter("username", username).uniqueResult().toString();
        return password;

    }
    
}