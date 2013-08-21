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

package org.hil.listmanagement.region.controller
{
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.resources.ResourceManager;
	
	import org.hil.core.controller.BaseController;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.Village;
	import org.granite.tide.events.TideFaultEvent;
	import org.granite.tide.events.TideResultEvent;
	import org.granite.tide.spring.PagedQuery;
	
	[Name("ListOfRegionsCtl")]
	[ResourceBundle("Notification")]
	[ResourceBundle("ListOfRegions")]
	[Bindable]
	public class ListOfRegionsCtl extends BaseController
	{
		[In]
		public var regionManager:Object;
		[In]
		public var province:Province;
		[Out]
		public var districts:ArrayCollection = new ArrayCollection();		
		[In]
		public var district:District;
		[Out]
		public var communes:ArrayCollection = new ArrayCollection();
		[In]
		public var commune:Commune;
		[Out]
		public var villages:ArrayCollection = new ArrayCollection();
		[In]
		public var village:Village;
		
		//save province
		[Observer("listmanagement_region_saveProvince")]
		public function saveProvince(province:Province):void {			
			regionManager.saveProvince(province, saveProvinceResult, errorHandler);
		}		
		public function saveProvinceResult(e:TideResultEvent):void {
			tideContext.raiseEvent("commondata_getAllProvinces");	
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}
		public function saveProvinceFault(e:TideFaultEvent):void {
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_unsuccessful'));
		}
		
		//delete province
		[Observer("listmanagement_region_deleteProvince")]
		public function deleteProvince():void {
			regionManager.deleteProvince(province, deleteProvinceResult, errorHandler);
		}		
		public function deleteProvinceResult(e:TideResultEvent):void {
			tideContext.raiseEvent("commondata_getAllProvinces");
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}		
		public function deleteProvinceFault(e :TideFaultEvent):void {
			Alert.show(ResourceManager.getInstance().getString('Notification','data_is_currently_in_use'));
		}		
	
		[Observer("listmanagement_region_getAllDistricts")]
		public function getAllDistricts(selectedProvince:Province):void {
			regionManager.getAllDistricts(selectedProvince, getAllDistrictsResult, errorHandler);			
		}
		
		public function getAllDistrictsResult(e:TideResultEvent):void {
			districts = ArrayCollection(e.result);					
		}
		
		[Observer("listmanagement_region_saveDistrict")]
		public function saveDistrict():void {
			regionManager.saveDistrict(district, saveDistrictResult, errorHandler);
		}
		
		public function saveDistrictResult(e:TideResultEvent):void {
			getAllDistricts(district.province);
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}
		public function saveDistrictFault(e:TideFaultEvent):void {
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_unsuccessful'));
		}
		
		[Observer("listmanagement_region_deleteDistrict")]
		public function deleteDistrict():void {			
			regionManager.deleteDistrict(district, deleteDistrictResult, errorHandler);
		}
		
		public function deleteDistrictResult(e:TideResultEvent):void {
			getAllDistricts(district.province);
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}
		
		public function deleteDistrictFault(e:TideFaultEvent):void {
			Alert.show(ResourceManager.getInstance().getString('Notification','data_is_currently_in_use'));
		}
		
		[Observer("listmanagement_region_getAllCommunes")]
		public function getAllCommunes(selectedDistrict:District):void {
			regionManager.getAllCommunes(selectedDistrict, getAllCommunesResult, errorHandler);			
		}
		
		public function getAllCommunesResult(e:TideResultEvent):void {
			communes = ArrayCollection(e.result);					
		}		
				
		[Observer("listmanagement_region_saveCommune")]
		public function saveCommune():void {
			regionManager.saveCommune(commune, saveCommuneResult, errorHandler);
		}
		
		public function saveCommuneResult(e:TideResultEvent):void {
			getAllCommunes(commune.district);
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}
		
		[Observer("listmanagement_region_deleteCommune")]
		public function deleteCommune():void {			
			regionManager.deleteCommune(commune, deleteCommuneResult, errorHandler);
		}
		
		public function deleteCommuneResult(e :TideResultEvent):void {
			var strResult:String = String(e.result);
			if (strResult.length == 0) {
				getAllCommunes(district);
				Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
			} else if (strResult == "data_is_currently_in_use") {
				Alert.show(ResourceManager.getInstance().getString('Notification','data_is_currently_in_use'));
			}
		}
		
		[Observer("listmanagement_region_getAllVillages")]
		public function getAllVillages(selectedCommune:Commune):void {
			regionManager.getAllVillages(selectedCommune, getAllVillagesResult, errorHandler);			
		}
		
		public function getAllVillagesResult(e:TideResultEvent):void {
			villages = ArrayCollection(e.result);					
		}
		
		[Observer("listmanagement_region_saveVillage")]
		public function saveVillage():void {
			regionManager.saveVillage(village, saveVillageResult, errorHandler);
		}
		
		public function saveVillageResult(e:TideResultEvent):void {
			getAllVillages(village.commune);
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}
		
		[Observer("listmanagement_region_deleteVillage")]
		public function deleteVillage():void {
			regionManager.deleteVillage(village, deleteVillageResult, errorHandler);
		}
		
		public function deleteVillageResult(e :TideResultEvent):void {
			getAllVillages(village.commune);
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
		}

	}
}