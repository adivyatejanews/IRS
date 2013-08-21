package org.hil.core.components
{
	import flash.events.KeyboardEvent;	
	import mx.collections.ArrayCollection;
	import mx.containers.TitleWindow;
	import mx.events.DataGridEvent;
	import mx.events.FlexEvent;

	public class ChooserPopUpViewBase extends TitleWindow
	{
		//public properties
		[Bindable]
		public var popupTitle:String;

		[Bindable]
		public var columnIDTitle:String;

		[Bindable]
		public var columnNameTitle:String;

		[Bindable]
		public var dataFieldID:String="id";

		[Bindable]
		public var dataFieldName:String;

		[Bindable]
		public var buttonLabel:String;

		[Bindable]
		public var dataProvider:ArrayCollection=new ArrayCollection();

		[Bindable]
		public var chooser:ChooserUTF8;
		
		[Bindable]
		public var sortIndex:int=1;

		[Bindable]
		public var idDataGrid:DataGrid;

		public function ChooserPopUpViewBase() {
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}

		private function creationCompleteHandler(event:FlexEvent):void	{
			focusManager.getFocus();
			focusManager.setFocus(idDataGrid);
		}

		public function handleSelectItem():void {
			if (idDataGrid.selectedItem != null)
				chooser.selectedItem=idDataGrid.selectedItem;
		}

		public function handleSelectItemKeyboard(event:KeyboardEvent):void	{
			if (event.charCode == 13) {
				handleSelectItem();
			}
		}

		public function sortColumn():void {
			idDataGrid.dispatchEvent(new DataGridEvent(DataGridEvent.HEADER_RELEASE,false, true, sortIndex, null, 0, null, null, 0));
		}

		public function focusDataGridItem():void {
			idDataGrid.validateNow();
			idDataGrid.scrollToIndex(idDataGrid.selectedIndex);
		}

	}
}