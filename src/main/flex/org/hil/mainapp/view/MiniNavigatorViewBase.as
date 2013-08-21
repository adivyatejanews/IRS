package org.hil.mainapp.view
{
import flash.events.Event;
import mx.containers.Box;
	import mx.controls.List;
	
	public class MiniNavigatorViewBase extends Box
	{
		public var myListNav:List;
		
		[Bindable]
		public var views:Array = [
		];
	
		[Bindable]
		public var currentView:Number = 0;
		
        public function changeView():void {
       		currentView = myListNav.selectedIndex; 
       		this.dispatchEvent( new Event("change") );
    	}
	}
}