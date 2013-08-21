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

package org.hil.generalreportdata.controller
{
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.resources.ResourceManager;	
	import org.granite.tide.events.TideFaultEvent;
	import org.granite.tide.events.TideResultEvent;
	import org.granite.tide.spring.PagedQuery;
	import org.hil.core.controller.BaseController;
	import org.hil.core.model.ChildrenUnder1;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.GeneralReportData;
	import org.hil.core.model.Province;
	
	[Name("GeneralReportDataCtl")]	
	[ResourceBundle("GeneralReportData")]
	[Bindable]
	public class GeneralReportDataCtl extends BaseController
	{
		[In]
		public var regionManager:Object;
		[In]
		public var generalReportDataManager:Object;
		[Out]
		public var districts1:ArrayCollection = new ArrayCollection();		
		[In][Out]
		public var childrenUnder1Datas:ArrayCollection = new ArrayCollection();
		[In]
		public var dataDistrict:District;
		[In]
		public var childrenUnder1Data:ChildrenUnder1;
		[In][Out]
		public var generalReportDatas:ArrayCollection = new ArrayCollection();
		[In]
		public var generalReportData:GeneralReportData;	
	
		[Observer("generalreportdata_getAllDistricts")]
		public function getAllDistricts(selectedProvince:Province):void {
			regionManager.getAllDistricts(selectedProvince, getAllDistrictsResult, errorHandler);			
		}
		
		public function getAllDistrictsResult(e:TideResultEvent):void {
			districts1 = ArrayCollection(e.result);					
			tideContext.raiseEvent("generalreportdata_afterGetAllDistricts");
		}		
		
		[Observer("generalreportdata_getAllChildrenUnder1InCommunesByDistrict")]
		public function getAllChildrenUnder1InCommunesByDistrict(timeData:String, communeId:Number):void {
			generalReportDataManager.getAllChildrenUnder1InCommunesByDistrict(timeData, communeId, dataDistrict, getAllChildrenUnder1InCommunesByDistrictResult, errorHandler);			
		}		
		public function getAllChildrenUnder1InCommunesByDistrictResult(e:TideResultEvent):void {
			childrenUnder1Datas = ArrayCollection(e.result);
			tideContext.raiseEvent("generalreportdata_afterGetAllChildrenUnder1InCommunesByDistrict");
		}
		
		[Observer("generalreportdata_createChildrenUnder1InCommunesByDistrict")]
		public function createChildrenUnder1InCommunesByDistrict(timeData:String, communeId:Number):void {
			generalReportDataManager.createChildrenUnder1InCommunesByDistrict(timeData, communeId, dataDistrict, createChildrenUnder1InCommunesByDistrictResult);
		}
		public function createChildrenUnder1InCommunesByDistrictResult(e:TideResultEvent):void {
			childrenUnder1Datas = ArrayCollection(e.result);
			tideContext.raiseEvent("generalreportdata_afterGetAllChildrenUnder1InCommunesByDistrict");
		}
		
		[Observer("generalreportdata_saveChildrenUnder1")]
		public function saveChildrenUnder1(timeData:String, communeId:Number):void {		
			generalReportDataManager.saveChildrenUnder1(timeData, communeId, childrenUnder1Datas, saveChildrenUnder1Result, errorHandler);
		}		
		public function saveChildrenUnder1Result(e:TideResultEvent):void {
			childrenUnder1Datas = ArrayCollection(e.result);
		}
		
		[Observer("generalreportdata_getAllGeneralReportDataInCommunesByDistrict")]
		public function getAllGeneralReportDataInCommunesByDistrict(timeData:String, communeId:Number):void {
			generalReportDataManager.getAllGeneralReportDataInCommunesByDistrict(timeData, communeId, dataDistrict, getAllGeneralReportDataInCommunesByDistrictResult, errorHandler);			
		}		
		public function getAllGeneralReportDataInCommunesByDistrictResult(e:TideResultEvent):void {
			generalReportDatas = ArrayCollection(e.result);
			tideContext.raiseEvent("generalreportdata_afterGetAllGeneralReportDataInCommunesByDistrict");
		}
		
		[Observer("generalreportdata_createGeneralReportDataInCommunesByDistrict")]
		public function createGeneralReportDataInCommunesByDistrict(timeData:String, communeId:Number):void {
			generalReportDataManager.createGeneralReportDataInCommunesByDistrict(timeData, communeId, dataDistrict, createGeneralReportDataInCommunesByDistrictResult);
		}
		public function createGeneralReportDataInCommunesByDistrictResult(e:TideResultEvent):void {
			generalReportDatas = ArrayCollection(e.result);
			tideContext.raiseEvent("generalreportdata_afterGetAllGeneralReportDataInCommunesByDistrict");
		}
		
		[Observer("generalreportdata_saveGeneralReportData")]
		public function saveGeneralReportData(timeData:String, communeId:Number):void {			
			generalReportDataManager.saveGeneralReportData(timeData, communeId, generalReportDatas, saveGeneralReportDataResult, errorHandler);
		}		
		public function saveGeneralReportDataResult(e:TideResultEvent):void {
			generalReportDatas = ArrayCollection(e.result);			
		}	

	}
}