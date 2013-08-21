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

package org.hil.vaccinationday.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hil.core.dao.ChildrenDao;
import org.hil.core.dao.ChildrenVaccinationHistoryDao;
import org.hil.core.dao.GenericChildrenVaccinationHistoryDao;
import org.hil.core.dao.GenericVaccinationDayDao;
import org.hil.core.dao.VaccinationDayDao;
import org.hil.core.model.Children;
import org.hil.core.model.ChildrenVaccinationHistory;
import org.hil.core.model.District;
import org.hil.core.model.Vaccination;
import org.hil.core.model.VaccinationDay;
import org.hil.core.service.BaseManager;
import org.hil.vaccinationday.service.VaccinationDayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("vaccinationDayManager")
public class VaccinationDayManagerImpl extends BaseManager implements VaccinationDayManager {
	
	@Autowired
	private GenericVaccinationDayDao vaccinationDayDao;
	
	@Autowired
	@Qualifier("vaccinationDayDaoExt")
	private  VaccinationDayDao  vaccinationDayDaoExt;
	
	@Autowired
	@Qualifier("childrenDaoExt")
	private ChildrenDao childrenDaoExt;
	
	@Autowired
	private GenericChildrenVaccinationHistoryDao childrenVaccinationHistoryDao;
	
	@Autowired
	@Qualifier("childrenVaccinationHistoryDaoExt")
	private ChildrenVaccinationHistoryDao childrenVaccinationHistoryDaoExt;
	
	public List<VaccinationDay> getAllVaccinationDateInDistrict(District district) {
		return vaccinationDayDaoExt.getAllVaccinationDateInDistrict(district);
	}
	
	public VaccinationDay saveVaccinationDate(VaccinationDay vaccinationDay) {
		VaccinationDay aVD = vaccinationDayDao.save(vaccinationDay);
//		updateChildrenVaccinationDay(aVD);
		return aVD;
	}
	
	public List<VaccinationDay> updateAllVaccinationDate(District district, Integer dateInt) {		
		List<VaccinationDay> listVD = vaccinationDayDaoExt.updateAllVaccinationDate(district, dateInt);
//		for (VaccinationDay aVD : listVD) {
//			updateChildrenVaccinationDay(aVD);
//		}
		return listVD;
	}
	
