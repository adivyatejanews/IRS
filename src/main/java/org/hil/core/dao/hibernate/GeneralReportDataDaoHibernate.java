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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hil.core.dao.CommuneDao;
import org.hil.core.dao.GenericGeneralReportDataDao;
import org.hil.core.dao.GeneralReportDataDao;
import org.hil.core.model.Commune;
import org.hil.core.model.District;
import org.hil.core.model.GeneralReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("generalReportDataDaoExt")
public class GeneralReportDataDaoHibernate extends GenericDaoHibernateSupportExt<GeneralReportData> implements GeneralReportDataDao {
	
	@Autowired
	@Qualifier("communeDaoExt")
	private CommuneDao communeDaoExt;
	
	@Autowired
	private GenericGeneralReportDataDao generalReportDataDao;
	
	public List<GeneralReportData> getAllGeneralReportDataInCommunesByDistrict(String time, Long communeId, District district) {
		List<GeneralReportData> listCommunes = new ArrayList<GeneralReportData>();

		String sql = "";
		if (communeId != null)
			sql = "Select rv from GeneralReportData rv where rv.commune.id =:communeId and  DATE_FORMAT(rv.time, '%m/%Y')=:timeData";
		else
			sql = "Select rv from GeneralReportData rv where rv.commune.district =:district and  DATE_FORMAT(rv.time, '%m/%Y')=:timeData";
		log.debug(time + ": " + sql);
		
		Query qry = getSession().createQuery(sql);
		
		if (communeId != null)
			qry.setParameter("communeId", communeId);
		else
			qry.setParameter("district", district);
		qry.setParameter("timeData", time);
		
		listCommunes = qry.list();		

		return listCommunes;
	}
	
	public List<GeneralReportData> createGeneralReportDataInCommunesByDistrict(String time, Long communeId, District district) {
		List<Commune> communes = communeDaoExt.findByDistrictId(district.getId());
		List<GeneralReportData> listCommunes = new ArrayList<GeneralReportData>();
		for (Commune c : communes) {
			GeneralReportData rv = new GeneralReportData();
			rv.setCommune(c);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date rdate = format.parse("01/" + time);
				rv.setTime(rdate);
			} catch (ParseException e) {		
				e.printStackTrace();
			}
			rv = generalReportDataDao.save(rv);
			if (communeId != null) {
				if (rv.getCommune().getId().compareTo(communeId) == 0)
					listCommunes.add(rv);
			} else
				listCommunes.add(rv);
		}
		return listCommunes;
	}
	
	public GeneralReportData findGeneralReportDataByCommuneAndTime(String time, Long communeId) {
		List<GeneralReportData> listCommunes = new ArrayList<GeneralReportData>();

		String sql = "Select rv from GeneralReportData rv where rv.commune.id =:communeId and  DATE_FORMAT(rv.time, '%m/%Y')=:timeData";
		
		log.debug(sql);
		
		Query qry = getSession().createQuery(sql);
		
		qry.setParameter("communeId", communeId);
		qry.setParameter("timeData", time);
		
		listCommunes = qry.list();
		
		if (listCommunes != null && listCommunes.size() > 0)
			return listCommunes.get(0);
		else
			return null;
	}
}