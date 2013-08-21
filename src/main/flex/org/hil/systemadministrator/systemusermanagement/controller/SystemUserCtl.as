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

package org.hil.systemadministrator.systemusermanagement.controller
{
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.resources.ResourceManager;	
	import org.granite.tide.events.TideResultEvent;
	import org.hil.core.controller.BaseController;
	import org.hil.core.model.Commune;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;
	import org.hil.core.model.SystemAccount;
	import org.hil.core.model.SystemUser;
	import org.hil.core.model.vo.UserAuthentication;
	
	[Name("SystemUserCtl")]
	[Bindable]
	[ResourceBundle("SystemUserManagement")]
	[ResourceBundle("Notification")]
	public class SystemUserCtl extends BaseController {
		[In]
		public var systemUserManager:Object;
		[In]
		public var regionManager:Object;		
		[In][Out]
		public var systemAccounts:ArrayCollection = new ArrayCollection();		
		[In][Out]
		public var systemUsers:ArrayCollection = new ArrayCollection();
		[Out]
		public var districts4:ArrayCollection = new ArrayCollection();		
		[Out]
		public var communes4:ArrayCollection = new ArrayCollection();		
		[Out]
		public var accountRoles:ArrayCollection = new ArrayCollection();		
		
		[Observer("systemadministrator_systemusermanagement_getAllSystemUsers")]
		public function getAllSystemUsers():void {
			systemUserManager.getAllSystemUsers(getAllSystemUsersResult, errorHandler);
		}		
		public function getAllSystemUsersResult(e:TideResultEvent):void {
			systemUsers = ArrayCollection(e.result);
		}
		
		[Observer("systemadministrator_systemusermanagement_getAllSystemAccounts")]
		public function getAllSystemAccounts():void {
			systemUserManager.getAllSystemAccounts(getAllSystemAccountsResult, errorHandler);
		}		
		public function getAllSystemAccountsResult(e:TideResultEvent):void {
			systemAccounts = ArrayCollection(e.result);
		}		
		
		[Observer("systemadministrator_systemusermanagement_saveSystemUser")]
		public function saveSystemUser(systemUser:SystemUser, systemAccount:SystemAccount, newPw:Boolean):void {
			systemUserManager.saveSystemUser(systemUser, systemAccount, newPw, saveSystemUserResult, errorHandler);
		}
		
		public function saveSystemUserResult(e:TideResultEvent):void {			
			var tmp:ArrayCollection = ArrayCollection(e.result);
			if (tmp == null || tmp.length == 0) {
				Alert.show(ResourceManager.getInstance().getString('SystemUserManagement','alert.account_name_already_exists'));	
			} else {
				systemUsers = ArrayCollection(e.result);
				Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));	
			}						
		}				

		[Observer("systemadministrator_systemusermanagement_getAllSystemRoles")]
		public function getAllSystemRoles():void {
			tideContext.raiseEvent("commondata_getAllSystemRoles");			
		}		
		
		[Observer("systemadministrator_systemusermanagement_getAllAccountRoles")]
		public function getAllAccountRoles(systemAccount:SystemAccount):void {
			systemUserManager.getAllAccountRoles(systemAccount, getAllAccountRolesResult, errorHandler);
		}		
		public function getAllAccountRolesResult(e:TideResultEvent):void {
			accountRoles = ArrayCollection(e.result);
			tideContext.raiseEvent("systemadministrator_systemusermanagement_afterGetAllAccountRoles");			
		}
		
		[Observer("systemadministrator_systemusermanagement_deleteSystemUser")]
		public function deleteSystemUser(systemUser:SystemUser):void {
			systemUserManager.deleteSystemUser(systemUser, deleteSystemUserResult, errorHandler);
		}		
		public function deleteSystemUserResult(e:TideResultEvent):void {			
			var lastLength:Number = systemUsers.length;
			systemUsers = ArrayCollection(e.result);
			if(systemUsers.length < lastLength) {
				Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));
			} else {
				Alert.show(ResourceManager.getInstance().getString('Notification','operation_unsuccessful'));
			}			
		}
		
		// save account roles
		[Observer("systemadministrator_systemusermanagement_saveAccountRoles")]
		public function saveAccountRoles(tmpAccountRoles:ArrayCollection, systemAccount:SystemAccount):void {
			systemUserManager.saveAccountRoles(tmpAccountRoles, systemAccount, saveAccountRolesResult, errorHandler);
		}		
		public function saveAccountRolesResult(e:TideResultEvent):void {
			accountRoles = ArrayCollection(e.result);
			tideContext.raiseEvent("systemadministrator_systemusermanagement_afterGetAllAccountRoles");
		}		
		
		[Observer("systemadministrator_systemusermanagement_getAllDistricts")]
		public function getAllDistricts(selectedProvince:Province):void {
			regionManager.getAllDistricts(selectedProvince, getAllDistrictsResult, errorHandler);			
		}		
		public function getAllDistrictsResult(e:TideResultEvent):void {
			districts4 = ArrayCollection(e.result);
			tideContext.raiseEvent("systemadministrator_systemusermanagement_afterGetAllDistricts");
		} 
		
		[Observer("systemadministrator_systemusermanagement_getAllCommunes")]
		public function getAllCommunes(selectedDistrict:District):void {
			regionManager.getAllCommunes(selectedDistrict, getAllCommunesResult, errorHandler);			
		}		
		public function getAllCommunesResult(e:TideResultEvent):void {
			communes4 = ArrayCollection(e.result);
			tideContext.raiseEvent("systemadministrator_systemusermanagement_afterGetAllCommunes");
		}	
	}
}