package org.hil.mainapp.view
{
	import org.hil.core.view.BaseViewWindow;

	[ResourceBundle("MainApp")]
	public class RemoteServiceErrorViewBase extends BaseViewWindow
	{
		[Bindable]
		public var errorMsg:String;
		
		public function RemoteServiceErrorViewBase() {
			super();
		}
		
	}
}