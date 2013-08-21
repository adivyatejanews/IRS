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

package org.hil.personal.controller
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
	
	[Name("PersonalCtl")]
	[Bindable]
	[ResourceBundle("Personal")]
	[ResourceBundle("Notification")]
	public class PersonalCtl extends BaseController {
		[In]
		public var systemUserManager:Object;
				
		[Observer("personal_savePassword")]
		public function savePassword(systemAccount:SystemAccount, newpassword:String):void {
			systemUserManager.savePassword(systemAccount, newpassword, savePasswordResult, errorHandler);
		}
		
		public function savePasswordResult(e:TideResultEvent):void {	
			var sa:SystemAccount = SystemAccount(e.result);
			Alert.show(ResourceManager.getInstance().getString('Notification','operation_successful'));				
		}		
	}
}