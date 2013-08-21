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

import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.GrantedAuthority;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "system_account")
public class SystemAccount extends AbstractEntity implements Serializable, UserDetails {
	
	@Column(name = "account_name", length = 50, unique = true)
	private String accountName;

	@Column(name = "password", length = 100)
	private String password;

	@Column(name = "expired")
	private Boolean expired = new Boolean(false);

	@Column(name = "locked")
	private Boolean locked = new Boolean(false);

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "system_account_role", joinColumns = { @JoinColumn(name = "id_system_account") }, inverseJoinColumns = @JoinColumn(name = "id_system_role"))
	private Set<SystemRole> roles = new HashSet<SystemRole>();

	public Set<SystemRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<SystemRole> roles) {
		this.roles = roles;
	}

	public void addRole(SystemRole role) {
		getRoles().add(role);

	}	

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	

	@Transient
	public GrantedAuthority[] getAuthorities() {
		return roles.toArray(new GrantedAuthority[0]);
	}

	@Transient
	public String getUsername() {
		return getAccountName();
	}

	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	public boolean isEnabled() {
		return true;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Transient
	public String getPassword() {
		return password;
	}
}
