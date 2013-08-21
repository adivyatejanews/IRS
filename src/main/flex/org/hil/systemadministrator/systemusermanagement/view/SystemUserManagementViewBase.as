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

package org.hil.systemadministrator.systemusermanagement.view
{
	import com.hillelcoren.components.Chooser;	
	import flash.events.Event;
	import flash.events.KeyboardEvent;	
	import mx.collections.ArrayCollection;
	import mx.containers.FormItem;
	import mx.containers.VBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.controls.CheckBox;
	import mx.controls.ComboBox;
	import mx.controls.DateField;
	import mx.controls.TextInput;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.events.ValidationResultEvent;
	import mx.managers.PopUpManager;
	import mx.utils.ObjectUtil;
	import mx.validators.DateValidator;
	import mx.validators.StringValidator;
	import mx.utils.StringUtil;
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	import org.hil.core.components.AutoComplete;
	import org.hil.core.components.ChooserUTF8;
	import org.hil.core.components.DataGridColumn;
	import org.hil.core.components.DataGridSortable;
	import org.hil.core.components.TextInputUTF8;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.SystemAccount;
	import org.hil.core.model.SystemRole;
	import org.hil.core.model.SystemUser;
	import org.hil.core.model.vo.CommonDataVO;
	import org.hil.core.model.vo.UserAuthentication;
	import org.hil.core.view.BaseView;
	
	[ResourceBundle("SystemUserManagement")]
	[ResourceBundle("Notification")]
	public class SystemUserManagementViewBase extends VBox
	{
		public var tideContext:Context = Spring.getInstance().getSpringContext();
		[Bindable] [In]
		[ArrayElementType("org.hil.model.SystemUser")]
		public var systemUsers:ArrayCollection = new ArrayCollection();
		[Bindable] [In]
		[ArrayElementType("org.hil.model.SystemAccount")]
		public var systemAccounts:ArrayCollection = new ArrayCollection();
		[Bindable]
		public var systemUser:SystemUser = new SystemUser();
		[Bindable] 
		public var systemAccount:SystemAccount = new SystemAccount();
		[Bindable][In]
		public var provinces:ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var districts4: ArrayCollection = new ArrayCollection();
		[Bindable][In]	
		public var communes4: ArrayCollection = new ArrayCollection();
		[Bindable]
		public var systemRoles:ArrayCollection = new ArrayCollection();
		[Bindable] [In]
		public var accountRoles:ArrayCollection = new ArrayCollection();
		public var isNew:Boolean = true;
		[In]
		public var commonDataVO:CommonDataVO;
		//--Id components		
		[Bindable]
		public var systemAccountGrid:DataGridSortable;
		[Bindable]
		public var comboboxProvince:ComboBox;
		[Bindable]
		public var comboboxDistrict:ComboBox;
		[Bindable]
		public var comboboxCommune:ComboBox;		
		[Bindable]
		public var textAccountName:TextInputUTF8;
		[Bindable]
		public var textPassword:TextInputUTF8;		
		[Bindable]
		public var textRetypePassword:TextInputUTF8;			
		
		public var buttonSaveSystemUser:Button;		
		public var buttonResetSystemUser:Button;
		public var buttonDeleteSystemUser:Button;
		
		public var dataGridSystemRoles:DataGridSortable;
		public var dataGridAccountRoles:DataGridSortable;
		
		public var accountRolesVBox:VBox;
		
		public var buttonMoveLeft:Button;		
		public var buttonMoveRight:Button;
		public var buttonSaveAccountRole:Button;
        [Bindable] [In]
    	public var globalUserAuthentication:UserAuthentication = new UserAuthentication();		

		public function SystemUserManagementViewBase() {
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			Alert.buttonWidth = 80;
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {
			tideContext.systemUserManagementView = this;
			getAllSystemUser();			
			getAllSystemAccounts();
			getAllSystemRoles();
			resetSystemUser();
			
			comboboxProvince.enabled = false;
			comboboxDistrict.enabled = false;
			comboboxCommune.enabled = false;
			
			comboboxProvince.selectedIndex = -1;
			comboboxProvince.selectedItem = null;
			
			textAccountName.enabled = false;
			textPassword.enabled = false;
			textRetypePassword.enabled = false;
			
			buttonSaveSystemUser.enabled = false;
			buttonResetSystemUser.enabled = false;
			buttonDeleteSystemUser.enabled = false;
			
			accountRolesVBox.enabled = false;
			comboboxProvince.selectedIndex = -1;
		}		

		public function getAllSystemUser():void {
			dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_getAllSystemUsers"));
		}			

		public function getAllSystemAccounts():void {
			dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_getAllSystemAccounts"));
		}
		
		public function getAllSystemRoles():void {
			dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_getAllSystemRoles"));
		}
		
		//new
		public function newSystemUser():void {
			isNew = true;
			systemAccountGrid.selectedIndex = -1;
			
			resetSystemUser();
			systemUser = new SystemUser();
			systemAccount = new SystemAccount();
			
			comboboxProvince.enabled = true;
			comboboxDistrict.enabled = true;
			comboboxCommune.enabled = true;			
			
			textAccountName.enabled = true;
			textAccountName.editable = true;
			textPassword.enabled = true;
			textRetypePassword.enabled = true;
			
			buttonSaveSystemUser.enabled = true;
			buttonResetSystemUser.enabled = true;
			buttonDeleteSystemUser.enabled = false;
			accountRolesVBox.enabled = false;
		}
		public function newSystemUserKeyboardEventHandler(event:KeyboardEvent):void {
			if(event.charCode == 13) {
				newSystemUser();
			}
		}
		
		public function getSystemUser():void {
			isNew = false;			
			systemUser = SystemUser(systemAccountGrid.selectedItem);
			systemAccount = systemUser.systemAccount;
			
			comboboxProvince.enabled = true;
			comboboxDistrict.enabled = true;
			comboboxCommune.enabled = true;			
			
			textAccountName.enabled = true;
			textAccountName.editable = false;
			textPassword.enabled = true;
			textRetypePassword.enabled = true;
			
			buttonSaveSystemUser.enabled = true;
			buttonResetSystemUser.enabled = true;
			buttonDeleteSystemUser.enabled = true;
			
			textAccountName.text = systemAccount.accountName;
			comboboxProvince.selectedItem = systemUser.commune.district.province;
			
			if (districts4.length == 0 || District(districts4.getItemAt(0)).province.id != systemUser.commune.district.province.id) {
				getAllDistricts();				
			} else {
				comboboxDistrict.selectedItem = systemUser.commune.district;	
			}
			
			if (communes4.length == 0 || Commune(communes4.getItemAt(0)).district.id != systemUser.commune.district.id) {
				dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_getAllCommunes", systemUser.commune.district));				
			} else {
				comboboxCommune.selectedItem = systemUser.commune;	
			}
			
			getAllAccountRoles();
			accountRolesVBox.enabled = true;
			buttonSaveAccountRole.enabled = false;
		}		
		
		// save		
		public function saveSystemUser():void {
			var newPw:Boolean = false;
			if(isNew) {
				if (textPassword.text.length == 0
					|| textRetypePassword.text.length == 0
					|| textPassword.text != textRetypePassword.text) {
					Alert.show(resourceManager.getString('SystemUserManagement','alert.wrong_password'));
					return;
				}
				systemAccount.password = StringUtil.trim(textPassword.text);
				newPw = true;
			} else {
				if ((textPassword.text.length > 0 || textRetypePassword.text.length > 0)
						&& textPassword.text != textRetypePassword.text) {
					Alert.show(resourceManager.getString('SystemUserManagement','alert.wrong_password'));
					return;
				} else {
					if (textPassword.text.length > 0) {
						newPw = true;
						systemAccount.password = StringUtil.trim(textPassword.text);
					}
				}
			}
			
			if (textAccountName.text.length == 0) {
				Alert.show(resourceManager.getString('SystemUserManagement','alert.account_name_must_not_be_empty'));
				return;
			}
			
			if (comboboxCommune.selectedItem == null) {
				Alert.show(resourceManager.getString('SystemUserManagement','alert.location_must_not_be_empty'));
				return;
			}
			systemAccount.accountName = textAccountName.text;
			systemUser.commune = Commune(this.comboboxCommune.selectedItem);
			dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_saveSystemUser", systemUser, systemAccount, newPw));
			resetSystemUser();
			accountRolesVBox.enabled = true;
			buttonSaveAccountRole.enabled = false;
			systemRoles = commonDataVO.allRoles;
		}
		
		public function saveSystemUserKeyboardEventHandler(event:KeyboardEvent):void {
			if (event.charCode == 13) {
				saveSystemUser();
	        }
		}		
		
		public function resetSystemUser():void {
			comboboxProvince.selectedItem = null;
			comboboxDistrict.selectedItem = null;
			comboboxCommune.selectedItem = null;
			
			textAccountName.text = "";
			textPassword.text = "";
			textRetypePassword.text = "";
			
			buttonSaveSystemUser.enabled = true;
			buttonDeleteSystemUser.enabled = false;

		}
		
		// reset
		public function resetSystemUserKeyboardEventHandler(event:KeyboardEvent):void {
			if (event.charCode == 13) {
	        	resetSystemUser();	
	        }
		}
		
		public function refreshListOfSystemUsers():void {
			
		}
		
		public function refreshListOfSystemUsersKeyboardEventHandler(event:KeyboardEvent):void {
			if (event.charCode == 13) {
				refreshListOfSystemUsers();
			}
		}
		
		// delete
		public function alertDeleteSystemUser():void {
			Alert.show(resourceManager.getString('Notification','confirmation'), "",
				Alert.YES|Alert.NO, 
				this, 
				alertDeleteSystemUserListener, 
				null, 
				Alert.YES);			
		}
		
		public function alertDeleteSystemUserListener(eventObj:CloseEvent):void {
			if(eventObj.detail==Alert.YES) {
				deleteSystemUser();
			}	
		}
		
		public function deleteSystemUser():void {			
			dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_deleteSystemUser", systemUser));
			resetSystemUser();
		}

		public function deleteSystemUserKeyboardEventHandler(event:KeyboardEvent):void {
			if (event.charCode == 13) {
	        	alertDeleteSystemUser();
	        }
		}				
		
		public function getAllDistricts():void {
			if (communes4 != null && communes4.length > 0)
				communes4.removeAll();
			if (comboboxProvince.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_getAllDistricts", Province(comboboxProvince.selectedItem)));
			}			
		}
		[Observer("systemadministrator_systemusermanagement_afterGetAllDistricts")]
		public function afterGetAllDistricts():void {			
			comboboxDistrict.selectedItem = systemUser.commune.district;
		}
		
		public function getAllCommunes():void {
			if (comboboxDistrict.selectedItem != null) {				
				dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_getAllCommunes", District(comboboxDistrict.selectedItem)));
			}			
		}
		[Observer("systemadministrator_systemusermanagement_afterGetAllCommunes")]
		public function afterGetAllCommunes():void {			
			comboboxCommune.selectedItem = systemUser.commune;
		}
		
		public function getFullAddress(item:Object, column:DataGridColumn):String {
			var commune:Commune = Commune(item.commune);
			return commune.communeName + " - " + commune.district.districtName + " - " + commune.district.province.provinceName;
		}		
		
		//role
		public function getAllAccountRoles():void {
			dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_getAllAccountRoles", systemAccount ));
		}
		[Observer("systemadministrator_systemusermanagement_afterGetAllAccountRoles")]
		public function afterGetAllAccountRoles():void {			
			systemRoles = new ArrayCollection();
			for (var i:Number = 0; i<commonDataVO.allRoles.length; i++) {			
				if (!accountRoles.contains(commonDataVO.allRoles.getItemAt(i))) {	
					systemRoles.addItem(commonDataVO.allRoles.getItemAt(i));
				}
			}
		}
		
		// save account roles
		public function saveAccountRoles():void {
			dispatchEvent(new TideUIEvent("systemadministrator_systemusermanagement_saveAccountRoles", dataGridAccountRoles.dataProvider, systemAccount));
			buttonSaveAccountRole.enabled = false;
		}		
		public function saveAccountRolesKeyboardEventHandler(event:KeyboardEvent):void {
			if (event.charCode == 13) {
				saveAccountRoles();
			}
		}
		
		public function moveRight():void {
			var item:Object = dataGridSystemRoles.selectedItem;
			if (item) {
				var idx:int = systemRoles.getItemIndex(item);
				accountRoles.addItem(item);
				systemRoles.removeItemAt(idx);
			}
		}
		
		public function moveLeft():void {
			var item:Object = dataGridAccountRoles.selectedItem;
			if (item) {
				var idx:int = accountRoles.getItemIndex(item);
				systemRoles.addItem(item);
				accountRoles.removeItemAt(idx);
			}
		}

	}
}