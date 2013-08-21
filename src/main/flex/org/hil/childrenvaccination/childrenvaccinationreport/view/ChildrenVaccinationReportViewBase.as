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

package org.hil.childrenvaccination.childrenvaccinationreport.view
{
	import flash.events.Event;
	import flash.events.KeyboardEvent;	
	import flexlib.controls.textClasses.StringBoundaries;	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	import mx.containers.FormItem;
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.controls.CheckBox;
	import mx.controls.ComboBox;
	import mx.controls.DataGrid;
	import mx.controls.DateField;
	import mx.controls.Label;
	import mx.controls.NumericStepper;
	import mx.controls.TextInput;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.formatters.DateFormatter;
	import mx.managers.PopUpManager;
	import mx.utils.ObjectUtil;	
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	import org.hil.core.components.DataGrid;
	import org.hil.core.components.DataGridColumn;
	import org.hil.core.components.DataGridSortable;
	import org.hil.core.components.DateFieldAutoCorrect;
	import org.hil.core.components.RowColorDataGrid;
	import org.hil.core.components.TextInputUTF8;
	import org.hil.core.model.Children;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.Village;
	import org.hil.core.model.vo.ChildrenDuePrintVO;
	import org.hil.core.model.vo.UserAuthentication;
	
	[ResourceBundle("ChildrenVaccinationReport")]
	public class ChildrenVaccinationReportViewBase extends VBox
	{
		public var tideContext:Context=Spring.getInstance().getSpringContext();		
		private var formatStr : DateFormatter = new DateFormatter();
		[Bindable][In]
		[ArrayElementType("org.hil.core.model.vo.RegionVaccinationReportData")]
		public var statistics:ArrayCollection=new ArrayCollection();		
		[Bindable][In]
		public var provinces:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var districts8:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var communes8:ArrayCollection=new ArrayCollection();
		[Bindable] [In]
		public var globalUserAuthentication:UserAuthentication = new UserAuthentication();
		var today:Date= new Date();
		//----Id of components		
		public var comboboxProvince:ComboBox;
		public var comboboxDistrict:ComboBox;
		public var comboboxCommune:ComboBox;
		[Bindable]
		public var buttonPrintPDF:Button;
		[Bindable]
		public var buttonPrintExcel:Button;
		public var timeFrom:DateFieldAutoCorrect;
		public var timeTo:DateFieldAutoCorrect;
		
		public function ChildrenVaccinationReportViewBase() {
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			Alert.buttonWidth = 80;
		}
		
		public function creationCompleteHandler(event:FlexEvent):void {
			tideContext.reportVaccinationReportView=this;
			
			timeFrom.selectedDate = today;			
			timeTo.selectedDate = today;
			comboboxProvince.selectedItem = globalUserAuthentication.user.commune.district.province;
			comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
			
			if (districts8 == null || districts8.length == 0 
				|| District(districts8.getItemAt(0)).province.id != globalUserAuthentication.user.commune.district.province.id) {
				dispatchEvent(new TideUIEvent("childrenvaccination_childrenvaccinationreport_getAllDistricts", globalUserAuthentication.user.commune.district.province));
			}
			
			if (communes8 == null || communes8.length == 0 
				|| Commune(communes8.getItemAt(0)).district.id != globalUserAuthentication.user.commune.district.id)
				dispatchEvent(new TideUIEvent("childrenvaccination_childrenvaccinationreport_getAllCommunes", globalUserAuthentication.user.commune.district));
			else
				comboboxCommune.selectedItem = globalUserAuthentication.user.commune;
			
			if (globalUserAuthentication.hasRole("ROLE_COMMUNE") && !globalUserAuthentication.hasRole("ROLE_DISTRICT")) {				
				comboboxDistrict.enabled = false;
				comboboxCommune.enabled = false;		
			} else if (globalUserAuthentication.hasRole("ROLE_DISTRICT") && !globalUserAuthentication.hasRole("ROLE_ADMIN")) {				
				comboboxDistrict.enabled = false;
				comboboxCommune.enabled = true;
			} else {				
				comboboxDistrict.enabled = true;
				comboboxCommune.enabled = true;
			}
		}
		
		public function getAllDistricts():void {
			if (comboboxProvince.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("childrenvaccination_childrenvaccinationreport_getAllDistricts", Province(comboboxProvince.selectedItem)));
			}			
		}		
		[Observer("childrenvaccination_childrenvaccinationreport_afterGetAllDistricts")]
		public function afterGetAllDistricts():void {		
			if (districts8.contains(globalUserAuthentication.user.commune.district))
				comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
		}
		
		public function getAllCommunes():void {
			dispatchEvent(new TideUIEvent("childrenvaccination_childrenvaccinationreport_getAllCommunes", globalUserAuthentication.user.commune.district));	
		}
		[Observer("childrenvaccination_childrenvaccinationreport_afterGetAllCommunes")]
		public function afterGetAllCommunes():void {
			if (globalUserAuthentication.hasRole("ROLE_COMMUNE") && !globalUserAuthentication.hasRole("ROLE_DISTRICT")) {
				if (communes8.contains(globalUserAuthentication.user.commune))
					comboboxCommune.selectedItem = globalUserAuthentication.user.commune;	
			} else
				comboboxCommune.selectedIndex = -1;
			
		}
		
		public function getChildrenVaccinationReportInSelectedLocation():void {			
			var timeFYM:Array = timeFrom.text.split("/");
			var timeTYM:Array = timeTo.text.split("/");			
			var month:Number = today.month + 1;			
			if (timeFYM[1] != timeTYM[1]) {
				Alert.show(resourceManager.getString('ChildrenVaccinationReport','alert.error_year'));
				return;
			} else if (timeFYM[0] > timeTYM[0]) {
				Alert.show(resourceManager.getString('ChildrenVaccinationReport','alert.error_month'));
				return;
			} else if (timeTYM[1] > today.fullYear || (timeTYM[1] == today.fullYear && timeTYM[0] > month)) {
				Alert.show(resourceManager.getString('ChildrenVaccinationReport','alert.error_date'));
				return;
			}
			
			if (globalUserAuthentication.hasRole("ROLE_COMMUNE") && !globalUserAuthentication.hasRole("ROLE_DISTRICT")) {
				if (comboboxCommune.selectedItem == null || comboboxCommune.text.length == 0) {
					return;
				}
				dispatchEvent(new TideUIEvent("childrenvaccination_childrenvaccinationreport_getChildrenVaccinationReport", timeFrom.text,timeTo.text, Commune(comboboxCommune.selectedItem),null));
			} else {
				if (comboboxDistrict.selectedItem == null) {
					return;
				}
				if (comboboxCommune.selectedIndex == 0)
					comboboxCommune.selectedIndex = -1;
				dispatchEvent(new TideUIEvent("childrenvaccination_childrenvaccinationreport_getChildrenVaccinationReport", timeFrom.text,timeTo.text, Commune(comboboxCommune.selectedItem),District(comboboxDistrict.selectedItem)));
			}			
		}
		
		[Observer("childrenvaccination_childrenvaccinationreport_enableButtonPrint")]
		public function enablePrintButton():void {
			if (statistics == null) {				
				return;
			} 
			if (statistics.length > 0) {
				buttonPrintPDF.enabled = true;
				buttonPrintExcel.enabled = true;
			}
			else {				
				buttonPrintPDF.enabled = true;
				buttonPrintExcel.enabled = true;
			}
		}
		
		public function printListVaccinationReport(type:String):void {
			if (globalUserAuthentication.hasRole("ROLE_COMMUNE") && !globalUserAuthentication.hasRole("ROLE_DISTRICT")) {
				if (comboboxCommune.selectedItem == null || comboboxCommune.text.length == 0) {
					return;
				}
				dispatchEvent(new TideUIEvent("childrenvaccination_childrenvaccinationreport_printListVaccinationReport", type, timeFrom.text,timeTo.text, Commune(comboboxCommune.selectedItem),null));
			} else {
				if (comboboxDistrict.selectedItem == null) {
					return;
				}
				if (comboboxCommune.selectedIndex == 0)
					comboboxCommune.selectedIndex = -1;
				dispatchEvent(new TideUIEvent("childrenvaccination_childrenvaccinationreport_printListVaccinationReport", type, timeFrom.text,timeTo.text, Commune(comboboxCommune.selectedItem),District(comboboxDistrict.selectedItem)));
			}
		}
		
		public function formatDate(item:Object, column:DataGridColumn):String {
			var dateFormatter:DateFormatter = new DateFormatter();
			dateFormatter.formatString = "DD/MM/YYYY";
			return dateFormatter.format(item[column.dataField]);	
		}
		
		public function convertNumberZero(item:Object, column:DataGridColumn):String {
			if (String(item[column.dataField]) == 'NaN')
				return "";
			else
				return String(item[column.dataField]);
		}
		
		public function convertRegionName(item:Object, column:DataGridColumn):String {
			var index:int = statistics.getItemIndex(item);
			if (index  == statistics.length - 2)
				return resourceManager.getString('ChildrenVaccinationReport','sum');
			else if (index  == statistics.length - 1)
				return resourceManager.getString('ChildrenVaccinationReport','total');
			else
				return String(item[column.dataField]);
		}
		public function changeRowColor(datagrid:RowColorDataGrid, rowIndex:int, color:uint):uint {
			var rColor:uint;
			if (rowIndex == statistics.length - 2) 
				rColor = 0xF5A9A9;
			else if (rowIndex == statistics.length - 1) 
				rColor = 0xF5A9A9;
			else if (rowIndex % 2 == 0) 
				rColor = 0xFFFFFF;
			else if (rowIndex % 2 == 1) 
				rColor = 0xFAFAFA;
			else
				rColor = 0xFFFFFF;
			return rColor;
		}
	}
}