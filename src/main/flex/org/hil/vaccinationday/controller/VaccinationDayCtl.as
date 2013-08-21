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

package org.hil.vaccinationday.controller
{
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.resources.ResourceManager;	
	import org.hil.core.controller.BaseController;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.Commune;
	import org.hil.core.model.VaccinationDay;
	import org.granite.tide.events.TideFaultEvent;
	import org.granite.tide.events.TideResultEvent;
	import org.granite.tide.spring.PagedQuery;
	
	[Name("VaccinationDayCtl")]	
	[ResourceBundle("VaccinationDay")]
	[Bindable]
	public class VaccinationDayCtl extends BaseController
	{
		[In]
		public var regionManager:Object;
		[In]
		public var vaccinationDayManager:Object;
		[Out]
		public var districts1:ArrayCollection = new ArrayCollection();		
		[Out]
		public var vaccinationDates:ArrayCollection = new ArrayCollection();
		[In]
		public var vacinDistrict:District;
		[In]
		public var vaccinationDay:VaccinationDay;		
	
		[Observer("vaccinationday_getAllDistricts")]
		public function getAllDistricts(selectedProvince:Province):void {
			regionManager.getAllDistricts(selectedProvince, getAllDistrictsResult, errorHandler);			
		}
		
		public function getAllDistrictsResult(e:TideResultEvent):void {
			districts1 = ArrayCollection(e.result);					
			tideContext.raiseEvent("vaccinationday_afterGetAllDistricts");
		}		
		
		[Observer("vaccinationday_getAllVaccinationDateByDistrict")]
		public function getAllVaccinationDateInDistrict():void {
			vaccinationDayManager.getAllVaccinationDateInDistrict(vacinDistrict, getAllVaccinationDateInDistrictResult, errorHandler);			
		}
		
		public function getAllVaccinationDateInDistrictResult(e:TideResultEvent):void {
			vaccinationDates = ArrayCollection(e.result);					
		}
		
		[Observer("vaccinationday_saveVaccinationDate")]
		public function saveVaccinationDate():void {			
			vaccinationDayManager.saveVaccinationDate(vaccinationDay, saveVaccinationDateResult, errorHandler);
		}		
		public function saveVaccinationDateResult(e:TideResultEvent):void {
			getAllVaccinationDateInDistrict();
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}
		
		[Observer("vaccinationday_updateAllVaccinationDate")]
		public function updateAllVaccinationDate(dateInt:Number):void {			
			vaccinationDayManager.updateAllVaccinationDate(vacinDistrict, dateInt, updateAllVaccinationDateResult, errorHandler);
		}		
		public function updateAllVaccinationDateResult(e:TideResultEvent):void {
			vaccinationDates = ArrayCollection(e.result);
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));;
		}
	}
}