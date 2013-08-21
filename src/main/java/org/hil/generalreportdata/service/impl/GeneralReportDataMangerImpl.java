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

package org.hil.generalreportdata.service.impl;

import java.util.List;

import org.hil.core.dao.ChildrenUnder1Dao;
import org.hil.core.dao.GenericChildrenUnder1Dao;
import org.hil.core.dao.GenericGeneralReportDataDao;
import org.hil.core.dao.GeneralReportDataDao;
import org.hil.core.model.ChildrenUnder1;
import org.hil.core.model.District;
import org.hil.core.model.GeneralReportData;
import org.hil.core.service.BaseManager;
import org.hil.generalreportdata.service.GeneralReportDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("generalReportDataManager")
public class GeneralReportDataMangerImpl extends BaseManager implements GeneralReportDataManager {
	
	@Autowired
	private GenericChildrenUnder1Dao generalChildrenUnder1Dao; 
	
	@Autowired
	@Qualifier("childrenUnder1DaoExt")
	private ChildrenUnder1Dao childrenUnder1DaoExt;
	
	@Autowired
	private GenericGeneralReportDataDao generalReportDataDao; 
	
	@Autowired
	@Qualifier("generalReportDataDaoExt")
	private GeneralReportDataDao generalReportDataDaoExt;

	
	public List<ChildrenUnder1> getAllChildrenUnder1InCommunesByDistrict(String time, Long communeId, District district) {
		return childrenUnder1DaoExt.getAllChildrenUnder1InCommunesByDistrict(time, communeId, district);
	}
	
	public List<ChildrenUnder1> createChildrenUnder1InCommunesByDistrict(String time, Long communeId, District district) {
		return childrenUnder1DaoExt.createChildrenUnder1InCommunesByDistrict(time, communeId, district);
	}
	
	public List<ChildrenUnder1> saveChildrenUnder1(String time, Long communeId, List<ChildrenUnder1> childrenUnder1Datas) {
		for (ChildrenUnder1 cu : childrenUnder1Datas) {
			generalChildrenUnder1Dao.save(cu);
		}		
		return getAllChildrenUnder1InCommunesByDistrict(time, communeId, childrenUnder1Datas.get(0).getCommune().getDistrict());
	}
	
	public List<GeneralReportData> getAllGeneralReportDataInCommunesByDistrict(String time, Long communeId, District district) {
		return generalReportDataDaoExt.getAllGeneralReportDataInCommunesByDistrict(time, communeId, district);
	}
	
	public List<GeneralReportData> createGeneralReportDataInCommunesByDistrict(String time, Long communeId, District district) {
		return generalReportDataDaoExt.createGeneralReportDataInCommunesByDistrict(time, communeId, district);
	}
	
	public List<GeneralReportData> saveGeneralReportData(String time, Long communeId, List<GeneralReportData> generalReportDatas) {
		for (GeneralReportData generalReportData : generalReportDatas) {
			generalReportDataDao.save(generalReportData);
		}		
		return getAllGeneralReportDataInCommunesByDistrict(time, communeId, generalReportDatas.get(0).getCommune().getDistrict());
	}

}
