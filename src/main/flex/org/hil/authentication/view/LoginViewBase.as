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

package org.hil.authentication.view
{
	import org.hil.core.util.MD5;	
	import flash.events.KeyboardEvent;	
	import mx.containers.HBox;
	import mx.containers.Panel;
	import mx.controls.Button;
	import mx.controls.Label;
	import mx.controls.TextInput;
	import mx.events.FlexEvent;	
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Identity;
	import org.granite.tide.spring.Spring;

	[ResourceBundle("MainApp")]
    public class LoginViewBase extends HBox {
		public var txtUserName:TextInput;
		public var txtPassword:TextInput;
		public var txtMessage:Label;
		public var btLogin:Button;

		[Bindable]
		[In]
		public var loginMessage:String="";

		[Bindable]
		[In]
		public var identity:Identity;

		public var tideContext:Context=Spring.getInstance().getSpringContext();

		public function LoginViewBase():void {
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}

		private function creationCompleteHandler(event:FlexEvent):void {

		}

		public function handleLoginKeyboard(e:KeyboardEvent):void {
			if (e.charCode == 13) {				
				handleLoginMouse();
			}
		}

		public function handleLoginMouse():void {
			dispatchEvent(new TideUIEvent("authentication_doLogin", txtUserName.text, MD5.hash(txtPassword.text.toLowerCase())));
		}
	}
}