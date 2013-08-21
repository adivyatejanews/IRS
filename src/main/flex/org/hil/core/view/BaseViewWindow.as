package org.hil.core.view
{
	import org.hil.core.components.DataGridColumn;	
	import org.hil.core.model.vo.UserAuthentication;
	import mx.containers.TitleWindow;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.formatters.DateFormatter;
	import mx.resources.ResourceManager;	
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;

	public class BaseViewWindow extends TitleWindow
	{
		[Bindable]
		public var tideContext:Context=Spring.getInstance().getSpringContext();

		[Bindable]
		[In]
		public var globalUserAuthentication:UserAuthentication;


		public function BaseViewWindow() {
			super();
		}

		public function formatDateField(item:Object, column:DataGridColumn):String {
			var dateFormat:DateFormatter=new DateFormatter();
			dateFormat.formatString=ResourceManager.getInstance().getString('InformationOfDatabase', 'date_format');
			if (column.dataField != null && item != null)
				return dateFormat.format(item[column.dataField]);
			return "";
		}
		
		public function formatDateTimeHour(item:Object, column:DataGridColumn):String {
			var dateFormat:DateFormatter=new DateFormatter();
			dateFormat.formatString="HH:NN";
			if (column.dataField != null && item != null)
				return dateFormat.format(item[column.dataField]);
			return "";
		}

	}
}