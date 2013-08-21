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

package org.hil.listmanagement.vaccination.view
{
	import flash.events.KeyboardEvent;	
	import mx.collections.ArrayCollection;
	import mx.containers.VBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.controls.ComboBox;
	import mx.controls.NumericStepper;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.events.ValidationResultEvent;
	import mx.validators.StringValidator;	
	import org.hil.core.components.DataGridColumn;
	import org.hil.core.components.RowColorDataGrid;
	import org.hil.core.components.TextAreaUTF8;
	import org.hil.core.components.TextInputUTF8;
	import org.hil.core.model.Vaccination;
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	
	[ResourceBundle("ListOfVaccinations")]
	[ResourceBundle("Notification")]
	public class ListOfVaccinationsViewBase extends VBox
	{
		public var tideContext:Context = Spring.getInstance().getSpringContext();
		[Bindable][In]		
		public var vaccinations: ArrayCollection = new ArrayCollection();
		[Bindable][In][Out]
		public var vaccination:Vaccination = new Vaccination();
		
		//----Id of components
		[Bindable]
		public var textVaccinationName:TextInputUTF8;    	
		[Bindable]
		public var numericVaccinationAge:NumericStepper;
		[Bindable]
		public var comboboxVaccinationAgeUnit:ComboBox;
		public var comboboxVaccinations:ComboBox;
		[Bindable]
		public var textGap:TextInputUTF8;
		[Bindable]
		public var textLimitDays:TextInputUTF8;
		[Bindable]
		public var textNotes:TextAreaUTF8;		
		[Bindable]
		public var vaccinationGrid:RowColorDataGrid;
		
		public var buttonNew:Button;		
		public var buttonDelete:Button;		
		public var buttonReset:Button;		
		public var buttonSave:Button;
		[Bindable]
		public var listAgeUnit:Array;   	
  	
    	public var isNew:Boolean = true;    	
		
    	public function ListOfVaccinationsViewBase() {		
			super();			
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			Alert.buttonWidth = 80;
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {			
			tideContext.listOfVaccinationsView = this;
			textGap.restrict = "0-9";
			textLimitDays.restrict = "0-9";
			
			//set status of input form
			setInputDataStatus(false);
			comboboxVaccinations.enabled = false;
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = false;
			buttonReset.enabled = false;
			getAllVaccinations();
		}
		
		public function getAllVaccinations():void {			
			dispatchEvent(new TideUIEvent("listmanagement_listofvaccinations_getAllVaccinations"));			
		}
		
		//new
		public function newVaccination():void {
			textVaccinationName.enabled = true;
			numericVaccinationAge.enabled = false;
			comboboxVaccinationAgeUnit.enabled = false;
			comboboxVaccinations.enabled = false;
			textGap.enabled = false;
			textLimitDays.enabled = false;
			textNotes.enabled = false;
			
			textVaccinationName.text = "";
			numericVaccinationAge.value = 0;
			textNotes.text = "";
			comboboxVaccinations.selectedIndex = -1;
			textGap.text = "";
			textLimitDays.text = "";
			
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = false;
			buttonReset.enabled = true;
			
			isNew = true;
			vaccination = new Vaccination();
		}
		public function newVaccinationKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				newVaccination();
			}
		}
		
		public function setInputDataStatus(status:Boolean):void {
			textVaccinationName.enabled = status;
			numericVaccinationAge.enabled = status;
			comboboxVaccinationAgeUnit.enabled = status;
			textGap.enabled = status;
			textLimitDays.enabled = status;
			textNotes.enabled = status;
		}
		
		//save
		public function saveVaccination():void {			
			if (textGap.text.length > 0 && textLimitDays.text.length > 0
					&& Number(textGap.text) > Number(textLimitDays.text)) {
				Alert.show('ListOfVaccinations','alert.wrong_gap_and_limit_days');
				return;
			}			
			if (comboboxVaccinations.selectedIndex >= 0 && textGap.text.length == 0)
				return;
			if (comboboxVaccinations.selectedIndex >= 0)
				vaccination.dependentVaccination = Vaccination(comboboxVaccinations.selectedItem);
			else
				vaccination.dependentVaccination = null;
			vaccination.name = textVaccinationName.text;
			vaccination.age = numericVaccinationAge.value;
			vaccination.ageUnit = comboboxVaccinationAgeUnit.selectedIndex;
			vaccination.notes = textNotes.text;
			vaccination.gap = textGap.text.length > 0 ? Number(textGap.text) : Number.NaN;
			vaccination.limitDays = textLimitDays.text.length > 0 ? Number(textLimitDays.text) : Number.NaN;
			dispatchEvent(new TideUIEvent("listmanagement_listofvaccinations_saveVaccination"));
			newVaccination();						
		}
		public function saveVaccinationKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {		
				saveVaccination();
			}
		}
		
		public function selectDependentVaccination():void {
			if (comboboxVaccinations.selectedIndex == -1) {
				textGap.text = "";
				textGap.enabled = false;
			} else
				textGap.enabled = true;
		}
		
		//reset
		public function resetVaccination():void {			
			textVaccinationName.text = "";
			numericVaccinationAge.value = 0;
			textNotes.text = "";
			textGap.text = "";
			textLimitDays.text = "";
			
			buttonNew.enabled = true;
			buttonSave.enabled = false;
			buttonDelete.enabled = false;
			buttonReset.enabled = false;
		}
		public function resetVaccinationKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				resetVaccination();
			}
		}
		
		//get detail
		public function getVaccination():void {
			vaccination = Vaccination(vaccinationGrid.selectedItem);
			isNew = false;
			fillData();
			setInputDataStatus(true);
			buttonNew.enabled = true;
			buttonSave.enabled = true;
			buttonDelete.enabled = true;
			buttonReset.enabled = true;
		}
		
		public function fillData():void {			
			textVaccinationName.text = vaccination.name;
			numericVaccinationAge.value = vaccination.age;
			comboboxVaccinationAgeUnit.selectedIndex = vaccination.ageUnit;
			textNotes.text = vaccination.notes;
			comboboxVaccinations.enabled = true;
			if (vaccination.dependentVaccination == null)
				comboboxVaccinations.selectedIndex = -1;
			else {				
				comboboxVaccinations.selectedItem = vaccination.dependentVaccination;
			}
			textGap.text = isNaN(vaccination.gap) ? "" : String(vaccination.gap);
			textLimitDays.text = isNaN(vaccination.limitDays) ? "" : String(vaccination.limitDays);
		}
		
		public function ageConvert(item:Object, column:DataGridColumn):String {
			return item.age + " " + listAgeUnit[item.ageUnit].label;
		}
		
		public function nanConvert(item:Object, column:DataGridColumn):String {
			if (isNaN(Number(item[column.dataField])))
				return "";
			else
				return String(item[column.dataField]) + " " + resourceManager.getString('ListOfVaccinations','days');	
			
		}
		
		public function gapConvert(item:Object, column:DataGridColumn):String {
			if (isNaN(Number(item[column.dataField])))
				return "";
			else
				return Vaccination(item.dependentVaccination).name + ": " + String(item[column.dataField]) + " " + resourceManager.getString('ListOfVaccinations','days');			
		}
		
		//delete
		public function alertDeleteVaccination():void
		{
			Alert.show(resourceManager.getString('Notification','confirmation_delete'),
				resourceManager.getString('Notification','notice'),
				Alert.YES|Alert.NO, this, alertDeleteVaccinationListener, null, Alert.YES);
		}
		public function alertDeleteVaccinationListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				deleteVaccination();
			}			
		}	
		public function deleteVaccination():void {
			dispatchEvent(new TideUIEvent("listmanagement_listofvaccinations_deleteVaccination", vaccination));
			newVaccination();
		}
		public function deleteVaccinationKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				alertDeleteVaccination();	
			}
		}		
		
	}
}