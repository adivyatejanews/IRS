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

package org.hil.generalreportdata.view
{
	import flash.events.KeyboardEvent;	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	import mx.containers.VBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.controls.ComboBox;
	import mx.controls.NumericStepper;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.events.ValidationResultEvent;
	import mx.managers.PopUpManager;
	import mx.validators.StringValidator;
	import mx.containers.HBox;
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	import org.hil.core.components.ChooserUTF8;
	import org.hil.core.components.DataGrid;
	import org.hil.core.components.DataGridColumn;
	import org.hil.core.components.DateFieldAutoCorrect;
	import org.hil.core.components.RowColorDataGrid;
	import org.hil.core.components.TextAreaUTF8;
	import org.hil.core.components.TextInputUTF8;
	import org.hil.core.model.ChildrenUnder1;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.vo.UserAuthentication;
	
	[ResourceBundle("GeneralReportData")]
	public class ChildrenUnder1TabPanelViewBase extends VBox {
		public var tideContext:Context = Spring.getInstance().getSpringContext();
		[Bindable][In]	
		public var provinces:ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var districts1: ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var childrenUnder1Datas: ArrayCollection = new ArrayCollection();
		[Bindable][In][Out]
		public var childrenUnder1Data:ChildrenUnder1 = new ChildrenUnder1();
		[Bindable][Out]
		public var dataDistrict:District = new District();
		[Bindable] [In]
		public var globalUserAuthentication:UserAuthentication = new UserAuthentication();
		//----Id of components		
		[Bindable]
		public var gridChildrenUnder1:RowColorDataGrid;
		public var buttonSave:Button;
		public var buttonEdit:Button;
		public var buttonCancel:Button;	
		public var buttonClearAll:Button;
		[Bindable]
		public var textCommuneName:TextInputUTF8;
		[Bindable]
		public var numericChildrenUnder1:NumericStepper;
		[Bindable]
		public var textNotes:TextAreaUTF8;
		public var isNew:Boolean = true;
		[Bindable]
		public var comboboxProvince:ComboBox;
		[Bindable]
		public var comboboxDistrict:ComboBox;
		[Bindable]
		public var communeName:HBox;
		[Bindable]
		public var timeData:DateFieldAutoCorrect;
		public var communeId:Number;
		
		public function ChildrenUnder1TabPanelViewBase() {		
			super();			
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			this.addEventListener(FlexEvent.SHOW, showHandler);
			Alert.buttonWidth = 80;
		}
		
		public function creationCompleteHandler(event:FlexEvent):void {			
			tideContext.childrenUnder1TabPanelView = this;			
			comboboxProvince.selectedItem = globalUserAuthentication.user.commune.district.province;
			getAllDistricts();			
			dispatchEvent(new TideUIEvent("generalreportdata_getAllDistricts", globalUserAuthentication.user.commune.district));
			dataDistrict = globalUserAuthentication.user.commune.district;
			if (globalUserAuthentication.hasRole("ROLE_COMMUNE") && !globalUserAuthentication.hasRole("ROLE_DISTRICT")) {
				comboboxDistrict.enabled = false;				
				comboboxDistrict.visible = true;				
				communeName.visible = true;
				communeId = globalUserAuthentication.user.commune.id;
			} else if (globalUserAuthentication.hasRole("ROLE_DISTRICT") && !globalUserAuthentication.hasRole("ROLE_ADMIN")) {				
				comboboxDistrict.enabled = false;				
				comboboxDistrict.visible = true;				
				communeName.visible = false;
				communeId = Number.NaN;
			} else {				
				comboboxDistrict.enabled = true;				
				comboboxDistrict.visible = true;				
				communeName.visible = false;
				communeId = Number.NaN;
			}
			
			dispatchEvent(new TideUIEvent("generalreportdata_getAllChildrenUnder1InCommunesByDistrict", timeData.text, communeId));
		}
		
		public function showHandler(event:FlexEvent):void {			
			if (districts1 != null && districts1.length > 0) {				
				comboboxProvince.selectedItem = District(districts1.getItemAt(0)).province;
				comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;	
			}
			else 
				comboboxProvince.selectedIndex = -1;			
		}
		
		public function getAllDistricts():void {
			if (comboboxProvince.selectedItem != null) {
				dispatchEvent(new TideUIEvent("generalreportdata_getAllDistricts", Province(comboboxProvince.selectedItem)));
			}			
		}
		[Observer("generalreportdata_afterGetAllDistricts")]
		public function afterGetAllDistricts():void {
			if (districts1.contains(globalUserAuthentication.user.commune.district))
				comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
		}
		
		public function getAllChildrenUnder1InCommunesByDistrict():void {
			if (comboboxProvince.selectedItem == null) {
				return;
			}
			if (comboboxDistrict.selectedItem == null) {
				return
			}
			dataDistrict = District(comboboxDistrict.selectedItem);
			dispatchEvent(new TideUIEvent("generalreportdata_getAllChildrenUnder1InCommunesByDistrict", timeData.text, communeId));
		}		
		
		public function searchKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				getAllChildrenUnder1InCommunesByDistrict();
			}
		}
		
		[Observer("generalreportdata_afterGetAllChildrenUnder1InCommunesByDistrict")]
		public function afterGetAllChildrenUnder1InCommunesByDistrict():void {
			if (childrenUnder1Datas.length == 0) {
				Alert.show(resourceManager.getString('GeneralReportData', 'alert.no_content_in_the_selected_time'),
					resourceManager.getString('GeneralReportData','notice'),
					Alert.YES|Alert.NO, this, alertNoContent, null, Alert.YES);
				DataGridColumn(gridChildrenUnder1.columns[1]).editable = true;
				DataGridColumn(gridChildrenUnder1.columns[2]).editable = true;
				buttonEdit.enabled = false;
				buttonSave.enabled = false;
				buttonCancel.enabled = false;
				buttonClearAll.enabled = false;
				
			} else {				
				DataGridColumn(gridChildrenUnder1.columns[1]).editable = false;
				DataGridColumn(gridChildrenUnder1.columns[2]).editable = false;
				buttonEdit.enabled = true;
				buttonSave.enabled = false;
				buttonCancel.enabled = false;
				buttonClearAll.enabled = false;
			}
		}
		public function alertNoContent(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				createNewData();
			} else {
				return;
			}
		}
		public function createNewData():void {
			dispatchEvent(new TideUIEvent("generalreportdata_createChildrenUnder1InCommunesByDistrict", timeData.text, communeId));
		}
		
		public function saveChildrenUnder1():void {
			dispatchEvent(new TideUIEvent("generalreportdata_saveChildrenUnder1", timeData.text, communeId));			
			DataGridColumn(gridChildrenUnder1.columns[1]).editable = false;
			DataGridColumn(gridChildrenUnder1.columns[2]).editable = false;
			buttonEdit.enabled = true;
			buttonSave.enabled = false;
			buttonCancel.enabled = false;
			buttonClearAll.enabled = false;
		}
		public function saveChildrenUnder1KeyboardEventHamdler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				saveChildrenUnder1();
			}
		}
		
		public function editChildrenUnder1():void {			
			DataGridColumn(gridChildrenUnder1.columns[1]).editable = true;			
			DataGridColumn(gridChildrenUnder1.columns[2]).editable = true;
			buttonEdit.enabled = false;
			buttonSave.enabled = true;
			buttonCancel.enabled = true;
			buttonClearAll.enabled = true;
		}
		public function editChildrenUnder1KeyboardEventHamdler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				editChildrenUnder1();
			}
		}
		
		public function cancelChildrenUnder1():void {
			dispatchEvent(new TideUIEvent("generalreportdata_getAllChildrenUnder1InCommunesByDistrict", timeData.text, communeId));
		}
		public function cancelChildrenUnder1KeyboardEventHamdler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				cancelChildrenUnder1();
			}
		}
		
		public function clearAllChildrenUnder1():void {
			for (var i:Number = 0; i< childrenUnder1Datas.length; i++) {
				ChildrenUnder1(childrenUnder1Datas.getItemAt(i)).totalChildrenUnder1 = Number.NaN;
			}
		}
		public function clearAllChildrenUnder1KeyboardEventHamdler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				clearAllChildrenUnder1();
			}
		}
	}
}