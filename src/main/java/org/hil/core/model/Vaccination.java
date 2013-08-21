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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vaccination")
public class Vaccination extends AbstractEntity implements Serializable {
	
	@Column(name = "name", length = 200, columnDefinition = "nvarchar(200)")
	private String name;

	@Column(name = "age")
	private Integer age;
	
	@Column(name = "age_unit")
	private Integer ageUnit;
	
	@Column(name = "gap")
	private Integer gap;
	
	@Column(name = "limit_days")
	private Integer limitDays;
	
//	@Column(name = "dependent_vaccination")
//	private Long dependentVaccination;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "id_dependent_vaccination")
	private Vaccination dependentVaccination;
	
	@Column(name = "notes", length = 255, columnDefinition = "nvarchar(255)")
	private String notes;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * @return the ageUnit
	 */
	public Integer getAgeUnit() {
		return ageUnit;
	}
	/**
	 * @param ageUnit the ageUnit to set
	 */
	public void setAgeUnit(Integer ageUnit) {
		this.ageUnit = ageUnit;
	}
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Integer getGap() {
		return gap;
	}
	public void setGap(Integer gap) {
		this.gap = gap;
	}
	public Integer getLimitDays() {
		return limitDays;
	}
	public void setLimitDays(Integer limitDays) {
		this.limitDays = limitDays;
	}
	public Vaccination getDependentVaccination() {
		return dependentVaccination;
	}
	public void setDependentVaccination(Vaccination dependentVaccination) {
		this.dependentVaccination = dependentVaccination;
	}

}
