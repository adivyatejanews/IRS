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

package org.hil.childrenvaccination.listofchildrendue.controller
{
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	import mx.controls.Alert;
	
	import org.granite.tide.events.TideResultEvent;
	import org.granite.tide.spring.PagedQuery;
	import org.hil.core.controller.BaseController;
	import org.hil.core.model.Children;
	import org.hil.core.model.ChildrenVaccinationHistory;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.Village;
	
	[ResourceBundle("ListOfChildrenDue")]
	[Name("ListOfChildrenDueCtl")]
	[Bindable]
	public class ListOfChildrenDueCtl extends BaseController
	{
		[In]
		public var childrenManager:Object;
		[In]
		public var regionManager:Object;
		[In][Out]
		[ArrayElementType("org.hil.core.model.vo.ChildrenDuePrintVO")]
		public var childrenDue:ArrayCollection=new ArrayCollection();
		[Out]	
		public var districts5:ArrayCollection = new ArrayCollection();
		[Out]	
		public var communes5:ArrayCollection = new ArrayCollection();
		[Out]
		public var districts9:ArrayCollection=new ArrayCollection();
		[Out]
		public var communes9:ArrayCollection=new ArrayCollection();
		[Out]
		[ArrayElementType("org.hil.core.model.ChildrenVaccinationHistory")]
		public var listVaccinationDueHistory:ArrayCollection = new ArrayCollection();
		
		[Observer("childrenvaccination_listofchildrendue_getAllDistricts")]
		public function getAllDistricts(selectedProvince:Province):void {
			regionManager.getAllDistricts(selectedProvince, getAllDistrictsResult, errorHandler);			
		}
		
		public function getAllDistrictsResult(e:TideResultEvent):void {
			districts5 = ArrayCollection(e.result);					
			tideContext.raiseEvent("childrenvaccination_listofchildrendue_afterGetAllDistricts");
		}
		
		[Observer("childrenvaccination_listofchildrendue_getAllCommunes")]
		public function getAllCommunes(selectedDistrict:District):void {
			regionManager.getAllCommunes(selectedDistrict, getAllCommunesResult, errorHandler);			
		}
		
		public function getAllCommunesResult(e:TideResultEvent):void {
			communes5 = ArrayCollection(e.result);					
			tideContext.raiseEvent("childrenvaccination_listofchildrendue_afterGetAllCommunes");
		}
		
		[Observer("childrenvaccination_listofchildrendue_getListOfChildrenDue")]
		public function getListChildrenDue(timeDue:String, commune:Commune):void {
			childrenManager.getListChildrenDue(timeDue, commune, getListChildrenDueResult, errorHandler);	
		}
		public function getListChildrenDueResult(e:TideResultEvent):void {			
			childrenDue = ArrayCollection(e.result);			
			tideContext.raiseEvent("childrenvaccination_listofchildrendue_enableButtonPrint");
		}
		
		[Observer("childrenvaccination_listofchildrendue_printListChildrenDue")]
		public function printListChildrenDue(timeDue:String, commune:Commune):void {
			childrenManager.printListChildrenDue(timeDue, commune, childrenDue, printListChildrenDueResult);	
		}
		public function printListChildrenDueResult(e:TideResultEvent):void {			
			var url:String=String(e.result);
			if (url != null && url.length > 0) {
				var u:URLRequest=new URLRequest(url);
				navigateToURL(u, "_blank");
			}
			if (url == null || url.length == 0)
				Alert.show("Error!");
		}
		
		[Observer("childrenvaccination_listofchildrendue_getVaccinationHistory")]
		public function getVaccinationHistory(childId:Number, vaccinated:Number, vaccines:ListCollectionView, asc:Boolean):void {
			childrenManager.getVaccinationHistoryByVaccines(childId, vaccinated, vaccines, asc, getVaccinationHistoryResult, errorHandler);
		}
		public function getVaccinationHistoryResult(e:TideResultEvent):void {
			listVaccinationDueHistory = ArrayCollection(e.result);			
			tideContext.raiseEvent("after_childrenvaccination_listofchildrendue_getVaccinationHistory");
		}
		
		[Observer("childrenvaccination_listofchildrendue_getAllDistricts3")]
		public function getAllDistricts3(selectedProvince:Province):void {
			regionManager.getAllDistricts(selectedProvince, getAllDistricts3Result, errorHandler);			
		}		
		public function getAllDistricts3Result(e:TideResultEvent):void {
			districts9 = ArrayCollection(e.result);					
			tideContext.raiseEvent("childrenvaccination_listofchildrendue_afterGetAllDistricts3");
		}
		
		[Observer("childrenvaccination_listofchildrendue_getAllCommunes3")]
		public function getAllCommunes3(selectedDistrict:District):void {
			regionManager.getAllCommunes(selectedDistrict, getAllCommunes3Result, errorHandler);			
		}		
		public function getAllCommunes3Result(e:TideResultEvent):void {
			communes9 = ArrayCollection(e.result);					
			tideContext.raiseEvent("childrenvaccination_listofchildrendue_afterGetAllCommunes3");
		}
		
		[Observer("childrenvaccination_listofchildrendue_saveVaccinationHistory")]
		public function saveVaccinationHistory(vaccinationEvent:ChildrenVaccinationHistory,childId:Number, vaccinated:Number, vaccines:ListCollectionView, asc:Boolean):void {			
			childrenManager.saveVaccinationHistory(vaccinationEvent, childId, vaccinated, vaccines, asc, saveVaccinationHistoryResult, errorHandler);
		}
		public function saveVaccinationHistoryResult(e:TideResultEvent):void {
			listVaccinationDueHistory = ArrayCollection(e.result);
			tideContext.raiseEvent("childrenvaccination_listofchildrendue_afterSaveVaccinationHistory");
		}
	}
}