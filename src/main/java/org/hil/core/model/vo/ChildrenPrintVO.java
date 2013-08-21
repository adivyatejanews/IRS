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

public class ChildrenPrintVO {

	private String fullName;	
	private Date dateOfBirth;	
	private String childCode;	
	private boolean gender;	
	private String villageName;	
	private String communeName;	
	private String fatherName;	
	private Integer fatherBirthYear;	
	private String fatherID;	
	private String fatherMobile;	
	private String motherName;	
	private Integer motherBirthYear;	
	private String motherID;	
	private String motherMobile;	
	private Integer currentCaretaker;	
	private String caretakerName;	
	private Integer caretakerBirthYear;	
	private String caretakerID;	
	private String caretakerMobile;
	private String VGB;
	private String BCG;
	private String DPT_VGB_Hib1;
	private String OPV1;
	private String DPT_VGB_Hib2;
	private String OPV2;
	private String DPT_VGB_Hib3;
	private String OPV3;
	private String measles1;	
	
	public String getVGB() {
		return VGB;
	}
	public void setVGB(String vGB) {
		VGB = vGB;
	}
	public String getBCG() {
		return BCG;
	}
	public void setBCG(String bCG) {
		BCG = bCG;
	}
	public String getDPT_VGB_Hib1() {
		return DPT_VGB_Hib1;
	}
	public void setDPT_VGB_Hib1(String dPT_VGB_Hib1) {
		DPT_VGB_Hib1 = dPT_VGB_Hib1;
	}
	public String getOPV1() {
		return OPV1;
	}
	public void setOPV1(String oPV1) {
		OPV1 = oPV1;
	}
	public String getDPT_VGB_Hib2() {
		return DPT_VGB_Hib2;
	}
	public void setDPT_VGB_Hib2(String dPT_VGB_Hib2) {
		DPT_VGB_Hib2 = dPT_VGB_Hib2;
	}
	public String getOPV2() {
		return OPV2;
	}
	public void setOPV2(String oPV2) {
		OPV2 = oPV2;
	}
	public String getDPT_VGB_Hib3() {
		return DPT_VGB_Hib3;
	}
	public void setDPT_VGB_Hib3(String dPT_VGB_Hib3) {
		DPT_VGB_Hib3 = dPT_VGB_Hib3;
	}
	public String getOPV3() {
		return OPV3;
	}
	public void setOPV3(String oPV3) {
		OPV3 = oPV3;
	}
	public String getMeasles1() {
		return measles1;
	}
	public void setMeasles1(String measles1) {
		this.measles1 = measles1;
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
	public String getVillageName() {
		return villageName;
	}
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	public String getCommuneName() {
		return communeName;
	}
	public void setCommuneName(String communeName) {
		this.communeName = communeName;
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
	public Integer getCurrentCaretaker() {
		return currentCaretaker;
	}
	public void setCurrentCaretaker(Integer currentCaretaker) {
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
	
}
