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

package org.hil.core.dao;

import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

public interface SystemAccountDao {

        @Transactional
        UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

        @Transactional(propagation = Propagation.NOT_SUPPORTED)
        String getUserPassword(String username);

}