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
	import org.hil.core.model.vo.UserAuthentication;
	import org.hil.mainapp.controller.AccessDeniedExceptionHandler;
	import org.hil.mainapp.controller.InvalidCredentialsExceptionHandler;
	import org.hil.mainapp.controller.NotLoggedInExceptionHandler;	
	import mx.controls.Alert;
	import flash.system.ApplicationDomain;
	import org.hil.core.controller.CommonDataCtl;
	import mx.containers.Canvas;
	import mx.containers.ViewStack;
	import mx.core.Application;
	import mx.effects.Resize;
	import mx.events.FlexEvent;
	import mx.events.ModuleEvent;
	import mx.modules.IModuleInfo;
	import mx.modules.Module;
	import mx.modules.ModuleManager;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Identity;
	import org.granite.tide.spring.Spring;
	import org.hil.core.model.vo.MenuVO;
	
	[ResourceBundle("MainApp")]
	public class MainAppBase extends Application
	{
		public var tideContext:Context=Spring.getInstance().getSpringContext();
		Spring.getInstance().addComponents([CommonDataCtl]);
		Spring.getInstance().addExceptionHandler(NotLoggedInExceptionHandler);
		Spring.getInstance().addExceptionHandler(AccessDeniedExceptionHandler);
		Spring.getInstance().addExceptionHandler(InvalidCredentialsExceptionHandler);

		public var viewStackLogin:Canvas;

		public var viewStackHealthInformationInLife:Canvas;

		private var loginModuleInfo:IModuleInfo;

		private var healthInformationSystemModuleInfo:IModuleInfo;

		public var shrink:Resize;

		public var grow:Resize;

		private var _moduleAppDomain:ApplicationDomain;

		[Bindable]
		[In]
		public var currentViewSelector:Number=0;

		[Bindable]
		[In]
		public var globalCurrentView:Number=0;

		[Bindable]
		public var isClosed:Boolean=false;

		[Bindable]
		public var lastView:Number;

		[Bindable]
		[In]
		public var identity:Identity;

		[Bindable]
		[In]
		public var globalUserAuthentication:UserAuthentication;

		public function MainAppBase() {
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}

		private function creationCompleteHandler(event:FlexEvent):void {
			_moduleAppDomain=ApplicationDomain.currentDomain;
			resourceManager.localeChain=['vi_VN'];
		}

		public function loginViewCreationCompleteHandler(event:FlexEvent):void {
			loginModuleInfo=ModuleManager.getModule("org/hil/module/components/LoginModule.swf");
			loginModuleInfo.addEventListener(ModuleEvent.READY, loginModEventHandler);
			loginModuleInfo.load(_moduleAppDomain);
		}

		public function healthInformationInLifeViewCreationCompleteHandler(event:FlexEvent):void {
			healthInformationSystemModuleInfo=ModuleManager.getModule("org/hil/module/components/HealthInformationInLifeModule.swf");
			healthInformationSystemModuleInfo.addEventListener(ModuleEvent.READY, healthInformationSystemModEventHandler);
			healthInformationSystemModuleInfo.load(_moduleAppDomain);
		}

		public function changeLocale(locale:String):void {
			resourceManager.localeChain=[locale];
		}

		private function loginModEventHandler(e:ModuleEvent):void {
			var _module:Object =loginModuleInfo.factory.create();
			_module.addModule(_moduleAppDomain);
			viewStackLogin.addChild(_module as Module);
		}

		private function healthInformationSystemModEventHandler(e:ModuleEvent):void {			
			var _module:Object =healthInformationSystemModuleInfo.factory.create();
			_module.addModule(_moduleAppDomain);
			viewStackHealthInformationInLife.addChild(_module as Module);
		}
		
		public function doLogout():void {
			navigateToURL(new URLRequest("javascript:location.reload(true)"), "_self");			
			var urlRequest:URLRequest = new URLRequest(Application.application.url);			
			navigateToURL(urlRequest,"_self");
		}
		
		public function gotoHelpInfor():void {
			var u:URLRequest=new URLRequest("http://nepi.vn/help/index.htm");
			navigateToURL(u, "_blank");
		}
	}
}