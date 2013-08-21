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

package org.hil.core.dao.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hil.core.dao.ChildrenVaccinationHistoryDao;
import org.hil.core.dao.GenericChildrenDao;
import org.hil.core.dao.GenericChildrenVaccinationHistoryDao;
import org.hil.core.model.Children;
import org.hil.core.model.ChildrenVaccinationHistory;
import org.hil.core.model.Commune;
import org.hil.core.model.Vaccination;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("childrenVaccinationHistoryDaoExt")
public class ChildrenVaccinationHistoryDaoHibernate extends GenericDaoHibernateSupportExt<ChildrenVaccinationHistory> implements
ChildrenVaccinationHistoryDao {
	
	@Autowired
	private GenericChildrenVaccinationHistoryDao childrenVaccinationHistoryDao;
	
	@Autowired
	private GenericChildrenDao childrenDao;
	
	public List<Children> getListChildrenDueByDueTimeCommune(String dueTime, Commune commune) {
		List<Children> listChildren = new ArrayList<Children>();	
		
		String sql = "Select c.* from children c, children_vaccination_history cvh, village vl, vaccination v"
				+ " where cvh.id_children=c.id and c.id_village =vl.id and vl.id_commune=:communeId and c.locked!=true"
				+ " and cvh.id_vaccination=v.id and cvh.vaccinated=0 and "
				+ " ("
				+ " (v.id_dependent_vaccination is null)"
				+ " or" 
				+ " (v.id_dependent_vaccination is not null"
				+ " and  (Select id from children_vaccination_history cvh2 where cvh2.id_vaccination=v.id_dependent_vaccination and cvh2.id_children=c.id "
				+ 			"and cvh2.vaccinated=1 and DATE(DATE_ADD(cvh2.date_of_immunization, INTERVAL v.gap DAY)) <= :dueTime) is not null"
				+ " )"
				+ " ) "
				+ " and (DATE(DATE_ADD(c.date_of_birth, INTERVAL v.age*30*v.age_unit DAY)) <= :dueTime)"
				+ " and (v.limit_days is null or (v.limit_days > 0 and DATE(DATE_ADD(c.date_of_birth, INTERVAL (v.limit_days + 1) DAY)) >= :dueTime))"
				+ " group by c.id order by c.date_of_birth asc";
		
		SQLQuery qry = getSession().createSQLQuery(sql);
		qry.addEntity(Children.class);
		qry.setParameter("dueTime", dueTime);
		qry.setParameter("communeId", commune.getId());
		
		//log.debug("SQL: " + sql);

		listChildren = qry.list();
		
		return listChildren;
	}
	
	public List<Vaccination> getListVaccinationByChild(String dueTime, Children child, Boolean overdue) {
		List<Vaccination> result = new ArrayList<Vaccination>();
		
		String sql = "Select v.* from children c, children_vaccination_history cvh, vaccination v"
				+ " where cvh.id_children=:childId and c.id=:childId"
				+ " and cvh.id_vaccination=v.id and cvh.vaccinated=0 and "
				+ " ("
				+ "  (v.id_dependent_vaccination is null)"
				+ "  or" 
				+ "  (v.id_dependent_vaccination is not null"
				+ "   and  (Select id from children_vaccination_history cvh2 where cvh2.id_vaccination=v.id_dependent_vaccination and cvh2.id_children=c.id "
				+ 			"and cvh2.vaccinated=1 and DATE(DATE_ADD(cvh2.date_of_immunization, INTERVAL v.gap DAY)) <= :dueTime) is not null"
				+ "  )"
				+ " ) "
				+ " and (DATE(DATE_ADD(c.date_of_birth, INTERVAL v.age*30*v.age_unit DAY)) <= :dueTime)";
		String ymtime = "";		
		if (overdue != null) {
			ymtime = dueTime.substring(0, 7);
			if (overdue.booleanValue() == true) {
				sql += " and  date_format(DATE_ADD(c.date_of_birth, INTERVAL v.age*30*v.age_unit DAY),'%Y-%m')!=:ymtime ";
			} else if (overdue.booleanValue() == false)
				sql += " and  date_format(DATE_ADD(c.date_of_birth, INTERVAL v.age*30*v.age_unit DAY),'%Y-%m')=:ymtime ";
		}
		
		
		sql	+= " and (v.limit_days is null or (v.limit_days > 0 and DATE(DATE_ADD(c.date_of_birth, INTERVAL (v.limit_days + 1) DAY)) >= :dueTime))"
				+ " group by v.id";		
		
		//log.debug(dueTime + " | " + ymtime + " | " + sql);
		SQLQuery qry = getSession().createSQLQuery(sql);
		qry.addEntity(Vaccination.class);
		qry.setParameter("dueTime", dueTime);
		if (overdue != null)
			qry.setParameter("ymtime", ymtime);
		qry.setParameter("childId", child.getId());
		result = qry.list();
		return result;
	}
	
	public List<ChildrenVaccinationHistory> findByChildAndVaccinatedAndOderbyVaccinationId(Children child, Short vaccinated, boolean asc) {
		List<ChildrenVaccinationHistory> result = new ArrayList<ChildrenVaccinationHistory>();
		String sql = "Select cvh from ChildrenVaccinationHistory cvh where cvh.vaccinated =:vaccinated and cvh.child=:child order by cvh.vaccination.id asc";				
		Query qry = getSession().createQuery(sql);
		qry.setParameter("vaccinated", vaccinated);
		qry.setParameter("child", child);
		result = qry.list();
		return result;
	}
	
	public List<ChildrenVaccinationHistory> findByChildAndVaccineAndVaccinatedAndOderbyVaccinationId(Long childId, Short vaccinated, List<Vaccination> vaccines, boolean asc) {
		List<ChildrenVaccinationHistory> result = new ArrayList<ChildrenVaccinationHistory>();
		String sql = "Select cvh from ChildrenVaccinationHistory cvh where (cvh.vaccinated =:vaccinated OR cvh.vaccinated=1) AND cvh.vaccination IN (:listVaccines) and cvh.child.id=:childId order by cvh.vaccination.id asc";				
		Query qry = getSession().createQuery(sql);
		qry.setParameter("vaccinated", vaccinated);
		qry.setParameter("childId", childId);
		qry.setParameterList("listVaccines", vaccines);
		result = qry.list();
		return result;
	}

	public List<ChildrenVaccinationHistory> findByChildOrderbyDueDate(Long childId) {
		List<ChildrenVaccinationHistory> result = new ArrayList<ChildrenVaccinationHistory>();
		String sql = "Select cvh from ChildrenVaccinationHistory cvh where cvh.child.id=:childId order by cvh.vaccination.ageUnit asc,cvh.vaccination.age asc, cvh.id asc";				
		Query qry = getSession().createQuery(sql);
		qry.setParameter("childId", childId);
		result = qry.list();
		return result;
	}
	
	public int removeByChildId(Long childId) {
		String sql = "Delete from children_vaccination_history where id_children=:childId";
		SQLQuery qry = getSession().createSQLQuery(sql);		
		qry.setParameter("childId", childId);		
		log.debug("SQL: " + sql);		
		return qry.executeUpdate();
	}
	
	public int removeByChildIdAndVaccinationIdAndVaccinationStatus(Long childId,Long vaccinId, Integer vaccinStatus) {
		String sql = "Delete from children_vaccination_history where id_children=:childId and id_vaccination=:vaccinId and vaccinated=:vaccinStatus";
		SQLQuery qry = getSession().createSQLQuery(sql);		
		qry.setParameter("childId", childId);
		qry.setParameter("vaccinId", vaccinId);
		qry.setParameter("vaccinStatus", vaccinStatus);
		log.debug("SQL: " + sql);		
		return qry.executeUpdate();
	}
	
	public ChildrenVaccinationHistory saveVaccinationHistory(ChildrenVaccinationHistory vaccinationEvent) {
		log.debug("Check before save vaccination event: "  + vaccinationEvent.getChild().getId() + " | " + vaccinationEvent.getId() + " | " + vaccinationEvent.getVaccinated());
		List<Number> temp1 = this.getChildAndVaccinationAndVaccinated(vaccinationEvent.getChild(), vaccinationEvent.getVaccination(), (short)1);
		
		if (vaccinationEvent.getVaccinated().shortValue() == 0) {
			if (temp1 != null && temp1.size() > 0)
				this.setNotVaccinated(temp1);
			return vaccinationEvent;
		}		
		
		if (vaccinationEvent.getVaccinated() == 1) {
			if (temp1 != null && temp1.size() > 0) {
				if (temp1.get(0).longValue() != vaccinationEvent.getId().longValue())
					return vaccinationEvent;
			}
		}
		
		List<Number> temp0 = this.getChildAndVaccinationAndVaccinated(vaccinationEvent.getChild(), vaccinationEvent.getVaccination(), (short)0);

		if (vaccinationEvent.getVaccinated() == 0) {
			if (temp1 != null && temp1.size() > 0) {
				if (temp1.get(0).longValue() != vaccinationEvent.getId().longValue())
					return vaccinationEvent;
			} else {
				if (temp0 != null && temp0.size() > 0) {
					if (temp0.get(0).longValue() != vaccinationEvent.getId().longValue())
						return vaccinationEvent;
				}
			}			
		}
		
		boolean createNewVaccinationEvent = false;
		if (vaccinationEvent.getVaccinated() == 2) {
			if (temp1 != null && temp1.size() > 0) {
				if (temp1.get(0).longValue() == vaccinationEvent.getId().longValue())
					return vaccinationEvent;
			} else {
				if (temp0 != null && temp0.size() > 0) {
					if (temp0.get(0).longValue() == vaccinationEvent.getId().longValue())
						createNewVaccinationEvent = true;
				}
			}
		}
		log.debug("Saving vaccination event: "  + vaccinationEvent.getChild().getId() + " | " + vaccinationEvent.getId() + " | " + vaccinationEvent.getVaccinated());
		
		if (vaccinationEvent.getVaccination().getId() == 1 && vaccinationEvent.getVaccinated() == 1) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(vaccinationEvent.getChild().getDateOfBirth());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date tmp = calendar.getTime();
			if (vaccinationEvent.getDateOfImmunization().getTime() <= tmp.getTime())
				vaccinationEvent.setOverdue(false);
			else
				vaccinationEvent.setOverdue(true);
		}
		vaccinationEvent.setModifiedTime(new Date());
		vaccinationEvent = childrenVaccinationHistoryDao.save(vaccinationEvent);		
		getSession().flush();		
		
		if (vaccinationEvent.getVaccinated() == 1) {
			temp0 = getChildAndVaccinationAndVaccinated(vaccinationEvent.getChild(), vaccinationEvent.getVaccination(), (short)0);
			
			for (int c=0; c<temp0.size();c++) {				
				childrenVaccinationHistoryDao.remove(childrenVaccinationHistoryDao.get(temp0.get(c).longValue()));
			}
			
			boolean finish = checkFinish(vaccinationEvent.getChild());
			if (finish) {
				Date finDate = checkFinishDate(vaccinationEvent.getChild());
				vaccinationEvent.getChild().setFinishedDate(finDate);
				childrenDao.save(vaccinationEvent.getChild());
			} else {
				if (vaccinationEvent.getChild().getFinishedDate() != null) {
					vaccinationEvent.getChild().setFinishedDate(null);
					childrenDao.save(vaccinationEvent.getChild());
				}
			}
		} else {
			boolean finish = checkFinish(vaccinationEvent.getChild());
			if (!finish) {
				vaccinationEvent.getChild().setFinishedDate(null);
				childrenDao.save(vaccinationEvent.getChild());
			}
		}
		
		if (createNewVaccinationEvent) {
			Children c = vaccinationEvent.getChild();
			ChildrenVaccinationHistory newVH = new ChildrenVaccinationHistory();
			newVH.setChild(c);
			newVH.setOverdue(false);
			newVH.setReasonIfMissed("");
			newVH.setVaccinated((short)0);			
			newVH.setVaccination(vaccinationEvent.getVaccination());
			newVH.setModifiedTime(new Date());
			childrenVaccinationHistoryDao.save(newVH);	
		}
		
		return vaccinationEvent;
	}
	
	public int setNotVaccinated(List<Number> shotIds) {
		String strIds = "(";
		for (Number id : shotIds)
			strIds += id.longValue() + "," ;
		strIds = strIds.substring(0, strIds.length()-1);
		strIds += ")";
		String sql = "UPDATE children_vaccination_history SET vaccinated = 0, date_of_immunization = null, id_vaccinated_location = null, other_vaccinated_location = 0, "
					+ " created_time = null, modified_time = null, from_mobile = null WHERE id IN " + strIds;
		log.debug(sql);
		SQLQuery qry = getSession().createSQLQuery(sql);			
		return qry.executeUpdate();
	}
	
	public boolean checkFinish(Children child) {
		String sql = "Select count(*) from children_vaccination_history cvh "
				+ " where cvh.id_children =:childId and cvh.vaccinated=:vaccinated and id_vaccination in ('2','3','4','5','6','7','8','9') ";
		SQLQuery qry = getSession().createSQLQuery(sql);		
		qry.setParameter("vaccinated", 1);
		qry.setParameter("childId", child.getId());
		log.debug("SQL: " + sql);		
		long result = ((Number)qry.uniqueResult()).longValue();
		log.debug(result);
		if (result == 8)
			return true;
		else
			return false;		
	}
	
	public Date checkFinishDate(Children child) {
		String sql = "Select max(cvh.date_of_immunization) from children_vaccination_history cvh "
				+ " where cvh.id_children =:childId and cvh.vaccinated=:vaccinated and id_vaccination in ('2','3','4','5','6','7','8','9') ";
		SQLQuery qry = getSession().createSQLQuery(sql);		
		qry.setParameter("vaccinated", 1);
		qry.setParameter("childId", child.getId());
		log.debug("SQL: " + sql);		
		Date result = ((Date)qry.uniqueResult());
		log.debug(result);
		return result;
	}
	
	public List<Number> getChildAndVaccinationAndVaccinated(Children child, Vaccination vaccination, Short vaccinated) {						
		String sql = "Select cvh.id from children_vaccination_history cvh where vaccinated =:vaccinated and id_children =:childId and id_vaccination =:vaccinationId ";
		SQLQuery qry = getSession().createSQLQuery(sql);		
		qry.setParameter("vaccinated", vaccinated);
		qry.setParameter("childId", child.getId());
		qry.setParameter("vaccinationId", vaccination.getId());		
		return qry.list();
	}
}
