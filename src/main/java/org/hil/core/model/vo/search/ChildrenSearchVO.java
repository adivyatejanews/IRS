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

package org.hil.core.model.vo.search;

import java.util.Date;

public class ChildrenSearchVO {
	private Long provinceId;
	private Long districtId;
	private Long communeId;
	private Long villageId;	
	private String childCode;
	private Date dateOfBirthFrom;
	private Date dateOfBirthTo;
	private String childName;
	private String motherName;	
	
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public Long getCommuneId() {
		return communeId;
	}
	public void setCommuneId(Long communeId) {
		this.communeId = communeId;
	}
	public Long getVillageId() {
		return villageId;
	}
	public void setVillageId(Long villageId) {
		this.villageId = villageId;
	}
	public String getChildCode() {
		return childCode;
	}
	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}
	public Date getDateOfBirthFrom() {
		return dateOfBirthFrom;
	}
	public void setDateOfBirthFrom(Date dateOfBirthFrom) {
		this.dateOfBirthFrom = dateOfBirthFrom;
	}
	public Date getDateOfBirthTo() {
		return dateOfBirthTo;
	}
	public void setDateOfBirthTo(Date dateOfBirthTo) {
		this.dateOfBirthTo = dateOfBirthTo;
	}
	
}