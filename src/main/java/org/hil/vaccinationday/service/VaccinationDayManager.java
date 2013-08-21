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

package org.hil.vaccinationday.service;

import java.util.List;
import org.hil.core.model.District;
import org.hil.core.model.VaccinationDay;

public interface VaccinationDayManager {
	public List<VaccinationDay> getAllVaccinationDateInDistrict(District district);
	public VaccinationDay saveVaccinationDate(VaccinationDay vaccinationDay);
	public List<VaccinationDay> updateAllVaccinationDate(District district, Integer dateInt);	
}
