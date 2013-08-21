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
import org.hil.core.model.ChildrenUnder1;
import org.hil.core.model.District;

public interface ChildrenUnder1Dao {
	
	public List<ChildrenUnder1> getAllChildrenUnder1InCommunesByDistrict(String time, Long communeId, District district);
	public List<ChildrenUnder1> createChildrenUnder1InCommunesByDistrict(String time, Long communeId, District district);
	
}