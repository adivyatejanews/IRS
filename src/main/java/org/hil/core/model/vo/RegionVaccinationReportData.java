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

public class RegionVaccinationReportData {
	private Integer childrenUnder1;
	private Integer reactionNormalCases;
	private Integer reactionSeriousCases;
	private Integer protectedTetanusCases;
		
	private Integer VGBL24;
	private Integer VGBG24;
	private Integer BCG;
	private Integer DPT_VGB_Hib1;
	private Integer DPT_VGB_Hib2;
	private Integer DPT_VGB_Hib3;
	private Integer OPV1;
	private Integer OPV2;
	private Integer OPV3;
	private Integer measles1;
	
	private Integer eVGBL24;
	private Integer eVGBG24;
	private Integer eBCG;
	private Integer eDPT_VGB_Hib1;
	private Integer eDPT_VGB_Hib2;
	private Integer eDPT_VGB_Hib3;
	private Integer eOPV1;
	private Integer eOPV2;
	private Integer eOPV3;
	private Integer eMeasles1;
	
	private Integer amountOfFinish;	
	
	private String regionName;

	public Integer getChildrenUnder1() {
		return childrenUnder1;
	}

	public void setChildrenUnder1(Integer childrenUnder1) {
		this.childrenUnder1 = childrenUnder1;
	}

	public Integer getReactionNormalCases() {
		return reactionNormalCases;
	}

	public void setReactionNormalCases(Integer reactionNormalCases) {
		this.reactionNormalCases = reactionNormalCases;
	}

	public Integer getReactionSeriousCases() {
		return reactionSeriousCases;
	}

	public void setReactionSeriousCases(Integer reactionSeriousCases) {
		this.reactionSeriousCases = reactionSeriousCases;
	}

	public Integer getProtectedTetanusCases() {
		return protectedTetanusCases;
	}

	public void setProtectedTetanusCases(Integer protectedTetanusCases) {
		this.protectedTetanusCases = protectedTetanusCases;
	}

	public Integer getBCG() {
		return BCG;
	}

	public void setBCG(Integer bCG) {
		BCG = bCG;
	}

	public Integer getVGBL24() {
		return VGBL24;
	}

	public void setVGBL24(Integer vGBL24) {
		VGBL24 = vGBL24;
	}

	public Integer getVGBG24() {
		return VGBG24;
	}

	public void setVGBG24(Integer vGBG24) {
		VGBG24 = vGBG24;
	}

	public Integer getDPT_VGB_Hib1() {
		return DPT_VGB_Hib1;
	}

	public void setDPT_VGB_Hib1(Integer dPT_VGB_Hib1) {
		DPT_VGB_Hib1 = dPT_VGB_Hib1;
	}

	public Integer getDPT_VGB_Hib2() {
		return DPT_VGB_Hib2;
	}

	public void setDPT_VGB_Hib2(Integer dPT_VGB_Hib2) {
		DPT_VGB_Hib2 = dPT_VGB_Hib2;
	}

	public Integer getDPT_VGB_Hib3() {
		return DPT_VGB_Hib3;
	}

	public void setDPT_VGB_Hib3(Integer dPT_VGB_Hib3) {
		DPT_VGB_Hib3 = dPT_VGB_Hib3;
	}

	public Integer getOPV1() {
		return OPV1;
	}

	public void setOPV1(Integer oPV1) {
		OPV1 = oPV1;
	}

	public Integer getOPV2() {
		return OPV2;
	}

	public void setOPV2(Integer oPV2) {
		OPV2 = oPV2;
	}

	public Integer getOPV3() {
		return OPV3;
	}

	public void setOPV3(Integer oPV3) {
		OPV3 = oPV3;
	}

	public Integer getMeasles1() {
		return measles1;
	}

	public void setMeasles1(Integer measles1) {
		this.measles1 = measles1;
	}	

	public Integer geteVGBL24() {
		return eVGBL24;
	}

	public void seteVGBL24(Integer eVGBL24) {
		this.eVGBL24 = eVGBL24 != null ? eVGBL24 : 0;
	}

	public Integer geteVGBG24() {
		return eVGBG24;
	}

	public void seteVGBG24(Integer eVGBG24) {
		this.eVGBG24 = eVGBG24 != null ? eVGBG24 : 0;
	}

	public Integer geteBCG() {
		return eBCG;
	}

	public void seteBCG(Integer eBCG) {
		this.eBCG = eBCG != null ? eBCG : 0;
	}

	public Integer geteDPT_VGB_Hib1() {
		return eDPT_VGB_Hib1;
	}

	public void seteDPT_VGB_Hib1(Integer eDPT_VGB_Hib1) {
		this.eDPT_VGB_Hib1 = eDPT_VGB_Hib1 != null ? eDPT_VGB_Hib1 : 0;
	}

	public Integer geteDPT_VGB_Hib2() {
		return eDPT_VGB_Hib2;
	}

	public void seteDPT_VGB_Hib2(Integer eDPT_VGB_Hib2) {
		this.eDPT_VGB_Hib2 = eDPT_VGB_Hib2 != null ? eDPT_VGB_Hib2 : 0;
	}

	public Integer geteDPT_VGB_Hib3() {
		return eDPT_VGB_Hib3;
	}

	public void seteDPT_VGB_Hib3(Integer eDPT_VGB_Hib3) {
		this.eDPT_VGB_Hib3 = eDPT_VGB_Hib3 != null ? eDPT_VGB_Hib3 : 0;
	}

	public Integer geteOPV1() {
		return eOPV1;
	}

	public void seteOPV1(Integer eOPV1) {
		this.eOPV1 = eOPV1 != null ? eOPV1 : 0;
	}

	public Integer geteOPV2() {
		return eOPV2;
	}

	public void seteOPV2(Integer eOPV2) {
		this.eOPV2 = eOPV2 != null ? eOPV2 : 0;
	}

	public Integer geteOPV3() {
		return eOPV3;
	}

	public void seteOPV3(Integer eOPV3) {
		this.eOPV3 = eOPV3 != null ? eOPV3 : 0;
	}

	public Integer geteMeasles1() {
		return eMeasles1;
	}

	public void seteMeasles1(Integer eMeasles1) {
		this.eMeasles1 = eMeasles1 != null ? eMeasles1 : 0;
	}

	public Integer getAmountOfFinish() {
		return amountOfFinish;
	}

	public void setAmountOfFinish(Integer amountOfFinish) {
		this.amountOfFinish = amountOfFinish;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}	
	
}