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

package org.hil.core.model;

import org.springframework.security.GrantedAuthority;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "system_role")
public class SystemRole extends AbstractEntity implements Serializable, GrantedAuthority {
	
	@Column(name = "role_name", length = 50)
	private String roleName;

	@Column(name = "description", length = 500)
	private String description;	

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Transient
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Transient
	public String getName() {
		return getRoleName();
	}

	public String getAuthority() {
		return getRoleName();
	}

	public int compareTo(Object o) {
		return (equals(o) ? 0 : -1);
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(
				getName()).toString();
	}
	
}