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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "province")
public class Province extends AbstractEntity implements Serializable {

	@Column(name = "province_id", length = 50)
	private String provinceId;

	@Column(name = "province_name", length = 250)
	private String provinceName;

	@Column(name = "notes", length = 500)
	private String notes;
	
	@Column(name = "id_region")
	private Short regionId;

	public Short getRegionId() {
		return regionId;
	}

	public void setRegionId(Short regionId) {
		this.regionId = regionId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}	

}