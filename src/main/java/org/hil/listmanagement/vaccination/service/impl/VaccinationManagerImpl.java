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

package org.hil.listmanagement.vaccination.service.impl;

import java.util.List;

import org.hil.core.dao.VaccinationDao;
import org.hil.core.dao.GenericVaccinationDao;
import org.hil.core.model.Vaccination;
import org.hil.core.service.BaseManager;
import org.hil.listmanagement.vaccination.service.VaccinationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("vaccinationManager")
public class VaccinationManagerImpl extends BaseManager implements VaccinationManager {
	@Autowired
	@Qualifier("vaccinationDaoExt")
	private VaccinationDao vaccinationDaoExt;
	
	@Autowired
	private GenericVaccinationDao vaccinationDao;	
	
	public List<Vaccination> getAllVaccinations() {	
		return vaccinationDao.getAll(null, null);
	}
	
	public Vaccination saveVaccination(Vaccination vaccination) {
		return vaccinationDao.save(vaccination);
	}

	public void deleteVaccination(
			Vaccination vaccination) {
		vaccinationDao.remove(vaccination);
	}

	public List<Vaccination> searchVaccination(
			String schString) {
		return vaccinationDaoExt.searchVaccination(schString);
	}	

}