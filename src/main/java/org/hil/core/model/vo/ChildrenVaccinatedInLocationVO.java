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

package org.hil.core.model.vo;

import java.util.Date;

public class ChildrenVaccinatedInLocationVO {
	private Long id;
	private String communeName;
	private String villageName;
	private String childCode;
	private String fullName;
	private Boolean gender;
	private Date dateOfBirth;
	private String motherName;
	private Date dateOfImmunization;
	private Short otherLocation;
	private String vaccinatedCommune;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCommuneName() {
		return communeName;
	}
	public void setCommuneName(String communeName) {
		this.communeName = communeName;
	}
	public String getVillageName() {
		return villageName;
	}
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	public String getChildCode() {
		return childCode;
	}
	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}
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
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public Date getDateOfImmunization() {
		return dateOfImmunization;
	}
	public void setDateOfImmunization(Date dateOfImmunization) {
		this.dateOfImmunization = dateOfImmunization;
	}
	public Short getOtherLocation() {
		return otherLocation;
	}
	public void setOtherLocation(Short otherLocation) {
		this.otherLocation = otherLocation;
	}
	public String getVaccinatedCommune() {
		return vaccinatedCommune;
	}
	public void setVaccinatedCommune(String vaccinatedCommune) {
		this.vaccinatedCommune = vaccinatedCommune;
	}
	
	
}
