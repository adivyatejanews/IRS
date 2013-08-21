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

package org.hil.personal.view
{
	import com.hillelcoren.components.Chooser;	
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import org.hil.core.util.StringUtils;
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
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	
	[ResourceBundle("Personal")]	
	public class PersonalViewBase extends VBox
	{
		public var tideContext:Context = Spring.getInstance().getSpringContext();				
		[Bindable]
		public var textAccountName:TextInputUTF8;
		[Bindable]
		public var textCurrentPassword:TextInputUTF8;
		[Bindable]
		public var textNewPassword:TextInputUTF8;
		[Bindable]
		public var textRetypePassword:TextInputUTF8;			
		
		public var buttonSaveSystemUser:Button;		
						
        [Bindable] [In]
    	public var globalUserAuthentication:UserAuthentication = new UserAuthentication();		

		public function PersonalViewBase() {			
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			Alert.buttonWidth = 80;
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {
			tideContext.systemUserManagementView = this;
			
			textAccountName.enabled = true;
			textNewPassword.enabled = true;
			textRetypePassword.enabled = true;
			
			buttonSaveSystemUser.enabled = false;
						
		}			
		
		public function savePassword():void {
			
			if ((textNewPassword.text.length > 0 || textRetypePassword.text.length > 0)
				&& textNewPassword.text != textRetypePassword.text) {
				Alert.show(resourceManager.getString('Personal','alert.wrong_password'));
				return;
			}	
			
			dispatchEvent(new TideUIEvent("personal_savePassword", globalUserAuthentication.user.systemAccount, StringUtils.trim(textNewPassword.text)));
			
		}
		
		public function enterCurrentPassword():void {
			if (StringUtils.trim(textCurrentPassword.text).length == 0) {
				buttonSaveSystemUser.enabled = false;
			} else {
				buttonSaveSystemUser.enabled = true;
			}
		}
		
		public function savePasswordKeyboardEventHandler(event:KeyboardEvent):void {
			if (event.charCode == 13) {
				savePassword();
			}
		}
	}
}