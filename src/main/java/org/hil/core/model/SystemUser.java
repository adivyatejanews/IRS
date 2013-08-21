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

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "system_user")
public class SystemUser extends AbstractEntity implements Serializable {
	
	@Column(name = "full_name", length = 150)
	private String fullName;

	@Column(name = "gender")
	private Boolean gender = new Boolean(false);

	@Column(name = "mobile", length = 50)
	private String mobile;

	@Column(name = "tel", length = 50)
	private String tel;

	@Column(name = "address", length = 250)
	private String address;

	@Column(name = "date_of_birth")
	private Date dateOfBirth;	

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_commune")
	private Commune commune;	

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_system_account")
	private SystemAccount systemAccount;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public java.util.Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(java.util.Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Commune getCommune() {
		return commune;
	}

	public void setCommune(Commune commune) {
		this.commune = commune;
	}

	public SystemAccount getSystemAccount() {
		return systemAccount;
	}

	public void setSystemAccount(SystemAccount systemAccount) {
		this.systemAccount = systemAccount;
	}	

}