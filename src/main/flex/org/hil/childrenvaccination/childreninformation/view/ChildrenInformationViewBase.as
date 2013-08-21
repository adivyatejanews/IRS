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
* Email: hieutt24@gmail.com | htran@path.org
*/

package org.hil.childrenvaccination.childreninformation.view
{
	import flash.events.KeyboardEvent;	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
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
	import mx.controls.Text;
	import mx.controls.TextInput;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.formatters.DateFormatter;
	import mx.managers.PopUpManager;
	import mx.utils.ObjectUtil;	
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	import org.hil.childrenvaccination.childreninformation.view.components.VaccinationHistoryPopupView;
	import org.hil.core.components.ChooserUTF8;
	import org.hil.core.components.DataGrid;
	import org.hil.core.components.DataGridColumn;
	import org.hil.core.components.DataGridSortable;
	import org.hil.core.components.DateFieldAutoCorrect;
	import org.hil.core.components.RowColorDataGrid;
	import org.hil.core.components.TextInputUTF8;	
	import org.hil.core.model.Children;
	import org.hil.core.model.ChildrenVaccinationHistory;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.Village;
	import org.hil.core.model.vo.UserAuthentication;
	import org.hil.core.model.vo.search.ChildrenSearchVO;
	import org.hil.core.model.vo.CommonDataVO;
	
	[ResourceBundle("ChildrenInformation")]
	[ResourceBundle("Notification")]
	public class ChildrenInformationViewBase extends VBox
	{
		public var tideContext:Context=Spring.getInstance().getSpringContext();		
		private var formatStr : DateFormatter = new DateFormatter();
		[Bindable][In]
		[ArrayElementType("org.hil.core.model.Children")]
		public var listChildren:ArrayCollection=new ArrayCollection();
		[Bindable] [Out]
		public var paramChildrenSearch:ChildrenSearchVO = new ChildrenSearchVO();
		[Bindable][In]
		public var provinces:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var districts2:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var communes2:ArrayCollection=new ArrayCollection();		
		[Bindable][In]
		public var villages2:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var districts7:ArrayCollection=new ArrayCollection();
		[Bindable][In]
		public var communes7:ArrayCollection=new ArrayCollection();		
		[Bindable][In]
		public var villages7:ArrayCollection=new ArrayCollection();
		[Bindable][In][Out]
		public var child:Children=new Children();
		[Bindable][In][Out]
		[ArrayElementType("org.hil.core.model.ChildrenVaccinationHistory")]
		public var listVaccinationHistory:ArrayCollection = new ArrayCollection();
		[Bindable] [In]
		public var globalUserAuthentication:UserAuthentication = new UserAuthentication();
		[Bindable]
		public var enableDetailInformation:Boolean = false;
		[Bindable]
		public var enableChildDetailInformation:Boolean = false;
		var today:Date= new Date();
		public var isNew:Boolean = true;
		//----Id of components
		public var comboboxProvinceFilter:ComboBox;
		public var comboboxDistrictFilter:ComboBox;
		public var comboboxCommuneFilter:ComboBox;
		public var comboboxVillageFilter:ComboBox;
		public var textDateOfBirthFromFilter:DateFieldAutoCorrect;
		public var textDateOfBirthToFilter:DateFieldAutoCorrect;
		public var textChildCodeFilter:TextInputUTF8;

		public var listChildrenGrid:RowColorDataGrid;
		public var vaccinationHistoryGrid:RowColorDataGrid;
		[Bindable]
		public var textChildName:TextInputUTF8;
		[Bindable]
		public var textDateOfBirth:DateFieldAutoCorrect;
		[Bindable]
		public var comboboxGender:ComboBox;
		[Bindable]
		public var childCodeText:TextInputUTF8;
		public var comboboxProvince:ComboBox;
		public var comboboxDistrict:ComboBox;
		public var comboboxCommune:ComboBox;
		public var comboboxVillage:ComboBox;
		[Bindable]
		public var textFatherName:TextInputUTF8;
		[Bindable]
		public var fatherYearOfBirth:TextInputUTF8;
		[Bindable]
		public var textFatherID:TextInputUTF8;
		[Bindable]
		public var textFatherMobile:TextInputUTF8;
		[Bindable]
		public var textMotherName:TextInputUTF8;
		[Bindable]
		public var motherYearOfBirth:TextInputUTF8;
		[Bindable]
		public var textMotherID:TextInputUTF8;
		[Bindable]
		public var textMotherMobile:TextInputUTF8;
		[Bindable]
		public var listCareTakerCBox:ComboBox;
		[Bindable]
		public var textCaretakerName:TextInputUTF8;
		[Bindable]
		public var caretakerYearOfBirth:TextInputUTF8;
		[Bindable]
		public var textCaretakerID:TextInputUTF8;
		[Bindable]
		public var textCaretakerMobile:TextInputUTF8;
		[Bindable]
		public var otherCaretakerForm:HBox;
		[Bindable]
		public var otherCaretakerLabel:Label;
		[Bindable]
		public var textChildLocked:CheckBox;		
		[Bindable]
		public var otherCaretaker:Boolean = false;
		[Bindable]
		public var textVaccinationFinishedStatus:Text;
		[Bindable]
		public var listCareTaker:Array;
		public var buttonSaveChild:Button;
		public var buttonEditChild:Button;
		public var buttonDeleteChild:Button;		
		public var linkExportExcel:Text;
		public var textChildNameFilter:TextInputUTF8;
		public var textChildMotherNameFilter:TextInputUTF8;
		[In]
		public var commonDataVO:CommonDataVO;
		[Bindable]	
		public var arrayOtherVaccinatedLocation:ArrayCollection;
		
		public function ChildrenInformationViewBase() {
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			Alert.buttonWidth = 80;
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {
			tideContext.childrenInformationView=this;			
			
			arrayOtherVaccinatedLocation = commonDataVO.arrayOtherVaccinatedLocation;
			
			comboboxProvinceFilter.selectedItem = globalUserAuthentication.user.commune.district.province;

			if (districts2 == null || districts2.length == 0 
				|| District(districts2.getItemAt(0)).province.id != globalUserAuthentication.user.commune.district.province.id) {
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts", globalUserAuthentication.user.commune.district.province));
			}
			if (communes2 == null || communes2.length == 0 
				|| Commune(communes2.getItemAt(0)).district.id != globalUserAuthentication.user.commune.district.id)
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes", globalUserAuthentication.user.commune.district));
			
			if (villages2 == null || villages2.length == 0 
				|| Village(villages2.getItemAt(0)).commune.id != globalUserAuthentication.user.commune.id) {				
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllVillages", globalUserAuthentication.user.commune));
			}
			var tmpFromYear:Number = today.getFullYear()-1; 
			var firstDate:Date = DateField.stringToDate("01-01-" +  today.getFullYear(),"DD/MM/YYYY");
			textDateOfBirthFromFilter.selectedDate = firstDate;
			
			this.textFatherMobile.restrict = "0-9";
			this.textMotherMobile.restrict = "0-9";
			this.textCaretakerMobile.restrict = "0-9";
			this.motherYearOfBirth.restrict = "0-9";
			this.fatherYearOfBirth.restrict = "0-9";
			this.caretakerYearOfBirth.restrict = "0-9";
			
			paramChildrenSearch.communeId = globalUserAuthentication.user.commune.id;
			paramChildrenSearch.dateOfBirthFrom = textDateOfBirthFromFilter.selectedDate;
			paramChildrenSearch.dateOfBirthTo = textDateOfBirthToFilter.selectedDate;
			dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_searchChildren",paramChildrenSearch));
			
			comboboxProvince.selectedItem = globalUserAuthentication.user.commune.district.province;
			buttonSaveChild.enabled = false;
			buttonEditChild.enabled = false;
			buttonDeleteChild.enabled = false;
			
			if (globalUserAuthentication.hasRole("ROLE_ADMIN")) {
				linkExportExcel.visible = true;
				linkExportExcel.enabled = false;
			}
		}
		
		public function refreshChildrenList():void {			
						
			if (comboboxDistrictFilter.text.length > 0 && comboboxDistrictFilter.selectedItem == null) {
				Alert.show(resourceManager.getString('ChildrenInformation', 'alert.the_district_is_not_listed_in_the_system'));
				return;
			}
			if (comboboxCommuneFilter.text.length > 0 && comboboxCommuneFilter.selectedItem == null) {
				Alert.show(resourceManager.getString('ChildrenInformation', 'alert.the_commune_is_not_listed_in_the_system'));
				return;
			}
			if (comboboxVillageFilter.text.length > 0 && comboboxVillageFilter.selectedItem == null) {
				Alert.show(resourceManager.getString('ChildrenInformation', 'alert.the_village_is_not_listed_in_the_system'));
				return;
			}
			
			if (textDateOfBirthFromFilter.selectedDate != null && textDateOfBirthToFilter.selectedDate != null
				&& textDateOfBirthFromFilter.selectedDate > textDateOfBirthToFilter.selectedDate) {
				textDateOfBirthFromFilter.selectedDate = textDateOfBirthToFilter.selectedDate;
			}
			if (comboboxProvinceFilter.selectedItem != null)				
				paramChildrenSearch.provinceId = Province(comboboxProvinceFilter.selectedItem).id;
			if (comboboxDistrictFilter.text.length > 0 && comboboxDistrictFilter.selectedItem != null)
				paramChildrenSearch.districtId = District(comboboxDistrictFilter.selectedItem).id;
			else
				paramChildrenSearch.districtId = -1;
			if (comboboxCommuneFilter.text.length > 0 && comboboxCommuneFilter.selectedItem != null)
				paramChildrenSearch.communeId = Commune(comboboxCommuneFilter.selectedItem).id;
			else
				paramChildrenSearch.communeId = -1;
			if (comboboxVillageFilter.text.length > 0 && comboboxVillageFilter.selectedItem != null)
				paramChildrenSearch.villageId = Village(comboboxVillageFilter.selectedItem).id;
			else
				paramChildrenSearch.villageId = -1;
			if (textDateOfBirthFromFilter.selectedDate != null)
				paramChildrenSearch.dateOfBirthFrom = textDateOfBirthFromFilter.selectedDate;
			if (textDateOfBirthToFilter.selectedDate) 
				paramChildrenSearch.dateOfBirthTo = textDateOfBirthToFilter.selectedDate;	
			
			paramChildrenSearch.childCode = textChildCodeFilter.text;
			paramChildrenSearch.childName = textChildNameFilter.text;
			paramChildrenSearch.motherName = textChildMotherNameFilter.text;

			dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_searchChildren",paramChildrenSearch));
			
			enableDetailInformation = false;
			enableChildDetailInformation = false;
			buttonSaveChild.enabled = false;
			buttonEditChild.enabled = false;
			buttonDeleteChild.enabled = false;
			isNew = true;
		}
		
		[Observer("childrenvaccination_childreninformation_afterSearchChildren")]
		public function afterSearchChildren():void {
			if (globalUserAuthentication.hasRole("ROLE_ADMIN")
				 && listChildren.length > 0 && comboboxCommuneFilter.selectedItem != null
				 && (textDateOfBirthToFilter.selectedDate.fullYear - textDateOfBirthFromFilter.selectedDate.fullYear <= 10)) {
				linkExportExcel.visible = true;
				linkExportExcel.enabled = true;
			} else {
				linkExportExcel.visible = false;
				linkExportExcel.enabled = false;
			}
		}
		
		public function getAllDistricts():void {
			if (communes7 != null && communes7.length > 0)
				communes7.removeAll();
			if (villages7 != null && villages7.length > 0)
				villages7.removeAll();			
			if (comboboxProvince.selectedItem != null) {		
				comboboxDistrict.selectedIndex = -1;
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts7", Province(comboboxProvince.selectedItem)));
			}			
		}
		[Observer("childrenvaccination_childreninformation_afterGetAllDistricts7")]
		public function afterGetAllDistricts7():void {	
			if (isNew) {
				if (districts7.contains(globalUserAuthentication.user.commune.district)) {
					comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;				
				} else {
					comboboxDistrict.selectedIndex = -1;				
				}
			} else {
				if (child != null && child.village != null)
					comboboxDistrict.selectedItem = child.village.commune.district;
				else
					comboboxDistrict.selectedIndex = -1;	
			}
		}
		public function getAllDistrictsFilter():void {
			if (communes2 != null && communes2.length > 0)
				communes2.removeAll();
			if (villages2 != null && villages2.length > 0)
				villages2.removeAll();			
			if (comboboxProvinceFilter.selectedItem != null) {		
				comboboxDistrictFilter.selectedIndex = -1;
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts", Province(comboboxProvinceFilter.selectedItem)));
			}			
		}
		[Observer("childrenvaccination_childreninformation_afterGetAllDistricts")]
		public function afterGetAllDistricts():void {		
			if (districts2.contains(globalUserAuthentication.user.commune.district)) {				
				comboboxDistrictFilter.selectedItem = globalUserAuthentication.user.commune.district;
			} else {				
				comboboxDistrictFilter.selectedIndex = -1;
			}
		}

		public function getAllCommunes():void {
			if (villages7 != null && villages7.length > 0)
				villages7.removeAll();
			if (comboboxDistrict.text.length == 0) {
				if (communes7 != null && communes7.length > 0)
					communes7.removeAll();
				comboboxDistrict.selectedIndex = -1;
				comboboxCommune.selectedIndex = -1;
				comboboxVillage.selectedIndex = -1;
			}
			if (comboboxDistrict.text.length > 0 && comboboxDistrict.selectedItem != null) {
				comboboxCommune.selectedItem = null;
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes7", District(comboboxDistrict.selectedItem)));
			}			
		}
		[Observer("childrenvaccination_childreninformation_afterGetAllCommunes7")]
		public function afterGetAllCommunes7():void {
			if (isNew) {				
				if (communes7.contains(globalUserAuthentication.user.commune)) {
					comboboxCommune.selectedItem = globalUserAuthentication.user.commune;				
				} else {
					comboboxCommune.selectedIndex = -1;				
				}
			} else {
				if (child != null && child.village != null)
					comboboxCommune.selectedItem = child.village.commune;
				else
					comboboxCommune.selectedIndex = -1;	
			}
			
		}
		public function getAllCommunesFilter():void {
			if (villages2 != null && villages2.length > 0)
				villages2.removeAll();			
			if (comboboxDistrictFilter.text.length == 0) {
				if (communes2 != null && communes2.length > 0)
					communes2.removeAll();
				comboboxDistrictFilter.selectedIndex = -1;
				comboboxCommuneFilter.selectedIndex = -1;
				comboboxVillageFilter.selectedIndex = -1;
			}
			if (comboboxDistrictFilter.text.length > 0 && comboboxDistrictFilter.selectedItem != null) {
				comboboxCommuneFilter.selectedIndex = -1;
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes", District(comboboxDistrictFilter.selectedItem)));
			}
		}
		[Observer("childrenvaccination_childreninformation_afterGetAllCommunes")]
		public function afterGetAllCommunes():void {		
			if (communes2.contains(globalUserAuthentication.user.commune)) {				
				comboboxCommuneFilter.selectedItem = globalUserAuthentication.user.commune;
			} else {				
				comboboxCommuneFilter.selectedIndex = -1;
			}
		}
		
		public function getAllVillages():void {		
			if (comboboxCommune.text.length == 0) {
				if (villages7 != null && villages7.length > 0)
					villages7.removeAll();
				comboboxVillage.selectedIndex = -1;
				comboboxCommune.selectedIndex = -1;				
			}
			if (comboboxCommune.text.length > 0 && comboboxCommune.selectedItem != null) {
				comboboxVillage.selectedItem = null;
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllVillages7", Commune(comboboxCommune.selectedItem)));
			}			
		}
		[Observer("childrenvaccination_childreninformation_afterGetAllVillages7")]
		public function afterGetAllVillages7():void {
			if (isNew) {
				comboboxVillage.selectedIndex = -1;				
			} else {
				if (child != null && child.village != null)
					comboboxVillage.selectedItem = child.village;
				else
					comboboxVillage.selectedIndex = -1;
			}						
		}
		public function getAllVillagesFilter():void {			
			if (comboboxCommuneFilter.text.length == 0) {
				if (villages2 != null && villages2.length > 0)
					villages2.removeAll();
				comboboxVillageFilter.selectedIndex = -1;
				comboboxCommuneFilter.selectedIndex = -1;				
			}
			if (comboboxCommuneFilter.text.length > 0 && comboboxCommuneFilter.selectedItem != null) {
				comboboxVillageFilter.selectedIndex = -1;
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllVillages", Commune(comboboxCommuneFilter.selectedItem)));
			}			
		}
		[Observer("childrenvaccination_childreninformation_afterGetAllVillages")]
		public function afterGetAllVillages():void {		
			comboboxVillageFilter.selectedIndex = -1;
		}
		
		public function changeVillageFilter():void {
			if (comboboxVillageFilter.text.length == 0) {
				comboboxVillageFilter.selectedIndex = -1;
			}
		}
		
		public function getChild():void {		
			this.child = Children(listChildrenGrid.selectedItem);
			isNew = false;
			fillData();
			enableDetailInformation = true;
			enableChildDetailInformation = false;
			buttonSaveChild.enabled = false;
			if (child.village.commune.id == globalUserAuthentication.user.commune.id
				|| (globalUserAuthentication.hasRole("ROLE_DISTRICT") && child.village.commune.district.id == globalUserAuthentication.user.commune.district.id)
				|| globalUserAuthentication.hasRole("ROLE_ADMIN")) {
				buttonEditChild.enabled = true;
				buttonDeleteChild.enabled = true;
			} else {
				buttonEditChild.enabled = false;
				buttonDeleteChild.enabled = false;
			}
			dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getVaccinationHistory"));			
		}
		
		public function fillData():void {			
			textChildName.text = child.fullName;
			textDateOfBirth.selectedDate = child.dateOfBirth;
			comboboxGender.selectedIndex = child.gender ? 0 : 1;
			childCodeText.text= child.childCode;
			textFatherName.text= child.fatherName;
			fatherYearOfBirth.text = isNaN(child.fatherBirthYear) ? "" : String(child.fatherBirthYear) ;
			textFatherID.text=child.fatherID;
			textFatherMobile.text=child.fatherMobile;
			textMotherName.text=child.motherName;
			motherYearOfBirth.text = isNaN(child.motherBirthYear) ? "" : String(child.motherBirthYear);
			textMotherID.text=child.motherID;
			textMotherMobile.text=child.motherMobile;
			listCareTakerCBox.selectedIndex = child.currentCaretaker;
			textCaretakerName.text=child.caretakerName;			
			caretakerYearOfBirth.text = isNaN(child.caretakerBirthYear) ? "" : String(child.caretakerBirthYear);
			textCaretakerID.text=child.caretakerID;
			textCaretakerMobile.text=child.caretakerMobile;		
			textChildLocked.selected = child.locked;
			comboboxProvince.selectedItem = child.village.commune.district.province;
						
			if (districts7 == null || districts7.length == 0 
				|| District(districts7.getItemAt(0)).province.id != child.village.commune.district.province.id)
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts7", child.village.commune.district.province));
			else 
				comboboxDistrict.selectedItem = child.village.commune.district;
			if (communes7 == null || communes7.length == 0 
				|| Commune(communes7.getItemAt(0)).district.id != child.village.commune.district.id)
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes7", child.village.commune.district));
			else 
				comboboxCommune.selectedItem = child.village.commune;
			if (villages7 == null || villages7.length == 0 
				|| Village(villages7.getItemAt(0)).commune.id != child.village.commune.id)
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllVillages7", child.village.commune));
			else 
				comboboxVillage.selectedItem = child.village;
			
			if (child.currentCaretaker == 2) {
				otherCaretaker = true;
				otherCaretakerForm.height = 30;
				otherCaretakerLabel.height = 22;
			} else {
				otherCaretaker = false;
				otherCaretakerForm.height = 0;
				otherCaretakerLabel.height = 0;
			}
			textVaccinationFinishedStatus.visible = child.finishedDate != null ? true : false;
		}
		
		//new registry
		public function newRegistry():void {			
			isNew = true;
			if (districts7 == null || districts7.length == 0 
				|| District(districts7.getItemAt(0)).province.id != globalUserAuthentication.user.commune.district.province.id) {
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts7", globalUserAuthentication.user.commune.district.province));
			} else {
				comboboxDistrict.selectedItem = globalUserAuthentication.user.commune.district;
			}
			if (communes7 == null || communes7.length == 0 
				|| Commune(communes7.getItemAt(0)).district.id != globalUserAuthentication.user.commune.district.id)
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes7", globalUserAuthentication.user.commune.district));
			else
				comboboxCommune.selectedItem = globalUserAuthentication.user.commune;
			if (villages7 == null || villages7.length == 0 
				|| Village(villages7.getItemAt(0)).commune.id != globalUserAuthentication.user.commune.id) {				
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllVillages7", globalUserAuthentication.user.commune));
			} else 
				comboboxVillage.selectedIndex = -1;
			
			refreshData();
			enableDetailInformation = true;
			enableChildDetailInformation = true;
			buttonSaveChild.enabled = true;
			buttonEditChild.enabled = false;
			buttonDeleteChild.enabled = false;			
			child = new Children();
		}
		
		public function refreshData():void {
			textChildName.text = "";
			textDateOfBirth.selectedDate = null;
			childCodeText.text="";
			comboboxGender.selectedIndex = -1;
			textFatherName.text="";
			fatherYearOfBirth.text = "";
			textFatherID.text="";
			textFatherMobile.text="";
			textMotherName.text="";
			motherYearOfBirth.text = "";
			textMotherID.text="";
			textMotherMobile.text="";
			listCareTakerCBox.selectedIndex = 0;
			textCaretakerName.text="";
			caretakerYearOfBirth.text = "";
			textCaretakerID.text="";
			textCaretakerMobile.text="";
			comboboxProvince.selectedItem = globalUserAuthentication.user.commune.district.province;
//			if (districts2 == null || districts2.length == 0 
//				|| District(districts2.getItemAt(0)).province.id != globalUserAuthentication.user.commune.district.province.id) {
//				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts", globalUserAuthentication.user.commune.district.province));
//			}
//			if (communes2 == null || communes2.length == 0 
//				|| Commune(communes2.getItemAt(0)).district.id != globalUserAuthentication.user.commune.district.id)
//				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes", globalUserAuthentication.user.commune.district));
//			if (villages2 == null || villages2.length == 0 
//				|| Village(villages2.getItemAt(0)).commune.id != globalUserAuthentication.user.commune.id)
//				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllVillages", globalUserAuthentication.user.commune));
			if (districts2 == null || districts2.length == 0) {
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllDistricts", globalUserAuthentication.user.commune.district.province));
			}
			if (communes2 == null || communes2.length == 0)
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllCommunes", globalUserAuthentication.user.commune.district));
			if (villages2 == null || villages2.length == 0)
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_getAllVillages", globalUserAuthentication.user.commune));
			comboboxVillage.selectedIndex = -1;
			
			otherCaretaker = false;
			otherCaretakerForm.height = 0;
			otherCaretakerLabel.height = 0;
			listVaccinationHistory = new ArrayCollection();
			textVaccinationFinishedStatus.visible = false;
			comboboxVillage.selectedIndex = -1;
			focusManager.setFocus(textChildName);
		}
		
		public function disableMonitoring():void {
			if (textChildLocked.selected) {
				Alert.show(resourceManager.getString('ChildrenInformation', 'alert.are_you_sure_want_to_disable_this_child'),
					resourceManager.getString('ChildrenInformation','notice'),
					Alert.YES|Alert.NO, this, alertDisableMonitoringListener, null, Alert.YES);				
			}				
		}
		public function alertDisableMonitoringListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				return;
			} else {
				textChildLocked.selected = false;
			}
		}
		
		public function validateChild():void {			
			
			var validateResults:String = "";
			
			if (textDateOfBirth.selectedDate == null) {
				validateResults += "- " + resourceManager.getString('ChildrenInformation', 'alert.date_of_birth_is_missing');
			}
			else if (textDateOfBirth.selectedDate.time > today.time 
						|| (isNew == true && textDateOfBirth.selectedDate.fullYear < (today.getFullYear()-10))
						|| (isNew == false && textDateOfBirth.selectedDate.fullYear < 2000)) {
				validateResults += "- " + resourceManager.getString('ChildrenInformation', 'alert.input_wrong_date_of_birth');				
			}			
			
			if (comboboxGender.selectedIndex < 0) {
				validateResults += "\n- " + resourceManager.getString('ChildrenInformation', 'alert.missed_gender');
			}
			
			if (comboboxVillage.selectedItem == null || comboboxVillage.text.length == 0) {
				validateResults += "\n- " + resourceManager.getString('ChildrenInformation', 'alert.must_choose_location');
			}			
			if (textMotherName.length == 0) {
				validateResults += "\n- " + resourceManager.getString('ChildrenInformation', 'alert.missed_mother_name');
			}
			if (fatherYearOfBirth.text.length > 0 && 
					(fatherYearOfBirth.text.length != 4 || Number(fatherYearOfBirth.text) > (textDateOfBirth.selectedDate.getFullYear() - 12)
						|| Number(fatherYearOfBirth.text) < textDateOfBirth.selectedDate.fullYear - 75)) {
				validateResults += "\n- " + resourceManager.getString('ChildrenInformation', 'alert.father_year_of_birth_is_incorrect');
			}
			if (motherYearOfBirth.text.length > 0 && 
					(motherYearOfBirth.text.length != 4 || Number(motherYearOfBirth.text) > (textDateOfBirth.selectedDate.getFullYear() - 12)
						|| Number(motherYearOfBirth.text) < textDateOfBirth.selectedDate.fullYear - 50)) {
				validateResults += "\n- " + resourceManager.getString('ChildrenInformation', 'alert.mother_year_of_birth_is_incorrect');
			}
			if (caretakerYearOfBirth.text.length > 0 && 
					(caretakerYearOfBirth.text.length != 4 || Number(caretakerYearOfBirth.text) > (today.getFullYear() - 5)
						|| Number(caretakerYearOfBirth.text) < textDateOfBirth.selectedDate.fullYear - 75)) {
				validateResults += "\n- " + resourceManager.getString('ChildrenInformation', 'alert.caretaker_year_of_birth_is_incorrect');
			}
			
			if (listCareTakerCBox.selectedIndex == 0) {
				if (textMotherName.length == 0) {
					validateResults += "\n- " + resourceManager.getString('ChildrenInformation', 'alert.missed_name');
				}				
			} else if (listCareTakerCBox.selectedIndex == 1) {
				if (textFatherName.length == 0){
					validateResults += "\n- " + resourceManager.getString('ChildrenInformation', 'alert.missed_name');
				}				
			} else if (listCareTakerCBox.selectedIndex == 2) {
				if (textCaretakerName.length == 0){
					validateResults += "\n- " + resourceManager.getString('ChildrenInformation', 'alert.missed_name');
				}				
			}
			
			if (validateResults.length > 0) {
				Alert.show(validateResults);
				return;
			}
			
			if (listCareTakerCBox.selectedIndex == 0) {				
				if (textMotherMobile.length == 0) {				
					Alert.show(resourceManager.getString('ChildrenInformation', 'alert.missed_mobile_number'),
						resourceManager.getString('ChildrenInformation','notice'),
						Alert.YES|Alert.NO, this, alertMissingCaretakerMobilePhoneListener, null, Alert.YES);
				} else
					saveChild();
			} else if (listCareTakerCBox.selectedIndex == 1) {				
				if (textFatherMobile.length == 0) {
					Alert.show(resourceManager.getString('ChildrenInformation', 'alert.missed_mobile_number'),
						resourceManager.getString('ChildrenInformation','notice'),
						Alert.YES|Alert.NO, this, alertMissingCaretakerMobilePhoneListener, null, Alert.YES);			
				} else
					saveChild();
			} else if (listCareTakerCBox.selectedIndex == 2) {				
				if (textCaretakerMobile.length == 0) {
					Alert.show(resourceManager.getString('ChildrenInformation', 'alert.missed_mobile_number'),
						resourceManager.getString('ChildrenInformation','notice'),
						Alert.YES|Alert.NO, this, alertMissingCaretakerMobilePhoneListener, null, Alert.YES);		
				} else
					saveChild();
			}			
			
		}
		public function validateChildKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				validateChild();
			}
		}
		public function alertMissingCaretakerMobilePhoneListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				return;
			} else {
				saveChild();
			}
		}
		
		public function saveChild():void {		
			
			child.village = Village(comboboxVillage.selectedItem);
			child.fullName = textChildName.text;
			child.dateOfBirth = textDateOfBirth.selectedDate2;
			
			if (comboboxGender.selectedIndex == 0)
				child.gender = true;
			else
				child.gender = false;
			child.locked = textChildLocked.selected;
			var selProvince:Province = Province(comboboxProvince.selectedItem);
			var selDistrict:District = District(comboboxDistrict.selectedItem);
			var selCommune:Commune = Commune(comboboxCommune.selectedItem);
			
			if (isNew) {
				child.childCode = selProvince.provinceId + selDistrict.districtId + selCommune.communeId + "-" + child.dateOfBirth.fullYear;
				child.fromMobile = false;
			}
			child.fatherName = textFatherName.text;
			child.fatherBirthYear = fatherYearOfBirth.text.length > 0 ? Number(fatherYearOfBirth.text) : Number.NaN;
			child.fatherID = textFatherID.text;
			child.fatherMobile = textFatherMobile.text;
			
			child.motherName = textMotherName.text;
			child.motherBirthYear = motherYearOfBirth.text.length > 0 ? Number(motherYearOfBirth.text) : Number.NaN;
			child.motherID = textMotherID.text;
			child.motherMobile = textMotherMobile.text;
			
			child.caretakerName = textCaretakerName.text;
			child.caretakerBirthYear = caretakerYearOfBirth.text.length > 0 ? Number(caretakerYearOfBirth.text) : Number.NaN;
			child.caretakerID = textCaretakerID.text;
			child.caretakerMobile = textCaretakerMobile.text;
			
			child.currentCaretaker = listCareTakerCBox.selectedIndex;
			
			dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_saveChild",false));
			
		}
		
		[Observer("childrenvaccination_childreninformation_confirmSaveChild")]
		public function confirmSaveChild():void {
			Alert.show(resourceManager.getString('ChildrenInformation', 'alert.may_be_duplicate_information'),
				resourceManager.getString('ChildrenInformation','notice'),
				Alert.YES|Alert.NO, this, alertSaveChildListener, null, Alert.YES);
		}
		public function alertSaveChildListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {				
				dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_saveChild",true));
			}
		}
		
		[Observer("childrenvaccination_childreninformation_afterSaveChild")]
		public function afterSaveChild():void {
			isNew = false;
			enableDetailInformation = false;
			enableChildDetailInformation = false;
			buttonSaveChild.enabled = false;
			buttonEditChild.enabled = false;
			buttonDeleteChild.enabled = false;
		}
		
		public function fullNameFunction(item:Object, column:DataGridColumn):String {
			var name:String = item.fullName;
			if (name.length == 0) {
				var motherName:String = item.motherName;
				if (motherName.length > 0) {
					return "(M)" + motherName;
				} else {
					var fatherName:String = item.fatherName;
					return "(B)" + fatherName;
				}				
			} else {
				return name;
			}				
		}
		
		public function dateCompareFunction(itemA:Object, itemB:Object):int {
			var dateA:Date = new Date(Date.parse(itemA.dateOfBirth));
			var dateB:Date = new Date(Date.parse(itemB.dateOfBirth));
			return ObjectUtil.dateCompare(dateA, dateB);
		}
		
		public function formatDate(item:Object, column:DataGridColumn):String {
			var dateFormatter:DateFormatter = new DateFormatter();
			dateFormatter.formatString = "DD/MM/YYYY";
			return dateFormatter.format(item[column.dataField]);	
		}
		
		public function vaccinatedConvert(item:Object, column:DataGridColumn):String {
			if (item.vaccinated == 0)
				return "";
			else if (item.vaccinated == 1)
				return resourceManager.getString('ChildrenInformation','already');
			else if (item.vaccinated == 2)
				return resourceManager.getString('ChildrenInformation','missed');
			else if (item.vaccinated == 3)
				return resourceManager.getString('ChildrenInformation','overcall');
			else 
				return "";
		}
		
		public function overdueConvert(item:Object, column:DataGridColumn):String {
			if (item.overdue == false)
				return "";			
			else
				return "Y";
		}
		
		public function nameOtherVaccinatedLocation(item:Object, column:DataGridColumn):String {
			if (!isNaN(item.otherVaccinatedLocation) && item.otherVaccinatedLocation > 0) {				
				return arrayOtherVaccinatedLocation[item.otherVaccinatedLocation].name;
			} else
				return "";
		}
		
		public function selectCaretaker():void {
			if (listCareTakerCBox.selectedIndex == 2) {
				otherCaretaker = true;
				otherCaretakerForm.height = 30;
				otherCaretakerLabel.height = 22;
			} else {
				otherCaretaker = false;
				otherCaretakerForm.height = 0;
				otherCaretakerLabel.height = 0;
			}			
		}
		
		public function openVaccinationHistoryPopup():void {
			var formVaccinationHistory:VaccinationHistoryPopupView = VaccinationHistoryPopupView(PopUpManager.createPopUp(this, VaccinationHistoryPopupView, true));		
			formVaccinationHistory.vaccinationEvent = ChildrenVaccinationHistory(vaccinationHistoryGrid.selectedItem);
			formVaccinationHistory.vaccinationEventId = vaccinationHistoryGrid.selectedIndex;
			PopUpManager.centerPopUp(formVaccinationHistory);
		}
		
		public function editChild():void {
			enableChildDetailInformation = true;
			buttonSaveChild.enabled = true;
			buttonEditChild.enabled = false;
			buttonDeleteChild.enabled = true;
		}
		public function editChildKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				editChild();
			}
		}
		
		public function alertDeleteChild():void {
			Alert.show(resourceManager.getString('Notification','confirmation_delete'), resourceManager.getString('Notification','notice'),
				Alert.YES|Alert.NO, 
				this, 
				alertDeleteChildListener, 
				null, 
				Alert.YES);
		}
		public function deleteChildKeyboardEventHandler(e:KeyboardEvent):void {
			if(e.keyCode == 13) {
				alertDeleteChild();
			}
		}
		public function alertDeleteChildListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				deleteChild();
			}	
		}
		
		public function deleteChild():void {			
			dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_deleteChild"));
			resetData();		
		}
		
		public function resetData():void {
			enableDetailInformation = false;
			enableChildDetailInformation = false;
			buttonSaveChild.enabled = false;
			buttonEditChild.enabled = false;
			buttonDeleteChild.enabled = false;
			isNew = true;
			refreshData();
		}
		
		public function printListExcel():void {
			dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_printListChildren",paramChildrenSearch));
		}
		
		public function importExcel():void {
			dispatchEvent(new TideUIEvent("childrenvaccination_childreninformation_importExcel"));
		}
		
		public function changeChildRowColor(datagrid:RowColorDataGrid, rowIndex:int, color:uint):uint {
			var rColor:uint;
			var item:Object = datagrid.dataProvider.getItemAt(rowIndex);           
			if (item["finishedDate"] != null) 
				rColor = 0xF5A9A9;			
			else
				rColor = 0xFFFFFF;
			return rColor;
		}

		public function changeRowColor(datagrid:RowColorDataGrid, rowIndex:int, color:uint):uint {
			var rColor:uint;
			var item:Object = datagrid.dataProvider.getItemAt(rowIndex);           
			var value:int = item["vaccinated"];
			if (value == 0) 
				rColor = 0xFFFFFF;
			else if (value == 1) 
				rColor = 0xF5A9A9;
			else if (value == 2) 
				rColor = 0xF3F781;
			else if (value == 3) 
				rColor = 0xF4FA58;
			else
				rColor = 0xFFFFFF;
			return rColor;
		}
	}
}