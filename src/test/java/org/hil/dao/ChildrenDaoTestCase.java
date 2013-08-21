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

package org.hil.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hil.children.service.SMSManager;
import org.hil.core.dao.ChildrenDao;
import org.hil.core.dao.GenericVillageDao;
import org.hil.core.model.Children;
import org.hil.core.model.Village;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ChildrenDaoTestCase extends BaseDaoTestCase {
	@Autowired
	@Qualifier("childrenDaoExt")
	private ChildrenDao childrenDaoExt;
	
	@Autowired
	@Qualifier("smsManager")
	private SMSManager smsManager;
	
	@Autowired
	private GenericVillageDao villageDao;
	
	public void testCheckChild() {
		assertNotNull("Cannot inject dao", childrenDaoExt);
	}
	
	public void testGenearteCode() {
		Children child = new Children();
		Village village = villageDao.get(new Long(26));
		child.setVillage(village);
		child.setId(new Long(52));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date bdate = format.parse("2010-11-10");
			child.setDateOfBirth(bdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		childrenDaoExt.generateChildCode(child);
	}

}