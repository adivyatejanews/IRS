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
import org.hil.core.dao.ChildrenUnder1Dao;
import org.hil.core.dao.CommuneDao;
import org.hil.core.dao.GenericChildrenUnder1Dao;
import org.hil.core.model.ChildrenUnder1;
import org.hil.core.model.Commune;
import org.hil.core.model.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("childrenUnder1DaoExt")
public class ChildrenUnder1DaoHibernate extends GenericDaoHibernateSupportExt<ChildrenUnder1> implements ChildrenUnder1Dao {
	
	@Autowired
	@Qualifier("communeDaoExt")
	private CommuneDao communeDaoExt;
	
	@Autowired
	private GenericChildrenUnder1Dao childrenUnder1Dao;
	
	public List<ChildrenUnder1> getAllChildrenUnder1InCommunesByDistrict(String time, Long communeId, District district) {
		List<ChildrenUnder1> listCommunes = new ArrayList<ChildrenUnder1>();
		
		String sql = ""; 
		if (communeId != null)
			sql = "Select cu from ChildrenUnder1 cu where cu.commune.id =:communeId and  DATE_FORMAT(cu.time, '%m/%Y')=:timeData";
		else
			sql = "Select cu from ChildrenUnder1 cu where cu.commune.district =:district and  DATE_FORMAT(cu.time, '%m/%Y')=:timeData";
		log.debug(sql);
		
		Query qry = getSession().createQuery(sql);
		if (communeId != null)
			qry.setParameter("communeId", communeId);
		else
			qry.setParameter("district", district);
		qry.setParameter("timeData", time);
		
		listCommunes = qry.list();		

		return listCommunes;
	}
	
	public List<ChildrenUnder1> createChildrenUnder1InCommunesByDistrict(String time, Long communeId, District district) {
		List<Commune> communes = communeDaoExt.findByDistrictId(district.getId());
		List<ChildrenUnder1> listCommunes = new ArrayList<ChildrenUnder1>();
		for (Commune c : communes) {
			ChildrenUnder1 cu = new ChildrenUnder1();
			cu.setCommune(c);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date rdate = format.parse("01/" + time);
				cu.setTime(rdate);
			} catch (ParseException e) {		
				e.printStackTrace();
			}
			cu = childrenUnder1Dao.save(cu);
			if (communeId != null) {
				if (cu.getCommune().getId().compareTo(communeId) == 0)
					listCommunes.add(cu);
			} else
				listCommunes.add(cu);
		}
		return listCommunes;
	}
}