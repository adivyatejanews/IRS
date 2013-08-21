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

package org.hil.authentication.controller
{
	import org.hil.core.model.SystemRole;
	import org.hil.core.model.vo.MenuVO;
	import org.hil.core.model.vo.UserAuthentication;
	import org.hil.core.controller.BaseController;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.ResourceManager;	
	import org.granite.tide.events.TideFaultEvent;
	import org.granite.tide.events.TideResultEvent;
	import org.granite.tide.spring.Identity;

	[Bindable]
	[Name("AuthenticationCtl")]
	[ResourceBundle("MainApp")]
	public class AuthenticationCtl  extends BaseController {
		[In]
		public var loginManager:Object;

		[In]
		public var identity:Identity;

		[Out]
		public var loginMessage:String="no message";

		[Out]
		public var globalUserAuthentication:UserAuthentication = new UserAuthentication();

		private var menuVO:MenuVO=new MenuVO();

		[Out]
		public var globalCurrentView:Number=0;
		
		[Out]
		public var treeNavDataXML:XML=<menutree></menutree>;

		private var p_username:String;

		[Observer("authentication_doLogin")]
		public function doLogin(username:String, password:String):void {
			identity.login(username, password, loginHandler, loginFaultHandler);
			p_username=username;
		}

		private function loginHandler(e:TideResultEvent):void {
			loginMessage=ResourceManager.getInstance().getString('MainApp', 'username') + ": " + p_username + " " + ResourceManager.getInstance().getString('MainApp', 'login_successful');
			getPrincipal();
		}

		private function loginFaultHandler(e:TideFaultEvent):void {
			loginMessage=ResourceManager.getInstance().getString('MainApp', 'username') + ": " + p_username + " " + ResourceManager.getInstance().getString('MainApp', 'login_unsuccessful');			
		}

		[Observer("authentication_doLogout")]
		public function doLogout():void	{			
			globalUserAuthentication=new UserAuthentication();
			identity.logout();
			globalCurrentView=0;
		}

		private function getPrincipal():void {
			loginManager.getPrincipal(principalHandler);
		}

		private function principalHandler(e:TideResultEvent):void {
			globalUserAuthentication=UserAuthentication(e.result);			
			if (globalUserAuthentication.expired) {
				Alert.show(ResourceManager.getInstance().getString('MainApp', 'account_expired'), ResourceManager.getInstance().getString('MainApp', 'notice'), Alert.YES, null, function(ev:CloseEvent):void
					{
						if (ev.detail == Alert.YES)
						{
							doLogout();
						}
					});
			}
			if (globalUserAuthentication.lock && !globalUserAuthentication.expired) {
				Alert.show(ResourceManager.getInstance().getString('MainApp', 'account_locked'), ResourceManager.getInstance().getString('MainApp', 'notice'), Alert.YES, null, function(ev:CloseEvent):void
					{
						if (ev.detail == Alert.YES)
						{
							doLogout();
						}
					});
			}
			if (!globalUserAuthentication.lock)
			{
				treeNavDataXML=menuVO.vMenuXML.copy();
				checkVMenuRolesXML(treeNavDataXML);					
			}
		}

		private function checkVMenuRoles(list:XMLList):void {
			var node:XML;

			for (var i:int=0; i < list.length(); i++) {
				node=list[i];
				if (String(node.@roles) != "") {
					var roles:Array=String(node.@roles).split(/,/);
					if (!globalUserAuthentication.hasRoles(roles)) {						
						delete(list[i]);
					} else if (node.node) {
						checkVMenuRoles(node.node);
					}
				}
			}
		}

		private function checkVMenuRolesXML(xmlValue:XML):void {
			var node:XML;
			var list:XMLList=xmlValue.node;
			while (i < list.length())
				for (var i:int=0; i < list.length(); i++) {
					node=list[i];
					if (String(node.@roles) != "") {
						var roles:Array=String(node.@roles).split(/,/);
						if (!globalUserAuthentication.hasRoles(roles)) {						
							delete(list[i]);
							i=i - 1;
						}
						if (node.node) {
							checkVMenuRolesXML(node);
						}

					}
				}
		}

		[Observer("change_globalView")]
		public function changeGlobalView(view:Number):void {
			globalCurrentView=view;		
		}
	}
	
}