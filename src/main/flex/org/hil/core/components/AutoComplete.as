////////////////////////////////////////////////////////////////////////////////
//
//  Copyright (C) 2003-2006 Adobe Macromedia Software LLC and its licensors.
//  All Rights Reserved. The following is Source Code and is subject to all
//  restrictions on such code as contained in the End User License Agreement
//  accompanying this product.
//
//  Modified by Jens Krause [www.websector.de] to avoid issues using Flex 3
//	@see: http://www.websector.de/blog/2008/04/30/quick-tip-avoid-issues-using-adobes-autocomplete-input-component-using-flex-3/
//
// 	This class is for demonstration purposes only. 
//	To use this solution extend the original Autocomplete.as described at the blog entry mentioned above, please.
//
////////////////////////////////////////////////////////////////////////////////

package org.hil.core.components
{
import flash.events.Event;
import flash.events.FocusEvent;
import flash.events.KeyboardEvent;
import flash.net.SharedObject;
import flash.ui.Keyboard;

import mx.collections.ListCollectionView;
import mx.controls.ComboBox;
import mx.core.UIComponent;


//--------------------------------------
//  Events
//--------------------------------------

/**
 *  Dispatched when the <code>filterFunction</code> property changes.
 *
 *  You can listen for this event and update the component
 *  when the <code>filterFunction</code> property changes.</p>
 * 
 *  @eventType flash.events.Event
 */
[Event(name="filterFunctionChange", type="flash.events.Event")]

/**
 *  Dispatched when the <code>typedText</code> property changes.
 *
 *  You can listen for this event and update the component
 *  when the <code>typedText</code> property changes.</p>
 * 
 *  @eventType flash.events.Event
 */
[Event(name="typedTextChange", type="flash.events.Event")]

//--------------------------------------
//  Excluded APIs
//--------------------------------------

[Exclude(name="editable", kind="property")]

/**
 *  The AutoComplete control is an enhanced 
 *  TextInput control which pops up a list of suggestions 
 *  based on characters entered by the user. These suggestions
 *  are to be provided by setting the <code>dataProvider
 *  </code> property of the control.
 *  @mxml
 *
 *  <p>The <code>&lt;fc:AutoComplete&gt;</code> tag inherits all the tag attributes
 *  of its superclass, and adds the following tag attributes:</p>
 *
 *  <pre>
 *  &lt;fc:AutoComplete
 *    <b>Properties</b>
 *    keepLocalHistory="false"
 *    lookAhead="false"
 *    typedText=""
 *    filterFunction="<i>Internal filter function</i>"
 *
 *    <b>Events</b>
 *    filterFunctionChange="<i>No default</i>"
 *    typedTextChange="<i>No default</i>"
 *  /&gt;
 *  </pre>
 *
 *  @includeExample ../../../../../../docs/com/adobe/flex/extras/controls/example/AutoCompleteCountriesData/AutoCompleteCountriesData.mxml
 *
 *  @see mx.controls.ComboBox
 *
 */

[ResourceBundle("Validator")]
public class AutoComplete extends ComboBox 
{

	//--------------------------------------------------------------------------
	//
	//  Constructor
    //
    //--------------------------------------------------------------------------

    /**
     *  Constructor.
     */
	public function AutoComplete()
	{
	    super();

	    //Make ComboBox look like a normal text field
	    editable = true;
	    if(keepLocalHistory)
		    addEventListener("focusOut",focusOutHandler);

	    setStyle("arrowButtonWidth",0);
		setStyle("fontWeight","normal");
		setStyle("cornerRadius",0);
		setStyle("paddingLeft",0);
		setStyle("paddingRight",0);
		rowCount = 7;
	}

	//--------------------------------------------------------------------------
	//
	//  Variables
	//
	//--------------------------------------------------------------------------

	/**
	 *  @private
	 */
	private var cursorPosition:Number=0;

	/**
	 *  @private
	 */
	private var prevIndex:Number = -1;

	/**
	 *  @private
	 */
	private var removeHighlight:Boolean = false;	

	/**
	 *  @private
	 */
	private var showDropdown:Boolean=false;

	/**
	 *  @private
	 */
	private var showingDropdown:Boolean=false;

	/**
	 *  @private
	 */
	private var tempCollection:Object;

	/**
	 *  @private
	 */
	private var usingLocalHistory:Boolean=false;
	
	/**
	 *  @private
	 */
	private var dropdownClosed:Boolean=true;

	//--------------------------------------------------------------------------
	//
	//  Overridden Properties
	//
	//--------------------------------------------------------------------------

	override public function addEventListener(
        type:String, listener:Function, useCapture:Boolean = false,
        priority:int = 0, useWeakReference:Boolean = false):void  {
		
        if (type == Event.CHANGE && !hasEventListener(type))
        {
        	super.addEventListener(type, correctLinux, useCapture, priority, useWeakReference);
        }
                  
        super.addEventListener(type, listener, useCapture, priority, useWeakReference);
    }
    
     // correct text
	public function correctLinux(e:Event):void
	{
		var str:String;
		//Alert.show("current target: " + e.currentTarget);
		str=e.currentTarget.text;
		
		// a á à ả ã ạ	
		str=str.replace("Ã¡", "á");
		str=str.replace("Ã ", "à");
		str=str.replace("áº£", "ả");
		str=str.replace("Ã£", "ã");
		str=str.replace("áº¡", "ạ");
		   
		// ă ắ ằ ẳ ẵ ặ      
		str=str.replace("Äƒ", "ă");
		str=str.replace("áº¯", "ắ"); 
		str=str.replace("áº±", "ằ");
		str=str.replace("áº³", "ẳ");
		str=str.replace("áºµ", "ẵ");
		str=str.replace("áº·", "ặ");

		// â ấ ầ ẩ ẫ ậ       ­
		str=str.replace("Ã¢", "â");
		str=str.replace("áº¥", "ấ");
		str=str.replace("áº§", "ầ");
		str=str.replace("áº©", "ẩ");
		str=str.replace("áº«", "ẫ");
		str=str.replace("áº­", "ậ");
		
		// e é è ẻ ẽ ẹ      
		str=str.replace("Ã©", "é");
		str=str.replace("Ã¨", "è");
		str=str.replace("áº»", "ẻ");
		str=str.replace("áº½", "ẽ");
		str=str.replace("áº¹", "ẹ");
		
		// ê ế ề ể ễ ệ       
		str=str.replace("Ãª", "ê");
		str=str.replace("áº¿", "ế");
		str=str.replace("á»", "ề");
		str=str.replace("á»ƒ", "ể");
		str=str.replace("á»…", "ễ");
		str=str.replace("á»‡", "ệ");
		
		// i í ì ỉ ĩ ị  ­    
		str=str.replace("Ã­", "í");
		str=str.replace("Ã¬", "ì");
		str=str.replace("á»‰", "ỉ");
		str=str.replace("Ä©", "ĩ");
		str=str.replace("á»‹", "ị");
		
		// o ó ò ỏ õ ọ      
		str=str.replace("Ã³", "ó");
		str=str.replace("Ã²", "ò");
		str=str.replace("á»", "ỏ");
		str=str.replace("Ãµ", "õ");
		str=str.replace("á»", "ọ");
		
		// ô ố ồ ổ ỗ ộ       
		str=str.replace("Ã´", "ô");
		str=str.replace("á»‘", "ố");
		str=str.replace("á»“", "ồ");
		str=str.replace("á»•", "ổ");
		str=str.replace("á»—", "ỗ");
		str=str.replace("á»™", "ộ");
		
		// ơ ớ ờ ở ỡ ợ       
		str=str.replace("Æ¡", "ơ");
		str=str.replace("á»›", "ớ");
		str=str.replace("á»", "ờ");
		str=str.replace("á»Ÿ", "ở");
		str=str.replace("á»¡", "ỡ");
		str=str.replace("á»£", "ợ");
		
		// u ú ù ủ ũ ụ      
		str=str.replace("Ãº", "ú");
		str=str.replace("Ã¹", "ù");
		str=str.replace("á»§", "ủ");
		str=str.replace("Å©", "ũ");
		str=str.replace("á»¥", "ụ");

		// ư ứ ừ ử ữ ự      ­  
		str=str.replace("Æ°", "ư");
		str=str.replace("á»©", "ứ");
		str=str.replace("á»«", "ừ");
		str=str.replace("á»­", "ử");
		str=str.replace("á»¯", "ữ");
		str=str.replace("á»±", "ự");
		
		// y ý ỳ ỷ ỹ ỵ      
		str=str.replace("Ã½", "ý");
		str=str.replace("á»³", "ỳ");
		str=str.replace("á»·", "ỷ");
		str=str.replace("á»¹", "ỹ");
		str=str.replace("á»µ", "ỵ");

		// đ
		str=str.replace("Ä‘", "đ");
		
		// A Á À Ả Ã Ạ       
		str=str.replace("Ã", "Á");
		str=str.replace("Ã€", "À");
		str=str.replace("áº¢", "Ả");
		str=str.replace("Ãƒ", "Ã");
		str=str.replace("áº ", "Ạ");
		
		// Ă Ắ Ằ Ẳ Ẵ Ặ       
		str=str.replace("Ä‚", "Ă");
		str=str.replace("áº®", "Ắ");
		str=str.replace("áº°", "Ằ");
		str=str.replace("áº²", "Ẳ");
		str=str.replace("áº´", "Ẵ");
		str=str.replace("áº¶", "Ặ");

		// Â Ấ Ầ Ẩ Ẫ Ậ      
		str=str.replace("Ã‚", "Â");
		str=str.replace("áº¤", "Ấ");
		str=str.replace("áº¦", "Ầ");
		str=str.replace("áº¨", "Ẩ");
		str=str.replace("áºª", "Ẫ");
		str=str.replace("áº¬", "Ậ");

		// E É È Ẻ Ẽ Ẹ       
		str=str.replace("Ã‰", "É");
		str=str.replace("Ãˆ", "È");
		str=str.replace("áºº", "Ẻ");
		str=str.replace("áº¼", "Ẽ");
		str=str.replace("áº¸", "Ẹ");

		// Ê Ế Ề Ể Ễ Ệ       
		str=str.replace("ÃŠ", "Ê");
		str=str.replace("áº¾", "Ế");
		str=str.replace("á»€", "Ề");
		str=str.replace("á»‚", "Ể");
		str=str.replace("á»„", "Ễ");
		str=str.replace("á»†", "Ệ");

		// I Í Ì Ỉ Ĩ Ị       
		str=str.replace("Ã", "Í");
		str=str.replace("ÃŒ", "Ì");
		str=str.replace("á»ˆ", "Ỉ");
		str=str.replace("Ä¨", "Ĩ");
		str=str.replace("á»Š", "Ị");

		// O Ó Ò Ỏ Õ Ọ       
		str=str.replace("Ã“", "Ó");
		str=str.replace("Ã’", "Ò");
		str=str.replace("á»Ž", "Ỏ");
		str=str.replace("Ã•", "Õ");
		str=str.replace("á»Œ", "Ọ");

		// Ô Ố Ồ Ổ Ỗ Ộ          
		str=str.replace("Ã”", "Ô");
		str=str.replace("á»", "Ố");
		str=str.replace("á»’", "Ồ");
		str=str.replace("á»”", "Ổ");
		str=str.replace("á»–", "Ỗ");
		str=str.replace("á»˜", "Ộ");

		// Ơ Ớ Ờ Ở Ỡ Ợ         
		str=str.replace("Æ ", "Ơ");
		str=str.replace("á»š", "Ớ");
		str=str.replace("á»œ", "Ờ");
		str=str.replace("á»ž", "Ở");
		str=str.replace("á» ", "Ỡ");
		str=str.replace("á»¢", "Ợ");

		// U Ú Ù Ủ Ũ Ụ       
		str=str.replace("Ãš", "Ú");
		str=str.replace("Ã™", "Ù");
		str=str.replace("á»¦", "Ủ");
		str=str.replace("Å¨", "Ũ");
		str=str.replace("á»¤", "Ụ");

		// Ư Ứ Ừ Ử Ữ Ự        
		str=str.replace("Æ¯", "Ư");
		str=str.replace("á»¨", "Ứ");
		str=str.replace("á»ª", "Ừ");
		str=str.replace("á»¬", "Ử");
		str=str.replace("á»®", "Ữ");
		str=str.replace("á»°", "Ự");

		// Y Ý Ỳ Ỷ Ỹ Ỵ      
		str=str.replace("Ã", "Ý");
		str=str.replace("á»²", "Ỳ");
		str=str.replace("á»¶", "Ỷ");
		str=str.replace("á»¸", "Ỹ");
		str=str.replace("á»´", "Ỵ");

		// Ð
		str=str.replace("Ä", "Đ");
	
	  	e.currentTarget.text=str;
	}
	
	
 	//----------------------------------
	//  editable
	//----------------------------------
	/**
	 *  @private
	 */
 	override public function set editable(value:Boolean):void
	{
	    //This is done to prevent user from resetting the value to false
	    super.editable = true;
	}
	/**
	 *  @private
	 */
 	override public function set dataProvider(value:Object):void
	{
		super.dataProvider = value;
		if(!usingLocalHistory)
			tempCollection = value;
	}

	//----------------------------------
	//  labelField
	//----------------------------------
	/**
	 *  @private
	 */
 	override public function set labelField(value:String):void
	{
		super.labelField = value;
		
		invalidateProperties();
		invalidateDisplayList();
	}


	//--------------------------------------------------------------------------
	//
	//  Properties
	//
	//--------------------------------------------------------------------------


	//----------------------------------
	//  filterFunction
	//----------------------------------

	/**
	 *  @private
	 *  Storage for the filterFunction property.
	 */
	private var _filterFunction:Function = defaultFilterFunction;

	/**
	 *  @private
	 */
	private var filterFunctionChanged:Boolean = true;

	[Bindable("filterFunctionChange")]
	[Inspectable(category="General")]

	/**
	 *  A function that is used to select items that match the
	 *  function's criteria. 
	 *  A filterFunction is expected to have the following signature:
	 *
	 *  <pre>f(item:~~, text:String):Boolean</pre>
	 *
	 *  where the return value is <code>true</code> if the specified item
	 *  should displayed as a suggestion. 
	 *  Whenever there is a change in text in the AutoComplete control, this 
	 *  filterFunction is run on each item in the <code>dataProvider</code>.
	 *  
 	 *  <p>The default implementation for filterFunction works as follows:<br>
 	 *  If "AB" has been typed, it will display all the items matching 
	 *  "AB~~" (ABaa, ABcc, abAc etc.).</p>
	 *
	 *  <p>An example usage of a customized filterFunction is when text typed
	 *  is a regular expression and we want to display all the
	 *  items which come in the set.</p>
	 *
	 *  @example
	 *  <pre>
	 *  public function myFilterFunction(item:~~, text:String):Boolean
	 *  {
	 *     public var regExp:RegExp = new RegExp(text,"");
	 *     return regExp.test(item);
	 *  }
	 *  </pre>
	 *
	 */
	public function get filterFunction():Function
	{
		return _filterFunction;
	}

	/**
	 *  @private
	 */
	public function set filterFunction(value:Function):void
	{
		//An empty filterFunction is allowed but not a null filterFunction
		if(value!=null)
		{
			_filterFunction = value;
			filterFunctionChanged = true;

			invalidateProperties();
			invalidateDisplayList();
	
			dispatchEvent(new Event("filterFunctionChange"));
		}
		else
			_filterFunction = defaultFilterFunction;
	}

	//----------------------------------
	//  filterFunction
	//----------------------------------

	/**
	 *  @private
	 *  Storage for the keepLocalHistory property.
	 */
	private var _keepLocalHistory:Boolean = false;

	/**
	 *  @private
	 */
	private var keepLocalHistoryChanged:Boolean = true;

	[Bindable("keepLocalHistoryChange")]
	[Inspectable(category="General")]

	/**
	 *  When true, this causes the control to keep track of the
	 *  entries that are typed into the control, and saves the
	 *  history as a local shared object. When true, the
	 *  completionFunction and dataProvider are ignored.
	 *
	 *  @default "false"
	 */
	public function get keepLocalHistory():Boolean
	{
		return _keepLocalHistory;
	}

	/**
	 *  @private
	 */
	public function set keepLocalHistory(value:Boolean):void
	{
		_keepLocalHistory = value;
	}

	//----------------------------------
	//  lookAhead
	//----------------------------------

	/**
	 *  @private
	 *  Storage for the lookAhead property.
	 */
	private var _lookAhead:Boolean=false;

	/**
	 *  @private
	 */
	private var lookAheadChanged:Boolean;

	[Bindable("lookAheadChange")]
	[Inspectable(category="Data")]

	/**
	 *  lookAhead decides whether to auto complete the text in the text field
	 *  with the first item in the drop down list or not. 
	 *
	 *  @default "false"
	 */
	public function get lookAhead():Boolean
	{
		return _lookAhead;
	}

	/**
	 *  @private
	 */
	public function set lookAhead(value:Boolean):void
	{
		_lookAhead = value;
		lookAheadChanged = true;
	}

	//----------------------------------
	//  typedText
	//----------------------------------

	/**
	 *  @private
	 *  Storage for the typedText property.
	 */
	private var _typedText:String="";
	/**
	 *  @private
	 */
	private var typedTextChanged:Boolean;

	[Bindable("typedTextChange")]
	[Inspectable(category="Data")]

	/**
	 *  A String to keep track of the text changed as 
	 *  a result of user interaction.
	 */
	public function get typedText():String
	{
	    return _typedText;
	}

	/**
	 *  @private
	 */
	public function set typedText(input:String):void
	{
	    _typedText = input;
	    typedTextChanged = true;
			
	    invalidateProperties();
		invalidateDisplayList();
		dispatchEvent(new Event("typedTextChange"));
	}

    //--------------------------------------------------------------------------
    //
    //  Overridden methods
    //
    //--------------------------------------------------------------------------

	/**
	 *  @private
	 */
	override protected function commitProperties():void
	{
	    super.commitProperties();
		
  	    if(!dropdown)
			selectedIndex=-1;
  			
	    if(dropdown)
		{
		    if(typedTextChanged)
		    {
			    cursorPosition = textInput.selectionBeginIndex;

				updateDataProvider();

			    //In case there are no suggestions there is no need to show the dropdown
  			    if(collection.length==0 || typedText==""|| typedText==null)
  			    {
  			    	dropdownClosed=true;
			    	showDropdown=false;
  			    }
				else
				{
					showDropdown = true;
					selectedIndex = 0;
 		    	}
 		    }
		}
	}
		
	/**
	 *  @private
	 */
	override protected function focusOutHandler(event:FocusEvent):void
	{
		super.focusOutHandler(event)
 		if(keepLocalHistory && dataProvider.length==0)
 			addToLocalHistory();
	}
	/**
	 *  @private
	 */
	override public function getStyle(styleProp:String):*
	{
		if(styleProp != "openDuration")
			return super.getStyle(styleProp);
		else
		{
	     	if(dropdownClosed)
	     		return super.getStyle(styleProp);
     		else
     			return 0;
		}
	}
	/**
	 *  @private
	 */
	override protected function keyDownHandler(event:KeyboardEvent):void
	{
	    super.keyDownHandler(event);

	    if(!event.ctrlKey)
		{
		    //An UP "keydown" event on the top-most item in the drop-down
		    //or an ESCAPE "keydown" event should change the text in text
		    // field to original text
		    if(event.keyCode == Keyboard.UP && prevIndex==0)
			{
	 		    textInput.text = _typedText;
			    textInput.setSelection(textInput.text.length, textInput.text.length);
			    selectedIndex = -1; 
			}
		    else if(event.keyCode==Keyboard.ESCAPE && showingDropdown)
			{
	 		    textInput.text = _typedText;
			    textInput.setSelection(textInput.text.length, textInput.text.length);
			    showingDropdown = false;
			    dropdownClosed=true;
			}
			else if(event.keyCode == Keyboard.ENTER)
			{
 				if(keepLocalHistory && dataProvider.length==0)
					addToLocalHistory();
					
				
		
				/*
				textInput.text = selectedLabel;
				textInput.setSelection(cursorPosition, textInput.text.length);
				textInput.setSelection(textInput.text.length,_typedText.length);
				*/
				
 			}
			else if(lookAhead && event.keyCode ==  Keyboard.BACKSPACE 
			|| event.keyCode == Keyboard.DELETE)
			    removeHighlight = true;
	 	}
	 	else
		    if(event.ctrlKey && event.keyCode == Keyboard.UP)
		    	dropdownClosed=true;
	 	
 	    prevIndex = selectedIndex;
	}
	
	/**
	 *  @private
	 */
	override protected function measure():void
	{
	    super.measure();
	    measuredWidth = mx.core.UIComponent.DEFAULT_MEASURED_WIDTH;
	}

	/**
	 *  @private
	 */
	override protected function updateDisplayList(unscaledWidth:Number, 
						      unscaledHeight:Number):void
	{
		
	    super.updateDisplayList(unscaledWidth, unscaledHeight);
		
		//An UP "keydown" event on the top-most item in the drop 
		//down list otherwise changes the text in the text field to ""
  	    if(selectedIndex == -1)
	    	textInput.text = typedText;

	    if(dropdown)
		{
		    if(typedTextChanged)
			{
			    //This is needed because a call to super.updateDisplayList() set the text
			    // in the textInput to "" and thus the value 
			    //typed by the user losts
  			    if(lookAhead && showDropdown && typedText!="" && !removeHighlight)
				{
					var label:String = itemToLabel(collection[0]);
					var index:Number =  label.toLowerCase().indexOf(_typedText.toLowerCase());
					if(index==0)
					{
					    textInput.text = _typedText+label.substr(_typedText.length);
					    textInput.setSelection(textInput.text.length,_typedText.length);
					}
					else
					{
					    textInput.text = _typedText;
					    textInput.setSelection(cursorPosition, cursorPosition);
					    removeHighlight = false;
					}
						
				}
			    else
				{
				    textInput.text = _typedText;
				    textInput.setSelection(cursorPosition, cursorPosition);
				    removeHighlight = false;
				}
			    
			    typedTextChanged= false;
			}
		    else if(typedText)
		    	//Sets the selection when user navigates the suggestion list through
		    	//arrows keys.
				textInput.setSelection(_typedText.length,textInput.text.length);
		}
 	    if(showDropdown && !dropdown.visible)
 	    {
 	    	//This is needed to control the open duration of the dropdown
 	    	super.open();
	    	showDropdown = false;
 	    	showingDropdown = true;

 	    	if(dropdownClosed)
	 	    	dropdownClosed=false;
 	    }
	}
	

	/**
	 *  @private
	 */
	override protected function textInput_changeHandler(event:Event):void
	{
	    super.textInput_changeHandler(event);
	    //Stores the text typed by the user in a variable
	    typedText=text;
	}
	


	//--------------------------------------------------------------------------
	//
	//  Methods
	//
	//--------------------------------------------------------------------------
				
	/**
	 *  @private
	 *  If keepLocalHistory is enabled, stores the text typed 
	 * 	by the user in the local history on the client machine
	 */
	private function addToLocalHistory():void
	{
        if (id != null && id != "" && text != null && text != "")
        {
            var so:SharedObject = SharedObject.getLocal("AutoCompleteData");

            var savedData : Array = so.data.suggestions;
            //No shared object has been created so far
            if (savedData == null)
                savedData = new Array();

             var i:Number=0;
             var flag:Boolean=false;
             //Check if this entry is there in the previously saved shared object data
             for(i=0;i<savedData.length;i++)
				if(savedData[i]==text)
				{
					flag=true;
					break;
				}
             if(!flag)
             {
             	//Also ensure it is not there in the dataProvider
	             for(i=0;i<collection.length;i++)
	             	if(defaultFilterFunction(itemToLabel(ListCollectionView(collection).getItemAt(i)),text))
					{
						flag=true;
						break;
					}
             }
     		if(!flag)
     			savedData.push(text);

           so.data.suggestions = savedData;
           //write the shared object in the .sol file
	       so.flush();
        }
	}	
	/**
	 *  @private
	 */
	private function defaultFilterFunction(element:*, text:String):Boolean 
	{
	    var label:String = itemToLabel(element);
	    return (label.toLowerCase().substring(0,text.length) == text.toLowerCase());
	}
	/**
	 *  @private
	 */

 	private function templateFilterFunction(element:*):Boolean 
	{
		var flag:Boolean=false;
		if(filterFunction!=null)
			flag=filterFunction(element,typedText);
		return flag;
	}

	/**
	 *  @private
	 *  Updates the dataProvider used for showing suggestions
	 */
	private function updateDataProvider():void
	{
		dataProvider = tempCollection;
 		collection.filterFunction = templateFilterFunction;
		collection.refresh();

	    //In case there are no suggestions, check there is something in the localHistory
  	    if(collection.length==0 && keepLocalHistory)
  	    {
            var so:SharedObject = SharedObject.getLocal("AutoCompleteData");
			usingLocalHistory = true;
            dataProvider = so.data.suggestions;
            usingLocalHistory = false;
	 		collection.filterFunction = templateFilterFunction;
			collection.refresh();
  	    }
  	}

	//--------------------------------------------------------------------------
	//
	//  modified source
	//
	//--------------------------------------------------------------------------

	/**
	 *  Closes the combox and set the selection which is lost using Flex 3
	 * 
	 *  @event	Event	Trigger event to close the combobox
	 */  	
  	override public function close(event:Event = null):void
    {
    	super.close(event);
    	
    	if(selectedIndex == 0)
    	{
    		// set the text using the selected label
	    	textInput.text = selectedLabel;
	    	// select the text from typed text position to texts length
			textInput.setSelection(cursorPosition, textInput.text.length);     	
    	}
		      
    }	
	
  }

}