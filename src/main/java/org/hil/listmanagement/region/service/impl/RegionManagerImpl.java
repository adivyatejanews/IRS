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

package org.hil.listmanagement.region.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.hil.core.dao.ChildrenDao;
import org.hil.core.dao.GenericDistrictDao;
import org.hil.core.dao.GenericVillageDao;
import org.hil.core.dao.GenericProvinceDao;
import org.hil.core.dao.GenericCommuneDao;
import org.hil.core.dao.GenericVaccinationDayDao;
import org.hil.core.dao.ProvinceDao;
import org.hil.core.dao.VaccinationDayDao;
import org.hil.core.model.District;
import org.hil.core.model.Village;
import org.hil.core.model.Province;
import org.hil.core.model.Commune;
import org.hil.core.model.VaccinationDay;
import org.hil.core.service.BaseManager;
import org.hil.listmanagement.region.service.RegionManager;

@Service("regionManager")
public class RegionManagerImpl extends BaseManager implements RegionManager {
	
	@Autowired	
	private GenericProvinceDao provinceDao;
	
	@Autowired
	@Qualifier("childrenDaoExt")
	private ChildrenDao childrenDaoExt;
	
	@Autowired	
	@Qualifier("provinceDaoExt")
	private ProvinceDao provinceDaoExt;
	
	@Autowired	
	private GenericDistrictDao districtDao;	
	
	@Autowired	
	private GenericCommuneDao communeDao;
	
	@Autowired	
	private GenericVillageDao villageDao;
	
	@Autowired	
	private GenericVaccinationDayDao vaccinationDayDao;
	
	@Autowired	
	@Qualifier("vaccinationDayDaoExt")
	private VaccinationDayDao vaccinationDayDaoExt;
	
	public List<Province> getAllProvincesOrderByName(String asc) {
		return provinceDaoExt.getAllOrderbyName(asc);
	}
	
	public Province saveProvince(Province province) {		
		try {		
			province = provinceDao.save(province);
			log.debug("saved province: " + province.getId());
		} catch (Exception e) {
			log.debug("error constraint: " + e.toString());
		}
		return province;
	}
	
	public void deleteProvince(Province province) {
		try {
			log.debug("delete province:" +province.getId());
			provinceDao.remove(province);
		} catch (Exception e) {
			log.debug("error constraint:" + e.toString());
		}
	}
	
	public List<District> getAllDistricts(Province province) {
		return districtDao.findByProvince(province);
	}
	
	public District saveDistrict(District district) {		
		district = districtDao.save(district);			
		return district;
	}	
		
	public void deleteDistrict(District district) {
		try {			
			log.debug("delete district:" +district.getId());			
			districtDao.remove(district);		
		} catch (Exception e) {
			log.debug("error constraint :" + e.toString());
		}
	}	
	
	public List<Commune> getAllCommunes(District district) {
		return communeDao.findByDistrict(district);
	}
	
	public Commune saveCommune(Commune commune) {		
		boolean createVaccinationSchedule = false;
		if (commune.getId() == null || commune.getId() == 0) {
			createVaccinationSchedule = true;
		} else {
			List<VaccinationDay> vds = vaccinationDayDao.findByCommune(commune);
			if (vds == null || vds.size() == 0)
				createVaccinationSchedule = true;
		}		
		commune = communeDao.save(commune);
		if (createVaccinationSchedule) {
			VaccinationDay aVS = new VaccinationDay();
			aVS.setCommune(commune);
			aVS.setNotes("");
			vaccinationDayDao.save(aVS);
		}
		return commune;
	}	
	
	public String deleteCommune(Commune commune) {
		try {
			log.debug("delete commune:" +commune.getId() + commune.getCommuneName());
			
			if (childrenDaoExt.hasChildInCommnue(commune.getId()))
				return "data_is_currently_in_use";			
			vaccinationDayDaoExt.removeByCommuneId(commune.getId());
			communeDao.remove(commune);
			
		} catch (Exception e) {
			log.debug("error constraint:" + e.toString());			
		}	
		return "";
	}
	
	public List<Village> getAllVillages(Commune commune) {
		return villageDao.findByCommune(commune);
	}
	
	public Village saveVillage(Village village) {		
		village = villageDao.save(village);		
		return village;
	}	
	
	public void deleteVillage(Village village) {
		try {
			log.debug("delete village:" +village.getId());
			villageDao.remove(village);
		} catch (Exception e) {
			log.debug("error constraint:" + e.toString());
		}		
	}

}