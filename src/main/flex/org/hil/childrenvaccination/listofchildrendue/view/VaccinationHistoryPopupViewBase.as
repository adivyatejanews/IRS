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
	import flash.events.KeyboardEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
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
	import org.hil.core.model.vo.ChildrenDuePrintVO;
	import org.hil.core.model.vo.CommonDataVO;
	import org.hil.core.model.vo.UserAuthentication;
	import org.hil.core.util.DateMath;
	import org.hil.core.util.StringUtils;
	
	[ResourceBundle("ListOfChildrenDue")]
	public class VaccinationHistoryPopupViewBase extends TitleWindow
	{
		public var tideContext:Context=Spring.getInstance().getSpringContext();		
		private var formatStr : DateFormatter = new DateFormatter();
		public var comboboxVaccinationHistory:ComboBox;
		public var childDue:ChildrenDuePrintVO;
		[Bindable][In]
		[ArrayElementType("org.hil.core.model.ChildrenVaccinationHistory")]
		public var listVaccinationDueHistory:ArrayCollection = new ArrayCollection();
		public var listVaccinesDue:ListCollectionView = new ListCollectionView();
		public var vaccinationEvent:ChildrenVaccinationHistory=new ChildrenVaccinationHistory();		
		[Bindable][In]
		public var provinces:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var districts9:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var communes9:ArrayCollection=new ArrayCollection();
		[Bindable] [In]
		public var globalUserAuthentication:UserAuthentication = new UserAuthentication();
		public var vaccinationIndex:Number = 0;
		var today:Date = new Date();
		//--Id components		
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
			tideContext.vaccinationHistoryDuePopupViewBase=this;
			arrayOtherVaccinatedLocation = commonDataVO.arrayOtherVaccinatedLocation;
			formatStr.formatString="DD/MM/YYYY";
			this.title = resourceManager.getString('ListOfChildrenDue','vaccination_history') + ": " + childDue.fullName;			
			listVaccinesDue = childDue.listVaccines;
			dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getVaccinationHistory", childDue.childId, 0, childDue.listVaccines,true));			
		}
		
		[Observer("after_childrenvaccination_listofchildrendue_getVaccinationHistory")]
		public function afterGetVaccinationHistory():void {			
			comboboxVaccinationHistory.selectedIndex = 0;
			vaccinationIndex = 0;
			vaccinationEvent = ChildrenVaccinationHistory(comboboxVaccinationHistory.selectedItem);			
			fillData();
		}
		
		public function fillData():void {		
			onotherLocation = false;
			comboboxProvince.enabled = true;
			comboboxDistrict.enabled = true;
			comboboxCommune.enabled = true;
			comboboxOtherVaccinatedLocation.enabled = true;
			comboboxOtherVaccinatedLocation.selectedIndex = 0;
			
			if (vaccinationEvent.vaccinated == 0 || vaccinationEvent.vaccinated == 3) {
				dateOfImmunization.selectedDate = null;
				dateOfImmunization.enabled = false;
				dateOfImmunization.editable = false;
			} else
				dateOfImmunization.selectedDate = vaccinationEvent.dateOfImmunization;
			textReasonIfMissed.text = vaccinationEvent.reasonIfMissed;
			
			if (vaccinationEvent.vaccinated == 0 || vaccinationEvent.vaccinated == 3) {				
				comboboxProvince.selectedItem = globalUserAuthentication.user.commune.district.province;
				
				if (districts9 == null || districts9.length == 0 
					|| District(districts9.getItemAt(0)).province.id != globalUserAuthentication.user.commune.district.province.id) {					
					dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllDistricts3", globalUserAuthentication.user.commune.district.province));
				} else
					comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
				
				if (communes9 == null || communes9.length == 0 
					|| Commune(communes9.getItemAt(0)).district.id != globalUserAuthentication.user.commune.district.id)
					dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllCommunes3", globalUserAuthentication.user.commune.district));
				else
					comboboxCommune.selectedItem = globalUserAuthentication.user.commune;
			} else if (vaccinationEvent.vaccinatedLocation != null) {				
				comboboxProvince.selectedItem = vaccinationEvent.vaccinatedLocation.district.province;
				
				if (districts9 == null || districts9.length == 0 
					|| District(districts9.getItemAt(0)).province.id != vaccinationEvent.vaccinatedLocation.district.province.id) {
					dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllDistricts3", vaccinationEvent.vaccinatedLocation.district.province));
				} else
					comboboxDistrict.selectedItem = vaccinationEvent.vaccinatedLocation.district;
				
				if (communes9 == null || communes9.length == 0 
					|| Commune(communes9.getItemAt(0)).district.id != vaccinationEvent.vaccinatedLocation.district.id)
					dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllCommunes3", vaccinationEvent.vaccinatedLocation.district));
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
		
		[Observer("childrenvaccination_listofchildrendue_afterGetAllDistricts3")]
		public function afterGetAllDistricts():void {		
			if (vaccinationEvent.vaccinated == 0 || vaccinationEvent.vaccinated == 3) {
				if (districts9.contains(globalUserAuthentication.user.commune.district))
					comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
			} else {
				if (districts9.contains(vaccinationEvent.vaccinatedLocation.district))
					comboboxDistrict.selectedItem = vaccinationEvent.vaccinatedLocation.district;
			}
		}
		
		[Observer("childrenvaccination_listofchildrendue_afterGetAllCommunes3")]
		public function afterGetAllCommunes():void {
			if (vaccinationEvent.vaccinated == 0 || vaccinationEvent.vaccinated == 3) {
				if (communes9.contains(globalUserAuthentication.user.commune))
					comboboxCommune.selectedItem = globalUserAuthentication.user.commune;
			} else {
				if (communes9.contains(vaccinationEvent.vaccinatedLocation))
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
				Alert.show(resourceManager.getString('ListOfChildrenDue','alert.you_must_enter_the_date_of_vaccination'));
				return;
			}
			
			if (dateOfImmunization.selectedDate != null && (dateOfImmunization.selectedDate > today 
				|| DateMath.clearTime(DateMath.addDays(vaccinationEvent.child.dateOfBirth, vaccinationEvent.vaccination.age*vaccinationEvent.vaccination.ageUnit*30)) >  DateMath.clearTime(dateOfImmunization.selectedDate))) {
				Alert.show(resourceManager.getString('ListOfChildrenDue','alert.wrong_date_of_immunization'));
				return;
			}
			
			if (vaccinatedStatusChooser.selectedIndex == 1 && comboboxCommune.selectedItem == null && comboboxOtherVaccinatedLocation.selectedIndex == 0) {
				Alert.show(resourceManager.getString('ListOfChildrenDue','alert.you_must_enter_the_vaccinated_location'));
				return;
			}
			
			if (vaccinatedStatusChooser.selectedIndex == 1 && comboboxCommune.selectedItem != null && comboboxOtherVaccinatedLocation.selectedIndex == 0
				&& comboboxCommune.selectedItem.id != globalUserAuthentication.user.commune.id) {
				Alert.show(resourceManager.getString('ListOfChildrenDue', 'alert.please_check_the_vaccinated_location'),
					resourceManager.getString('ListOfChildrenDue','notice'),
					Alert.YES|Alert.NO, this, alertCheckLocationListener, null, Alert.YES);
				return;
			}
			saveVaccinationHistoryAction();
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
			if (vaccinatedStatusChooser.selectedIndex != vaccinationEvent.vaccinated
				 && vaccinatedStatusChooser.selectedIndex >= 1) {
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
				if (vaccinationEvent.vaccinated == 2 || vaccinationEvent.vaccinated == 3)
					listVaccinesDue.removeItemAt(vaccinationIndex);
				dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_saveVaccinationHistory",vaccinationEvent,childDue.childId, 0, listVaccinesDue,true));
			}
		}
		
		[Observer("childrenvaccination_listofchildrendue_afterSaveVaccinationHistory")]
		public function afterSaveVaccinationHistory():void {			
			Alert.show(resourceManager.getString('ListOfChildrenDue','alert.the_vaccination_history_is_saved_successful'));
			comboboxVaccinationHistory.selectedIndex = vaccinationIndex;
			vaccinationEvent = ChildrenVaccinationHistory(comboboxVaccinationHistory.selectedItem);
			fillData();			
		}

		public function getAllDistricts():void {
			if (communes9 != null && communes9.length > 0)
				communes9.removeAll();
			if (comboboxProvince.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllDistricts3", Province(comboboxProvince.selectedItem)));
			}			
		}		
		public function getAllCommunes():void {
			if (comboboxDistrict.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("childrenvaccination_listofchildrendue_getAllCommunes3", District(comboboxDistrict.selectedItem)));
			}			
		}
		
		public function changeVaccinationHistory():void {
			vaccinationEvent = ChildrenVaccinationHistory(comboboxVaccinationHistory.selectedItem);
			vaccinationIndex = comboboxVaccinationHistory.selectedIndex;
			fillData();
		}
		
		public function vaccineNameConvert(item:Object):String {
			return item.vaccination.name;
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