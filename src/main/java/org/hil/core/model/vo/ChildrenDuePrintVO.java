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
import java.util.List;
import org.hil.core.model.Vaccination;

public class ChildrenDuePrintVO {
	private Long childId;
	private String fullName;	
	private Date dateOfBirth;	
	private String childCode;	
	private boolean gender;	
	private String residen;
	private String address;	
	private String fatherName;	
	private Integer fatherBirthYear;	
	private String fatherID;	
	private String fatherMobile;	
	private String motherName;	
	private Integer motherBirthYear;	
	private String motherID;	
	private String motherMobile;	
	private Short currentCaretaker;	
	private String caretakerName;	
	private Integer caretakerBirthYear;	
	private String caretakerID;	
	private String caretakerMobile;
	private boolean locked;
	private String vaccinationOverdue;
	private String vaccinationDue;
	private String vaccines;
	private List<Vaccination> listVaccines;
	private Long pId;
	private String provinceName;
	private Long dId;
	private String districtName;
	private Long cId;
	private String communeName;
	private Long vId;	
	
	
	public List<Vaccination> getListVaccines() {
		return listVaccines;
	}
	public void setListVaccines(List<Vaccination> listVaccines) {
		this.listVaccines = listVaccines;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public Long getChildId() {
		return childId;
	}
	public void setChildId(Long childId) {
		this.childId = childId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getChildCode() {
		return childCode;
	}
	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public String getResiden() {
		return residen;
	}
	public void setResiden(String residen) {
		this.residen = residen;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public Integer getFatherBirthYear() {
		return fatherBirthYear;
	}
	public void setFatherBirthYear(Integer fatherBirthYear) {
		this.fatherBirthYear = fatherBirthYear;
	}
	public String getFatherID() {
		return fatherID;
	}
	public void setFatherID(String fatherID) {
		this.fatherID = fatherID;
	}
	public String getFatherMobile() {
		return fatherMobile;
	}
	public void setFatherMobile(String fatherMobile) {
		this.fatherMobile = fatherMobile;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public Integer getMotherBirthYear() {
		return motherBirthYear;
	}
	public void setMotherBirthYear(Integer motherBirthYear) {
		this.motherBirthYear = motherBirthYear;
	}
	public String getMotherID() {
		return motherID;
	}
	public void setMotherID(String motherID) {
		this.motherID = motherID;
	}
	public String getMotherMobile() {
		return motherMobile;
	}
	public void setMotherMobile(String motherMobile) {
		this.motherMobile = motherMobile;
	}
	public Short getCurrentCaretaker() {
		return currentCaretaker;
	}
	public void setCurrentCaretaker(Short currentCaretaker) {
		this.currentCaretaker = currentCaretaker;
	}
	public String getCaretakerName() {
		return caretakerName;
	}
	public void setCaretakerName(String caretakerName) {
		this.caretakerName = caretakerName;
	}
	public Integer getCaretakerBirthYear() {
		return caretakerBirthYear;
	}
	public void setCaretakerBirthYear(Integer caretakerBirthYear) {
		this.caretakerBirthYear = caretakerBirthYear;
	}
	public String getCaretakerID() {
		return caretakerID;
	}
	public void setCaretakerID(String caretakerID) {
		this.caretakerID = caretakerID;
	}
	public String getCaretakerMobile() {
		return caretakerMobile;
	}
	public void setCaretakerMobile(String caretakerMobile) {
		this.caretakerMobile = caretakerMobile;
	}
	public String getVaccinationOverdue() {
		return vaccinationOverdue;
	}
	public void setVaccinationOverdue(String vaccinationOverdue) {
		this.vaccinationOverdue = vaccinationOverdue;
	}
	public String getVaccinationDue() {
		return vaccinationDue;
	}
	public void setVaccinationDue(String vaccinationDue) {
		this.vaccinationDue = vaccinationDue;
	}
	public String getVaccines() {
		return vaccines;
	}
	public void setVaccines(String vaccines) {
		this.vaccines = vaccines;
	}
	public Long getpId() {
		return pId;
	}
	public void setpId(Long pId) {
		this.pId = pId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public Long getdId() {
		return dId;
	}
	public void setdId(Long dId) {
		this.dId = dId;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public Long getcId() {
		return cId;
	}
	public void setcId(Long cId) {
		this.cId = cId;
	}
	public String getCommuneName() {
		return communeName;
	}
	public void setCommuneName(String communeName) {
		this.communeName = communeName;
	}
	public Long getvId() {
		return vId;
	}
	public void setvId(Long vId) {
		this.vId = vId;
	}	
	
}