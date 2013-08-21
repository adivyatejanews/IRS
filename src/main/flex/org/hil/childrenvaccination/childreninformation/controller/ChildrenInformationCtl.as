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

package org.hil.childrenvaccination.childreninformation.controller
{
	import flash.net.URLRequest;
	import flash.net.navigateToURL;	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.ResourceManager;	
	import org.granite.tide.events.TideResultEvent;
	import org.granite.tide.spring.PagedQuery;
	import org.hil.core.controller.BaseController;
	import org.hil.core.model.Children;
	import org.hil.core.model.ChildrenVaccinationHistory;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.Village;
	import org.hil.core.model.vo.search.ChildrenSearchVO;
	
	[ResourceBundle("ChildrenInformation")]
	[Name("ChildrenInformationCtl")]
	[Bindable]
	public class ChildrenInformationCtl extends BaseController
	{
		[In]
		public var childrenManager:Object;
		[In]
		public var regionManager:Object;
		[In][Out]
		[ArrayElementType("org.hil.core.model.Children")]
		public var listChildren:ArrayCollection=new ArrayCollection();
		[In][Out]
		public var listVaccinationHistory:ArrayCollection=new ArrayCollection();
		[Out]	
		public var districts2:ArrayCollection = new ArrayCollection();
		[Out]	
		public var communes2:ArrayCollection = new ArrayCollection();
		[Out]	
		public var villages2:ArrayCollection = new ArrayCollection();
		[Out]	
		public var districts7:ArrayCollection = new ArrayCollection();
		[Out]	
		public var communes7:ArrayCollection = new ArrayCollection();
		[Out]	
		public var villages7:ArrayCollection = new ArrayCollection();
		[Out]
		public var districts3:ArrayCollection=new ArrayCollection();
		[Out]
		public var communes3:ArrayCollection=new ArrayCollection();
		[In][Out]
		public var child:Children=new Children();
		[In]
		public var paramChildrenSearch:ChildrenSearchVO = new ChildrenSearchVO();
		
		[Observer("childrenvaccination_childreninformation_searchChildren")]
		public function searchChildren():void {
			childrenManager.searchChildren(paramChildrenSearch, searchChildrenResult, errorHandler);
		}
		
		private function searchChildrenResult(e:TideResultEvent):void {
			listChildren=ArrayCollection(e.result);
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterSearchChildren");
		}	
		
		[Observer("childrenvaccination_childreninformation_getAllDistricts")]
		public function getAllDistricts(selectedProvince:Province):void {
			regionManager.getAllDistricts(selectedProvince, getAllDistrictsResult, errorHandler);			
		}		
		public function getAllDistrictsResult(e:TideResultEvent):void {
			districts2 = ArrayCollection(e.result);
			if (districts2 != null && districts2.length > 0) {
				var nullDistrict:District = new District();
				nullDistrict.districtName = "";
				nullDistrict.id = 0;
				districts2.addItemAt(nullDistrict, 0);
			}
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterGetAllDistricts");
		}
		
		[Observer("childrenvaccination_childreninformation_getAllDistricts7")]
		public function getAllDistricts7(selectedProvince:Province):void {
			regionManager.getAllDistricts(selectedProvince, getAllDistricts7Result, errorHandler);			
		}		
		public function getAllDistricts7Result(e:TideResultEvent):void {
			districts7 = ArrayCollection(e.result);			
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterGetAllDistricts7");
		}
		
		[Observer("childrenvaccination_childreninformation_getAllDistricts3")]
		public function getAllDistricts3(selectedProvince:Province):void {
			regionManager.getAllDistricts(selectedProvince, getAllDistricts3Result, errorHandler);			
		}		
		public function getAllDistricts3Result(e:TideResultEvent):void {
			districts3 = ArrayCollection(e.result);					
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterGetAllDistricts3");
		}
		
		[Observer("childrenvaccination_childreninformation_getAllCommunes")]
		public function getAllCommunes(selectedDistrict:District):void {
			regionManager.getAllCommunes(selectedDistrict, getAllCommunesResult, errorHandler);			
		}		
		public function getAllCommunesResult(e:TideResultEvent):void {
			communes2 = ArrayCollection(e.result);
			if (communes2 != null && communes2.length > 0) {
				var nullCommune:Commune = new Commune();
				nullCommune.communeName = "";
				nullCommune.id = 0;
				communes2.addItemAt(nullCommune, 0);
			}
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterGetAllCommunes");
		}
		
		[Observer("childrenvaccination_childreninformation_getAllCommunes7")]
		public function getAllCommunes7(selectedDistrict:District):void {
			regionManager.getAllCommunes(selectedDistrict, getAllCommunes7Result, errorHandler);			
		}		
		public function getAllCommunes7Result(e:TideResultEvent):void {
			communes7 = ArrayCollection(e.result);			
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterGetAllCommunes7");
		}
		
		[Observer("childrenvaccination_childreninformation_getAllCommunes3")]
		public function getAllCommunes3(selectedDistrict:District):void {
			regionManager.getAllCommunes(selectedDistrict, getAllCommunes3Result, errorHandler);			
		}		
		public function getAllCommunes3Result(e:TideResultEvent):void {
			communes3 = ArrayCollection(e.result);					
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterGetAllCommunes3");
		}
		
		[Observer("childrenvaccination_childreninformation_getAllVillages")]
		public function getAllVillages(selectedCommune:Commune):void {
			regionManager.getAllVillages(selectedCommune, getAllVillagesResult, errorHandler);			
		}		
		public function getAllVillagesResult(e:TideResultEvent):void {
			villages2 = ArrayCollection(e.result);					
			if (villages2 != null && villages2.length > 0) {
				var nullVillage:Village = new Village();
				nullVillage.villageName = "";
				nullVillage.id = 0;
				villages2.addItemAt(nullVillage, 0);
			}
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterGetAllVillages");
		}
		
		[Observer("childrenvaccination_childreninformation_getAllVillages7")]
		public function getAllVillages7(selectedCommune:Commune):void {
			regionManager.getAllVillages(selectedCommune, getAllVillages7Result, errorHandler);			
		}		
		public function getAllVillages7Result(e:TideResultEvent):void {
			villages7 = ArrayCollection(e.result);					
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterGetAllVillages7");
		}
		
		[Observer("childrenvaccination_childreninformation_saveChild")]
		public function saveChild(force:Boolean):void {			
			childrenManager.saveChild(child,force,refreshAfterSaveChildResult, errorHandler);
		}		
		public function refreshAfterSaveChildResult(e:TideResultEvent):void {
			var tmpchild:Children=Children(e.result);
			if (tmpchild == null) {
				tideContext.raiseEvent("childrenvaccination_childreninformation_confirmSaveChild");				
			} else {
				child = Children(e.result);
				searchChildren();
				getVaccinationHistory();
				tideContext.raiseEvent("childrenvaccination_childreninformation_afterSaveChild");				
			}
		}
		
		
		[Observer("childrenvaccination_childreninformation_deleteChild")]
		public function deleteChild():void {			
			childrenManager.deleteChild(child,refreshAfterDeleteChildResult, errorHandler);
		}		
		public function refreshAfterDeleteChildResult(e:TideResultEvent):void {						
			searchChildren();
		}
		
		[Observer("childrenvaccination_childreninformation_getVaccinationHistory")]
		public function getVaccinationHistory():void {
			childrenManager.getVaccinationHistory(child,getVaccinationHistoryResult, errorHandler);
		}
		public function getVaccinationHistoryResult(e:TideResultEvent):void {
			listVaccinationHistory = ArrayCollection(e.result);
		}
		
		[Observer("childrenvaccination_childreninformation_printListChildImmunization")]
		public function printListChildImmunization():void {
			childrenManager.printListChildImmunization(printListChildImmunizationResult);	
		}
		public function printListChildImmunizationResult(e:TideResultEvent):void {			
			var url:String=String(e.result);
			if (url != null && url.length > 0) {
				var u:URLRequest=new URLRequest(url);
				navigateToURL(u, "_blank");
			}
			if (url == null || url.length == 0)
				Alert.show("Error!");
		}
		
		[Observer("childrenvaccination_childreninformation_saveVaccinationHistory")]
		public function saveVaccinationHistory(vaccinationEvent:ChildrenVaccinationHistory):void {			
			childrenManager.saveVaccinationHistory(vaccinationEvent, saveVaccinationHistoryResult, errorHandler);
		}
		public function saveVaccinationHistoryResult(e:TideResultEvent):void {
			getVaccinationHistory();
			tideContext.raiseEvent("childrenvaccination_childreninformation_afterSaveVaccinationHistory");
		}
		
		[Observer("childrenvaccination_childreninformation_printListChildren")]
		public function printListChildren():void {
			childrenManager.printListChildren(paramChildrenSearch, printListChildrenResult, errorHandler);
		}
		public function printListChildrenResult(e:TideResultEvent):void {			
			var url:String=String(e.result);
			if (url != null && url.length > 0) {
				var u:URLRequest=new URLRequest(url);
				navigateToURL(u, "_blank");
			}
			if (url == null || url.length == 0)
				Alert.show("Error!");
		}
		
		[Observer("childrenvaccination_childreninformation_importExcel")]
		public function importExcel():void {
			childrenManager.importExcel(importExcelResult, errorHandler);
		}
		public function importExcelResult(e:TideResultEvent):void {			
			Alert.show("Import successful!");
		}
	}
}