	public void updateChildrenVaccinationDay(VaccinationDay vDay) {
		List<Children> listChildren = childrenDaoExt.findByCommuneAndFinishedAndLocked(vDay.getCommune(), false, false);
		log.debug("List update: " + listChildren.size());
		if (listChildren == null || listChildren.size() == 0)
			return;
		
		for (Children c : listChildren) {
			List<ChildrenVaccinationHistory> listVaccinationPending = childrenVaccinationHistoryDaoExt.findByChildAndVaccinatedAndOderbyVaccinationId(c, (short)0, true);
			List<ChildrenVaccinationHistory> listFinishedVaccinations = childrenVaccinationHistoryDaoExt.findByChildAndVaccinatedAndOderbyVaccinationId(c, (short)1, true);
			
			Date prevDueDate = null;
			log.debug("************Done: " + listFinishedVaccinations.size() + " Pending: " + listVaccinationPending.size());
			if (listFinishedVaccinations != null && listFinishedVaccinations.size() > 0) {
				prevDueDate = listFinishedVaccinations.get(listFinishedVaccinations.size()-1).getDateOfImmunization();
			}			
			log.debug("************DIM: " + vDay.getDateInMonth());			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strDueDate = "";
			Date dueDate = null;
			Integer dateInMonth = vDay.getDateInMonth();
			Date today = new Date();
			Integer currentMonth = today.getMonth() + 1;
			Integer currentYear = today.getYear() + 1900;			
							
			for (int i=0; i<listVaccinationPending.size(); i++) {				
				
				ChildrenVaccinationHistory vaccinationHistory = listVaccinationPending.get(i);
				Vaccination vaccination = vaccinationHistory.getVaccination();
				
				if (vaccination.getId() > 1) {				
					Calendar recommendedTime = Calendar.getInstance();
					recommendedTime.setTime(c.getDateOfBirth());
					Integer deltaDate = vaccination.getAgeUnit() == 0 ? 0 : vaccination.getAge()*30;
					recommendedTime.add(Calendar.DATE,deltaDate);				
				
					Integer deltaYear = 0;
					Integer dueMonth = 0;
					Integer dueYear = 0;
					strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
					try {						
						boolean overCall = false;
						if (vaccination.getLimitDays() != null && vaccination.getLimitDays() > 0) {
							Calendar c2 = Calendar.getInstance();
							c2.setTime(c.getDateOfBirth());
							c2.add(Calendar.DATE,vaccination.getLimitDays());
							if (c2.getTime().getTime() < today.getTime()) {
								vaccinationHistory.setVaccinated((short)3);
								vaccinationHistory.setDateOfImmunization(recommendedTime.getTime());
								overCall = true;
							}						
						}
						if (!overCall) {
							if (prevDueDate == null) {
								if (recommendedTime.getTime().getTime() >= today.getTime()) {
									if (recommendedTime.getTime().getYear() == today.getYear()) {
										if (recommendedTime.getTime().getMonth() == today.getMonth()) {
											if (dateInMonth >= recommendedTime.getTime().getDate()) {
												dueYear = recommendedTime.getTime().getYear() + 1900;
												dueMonth = recommendedTime.getTime().getMonth() + 1;
												strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
											} else {
												dueMonth = recommendedTime.getTime().getMonth() + 1 + 1;
												deltaYear = dueMonth / 12;
												dueMonth = dueMonth % 12;											
												dueYear = currentYear + deltaYear;
												strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
											}
										} else {
											if (dateInMonth >= recommendedTime.getTime().getDate()) {
												dueYear = recommendedTime.getTime().getYear() + 1900;
												dueMonth = recommendedTime.getTime().getMonth() + 1;
												strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
											} else {
												dueMonth = recommendedTime.getTime().getMonth() + 1 + 1;
												deltaYear = dueMonth / 12;
												dueMonth = dueMonth % 12;											
												dueYear = currentYear + deltaYear;
												strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
											}
										}
									} else {
										if (dateInMonth >= recommendedTime.getTime().getDate()) {
											dueYear = recommendedTime.getTime().getYear() + 1900;
											dueMonth = recommendedTime.getTime().getMonth() + 1;
											strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
										} else {
											dueMonth = recommendedTime.getTime().getMonth() + 1 + 1;
											deltaYear = dueMonth / 12;
											dueMonth = dueMonth % 12;											
											dueYear = recommendedTime.getTime().getYear() + 1900 + deltaYear;
											strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
										}
									}								
									dueDate = format.parse(strDueDate);
									vaccinationHistory.setDateOfImmunization(dueDate);
								} else {
									if (dateInMonth >= today.getDate()) {
										dueYear = currentYear;
										dueMonth = currentMonth;
										strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
									} else {
										dueMonth = currentMonth + 1;
										deltaYear = dueMonth / 12;
										dueMonth = dueMonth % 12;											
										dueYear = currentYear + deltaYear;
										strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
									}
									dueDate = format.parse(strDueDate);
									vaccinationHistory.setDateOfImmunization(dueDate);
								}
								prevDueDate = dueDate;
							} else {
								Calendar gapTime = Calendar.getInstance();
								gapTime.setTime(prevDueDate);
								Integer deltaGapDate = vaccination.getGap() > 0 ? vaccination.getGap() : 0;
								gapTime.add(Calendar.DATE,deltaGapDate);
								
								if (recommendedTime.getTime().getTime() < gapTime.getTime().getTime()) {
									recommendedTime = gapTime;
								}
								
								if (recommendedTime.getTime().getTime() >= today.getTime()) {
									if (recommendedTime.getTime().getYear() == today.getYear()) {
										if (recommendedTime.getTime().getMonth() == today.getMonth()) {
											if (dateInMonth >= recommendedTime.getTime().getDate()) {
												dueYear = recommendedTime.getTime().getYear() + 1900;
												dueMonth = recommendedTime.getTime().getMonth() + 1;
												strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
											} else {
												dueMonth = recommendedTime.getTime().getMonth() + 1 + 1;
												deltaYear = dueMonth / 12;
												dueMonth = dueMonth % 12;											
												dueYear = currentYear + deltaYear;
												strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
											}
										} else {
											if (dateInMonth >= recommendedTime.getTime().getDate()) {
												dueYear = recommendedTime.getTime().getYear() + 1900;
												dueMonth = recommendedTime.getTime().getMonth() + 1;
												strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
											} else {
												dueMonth = recommendedTime.getTime().getMonth() + 1 + 1;
												deltaYear = dueMonth / 12;
												dueMonth = dueMonth % 12;											
												dueYear = currentYear + deltaYear;
												strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
											}
										}
									} else {
										if (dateInMonth >= recommendedTime.getTime().getDate()) {
											dueYear = recommendedTime.getTime().getYear() + 1900;
											dueMonth = recommendedTime.getTime().getMonth() + 1;
											strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
										} else {
											dueMonth = recommendedTime.getTime().getMonth() + 1 + 1;
											deltaYear = dueMonth / 12;
											dueMonth = dueMonth % 12;											
											dueYear = recommendedTime.getTime().getYear() + 1900 + deltaYear;
											strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
										}
									}								
									dueDate = format.parse(strDueDate);
									vaccinationHistory.setDateOfImmunization(dueDate);
								} else {
									if (dateInMonth >= today.getDate()) {
										dueYear = currentYear;
										dueMonth = currentMonth;
										strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
									} else {
										dueMonth = currentMonth + 1;
										deltaYear = dueMonth / 12;
										dueMonth = dueMonth % 12;											
										dueYear = currentYear + deltaYear;
										strDueDate = dueYear + "-" + dueMonth + "-" + dateInMonth;
									}
									dueDate = format.parse(strDueDate);
									vaccinationHistory.setDateOfImmunization(dueDate);
								}
								prevDueDate = dueDate;
							}
						}
						childrenVaccinationHistoryDao.save(vaccinationHistory);
					} catch (ParseException e) {					
						e.printStackTrace();
					}
				}
			}			
		}
	}
}