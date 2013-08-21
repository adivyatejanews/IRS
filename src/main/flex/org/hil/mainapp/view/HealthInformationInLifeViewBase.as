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

package org.hil.mainapp.view
{	
	import org.hil.core.model.vo.MenuVO;	
	import org.hil.core.view.BaseView;
	import org.hil.mainapp.view.components.RemoteServiceErrorView;
	import flash.system.ApplicationDomain;
	import mx.containers.Canvas;
	import mx.containers.ViewStack;
	import mx.controls.ToggleButtonBar;
	import mx.effects.Resize;
	import mx.events.FlexEvent;
	import mx.events.IndexChangedEvent;
	import mx.events.ModuleEvent;
	import mx.managers.PopUpManager;
	import mx.modules.IModuleInfo;
	import mx.modules.Module;
	import mx.modules.ModuleManager;
	import org.granite.tide.events.TideUIEvent;
	import mx.controls.Alert;
	[ResourceBundle("MainApp")]
	public class HealthInformationInLifeViewBase extends BaseView
	{
		private var moduleInfoList:Array=new Array();
		
		private var moduleNames:Array=[
			"org/hil/module/components/SystemUserManagementModule.swf", //0			
			"org/hil/module/components/ListOfRegionsModule.swf", //1					
			"org/hil/module/components/ListOfVaccinationsModule.swf", //2
			"org/hil/module/components/ChildrenInformationModule.swf", //3
			"org/hil/module/components/VaccinationDayModule.swf", //4
			"org/hil/module/components/ListOfChildrenDueModule.swf", //5
			"org/hil/module/components/ChildrenVaccinationReportModule.swf", //6			
			"org/hil/module/components/GeneralReportDataModule.swf", //7
			"org/hil/module/components/PersonalModule.swf", //8
			"org/hil/module/components/ChildrenVaccinatedInLocationReportModule.swf" //9
		];
		
		public var viewToggle:ToggleButtonBar;
		public var navStack:ViewStack;
		public var shrink:Resize;
		public var grow:Resize;
		public var appView:ViewStack;

		[Bindable]
		public var currentView:Number=0;

		[Bindable]
		[Embed(source="/assets/closeNav.png")]
		public var closeNav:Class;

		[Bindable]
		[Embed(source="/assets/openNav.png")]
		public var openNav:Class;

		[Bindable]
		public var isClosed:Boolean=false;

		[Bindable]
		public var lastView:Number;

		private var _moduleHealthInformationInLifeDomain:ApplicationDomain;
		public var healthInformationInLifeView:ViewStack;

		[Bindable]
		[In]
		public var globalCurrentView:Number=0;
		
		public function HealthInformationInLifeViewBase() {
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}

		private function creationCompleteHandler(event:FlexEvent):void {
			tideContext.healthInformationInLifeView=this;
			preloadCommonData();
		}

		public function healthInformationInLifeViewCreationComplete(event:FlexEvent):void {
			_moduleHealthInformationInLifeDomain=ApplicationDomain.currentDomain;
			moduleInfoList=new Array(moduleNames.length);			
			for (var i:int=0; i < moduleNames.length; i++) {
				var view:Canvas=new Canvas();
				view.percentWidth=100;
				view.percentHeight=100;
				healthInformationInLifeView.addChild(view);
			}

		}

		public function changeHealthInformationInLifeView(event:IndexChangedEvent):void {
			var index:int=event.newIndex;			
			if (index >= 0) {
				if (moduleInfoList[index] == null) {
					var _module:IModuleInfo=ModuleManager.getModule(moduleNames[index]);
					_module.addEventListener(ModuleEvent.READY, moduleEventHandler);
					_module.load(_moduleHealthInformationInLifeDomain);
					moduleInfoList[index]=_module;
				}				
			}
		}

		private function moduleEventHandler(e:ModuleEvent):void {
			var _module:Object=e.module.factory.create() as Module;
			healthInformationInLifeView.selectedChild.addChild(_module as Module);
		}

		[Observer("mainApp_remoteServiceError")]
		public function handleRemoteServiceError(errorMsg:String):void {
			var _popUp:RemoteServiceErrorView=RemoteServiceErrorView(PopUpManager.createPopUp(this, RemoteServiceErrorView, true));
			_popUp.errorMsg=errorMsg;
			PopUpManager.centerPopUp(_popUp);
		}		

		public function discloseNav():void {
			if (isClosed == false) {
				navStack.visible=false;
				lastView=navStack.selectedIndex;
				navStack.selectedIndex=1;
				shrink.end();
				shrink.play();
				isClosed=true;
			} else {
				navStack.visible=false;
				navStack.selectedIndex=lastView;
				grow.end();
				grow.play();
				isClosed=false;
			}
		}
		
	}
}