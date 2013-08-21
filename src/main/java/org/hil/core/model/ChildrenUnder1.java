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
@Table(name = "children_under_1")
public class ChildrenUnder1 extends AbstractEntity implements Serializable {
	
	@Column(name = "total_children_under_1")
	private Integer totalChildrenUnder1;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_commune")
	private Commune commune;
	
	@Column(name = "time")
	private Date time;
	
	@Column(name = "notes", length = 255, columnDefinition = "nvarchar(255)")
	private String notes;	
	
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getTotalChildrenUnder1() {
		return totalChildrenUnder1;
	}

	public void setTotalChildrenUnder1(Integer totalChildrenUnder1) {
		this.totalChildrenUnder1 = totalChildrenUnder1;
	}
	
	public String getNotes() {
		return notes;
	}

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