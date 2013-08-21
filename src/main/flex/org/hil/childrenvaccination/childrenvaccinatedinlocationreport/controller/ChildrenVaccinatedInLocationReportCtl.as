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

package org.hil.childrenvaccination.childrenvaccinatedinlocationreport.controller
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
	import org.hil.core.model.Vaccination;
	import org.hil.core.model.Village;
	
	[ResourceBundle("ChildrenVaccinatedInLocationReport")]
	[Name("ChildrenVaccinatedInLocationReportCtl")]
	[Bindable]
	public class ChildrenVaccinatedInLocationReportCtl extends BaseController
	{
		[In]
		public var childrenManager:Object;
		[In]
		public var regionManager:Object;
		[In]
		public var vaccinationManager:Object;
		[In][Out]
		public var statisticsInLocaion:ArrayCollection=new ArrayCollection();		
		[Out]	
		public var districts6:ArrayCollection = new ArrayCollection();
		[Out]	
		public var communes6:ArrayCollection = new ArrayCollection();		
		[Out]	
		public var vaccines:ArrayCollection = new ArrayCollection();
		
		[Observer("childrenvaccination_childrenvaccinatedinlocationreport_getAllDistricts")]
		public function getAllDistricts(selectedProvince:Province):void	{
			regionManager.getAllDistricts(selectedProvince, getAllDistrictsResult, errorHandler);			
		}
		
		public function getAllDistrictsResult(e:TideResultEvent):void {
			districts6 = ArrayCollection(e.result);
			tideContext.raiseEvent("childrenvaccination_childrenvaccinatedinlocationreport_afterGetAllDistricts");
		}
		
		[Observer("childrenvaccination_childrenvaccinatedinlocationreport_getAllCommunes")]
		public function getAllCommunes(selectedDistrict:District):void {
			regionManager.getAllCommunes(selectedDistrict, getAllCommunesResult, errorHandler);			
		}
		
		public function getAllCommunesResult(e:TideResultEvent):void {
			communes6 = ArrayCollection(e.result);
			if (communes6 != null && communes6.length > 0) {
				var nullCommune:Commune = new Commune();
				nullCommune.communeName = "";
				nullCommune.id = 0;
				communes6.addItemAt(nullCommune, 0);
			}
			tideContext.raiseEvent("childrenvaccination_childrenvaccinatedinlocationreport_afterGetAllCommunes");
		}
		
		[Observer("childrenvaccination_childrenvaccinatedinlocationreport_getAllVaccines")]
		public function getAllVaccines():void {
			vaccinationManager.getAllVaccinations(getAllVaccinesResult, errorHandler);
		}
		public function getAllVaccinesResult(e:TideResultEvent):void {
			vaccines = ArrayCollection(e.result);
		}
		
		[Observer("childrenvaccination_childrenvaccinatedinlocationreport_getChildrenVaccinatedInLocationReport")]
		public function getChildrenVaccinatedInLocationReport(timeFrom:String, timeTo:String, commune:Commune, district:District, vaccine:Vaccination):void {
			childrenManager.getChildrenVaccinatedInLocationReport(timeFrom, timeTo, commune, district, vaccine, getChildrenVaccinationReportResult, errorHandler);	
		}
		public function getChildrenVaccinationReportResult(e:TideResultEvent):void {			
			statisticsInLocaion = ArrayCollection(e.result);
			tideContext.raiseEvent("childrenvaccination_childrenvaccinatedinlocationreport_enableButtonPrint");
		}
		
		[Observer("childrenvaccination_childrenvaccinatedinlocationreport_printListVaccinatedInLocationReport")]
		public function printListVaccinatedInLocationReport(type:String, timeFrom:String, timeTo:String, commune:Commune, district:District, vaccine:Vaccination):void {
			childrenManager.printListVaccinatedInLocationReport(type, timeFrom, timeTo, commune, district, vaccine, statisticsInLocaion, printListVaccinatedInLocationReportResult,errorHandler);	
		}
		public function printListVaccinatedInLocationReportResult(e:TideResultEvent):void {			
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