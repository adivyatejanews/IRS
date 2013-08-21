/*
* Children Immunization Registry System (IRS). Copyright (C) 2011 PATH (www.path.org)
* 
* IRS is based on HIL (http://code.google.com/p/hil)
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
* Email: hieutt24@gmail.com
*/

package org.hil.core.controller
{
	import org.hil.core.model.vo.CommonDataVO;
	import org.hil.core.model.Province;
	import org.hil.core.model.SystemRole;
	import mx.collections.ArrayCollection;
	import org.granite.tide.Tide;
	import org.granite.tide.events.TideResultEvent;
	import mx.controls.Alert;
	import mx.resources.ResourceManager;
	
	[Name("CommonDataCtl")]	
	[Bindable]
	public class CommonDataCtl extends BaseController {
		[In]
		public var systemUserManager:Object;
		[In]
		public var regionManager:Object;
		[Out]
		public var commonDataVO:CommonDataVO=new CommonDataVO();
		[Out]
		public var provinces:ArrayCollection = new ArrayCollection();

		[Observer("commondata_getAllSystemRoles")]
		public function getAllSystemRoles():void {
			if (commonDataVO.allRoles.length == 0) {
				systemUserManager.getAllSystemRoles(getAllSystemRolesResult,errorHandler);
			}			
		}		
		private function getAllSystemRolesResult(e:TideResultEvent):void {
			commonDataVO.allRoles=ArrayCollection(e.result);			
		}
		
		[Observer("commondata_getAllProvinces")]
		public function getAllProvinces():void {
			regionManager.getAllProvincesOrderByName("asc", getAllProvincesResult, errorHandler);			
		}		
		public function getAllProvincesResult(e:TideResultEvent):void {			
			provinces = ArrayCollection(e.result);			
		}
		
	}
	
}