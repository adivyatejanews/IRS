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
@Table(name = "vaccination_day")
public class VaccinationDay extends AbstractEntity implements Serializable {
	
	@Column(name = "date_in_month")
	private Integer dateInMonth;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_commune")
	private Commune commune;
	
	@Column(name = "notes", length = 255, columnDefinition = "nvarchar(255)")
	private String notes;

	/**
	 * @return the dateInMonth
	 */
	public Integer getDateInMonth() {
		return dateInMonth;
	}

	/**
	 * @param dateInMonth the dateInMonth to set
	 */
	public void setDateInMonth(Integer dateInMonth) {
		this.dateInMonth = dateInMonth;
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

	public Commune getCommune() {
		return commune;
	}

	public void setCommune(Commune commune) {
		this.commune = commune;
	}
}
