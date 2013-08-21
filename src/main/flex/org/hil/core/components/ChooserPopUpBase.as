package org.hil.core.components
{
	import org.hil.core.event.ChooserChangeEvent;
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;

	public class ChooserPopUpBase extends HBox
	{
		//public properties
		[Bindable]
		public var prompt:String;
		
		[Bindable]	
		public var labelField:String;
		
		public var matchType:String="anyPart";

		[Bindable]
		public var dataProvider:ArrayCollection=new ArrayCollection();

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
		private var _selectedItemId:Number;
		[Bindable]
		private var _selectedItem:Object;

		[Bindable]
		public var sortIndex:int=1;

		// components
		[Bindable]
		public var chooser:ChooserUTF8;

		private var isSorted:Boolean=false;

		public function ChooserPopUpBase() {			
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}

		private function creationCompleteHandler(event:FlexEvent):void {
			chooser.labelField=labelField;
			chooser.matchType=matchType;
		}

		public function get selectedItem():Object {
			_selectedItem=chooser.selectedItem;
			return _selectedItem;
		}

		[Bindable]
		public function set selectedItem(value:Object):void {
			_selectedItem=value;
			chooser.selectedItem=_selectedItem;
			chooser.invalidateDisplayList();
		}

		public function get selectedItemId():Number {
			return _selectedItemId;
		}

		[Bindable]
		public function set selectedItemId(value:Number):void {
			if (value > 0) {
				_selectedItemId=value;
				chooser.selectedItemId=value;
			} else {//clear chooser			
				chooser.text="";
				chooser.selectedItem=null;
				_selectedItem=null;
			}
		}


		public function handleChooserPopUp():void {
			var popUp:ChooserPopUpView=ChooserPopUpView(PopUpManager.createPopUp(this, ChooserPopUpView, true));
			// transparent titlebar
			popUp.setStyle("borderAlpha", 0.9);
			popUp.popupTitle=popupTitle;
			popUp.columnIDTitle=columnIDTitle;
			popUp.columnNameTitle=columnNameTitle;
			popUp.buttonLabel=buttonLabel;
			popUp.dataFieldID=dataFieldID;
			popUp.dataFieldName=dataFieldName;
			popUp.dataProvider=dataProvider;
			popUp.chooser=chooser;
			popUp.sortIndex=sortIndex;
			if (!isSorted) {
				isSorted=true;
				popUp.sortColumn();
			}
			PopUpManager.centerPopUp(popUp);
			popUp.focusDataGridItem();

		}

		public function handleChooserValue():void {
			this.dispatchEvent(new ChooserChangeEvent(ChooserChangeEvent.CHANGE));
		}

	}
}