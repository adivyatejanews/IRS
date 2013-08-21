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

package org.hil.listmanagement.vaccination.controller
{
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.resources.ResourceManager;
	import org.hil.core.model.Vaccination;
	import org.hil.core.controller.BaseController;
	import org.granite.tide.events.TideFaultEvent;
	import org.granite.tide.events.TideResultEvent;
	
	[Name("ListOfVaccinationsCtl")]	
	[ResourceBundle("ListOfVaccinations")]
	[ResourceBundle("Notification")]
	[Bindable]
	public class ListOfVaccinationsCtl extends BaseController
	{
		[In]
		public var vaccinationManager:Object;		
		[In][Out]
		public var vaccination:Vaccination = new Vaccination();		
		[Out]
		public var vaccinations:ArrayCollection = new ArrayCollection();
		
		//get all
		[Observer("listmanagement_listofvaccinations_getAllVaccinations")]
		public function getAllVaccinations():void {
			vaccinationManager.getAllVaccinations(getAllVaccinationsResult, errorHandler);
		}	
		public function getAllVaccinationsResult(e:TideResultEvent):void {
			vaccinations = ArrayCollection(e.result);
		}
		
		//save
		[Observer("listmanagement_listofvaccinations_saveVaccination")]
		public function saveVaccination():void {
			vaccinationManager.saveVaccination(vaccination, saveVaccinationResult, errorHandler);			
		}		
		private function saveVaccinationResult(e:TideResultEvent):void {
			getAllVaccinations();				
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}		
		
		//search
		[Observer("listmanagement_listofvaccinations_searchVaccination")]
		public function searchVaccination(searchStr:String):void {
			vaccinationManager.searchVaccination(searchStr,searchVaccinationResult, errorHandler);		
		}		
		public function searchVaccinationResult(e:TideResultEvent):void {
			vaccinations = ArrayCollection(e.result);
		}		
		
		//delete
		[Observer("listmanagement_listofvaccinations_deleteVaccination")]
		public function deleteVaccination(deleteVaccination:Vaccination):void {						
			vaccinationManager.deleteVaccination(deleteVaccination, deleteVaccinationResult, deleteVaccinationFault);
		}
		
		public function deleteVaccinationResult(e:TideResultEvent):void {
			getAllVaccinations();
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}
		
		public function deleteVaccinationFault(e:TideFaultEvent):void {
			
		}
		
	}
}