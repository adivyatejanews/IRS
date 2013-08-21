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
import java.util.Date;
import java.util.List;
import org.hil.core.dao.VaccinationDayDao;
import org.hil.core.model.District;
import org.hil.core.model.VaccinationDay;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

@Repository("vaccinationDayDaoExt")
public class VaccinationDayDaoHibernate extends GenericDaoHibernateSupportExt<VaccinationDay> implements
															VaccinationDayDao{
	public List<VaccinationDay> getAllVaccinationDateInDistrict(District district) {
		List<VaccinationDay> listVP = new ArrayList<VaccinationDay>();

		String sql = "Select vd from VaccinationDay vd where vd.commune.district =:district";
		
		Query qry = getSession().createQuery(sql);
		
		qry.setParameter("district", district);
		
		listVP = qry.list();		

		return listVP;
	}
	
	public List<VaccinationDay> updateAllVaccinationDate(District district, Integer dateInt) {
		List<VaccinationDay> listVP = new ArrayList<VaccinationDay>();

		String sql = "Update VaccinationDay set dateInMonth=:dateInMonth where commune in (Select commune from Commune commune where commune.district =:district)";
		
		Query qry = getSession().createQuery(sql);
		qry.setParameter("dateInMonth", dateInt);
		qry.setParameter("district", district);
		
		qry.executeUpdate();		

		return getAllVaccinationDateInDistrict(district);
	}
	
	public int removeByCommuneId(Long communeId) {
		String sql = "Delete from vaccination_day where id_commune=:communeId";
		SQLQuery qry = getSession().createSQLQuery(sql);		
		qry.setParameter("communeId", communeId);		
		log.debug("SQL: " + sql);		
		return qry.executeUpdate();
	}
	
	public List<VaccinationDay> getDaySMS(Integer beforedays) {
		List<VaccinationDay> listVP = new ArrayList<VaccinationDay>();
		Integer month = (new Date()).getMonth() + 1;
		Integer year = (new Date()).getYear() + 1900;
		String sql = "Select * from vaccination_day vd WHERE DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL " + beforedays + " DAY),'%e') = date_in_month";
		
		SQLQuery qry = getSession().createSQLQuery(sql);		
		qry.addEntity(VaccinationDay.class);
		listVP = qry.list();		

		return listVP;
	}

}
