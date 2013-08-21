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
	import org.hil.core.model.Village;
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
	public class VillageTabPanelViewBase extends VBox 
	{
		public var tideContext:Context = Spring.getInstance().getSpringContext();
		[Bindable][In]	
		public var provinces:ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var districts: ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var communes: ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var villages: ArrayCollection = new ArrayCollection();
		[Bindable][In][Out]
		public var village:Village = new Village();
		public var commune:Commune = new Commune();
		//----Id of components		
		[Bindable]
		public var gridVillage:RowColorDataGrid;
		public var buttonSave:Button;    	
		public var buttonDelete:Button;   
		public var buttonReset:Button;    	
		public var buttonNew:Button;
		[Bindable]
		public var textVillageName:TextInputUTF8;
		[Bindable]
		public var textVillageId:TextInputUTF8;		    	
		[Bindable]
		public var textNotes:TextAreaUTF8;
		public var isNew:Boolean = true;

		public var comboboxProvince:ComboBox;
		public var comboboxDistrict:ComboBox;
		public var comboboxCommune:ComboBox;
		
		public var textProvinceName:Text;
		public var textDistrictName:Text;
		public var textCommuneName:Text;
		
		public function VillageTabPanelViewBase() {		
			super();			
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			this.addEventListener(FlexEvent.SHOW, showHandler);
			Alert.buttonWidth = 80;
		}
		
		public function creationCompleteHandler(event:FlexEvent):void {			
			tideContext.villageTabPanelView = this;
			setInputDataStatus(false);
			
			comboboxProvince.selectedIndex = -1;
			comboboxDistrict.selectedIndex = -1;
			comboboxCommune.selectedIndex = -1;
			
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
			if (villages != null && villages.length > 0)				
				comboboxCommune.selectedItem = Village(villages.getItemAt(0)).commune;
			else 
				comboboxCommune.selectedIndex = -1;
		}
		
		public function getAllDistricts():void {
			if (communes != null && communes.length > 0)
				communes.removeAll();
			if (comboboxProvince.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("listmanagement_region_getAllDistricts", Province(comboboxProvince.selectedItem)));
			}			
		}
		
		public function getAllCommunes():void {
			if (comboboxDistrict.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("listmanagement_region_getAllCommunes", District(comboboxDistrict.selectedItem)));
			}			
		}
		
		public function getAllVillages():void {
			if (comboboxProvince.selectedItem == null) {
				return;
			}
			if (comboboxDistrict.selectedItem == null) {
				return
			}
			if (comboboxCommune.selectedItem == null) {
				return
			}
			commune = Commune(comboboxCommune.selectedItem);
			dispatchEvent(new TideUIEvent("listmanagement_region_getAllVillages", commune));
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;
		}		
		
		public function searchKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				getAllVillages();
			}
		}
		
		//new
		public function newVillage():void {
			textProvinceName.text = commune.district.province.provinceName;
			textDistrictName.text = commune.district.districtName;
			textCommuneName.text = commune.communeName;
			textVillageName.enabled = true;
			textVillageId.enabled = false;
			textNotes.enabled = false;
			textVillageId.text = "";
			textVillageName.text = "";
			textNotes.text = "";
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;
			buttonNew.enabled = true;
			isNew = true;			
			village = new Village();
			village.commune = commune;			
		}
		
		public function newKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				newVillage();
			}
		}
		
		public function setInputDataStatus(status:Boolean):void {
			textVillageId.enabled = status;
			textVillageName.enabled = status;			
			textNotes.enabled = status;
		}
		
		public function autoUpperVillageId():void {
			textVillageId.text = String(textVillageId.text.toUpperCase());
			
		}
		
		public function enableButtonSave():void {
			if(textVillageId.text.length>0 && textVillageName.text.length>0) {
				buttonSave.enabled = true;
			} else {
				buttonSave.enabled = false;
			}
		}		
		
		//save
		public function saveVillage():void {			
			village.villageId = textVillageId.text;
			village.villageName = textVillageName.text;
			village.notes = textNotes.text;			
			dispatchEvent(new TideUIEvent("listmanagement_region_saveVillage"));
			newVillage();			
		}
		public function saveKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				saveVillage();
			}
		}
		
		public function getVillage():void {
			village = Village(gridVillage.selectedItem);
			textProvinceName.text = village.commune.district.province.provinceName;
			textDistrictName.text = village.commune.district.districtName;
			textCommuneName.text = village.commune.communeName;
			textVillageName.text = village.villageName;
			textVillageId.text = village.villageId;
			textNotes.text = village.notes;
			
			textVillageName.enabled = true;
			textVillageId.enabled = true;
			textNotes.enabled = true;
			
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = true;			
			buttonReset.enabled = true;
			
		}
		
		//delete
		public function alertDeleteVillage():void {
			Alert.show(resourceManager.getString('Notification','confirmation_delete'), resourceManager.getString('Notification','notice'),
				Alert.YES|Alert.NO, 
				this, 
				alertDeleteVillageListener, 
				null, 
				Alert.YES);
		}
		
		public function alertDeleteVillageListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) { 
				deleteVillage();
			}	
		}
		
		public function deleteVillage():void {			
			dispatchEvent(new TideUIEvent("listmanagement_region_deleteVillage"));
			resetVillage();		
		}
		
		public function deleteKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				alertDeleteVillage();
			}
		}
		
		//reset
		public function resetVillage():void {			
			textProvinceName.text = commune.district.province.provinceName;
			textDistrictName.text = commune.district.districtName;
			textCommuneName.text = commune.communeName;
			textVillageName.enabled = true;
			textVillageId.enabled = false;
			textNotes.enabled = false;
			textVillageId.text = "";
			textVillageName.text = "";
			textNotes.text = "";
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;
			buttonNew.enabled = true;		
		}
		
		public function resetKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				resetVillage();
			}
		}
	}
}