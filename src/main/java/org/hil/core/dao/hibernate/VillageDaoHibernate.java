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
import java.util.List;

import org.hibernate.Query;
import org.hil.core.dao.VillageDao;
import org.hil.core.model.Village;
import org.springframework.stereotype.Repository;

@Repository("villageDaoExt")
public class VillageDaoHibernate extends GenericDaoHibernateSupportExt<Village> implements VillageDao {
	public List<Village> findByCommuneId(Long cId) {
		List<Village> result = new ArrayList<Village>();
		String sql = "Select c from Village c where c.commune.id=:cId";
		Query qry = getSession().createQuery(sql);
		qry.setParameter("cId", cId);
		result = qry.list();
		return result;
	}
	
	public List<Village> findByCommuneIdAndVillageName(Long cId, String villageName) {
		List<Village> result = new ArrayList<Village>();
		String sql = "Select c from Village c where c.commune.id=:cId and lower(c.villageName) like :villageName";
		Query qry = getSession().createQuery(sql);
		qry.setParameter("cId", cId);
		qry.setParameter("villageName", villageName);
		result = qry.list();
		return result;
	}

}