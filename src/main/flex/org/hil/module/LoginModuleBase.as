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

package org.hil.module
{
	import org.hil.authentication.controller.AuthenticationCtl;
	import org.hil.authentication.view.components.LoginView;
	import org.hil.core.module.BaseModule;
	import org.hil.module.components.LoginModule;	
	import flash.system.ApplicationDomain;	
	import org.granite.tide.ITideModule;
	import org.granite.tide.Tide;
	import org.granite.tide.spring.Spring;
	import mx.controls.Alert;
	public class LoginModuleBase extends BaseModule implements ITideModule {
		
		public function LoginModuleBase() {
			super();
		}

		public function init(tide:Tide):void {
            tide.addComponent("AuthenticationCtl",AuthenticationCtl,false,true,0,appDomain);			
		}

		public override function addModule(_appDomain:ApplicationDomain):void {
			super.addModule(_appDomain);
			Spring.getInstance().addModule(LoginModule, appDomain);			
		}

	}

}