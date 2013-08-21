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

package org.hil.childrenvaccination.listofchildrendue.view
{
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	
	import flexlib.controls.textClasses.StringBoundaries;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
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
	import org.hil.childrenvaccination.listofchildrendue.view.components.VaccinationHistoryPopupView;
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
	
	[ResourceBundle("ListOfChildrenDue")]
	public class ListOfChildrenDueViewBase extends VBox
	{
		public var tideContext:Context=Spring.getInstance().getSpringContext();		
		private var formatStr : DateFormatter = new DateFormatter();
		public var dataGridVaccinationHistory:DataGridSortable;
		[Bindable][In]
		[ArrayElementType("org.hil.core.model.vo.ChildrenDuePrintVO")]
		public var childrenDue:ArrayCollection=new ArrayCollection();		
		[Bindable][In]
		public var provinces:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var districts5:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var communes5:ArrayCollection=new ArrayCollection();
		[Bindable] [In]
		public var globalUserAuthentication:UserAuthentication = new UserAuthentication();
		public var testshow:VBox;
		//----Id of components		
		[Bindable]
		public var comboboxProvince:ComboBox;
		[Bindable]
		public var comboboxDistrict:ComboBox;
		[Bindable]
		public var comboboxCommune:ComboBox;
		[Bindable]
		public var buttonLoad:Button;
		[Bindable]
		public var buttonPrint:Button;
		[Bindable]
		public var timeDue:DateFieldAutoCorrect;		
		
		public function ListOfChildrenDueViewBase() {
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			Alert.buttonWidth = 80;
		}
		
		public function creationCompleteHandler(event:FlexEvent):void {
			tideContext.listOfChildrenDueView=this;
			var tmp:ChildrenDuePrintVO = new ChildrenDuePrintVO();
			var today:Date= new Date();			
			timeDue.selectedDate = today;			
			comboboxProvince.selectedItem = globalUserAuthentication.user.commune.district.province;
			comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
			
			if (districts5 == null || districts5.length == 0 
				|| District(districts5.getItemAt(0)).province.id != globalUserAuthentication.user.commune.district.province.id) {
				dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllDistricts", globalUserAuthentication.user.commune.district.province));
			}
			if (communes5 == null || communes5.length == 0 
				|| Commune(communes5.getItemAt(0)).district.id != globalUserAuthentication.user.commune.district.id)
				dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllCommunes", globalUserAuthentication.user.commune.district));
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
				dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllDistricts", Province(comboboxProvince.selectedItem)));
			}			
		}		
		[Observer("childrenvaccination_listofchildrendue_afterGetAllDistricts")]
		public function afterGetAllDistricts():void {		
			if (districts5.contains(globalUserAuthentication.user.commune.district))
				comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
		}
		
		public function getAllCommunes():void {			
			dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllCommunes", globalUserAuthentication.user.commune.district));
		}
		[Observer("childrenvaccination_listofchildrendue_afterGetAllCommunes")]
		public function afterGetAllCommunes():void {		
			if (communes5.contains(globalUserAuthentication.user.commune))
				comboboxCommune.selectedItem = globalUserAuthentication.user.commune;
		}
		
		public function getListOfChildrenDueInSelectedLocation():void {
			if (comboboxCommune.selectedItem == null) {
				return;
			}			
			dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getListOfChildrenDue", timeDue.text,Commune(comboboxCommune.selectedItem)));
		}
		
		[Observer("childrenvaccination_listofchildrendue_enableButtonPrint")]
		public function enablePrintButton():void {
			if (childrenDue == null) {
				Alert.show(resourceManager.getString('ListOfChildrenDue','alert.vaccination_day_is_null'));
				return;
			} 
			if (childrenDue.length > 0)
				buttonPrint.enabled = true;
			else {
				Alert.show(resourceManager.getString('ListOfChildrenDue','alert.list_children_due_empty'));
				buttonPrint.enabled = false;
			}
		}
		
		public function printListChildrenDue():void {
			dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_printListChildrenDue",timeDue.text,Commune(comboboxCommune.selectedItem)));
		}
		
		public function formatDate(item:Object, column:DataGridColumn):String {
			var dateFormatter:DateFormatter = new DateFormatter();
			dateFormatter.formatString = "DD/MM/YYYY";
			return dateFormatter.format(item[column.dataField]);	
		}
		
		public function genderConvert(item:Object, column:DataGridColumn): String {
			var gender:Boolean = Boolean(item.gender);			
			if (gender == true) {
				return resourceManager.getString('ListOfChildrenDue', "female");
			} else {
				return resourceManager.getString('ListOfChildrenDue', "male"); 
			}
		}
		
		public function motherBirthYearConvert(item:Object, column:DataGridColumn): String {			
			if (isNaN(Number(item.motherBirthYear))) {
				return "";
			} else {
				return Number(item.motherBirthYear) + ""; 
			}
		}
		
		public function dateCompareFunction(itemA:Object, itemB:Object):int {
			var dateA:Date = new Date(Date.parse(itemA.dateOfBirth));
			var dateB:Date = new Date(Date.parse(itemB.dateOfBirth));
			return ObjectUtil.dateCompare(dateA, dateB);
		}
		
		public function openVaccinationHistoryPopup():void {
			var formVaccinationHistory:VaccinationHistoryPopupView = VaccinationHistoryPopupView(PopUpManager.createPopUp(this, VaccinationHistoryPopupView, true));			
			formVaccinationHistory.childDue = ChildrenDuePrintVO(dataGridVaccinationHistory.selectedItem);
			PopUpManager.centerPopUp(formVaccinationHistory);
		}
	}
}