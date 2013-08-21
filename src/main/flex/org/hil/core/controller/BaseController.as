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

package org.hil.core.controller
{	
	import org.granite.tide.events.TideFaultEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	import mx.resources.ResourceManager;
	
	public class BaseController {
		public var tideContext:Context=Spring.getInstance().getSpringContext();			
		
		public function BaseController() {
			
		}
		
		public function errorHandler(event:TideFaultEvent,token:Object=null):void {
			var errorMsg:String = event.fault.toString();			
			tideContext.raiseEvent("mainApp_remoteServiceError",errorMsg);
		}
		
		public function errorHandlerSilent(event:TideFaultEvent,token:Object=null):void {
			var errorMsg:String = event.fault.toString();			
		}

	}
}