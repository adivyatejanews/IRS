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
import org.hil.core.model.Vaccination;
import org.parancoe.persistence.dao.generic.Dao;

@Dao(entity=ChildrenVaccinationHistory.class)
public interface GenericChildrenVaccinationHistoryDao extends GenericDaoExt<ChildrenVaccinationHistory, Long> {
	
	public List<ChildrenVaccinationHistory> findByChild(Children child);
	public List<ChildrenVaccinationHistory> findByChildAndVaccinated(Children child, Short vaccinated);
	public List<ChildrenVaccinationHistory> findByChildAndVaccination(Children child, Vaccination vaccination);
	public List<ChildrenVaccinationHistory> findByChildAndVaccinationAndVaccinated(Children child, Vaccination vaccination, Short vaccinated);

}
