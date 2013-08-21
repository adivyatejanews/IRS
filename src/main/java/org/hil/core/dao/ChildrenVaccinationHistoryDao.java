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

package org.hil.core.dao;

import java.util.List;
import org.hil.core.model.Children;
import org.hil.core.model.ChildrenVaccinationHistory;
import org.hil.core.model.Commune;
import org.hil.core.model.Vaccination;

public interface ChildrenVaccinationHistoryDao {
	
	public List<Children> getListChildrenDueByDueTimeCommune(String dueTime, Commune commune);
	public List<Vaccination> getListVaccinationByChild(String dueTime, Children child, Boolean overdue);
	public List<ChildrenVaccinationHistory> findByChildAndVaccinatedAndOderbyVaccinationId(Children child, Short vaccinated, boolean asc);
	public List<ChildrenVaccinationHistory> findByChildOrderbyDueDate(Long childId);
	public int removeByChildId(Long childId);
	public int removeByChildIdAndVaccinationIdAndVaccinationStatus(Long childId,Long vaccinId, Integer vaccinStatus);
	public ChildrenVaccinationHistory saveVaccinationHistory(ChildrenVaccinationHistory vaccinationEvent);
	public boolean checkFinish(Children child);
	public List<Number> getChildAndVaccinationAndVaccinated(Children child, Vaccination vaccination, Short vaccinated);
	public List<ChildrenVaccinationHistory> findByChildAndVaccineAndVaccinatedAndOderbyVaccinationId(Long childId, Short vaccinated, List<Vaccination> vaccines, boolean asc);
}