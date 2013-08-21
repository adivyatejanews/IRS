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
	import org.hil.core.components.ChooserUTF8;
	import org.hil.core.components.DataGrid;
	import org.hil.core.components.TextAreaUTF8;
	import org.hil.core.components.TextInputUTF8;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.components.RowColorDataGrid;
	import flash.events.KeyboardEvent;
	import mx.controls.ComboBox;
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	import mx.containers.VBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.events.ValidationResultEvent;
	import mx.managers.PopUpManager;
	import mx.validators.StringValidator;
	import mx.controls.Text;
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	
	[ResourceBundle("ListOfRegions")]
	[ResourceBundle("Notification")]
	public class CommuneTabPanelViewBase extends VBox 
	{
		public var tideContext:Context = Spring.getInstance().getSpringContext();
		[Bindable][In]	
		public var provinces:ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var districts: ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var communes: ArrayCollection = new ArrayCollection();
		[Bindable][Out]
		public var commune:Commune = new Commune();
		[Bindable][Out]
		public var district:District = new District();
		//----Id of components		
		[Bindable]
		public var gridCommune:RowColorDataGrid;
		public var buttonSave:Button;    	
		public var buttonDelete:Button;   
		public var buttonReset:Button;    	
		public var buttonNew:Button;
		[Bindable]
		public var textCommuneName:TextInputUTF8; 
		[Bindable]
		public var textCommuneId:TextInputUTF8;		   	
		[Bindable]
		public var textNotes:TextAreaUTF8;
		public var isNew:Boolean = true;
		[Bindable]
		public var comboboxProvince:ComboBox;
		public var comboboxDistrict:ComboBox;
		public var textProvinceName:Text;
		public var textDistrictName:Text;
		
		public function CommuneTabPanelViewBase() {		
			super();			
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			this.addEventListener(FlexEvent.SHOW, showHandler);
			Alert.buttonWidth = 80;
		}
		
		public function creationCompleteHandler(event:FlexEvent):void {			
			tideContext.communeTabPanelView = this;
			setInputDataStatus(false);
			
			comboboxProvince.selectedIndex = -1;
			comboboxDistrict.selectedIndex = -1;
			
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
			if (communes != null && communes.length > 0)				
				comboboxDistrict.selectedItem = Commune(communes.getItemAt(0)).district;
			else 
				comboboxDistrict.selectedIndex = -1;
		}
		
		public function getAllDistricts():void {
			if (comboboxProvince.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("listmanagement_region_getAllDistricts", Province(comboboxProvince.selectedItem)));
			}
		}
		
		public function getAllCommunes():void {
			if (comboboxProvince.selectedItem == null) {
				return;
			}
			if (comboboxDistrict.selectedItem == null) {
				return
			}
			district = District(comboboxDistrict.selectedItem);
			dispatchEvent(new TideUIEvent("listmanagement_region_getAllCommunes", district));
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;
		}		
		
		public function searchKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				getAllCommunes();
			}
		}
		
		//new
		public function newCommune():void {
			textProvinceName.text = district.province.provinceName;
			textDistrictName.text = district.districtName;
			textCommuneName.enabled = true;
			textCommuneId.enabled = false;
			textNotes.enabled = false;
			textCommuneId.text = "";
			textCommuneName.text = "";
			textNotes.text = "";
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;
			buttonNew.enabled = true;
			isNew = true;			
			commune = new Commune();
			commune.district = district;
		}
		
		public function newKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				newCommune();
			}
		}
		
		public function setInputDataStatus(status:Boolean):void {
			textCommuneId.enabled = status;
			textCommuneName.enabled = status;			
			textNotes.enabled = status;
		}
		
		public function autoUpperCommuneId():void {
			textCommuneId.text = String(textCommuneId.text.toUpperCase());
			
		}
		
		public function enableButtonSave():void {
			if(textCommuneId.text.length>0 && textCommuneName.text.length>0) {
				buttonSave.enabled = true;
			} else {
				buttonSave.enabled = false;
			}
		}		
		
		//save
		public function saveCommune():void {			
			commune.communeId = textCommuneId.text;
			commune.communeName = textCommuneName.text;
			commune.notes = textNotes.text;		
			dispatchEvent(new TideUIEvent("listmanagement_region_saveCommune"));
			newCommune();
		}
		public function saveKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				saveCommune();
			}
		}
		
		public function getCommune():void {
			commune = Commune(gridCommune.selectedItem);
			textProvinceName.text = commune.district.province.provinceName;
			textDistrictName.text = commune.district.districtName;
			textCommuneName.text = commune.communeName;
			textCommuneId.text = commune.communeId;
			textNotes.text = commune.notes;
			
			textCommuneName.enabled = true;
			textCommuneId.enabled = true;
			textNotes.enabled = true;
			
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = true;			
			buttonReset.enabled = true;
			
		}
		
		//delete
		public function alertDeleteCommune():void {
			Alert.show(resourceManager.getString('Notification','confirmation_delete'), resourceManager.getString('Notification','notice'),
				Alert.YES|Alert.NO, 
				this, 
				alertDeleteCommuneListener, 
				null, 
				Alert.YES);
		}
		
		public function alertDeleteCommuneListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				deleteCommune();
			}	
		}
		
		public function deleteCommune():void {			
			dispatchEvent(new TideUIEvent("listmanagement_region_deleteCommune"));
			resetCommune();		
		}
		
		public function deleteKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				alertDeleteCommune();
			}
		}
		
		//reset
		public function resetCommune():void {			
			textProvinceName.text = district.province.provinceName;
			textDistrictName.text = district.districtName;
			textCommuneName.enabled = true;
			textCommuneId.enabled = false;
			textNotes.enabled = false;
			textCommuneId.text = "";
			textCommuneName.text = "";
			textNotes.text = "";
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;
			buttonNew.enabled = true;
		}
		
		public function resetKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				resetCommune();
			}
		}
	}
}