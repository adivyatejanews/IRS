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

package org.hil.core.view
{
	import org.hil.core.components.AdvanceDatagridColumn;
	import org.hil.core.components.DataGridColumn;
	import org.hil.core.components.RowColorDataGrid;
	import org.hil.core.model.District;
	import org.hil.core.model.Province;	
	import org.hil.core.model.vo.UserAuthentication;	
	import org.hil.core.model.vo.CommonDataVO;
	import mx.containers.HBox;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.formatters.DateFormatter;
	import mx.resources.ResourceManager;	
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;

	public class BaseView extends HBox
	{

		[Bindable]
		public var tideContext:Context=Spring.getInstance().getSpringContext();
		
		[Bindable]
		[In]
		public var commonDataVO:CommonDataVO;

		[Bindable]
		[In]
		public var globalUserAuthentication:UserAuthentication=new UserAuthentication();

		public function BaseView() {
			super();
		}
		
		public function preloadCommonData():void {
			getAllProvinces();
		}
		
		public function getAllSystemRoles():void {
			dispatchEvent(new TideUIEvent("commondata_getAllSystemRoles"));
		}
		
		public function getAllProvinces():void {
			dispatchEvent(new TideUIEvent("commondata_getAllProvinces"));
		}

		public function formatDateShort(date:Date):String {
			var dateFormat:DateFormatter=new DateFormatter();
			dateFormat.formatString="DD/MM";
			return dateFormat.format(date);
		}	

		public function formatDateTimeHour(item:Object, column:DataGridColumn):String
		{
			var dateFormat:DateFormatter=new DateFormatter();
			dateFormat.formatString="HH:NN";
			if (column.dataField != null && item != null)
				return dateFormat.format(item[column.dataField]);
			return "";
		}

	}
}