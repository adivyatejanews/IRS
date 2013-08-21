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

package org.hil.childrenvaccination.childrenvaccinationreport.controller
{
	import flash.net.URLRequest;
	import flash.net.navigateToURL;	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;	
	import org.granite.tide.events.TideResultEvent;
	import org.granite.tide.spring.PagedQuery;
	import org.hil.core.controller.BaseController;
	import org.hil.core.model.Children;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.Village;
	
	[ResourceBundle("ChildrenVaccinationReport")]
	[Name("ChildrenVaccinationReportCtl")]
	[Bindable]
	public class ChildrenVaccinationReportCtl extends BaseController
	{
		[In]
		public var childrenManager:Object;
		[In]
		public var regionManager:Object;
		[In][Out]
		public var statistics:ArrayCollection=new ArrayCollection();		
		[Out]	
		public var districts8:ArrayCollection = new ArrayCollection();
		[Out]	
		public var communes8:ArrayCollection = new ArrayCollection();		
		
		[Observer("childrenvaccination_childrenvaccinationreport_getAllDistricts")]
		public function getAllDistricts(selectedProvince:Province):void	{
			regionManager.getAllDistricts(selectedProvince, getAllDistrictsResult, errorHandler);			
		}
		
		public function getAllDistrictsResult(e:TideResultEvent):void {
			districts8 = ArrayCollection(e.result);
			tideContext.raiseEvent("childrenvaccination_childrenvaccinationreport_afterGetAllDistricts");
		}
		
		[Observer("childrenvaccination_childrenvaccinationreport_getAllCommunes")]
		public function getAllCommunes(selectedDistrict:District):void {
			regionManager.getAllCommunes(selectedDistrict, getAllCommunesResult, errorHandler);			
		}
		
		public function getAllCommunesResult(e:TideResultEvent):void {
			communes8 = ArrayCollection(e.result);
			if (communes8 != null && communes8.length > 0) {
				var nullCommune:Commune = new Commune();
				nullCommune.communeName = "";
				nullCommune.id = 0;
				communes8.addItemAt(nullCommune, 0);
			}
			tideContext.raiseEvent("childrenvaccination_childrenvaccinationreport_afterGetAllCommunes");
		}
		
		[Observer("childrenvaccination_childrenvaccinationreport_getChildrenVaccinationReport")]
		public function getChildrenVaccinationReport(timeFrom:String, timeTo:String, commune:Commune, district:District):void {
			childrenManager.getChildrenVaccinationReport(timeFrom, timeTo, commune, district, getChildrenVaccinationReportResult, errorHandler);	
		}
		public function getChildrenVaccinationReportResult(e:TideResultEvent):void {			
			statistics = ArrayCollection(e.result);
			tideContext.raiseEvent("childrenvaccination_childrenvaccinationreport_enableButtonPrint");
		}
		
		[Observer("childrenvaccination_childrenvaccinationreport_printListVaccinationReport")]
		public function printListVaccinationReport(type:String, timeFrom:String, timeTo:String, commune:Commune, district:District):void {
			childrenManager.printListVaccinationReport(type, timeFrom, timeTo, commune, district, statistics, printListVaccinationReportResult);	
		}
		public function printListVaccinationReportResult(e:TideResultEvent):void {			
			var url:String=String(e.result);
			if (url != null && url.length > 0) {
				var u:URLRequest=new URLRequest(url);
				navigateToURL(u, "_blank");
			}
			if (url == null || url.length == 0)
				Alert.show("Error!");
		}
	}
}