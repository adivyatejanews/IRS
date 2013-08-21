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

package org.hil.childrenvaccination.childreninformation.view
{
	import flash.events.KeyboardEvent;	
	import mx.collections.ArrayCollection;
	import mx.containers.Form;
	import mx.containers.TitleWindow;
	import mx.controls.Alert;
	import mx.controls.CheckBox;
	import mx.controls.ComboBox;
	import mx.controls.Label;
	import mx.controls.NumericStepper;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.formatters.DateFormatter;
	import mx.managers.PopUpManager;
	import org.hil.core.model.vo.CommonDataVO;
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	import org.hil.core.components.ChooserUTF8;
	import org.hil.core.components.DateFieldAutoCorrect;
	import org.hil.core.components.TextAreaUTF8;
	import org.hil.core.components.TextInputUTF8;
	import org.hil.core.model.ChildrenVaccinationHistory;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.vo.UserAuthentication;
	import org.hil.core.util.DateMath;
	import org.hil.core.util.StringUtils;
	
	[ResourceBundle("ChildrenInformation")]
	public class VaccinationHistoryPopupViewBase extends TitleWindow
	{
		public var tideContext:Context=Spring.getInstance().getSpringContext();		
		private var formatStr : DateFormatter = new DateFormatter();
		[In]
		[ArrayElementType("org.hil.core.model.ChildrenVaccinationHistory")]
		public var listVaccinationHistory:ArrayCollection = new ArrayCollection();		
		public var vaccinationEvent:ChildrenVaccinationHistory=new ChildrenVaccinationHistory();
		public var vaccinationEventId:Number;
		[Bindable][In]
		public var provinces:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var districts3:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var communes3:ArrayCollection=new ArrayCollection();
		[Bindable] [In]
		public var globalUserAuthentication:UserAuthentication = new UserAuthentication();
		var today:Date = new Date();
		//--Id components
		[Bindable]
		public var textVaccinationName:TextInputUTF8;
		[Bindable]
		public var dateOfImmunization:DateFieldAutoCorrect;
		[Bindable]
		public var textReasonIfMissed:TextAreaUTF8;
		[Bindable]
		public var vaccinatedStatusChooser:ComboBox;
		[Bindable]
		public var vaccinationOverdue:CheckBox;
		public var comboboxProvince:ComboBox;
		public var comboboxDistrict:ComboBox;
		public var comboboxCommune:ComboBox;
		[Bindable]
		public var comboboxOtherVaccinatedLocation:ComboBox;
		[In]
		public var commonDataVO:CommonDataVO;		
		[Bindable]
		[In]			
		public var arrayOtherVaccinatedLocation:ArrayCollection;
		
		var provinceVaccinatedSelIndex:Number = 0;
		var districtVaccinatedSelIndex:Number = 0;
		var communeVaccinatedSelIndex:Number = 0;
		var onotherLocation:Boolean = false;
		
		public function VaccinationHistoryPopupViewBase() {
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			Alert.buttonWidth = 80;
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {
			tideContext.vaccinationHistoryPopupViewBase=this;
			arrayOtherVaccinatedLocation = commonDataVO.arrayOtherVaccinatedLocation;
			formatStr.formatString="DD/MM/YYYY";			
			fillData();
		}
		
		public function fillData():void {
			textVaccinationName.text = vaccinationEvent.vaccination.name;
			if (vaccinationEvent.vaccinated == 0 || vaccinationEvent.vaccinated == 3) {
				dateOfImmunization.selectedDate = null;
				dateOfImmunization.enabled = false;
				dateOfImmunization.editable = false;
			} else
				dateOfImmunization.selectedDate = vaccinationEvent.dateOfImmunization;
			textReasonIfMissed.text = vaccinationEvent.reasonIfMissed;
			
			if (vaccinationEvent.vaccinated == 0 || vaccinationEvent.vaccinated == 3) {
				comboboxProvince.selectedItem = globalUserAuthentication.user.commune.district.province;
				
				if (districts3 == null || districts3.length == 0 
					|| District(districts3.getItemAt(0)).province.id != globalUserAuthentication.user.commune.district.province.id) {					
					dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts3", globalUserAuthentication.user.commune.district.province));
				} else
					comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
				
				if (communes3 == null || communes3.length == 0 
					|| Commune(communes3.getItemAt(0)).district.id != globalUserAuthentication.user.commune.district.id)
					dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes3", globalUserAuthentication.user.commune.district));
				else
					comboboxCommune.selectedItem = globalUserAuthentication.user.commune;
			} else if (vaccinationEvent.vaccinatedLocation != null) {
				comboboxProvince.selectedItem = vaccinationEvent.vaccinatedLocation.district.province;
				
				if (districts3 == null || districts3.length == 0 
					|| District(districts3.getItemAt(0)).province.id != vaccinationEvent.vaccinatedLocation.district.province.id) {
					dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts3", vaccinationEvent.vaccinatedLocation.district.province));
				} else
					comboboxDistrict.selectedItem = vaccinationEvent.vaccinatedLocation.district;
				
				if (communes3 == null || communes3.length == 0 
					|| Commune(communes3.getItemAt(0)).district.id != vaccinationEvent.vaccinatedLocation.district.id)
					dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes3", vaccinationEvent.vaccinatedLocation.district));
				else
					comboboxCommune.selectedItem = vaccinationEvent.vaccinatedLocation;
			}
			if (!isNaN(vaccinationEvent.otherVaccinatedLocation) && vaccinationEvent.otherVaccinatedLocation > 0) {
				comboboxOtherVaccinatedLocation.selectedIndex = vaccinationEvent.otherVaccinatedLocation;
				comboboxProvince.enabled = false;
				comboboxDistrict.enabled = false;
				comboboxCommune.enabled = false;
				comboboxProvince.selectedIndex = -1;
				comboboxDistrict.selectedIndex = -1;
				comboboxCommune.selectedIndex = -1;
			}
			vaccinatedStatusChooser.selectedIndex = vaccinationEvent.vaccinated;
			vaccinationOverdue.selected = vaccinationEvent.overdue;
		}
		
		[Observer("childrenvaccination_childreninformation_afterGetAllDistricts3")]
		public function afterGetAllDistricts():void {		
			if (vaccinationEvent.vaccinated == 0 || vaccinationEvent.vaccinated == 3) {
				if (districts3.contains(globalUserAuthentication.user.commune.district))
					comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
			} else {
				if (districts3.contains(vaccinationEvent.vaccinatedLocation.district))
					comboboxDistrict.selectedItem = vaccinationEvent.vaccinatedLocation.district;
			}
		}
		
		[Observer("childrenvaccination_childreninformation_afterGetAllCommunes3")]
		public function afterGetAllCommunes():void {
			if (vaccinationEvent.vaccinated == 0 || vaccinationEvent.vaccinated == 3) {
				if (communes3.contains(globalUserAuthentication.user.commune))
					comboboxCommune.selectedItem = globalUserAuthentication.user.commune;
			} else {
				if (communes3.contains(vaccinationEvent.vaccinatedLocation))
					comboboxCommune.selectedItem = vaccinationEvent.vaccinatedLocation;
			}
		}
		
		public function changeVaccinationStatus():void {
			if (vaccinatedStatusChooser.selectedIndex == 0 || vaccinatedStatusChooser.selectedIndex == 3) {
				dateOfImmunization.selectedDate = null;
				dateOfImmunization.enabled = false;
				dateOfImmunization.editable = false;
			} else {
				dateOfImmunization.enabled = true;
				dateOfImmunization.editable = true;
				dateOfImmunization.selectedDate = vaccinationEvent.dateOfImmunization;
			}
		}
		
		public function onOtherLocation():void {
			if (comboboxOtherVaccinatedLocation.selectedIndex == 0) {
				onotherLocation = false;
				comboboxProvince.enabled = true;
				comboboxProvince.selectedIndex = provinceVaccinatedSelIndex;
				comboboxDistrict.enabled = true;
				comboboxDistrict.selectedIndex = districtVaccinatedSelIndex;
				comboboxCommune.enabled = true;
				comboboxCommune.selectedIndex = communeVaccinatedSelIndex;
			} else {
				if (!onotherLocation) {
					onotherLocation = true;
					provinceVaccinatedSelIndex = comboboxProvince.selectedIndex;
					comboboxProvince.selectedIndex = -1;
					comboboxProvince.enabled = false;
					districtVaccinatedSelIndex = comboboxDistrict.selectedIndex;
					comboboxDistrict.selectedIndex = -1;
					comboboxDistrict.enabled = false;
					communeVaccinatedSelIndex = comboboxCommune.selectedIndex;
					comboboxCommune.selectedIndex = -1;
					comboboxCommune.enabled = false;
				}
				
			}
		}
		
		public function saveVaccinationHistory():void {
			if (vaccinatedStatusChooser.selectedIndex == 1 && dateOfImmunization.selectedDate == null) {
				Alert.show(resourceManager.getString('ChildrenInformation','alert.you_must_enter_the_date_of_vaccination'));
				return;
			}
			
			if (dateOfImmunization.selectedDate != null && (dateOfImmunization.selectedDate > today 
				|| DateMath.clearTime(DateMath.addDays(vaccinationEvent.child.dateOfBirth, vaccinationEvent.vaccination.age*vaccinationEvent.vaccination.ageUnit*30)) >  DateMath.clearTime(dateOfImmunization.selectedDate))) {
				Alert.show(resourceManager.getString('ChildrenInformation','alert.wrong_date_of_immunization'));
				return;
			}
			
			if (vaccinatedStatusChooser.selectedIndex == 1 && comboboxCommune.selectedItem == null && comboboxOtherVaccinatedLocation.selectedIndex == 0) {
				Alert.show(resourceManager.getString('ChildrenInformation','alert.you_must_enter_the_vaccinated_location'));
				return;
			}
			
			if (vaccinatedStatusChooser.selectedIndex == 1 && comboboxCommune.selectedItem != null && comboboxOtherVaccinatedLocation.selectedIndex == 0
					&& comboboxCommune.selectedItem.id != globalUserAuthentication.user.commune.id) {
				Alert.show(resourceManager.getString('ChildrenInformation', 'alert.please_check_the_vaccinated_location'),
					resourceManager.getString('ChildrenInformation','notice'),
					Alert.YES|Alert.NO, this, alertCheckLocationListener, null, Alert.YES);
				return;
			}
			saveVaccinationHistoryAction();
//			if ((vaccinatedStatusChooser.selectedIndex == 2 || vaccinatedStatusChooser.selectedIndex == 3) && vaccinationEventId > 0 
//					&& ChildrenVaccinationHistory(listVaccinationHistory.getItemAt(vaccinationEventId -1)).vaccinated == 0) {
//				Alert.show(resourceManager.getString('ChildrenInformation','alert.wrong_information'));
//				return;
//			}			
			
		}
		
		public function saveVaccinationHistoryEnterEvent(e : KeyboardEvent):void {
			if(e.charCode == 13) {
				saveVaccinationHistory();
			}
		}
		
		public function alertCheckLocationListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				saveVaccinationHistoryAction();
			} else {
				return;
			}
		}
		
		public function saveVaccinationHistoryAction():void {
			
			if (vaccinatedStatusChooser.selectedIndex != vaccinationEvent.vaccinated) {				
				if (vaccinatedStatusChooser.selectedIndex > 0) {				
					vaccinationEvent.dateOfImmunization = dateOfImmunization.selectedDate2;
					
					vaccinationEvent.reasonIfMissed = textReasonIfMissed.text;
					vaccinationEvent.vaccinated = vaccinatedStatusChooser.selectedIndex;
					
					if (comboboxOtherVaccinatedLocation.selectedIndex == 0) {
						vaccinationEvent.vaccinatedLocation = Commune(comboboxCommune.selectedItem);
						vaccinationEvent.otherVaccinatedLocation = 0;	
					} else {
						vaccinationEvent.otherVaccinatedLocation = comboboxOtherVaccinatedLocation.selectedIndex;
						vaccinationEvent.vaccinatedLocation = null;
					}
					dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_saveVaccinationHistory",vaccinationEvent));	
				} else {
					vaccinationEvent.dateOfImmunization = null;					
					vaccinationEvent.reasonIfMissed = "";
					vaccinationEvent.vaccinated = vaccinatedStatusChooser.selectedIndex;
					vaccinationEvent.otherVaccinatedLocation = 0;
					vaccinationEvent.vaccinatedLocation = null;
					dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_saveVaccinationHistory",vaccinationEvent));
				}
			} else {
				if (vaccinationEvent.vaccinated > 0) {
					vaccinationEvent.dateOfImmunization = dateOfImmunization.selectedDate2;
					
					vaccinationEvent.reasonIfMissed = textReasonIfMissed.text;
					vaccinationEvent.vaccinated = vaccinatedStatusChooser.selectedIndex;
					
					if (comboboxOtherVaccinatedLocation.selectedIndex == 0) {
						vaccinationEvent.vaccinatedLocation = Commune(comboboxCommune.selectedItem);
						vaccinationEvent.otherVaccinatedLocation = 0;	
					} else {
						vaccinationEvent.otherVaccinatedLocation = comboboxOtherVaccinatedLocation.selectedIndex;
						vaccinationEvent.vaccinatedLocation = null;
					}
					dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_saveVaccinationHistory",vaccinationEvent));
				}
			}
		}
		
		[Observer("childrenvaccination_childreninformation_afterSaveVaccinationHistory")]
		public function afterSaveVaccinationHistory():void {
			PopUpManager.removePopUp(this);
		}

		public function getAllDistricts():void {
			if (communes3 != null && communes3.length > 0)
				communes3.removeAll();
			if (comboboxProvince.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts3", Province(comboboxProvince.selectedItem)));
			}			
		}		
		public function getAllCommunes():void {
			if (comboboxDistrict.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes3", District(comboboxDistrict.selectedItem)));
			}			
		}
		
		//Close popup
		public function closeVaccinationHistoryPopup():void {
			PopUpManager.removePopUp(this);
		}
		public function closeVaccinationHistoryPopupEnterEvent(e : KeyboardEvent):void {
			if(e.charCode == 13) {
				closeVaccinationHistoryPopup();
			}
		}

	}
}