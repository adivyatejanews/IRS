package org.hil.mainapp.view 
{	
	import flash.events.Event;	
	import mx.containers.Box;
	import mx.controls.Tree;
	import org.hil.core.model.vo.MenuVO;
	import org.granite.tide.events.TideUIEvent;
	import org.granite.tide.spring.Context;
	import org.granite.tide.spring.Spring;
	
	public class TreeNavigatorViewBase extends Box
	{
	    public var tideContext:Context = Spring.getInstance().getSpringContext();
	    [Bindable]
	    public var currentView:Number = 0;
	
	    [Bindable]
	    public var selectedNode:XML;
	
	    [Bindable]
	    [Embed(source="/assets/disclose_close.png")]
	    public var arrowSide:Class;
	
	    [Bindable]
	    [Embed(source="/assets/disclose_open.png")]
	    public var arrowDown:Class;
	
	    [Bindable]
	    [In]
	    public var treeNavDataXML:XML;
	
	    [Bindable]
	    [In]
	    public var globalCurrentView:Number=0;
	    
	    [Bindable]
	    public var myTreeNav:Tree;
	
	    public function creationCompleteHandler():void {
			
	    }
	
	    public function treeChanged(event:Event):void {
	        selectedNode = Tree(event.target).selectedItem as XML;
	        currentView = selectedNode.@position;
			if (currentView != -1) {
	            dispatchEvent(new TideUIEvent("change_globalView",currentView));
	        }
	    }
	    
	    // expand item when click
	    public function hanldeTreeExpandItem():void {
	    	if(myTreeNav.selectedItem && myTreeNav.dataDescriptor.isBranch(myTreeNav.selectedItem)) {
	    		myTreeNav.expandItem(myTreeNav.selectedItem, !myTreeNav.isItemOpen(myTreeNav.selectedItem));	
	    	}
	    }
	
		public function multiLanguage(item:XML):String {
			var label:String;		
			return resourceManager.getString('MainApp',item.@label);
		}
	
	}
}