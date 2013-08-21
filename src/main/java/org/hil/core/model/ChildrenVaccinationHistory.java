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
@Table(name = "children_vaccination_history")
public class ChildrenVaccinationHistory extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_children")
	private Children child;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_vaccination")
	private Vaccination vaccination;
	
	@Column(name = "date_of_immunization")
	private Date dateOfImmunization;
	
	//0 - waiting, 1 - done, 2 - missed, 3 - not indicated
	@Column(name = "vaccinated")
	private Short vaccinated;
	
	@Column(name = "reason_if_missed", length = 255, columnDefinition = "nvarchar(255)")
	private String reasonIfMissed;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "id_vaccinated_location")
	private Commune vaccinatedLocation;
	
	@Column(name = "overdue")
	private boolean overdue;
	
	@Column(name = "other_location", length = 255, columnDefinition = "nvarchar(255)")
	private String otherLocation;
	
	//1 - National hospital, 2 - Province hospital, 3 - District hospital, 4 - Clinic/Private hospital, 5 - Other 
	@Column(name = "other_vaccinated_location")
	private Short otherVaccinatedLocation;
	
	//0 - no, 1 - normal, 2 - serious
	@Column(name = "reaction_status")
	private Short reactionStatus;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "modified_time")
	private Date modifiedTime;
	
	@Column(name = "from_mobile")
	private Boolean fromMobile;

	public Short getReactionStatus() {
		return reactionStatus;
	}

	public void setReactionStatus(Short reactionStatus) {
		this.reactionStatus = reactionStatus;
	}

	/**
	 * @return the child
	 */
	public Children getChild() {
		return child;
	}

	/**
	 * @param child the child to set
	 */
	public void setChild(Children child) {
		this.child = child;
	}

	/**
	 * @return the vaccination
	 */
	public Vaccination getVaccination() {
		return vaccination;
	}

	/**
	 * @param vaccination the vaccination to set
	 */
	public void setVaccination(Vaccination vaccination) {
		this.vaccination = vaccination;
	}

	/**
	 * @return the dateOfImmunization
	 */
	public Date getDateOfImmunization() {
		return dateOfImmunization;
	}

	/**
	 * @param dateOfImmunization the dateOfImmunization to set
	 */
	public void setDateOfImmunization(Date dateOfImmunization) {
		this.dateOfImmunization = dateOfImmunization;
	}

	/**
	 * @return the reasonIfMissed
	 */
	public String getReasonIfMissed() {
		return reasonIfMissed;
	}

	/**
	 * @param reasonIfMissed the reasonIfMissed to set
	 */
	public void setReasonIfMissed(String reasonIfMissed) {
		this.reasonIfMissed = reasonIfMissed;
	}

	/**
	 * @return the vaccinatedLocation
	 */
	public Commune getVaccinatedLocation() {
		return vaccinatedLocation;
	}

	/**
	 * @param vaccinatedLocation the vaccinatedLocation to set
	 */
	public void setVaccinatedLocation(Commune vaccinatedLocation) {
		this.vaccinatedLocation = vaccinatedLocation;
	}

	public boolean isOverdue() {
		return overdue;
	}

	public void setOverdue(boolean overdue) {
		this.overdue = overdue;
	}

	public Short getVaccinated() {
		return vaccinated;
	}

	public void setVaccinated(Short vaccinated) {
		this.vaccinated = vaccinated;
	}

	public String getOtherLocation() {
		return otherLocation;
	}

	public void setOtherLocation(String otherLocation) {
		this.otherLocation = otherLocation;
	}

	public Short getOtherVaccinatedLocation() {
		return otherVaccinatedLocation;
	}

	public void setOtherVaccinatedLocation(Short otherVaccinatedLocation) {
		this.otherVaccinatedLocation = otherVaccinatedLocation;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public boolean isFromMobile() {
		return fromMobile;
	}

	public void setFromMobile(boolean fromMobile) {
		this.fromMobile = fromMobile;
	}
	
}
