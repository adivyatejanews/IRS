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

package org.hil.listmanagement.region.service;

import java.util.List;
import org.hil.core.model.District;
import org.hil.core.model.Village;
import org.hil.core.model.Province;
import org.hil.core.model.Commune;

public interface RegionManager {
	public List<Province> getAllProvincesOrderByName(String asc);	
	public Province saveProvince(Province province);
	public void deleteProvince(Province province);
	
	public List<District> getAllDistricts(Province province);
	public District saveDistrict(District district);
	public void deleteDistrict(District district);	

	public List<Commune> getAllCommunes(District district) ;	
	public Commune saveCommune(Commune commune);
	public String deleteCommune(Commune commune);
	
	public List<Village> getAllVillages(Commune commune);	
	public Village saveVillage(Village village);
	public void deleteVillage(Village village);

}
