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

import java.util.List;

public class VaccinationReportPrintVO {
	private String regionName;
	private Short regionId;
	private String provinceName;
	private String districtName;
	private String communeName;
	private String timeData;
	private List<RegionVaccinationReportData> statistics;
	
	private Integer sumChildrenUnder1;
	private Integer sumReactionNormalCases;
	private Integer sumReactionSeriousCases;
	private Integer sumProtectedTetanusCases;
		
	private String sumVGBL24;
	private String sumVGBG24;
	private String sumBCG;
	private String sumDPT_VGB_Hib1;
	private String sumDPT_VGB_Hib2;
	private String sumDPT_VGB_Hib3;
	private String sumOPV1;
	private String sumOPV2;
	private String sumOPV3;
	private String sumMeasles1;
	private Integer sumAmountOfFinish;
	
	private Integer totalChildrenUnder1;
	private Integer totalReactionNormalCases;
	private Integer totalReactionSeriousCases;
	private Integer totalProtectedTetanusCases;
		
	private String totalVGBL24;
	private String totalVGBG24;
	private String totalBCG;
	private String totalDPT_VGB_Hib1;
	private String totalDPT_VGB_Hib2;
	private String totalDPT_VGB_Hib3;
	private String totalOPV1;
	private String totalOPV2;
	private String totalOPV3;
	private String totalMeasles1;
	private Integer totalAmountOfFinish;
	
	
	public Short getRegionId() {
		return regionId;
	}
	public void setRegionId(Short regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getTimeData() {
		return timeData;
	}
	public void setTimeData(String timeData) {
		this.timeData = timeData;
	}
	public List<RegionVaccinationReportData> getStatistics() {
		return statistics;
	}
	public void setStatistics(List<RegionVaccinationReportData> statistics) {
		this.statistics = statistics;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getCommuneName() {
		return communeName;
	}
	public void setCommuneName(String communeName) {
		this.communeName = communeName;
	}
	public Integer getSumChildrenUnder1() {
		return sumChildrenUnder1;
	}
	public void setSumChildrenUnder1(Integer sumChildrenUnder1) {
		this.sumChildrenUnder1 = sumChildrenUnder1;
	}
	public Integer getSumReactionNormalCases() {
		return sumReactionNormalCases;
	}
	public void setSumReactionNormalCases(Integer sumReactionNormalCases) {
		this.sumReactionNormalCases = sumReactionNormalCases;
	}
	public Integer getSumReactionSeriousCases() {
		return sumReactionSeriousCases;
	}
	public void setSumReactionSeriousCases(Integer sumReactionSeriousCases) {
		this.sumReactionSeriousCases = sumReactionSeriousCases;
	}
	public Integer getSumProtectedTetanusCases() {
		return sumProtectedTetanusCases;
	}
	public void setSumProtectedTetanusCases(Integer sumProtectedTetanusCases) {
		this.sumProtectedTetanusCases = sumProtectedTetanusCases;
	}
	public String getSumVGBL24() {
		return sumVGBL24;
	}
	public void setSumVGBL24(String sumVGBL24) {
		this.sumVGBL24 = sumVGBL24;
	}
	public String getSumVGBG24() {
		return sumVGBG24;
	}
	public void setSumVGBG24(String sumVGBG24) {
		this.sumVGBG24 = sumVGBG24;
	}
	public String getSumBCG() {
		return sumBCG;
	}
	public void setSumBCG(String sumBCG) {
		this.sumBCG = sumBCG;
	}
	public String getSumDPT_VGB_Hib1() {
		return sumDPT_VGB_Hib1;
	}
	public void setSumDPT_VGB_Hib1(String sumDPT_VGB_Hib1) {
		this.sumDPT_VGB_Hib1 = sumDPT_VGB_Hib1;
	}
	public String getSumDPT_VGB_Hib2() {
		return sumDPT_VGB_Hib2;
	}
	public void setSumDPT_VGB_Hib2(String sumDPT_VGB_Hib2) {
		this.sumDPT_VGB_Hib2 = sumDPT_VGB_Hib2;
	}
	public String getSumDPT_VGB_Hib3() {
		return sumDPT_VGB_Hib3;
	}
	public void setSumDPT_VGB_Hib3(String sumDPT_VGB_Hib3) {
		this.sumDPT_VGB_Hib3 = sumDPT_VGB_Hib3;
	}
	public String getSumOPV1() {
		return sumOPV1;
	}
	public void setSumOPV1(String sumOPV1) {
		this.sumOPV1 = sumOPV1;
	}
	public String getSumOPV2() {
		return sumOPV2;
	}
	public void setSumOPV2(String sumOPV2) {
		this.sumOPV2 = sumOPV2;
	}
	public String getSumOPV3() {
		return sumOPV3;
	}
	public void setSumOPV3(String sumOPV3) {
		this.sumOPV3 = sumOPV3;
	}
	public String getSumMeasles1() {
		return sumMeasles1;
	}
	public void setSumMeasles1(String sumMeasles1) {
		this.sumMeasles1 = sumMeasles1;
	}
	public Integer getSumAmountOfFinish() {
		return sumAmountOfFinish;
	}
	public void setSumAmountOfFinish(Integer sumAmountOfFinish) {
		this.sumAmountOfFinish = sumAmountOfFinish;
	}
	public Integer getTotalChildrenUnder1() {
		return totalChildrenUnder1;
	}
	public void setTotalChildrenUnder1(Integer totalChildrenUnder1) {
		this.totalChildrenUnder1 = totalChildrenUnder1;
	}
	public Integer getTotalReactionNormalCases() {
		return totalReactionNormalCases;
	}
	public void setTotalReactionNormalCases(Integer totalReactionNormalCases) {
		this.totalReactionNormalCases = totalReactionNormalCases;
	}
	public Integer getTotalReactionSeriousCases() {
		return totalReactionSeriousCases;
	}
	public void setTotalReactionSeriousCases(Integer totalReactionSeriousCases) {
		this.totalReactionSeriousCases = totalReactionSeriousCases;
	}
	public Integer getTotalProtectedTetanusCases() {
		return totalProtectedTetanusCases;
	}
	public void setTotalProtectedTetanusCases(Integer totalProtectedTetanusCases) {
		this.totalProtectedTetanusCases = totalProtectedTetanusCases;
	}
	public String getTotalVGBL24() {
		return totalVGBL24;
	}
	public void setTotalVGBL24(String totalVGBL24) {
		this.totalVGBL24 = totalVGBL24;
	}
	public String getTotalVGBG24() {
		return totalVGBG24;
	}
	public void setTotalVGBG24(String totalVGBG24) {
		this.totalVGBG24 = totalVGBG24;
	}
	public String getTotalBCG() {
		return totalBCG;
	}
	public void setTotalBCG(String totalBCG) {
		this.totalBCG = totalBCG;
	}
	public String getTotalDPT_VGB_Hib1() {
		return totalDPT_VGB_Hib1;
	}
	public void setTotalDPT_VGB_Hib1(String totalDPT_VGB_Hib1) {
		this.totalDPT_VGB_Hib1 = totalDPT_VGB_Hib1;
	}
	public String getTotalDPT_VGB_Hib2() {
		return totalDPT_VGB_Hib2;
	}
	public void setTotalDPT_VGB_Hib2(String totalDPT_VGB_Hib2) {
		this.totalDPT_VGB_Hib2 = totalDPT_VGB_Hib2;
	}
	public String getTotalDPT_VGB_Hib3() {
		return totalDPT_VGB_Hib3;
	}
	public void setTotalDPT_VGB_Hib3(String totalDPT_VGB_Hib3) {
		this.totalDPT_VGB_Hib3 = totalDPT_VGB_Hib3;
	}
	public String getTotalOPV1() {
		return totalOPV1;
	}
	public void setTotalOPV1(String totalOPV1) {
		this.totalOPV1 = totalOPV1;
	}
	public String getTotalOPV2() {
		return totalOPV2;
	}
	public void setTotalOPV2(String totalOPV2) {
		this.totalOPV2 = totalOPV2;
	}
	public String getTotalOPV3() {
		return totalOPV3;
	}
	public void setTotalOPV3(String totalOPV3) {
		this.totalOPV3 = totalOPV3;
	}
	public String getTotalMeasles1() {
		return totalMeasles1;
	}
	public void setTotalMeasles1(String totalMeasles1) {
		this.totalMeasles1 = totalMeasles1;
	}
	public Integer getTotalAmountOfFinish() {
		return totalAmountOfFinish;
	}
	public void setTotalAmountOfFinish(Integer totalAmountOfFinish) {
		this.totalAmountOfFinish = totalAmountOfFinish;
	}
	
	
}
