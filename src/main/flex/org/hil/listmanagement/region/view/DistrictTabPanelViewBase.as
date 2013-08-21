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

package org.hil.listmanagement.region.view
{
	import flash.events.KeyboardEvent;
	
	import mx.collections.ArrayCollection;
	import mx.containers.VBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.controls.ComboBox;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.events.ValidationResultEvent;
	import mx.managers.PopUpManager;
	import mx.validators.StringValidator;
	import mx.controls.Text;
	import org.hil.core.components.RowColorDataGrid;
	import org.hil.core.components.TextAreaUTF8;
	import org.hil.core.components.TextInputUTF8;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	
	[ResourceBundle("ListOfRegions")]	
	[ResourceBundle("Notification")]
	public class DistrictTabPanelViewBase extends VBox
	{
		public var tideContext:Context = Spring.getInstance().getSpringContext();
		[Bindable][In]	
		public var provinces:ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var districts: ArrayCollection = new ArrayCollection();
		[Bindable][In][Out]
		public var district:District = new District();
		public var province:Province = new Province();
		//----Id of components		
		[Bindable]
		public var gridDistrict:RowColorDataGrid;
		
		public var buttonNew:Button;
		public var buttonSave:Button;    	
		public var buttonDelete:Button;   
		public var buttonReset:Button;    	
		
		[Bindable]
		public var textDistrictName:TextInputUTF8;
    	[Bindable]
    	public var textDistrictId:TextInputUTF8;    	    	
    	[Bindable]
    	public var textNotes:TextAreaUTF8;
		
		public var isNew:Boolean = true;
		[Bindable]
		public var comboboxProvince:ComboBox;
		public var textProvinceName:Text;
		
		public function DistrictTabPanelViewBase() {		
			super();			
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);	
			this.addEventListener(FlexEvent.SHOW, showHandler);
			Alert.buttonWidth = 80;
		}
		
		public function creationCompleteHandler(event:FlexEvent):void {			
			tideContext.districtTabPanelView = this;
			setInputDataStatus(false);				
			
			buttonNew.enabled = false;
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;
		}
		
		public function showHandler(event:FlexEvent):void {
			if (districts != null && districts.length > 0)				
				comboboxProvince.selectedItem = District(districts.getItemAt(0)).province;
			else 
				comboboxProvince.selectedIndex = -1;
		}
		
		public function getAllDistricts():void {	
			if (comboboxProvince.selectedItem != null) {
				province = Province(comboboxProvince.selectedItem);
				dispatchEvent(new TideUIEvent("listmanagement_region_getAllDistricts", province));
				buttonNew.enabled = true;
				buttonSave.enabled = false;
				buttonDelete.enabled = false;			
				buttonReset.enabled = false;
			}			
		}		
		public function searchKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				getAllDistricts();
			}
		}
		
		//new
		public function newDistrict():void {
			textProvinceName.text = province.provinceName;
			textDistrictName.enabled = true;
			textDistrictId.enabled = false;
			textNotes.enabled = false;
			textDistrictId.text = "";
			textDistrictName.text = "";
			textNotes.text = "";
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;
			buttonNew.enabled = true;
			isNew = true;			
			district = new District();
			district.province = province;
		}
		
		public function newKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				newDistrict();
			}
		}
		
		public function setInputDataStatus(status:Boolean):void {
			textDistrictId.enabled = status;
			textDistrictName.enabled = status;			
			textNotes.enabled = status;
		}
		
		public function autoUpperDistrictId():void {
			textDistrictId.text = String(textDistrictId.text.toUpperCase());			
		}
		
		public function enableButtonSave():void {
			if(textDistrictName.text.length > 0 && textDistrictId.text.length > 0) {
				buttonSave.enabled = true;
			} else {
				buttonSave.enabled = false;
			}
		}
				
		//save
		public function saveDistrict():void {				
			district.districtId = textDistrictId.text;
			district.districtName = textDistrictName.text;
			district.notes = textNotes.text;
			dispatchEvent(new TideUIEvent("listmanagement_region_saveDistrict"));
			newDistrict();
			
		}
		public function saveKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				saveDistrict();
			}
		}
		
		public function getDistrict():void {
			district = District(gridDistrict.selectedItem);
			textProvinceName.text = district.province.provinceName;
			textDistrictName.text = district.districtName;
			textDistrictId.text = district.districtId;
			textNotes.text = district.notes;
			
			textDistrictName.enabled = true;
			textDistrictId.enabled = true;
			textNotes.enabled = true;
			
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = true;			
			buttonReset.enabled = true;
			
		}
		
		//delete
		public function alertDeleteDistrict():void {
			Alert.show(resourceManager.getString('Notification','confirmation_delete'), resourceManager.getString('Notification','notice'),
				Alert.YES|Alert.NO, 
				this, 
				alertDeleteDistrictListener, 
				null, 
				Alert.YES);
		}
		
		public function alertDeleteDistrictListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				deleteDistrict();
			}	
		}
		
		public function deleteDistrict():void {			
			dispatchEvent(new TideUIEvent("listmanagement_region_deleteDistrict"));
			resetDistrict();		
		}
		
		public function deleteKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				alertDeleteDistrict();
			}
		}
		
		//reset
		public function resetDistrict():void {			
			textProvinceName.text = province.provinceName;
			textDistrictName.enabled = true;
			textDistrictId.enabled = false;
			textNotes.enabled = false;
			textDistrictId.text = "";
			textDistrictName.text = "";
			textNotes.text = "";
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;
			buttonNew.enabled = true;			
		}
		
		public function resetKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				resetDistrict();
			}
		}	

	}
}