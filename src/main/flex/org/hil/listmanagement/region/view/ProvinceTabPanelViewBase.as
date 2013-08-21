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
	import org.hil.core.components.RowColorDataGrid;
	import org.hil.core.components.TextAreaUTF8;
	import org.hil.core.components.TextInputUTF8;
	import org.hil.core.model.Province;	
	import flash.events.KeyboardEvent;	
	import mx.collections.ArrayCollection;
	import mx.containers.VBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.events.ValidationResultEvent;
	import mx.validators.StringValidator;	
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	import org.hil.core.model.vo.CommonDataVO;
	
	[ResourceBundle("ListOfRegions")]
	[ResourceBundle("Notification")]
	public class ProvinceTabPanelViewBase extends VBox {
		
		public var tideContext:Context = Spring.getInstance().getSpringContext();				
		[Bindable][In]
		public var provinces:ArrayCollection = new ArrayCollection();
		[Bindable][In][Out]
		public var province:Province = new Province();
		//----Id of components
		[Bindable]
		public var gridProvince:RowColorDataGrid;
		public var buttonNew:Button;
		public var buttonDelete:Button; 
		public var buttonSave:Button;		
		public var buttonReset:Button;    	
    	[Bindable]
    	public var textProvinceId:TextInputUTF8;    	
    	[Bindable]
    	public var textProvinceName:TextInputUTF8;    	
    	[Bindable]
    	public var textNotes:TextAreaUTF8;
		public var isNew:Boolean = true;
		    	  	
		public function ProvinceTabPanelViewBase() {		
			super();			
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			Alert.buttonWidth = 80;
		}
		
		public function creationCompleteHandler(event:FlexEvent):void {		
			tideContext.provinceTabPanelView = this;
			setInputDataStatus(false);
			
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;			
		}				
		
		public function enableButtonSave():void {
			if(textProvinceName.text.length>0 && textProvinceId.text.length >0) {
				buttonSave.enabled = true;
			} else {
				buttonSave.enabled = false;
			}
		}		
		
		public function setInputDataStatus(status:Boolean):void {
			textProvinceId.enabled = status;
			textProvinceName.enabled = status;			
			textNotes.enabled = status;
		}
		
		//new
		public function newProvince():void {
			textProvinceName.enabled = true;
			textProvinceId.enabled = false;
			textNotes.enabled = false;
			textProvinceId.text = "";
			textProvinceName.text = "";
			textNotes.text = "";
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;			
			isNew = true;			
			province = new Province();
		}
		public function newKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				newProvince();
			}
		}
		
		//save
		public function saveProvince():void {			
			province.provinceId = textProvinceId.text;
			province.provinceName = textProvinceName.text;
			province.notes = textNotes.text;
			dispatchEvent(new TideUIEvent("listmanagement_region_saveProvince", province));
			newProvince();
		}
		public function saveKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				saveProvince();
			}
		}
		
		public function getProvince():void {
			province = Province(gridProvince.selectedItem);
			textProvinceName.text = province.provinceName;
			textProvinceId.text = province.provinceId;
			textNotes.text = province.notes;
			
			textProvinceName.enabled = true;
			textProvinceId.enabled = true;
			textNotes.enabled = true;
			
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = true;			
			buttonReset.enabled = true;
			
		}

		//delete
		public function alertDeleteProvince():void {
			Alert.show(resourceManager.getString('Notification','confirmation_delete'), resourceManager.getString('Notification','notice'),
				Alert.YES|Alert.NO, 
				this, 
				alertDeleteProvinceListener, 
				null, 
				Alert.YES);
		}
		public function alertDeleteProvinceListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				deleteProvince();
			}    		
		}
		public function deleteProvince():void {
			dispatchEvent(new TideUIEvent("listmanagement_region_deleteProvince"));
			resetProvince();
		}
		public function deleteKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				alertDeleteProvince();
			}
		}
		
		//reset
		public function resetProvince():void {			
			textProvinceName.enabled = true;
			textProvinceId.enabled = false;
			textNotes.enabled = false;
			textProvinceId.text = "";
			textProvinceName.text = "";
			textNotes.text = "";
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = false;			
			buttonReset.enabled = false;					
		}
		public function resetKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				resetProvince();
			}
		}	
		
	}
}