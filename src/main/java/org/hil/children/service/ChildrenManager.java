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

package org.hil.children.service;

import java.util.List;
import org.hil.core.model.Children;
import org.hil.core.model.ChildrenVaccinationHistory;
import org.hil.core.model.Commune;
import org.hil.core.model.District;
import org.hil.core.model.Vaccination;
import org.hil.core.model.vo.ChildrenDuePrintVO;
import org.hil.core.model.vo.ChildrenVaccinatedInLocationVO;
import org.hil.core.model.vo.RegionVaccinationReportData;
import org.hil.core.model.vo.search.ChildrenSearchVO;

public interface ChildrenManager {
	
	List<Children> searchChildren(ChildrenSearchVO params);
	public Children saveChild(Children child, boolean force);
	public List<ChildrenDuePrintVO> getListChildrenDue(String timeDue, Commune commune);
	public String printListChildrenDue(String timeDue, Commune commune, List<ChildrenDuePrintVO> childrenDue);
	public List<ChildrenVaccinationHistory> getVaccinationHistory(Children child);
	public ChildrenVaccinationHistory saveVaccinationHistory(ChildrenVaccinationHistory vaccinationEvent);
	public List<ChildrenVaccinationHistory> saveVaccinationHistory(ChildrenVaccinationHistory vaccinationEvent,Long childId, Short vaccinated, List<Vaccination> vaccines, boolean asc);
	public void deleteChild(Children child);	
	public List<RegionVaccinationReportData> getChildrenVaccinationReport(String timeFrom,String timeTo, Commune commune, District district);
	public String printListVaccinationReport(String type, String timeFrom,String timeTo, Commune commune, District district, List<RegionVaccinationReportData> statistics);
	public String printListChildren(ChildrenSearchVO params);	
	public void importExcel();
	public List<ChildrenVaccinatedInLocationVO> getChildrenVaccinatedInLocationReport(String timeFrom,String timeTo, Commune commune, District district, Vaccination vaccine);
	public String printListVaccinatedInLocationReport(String type, String timeFrom,String timeTo, Commune commune, District district, Vaccination vaccine, List<ChildrenVaccinatedInLocationVO> statistics);
	public List<ChildrenVaccinationHistory> getVaccinationHistoryByVaccines(Long childId, Short vaccinated, List<Vaccination> vaccines, boolean asc);
}