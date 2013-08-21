package org.hil.core.components
{
	import com.hillelcoren.components.Chooser;
	
	import flash.events.Event;
	
	import mx.controls.dataGridClasses.DataGridListData;
	
	
	[ResourceBundle("Validator")]
	public class ChooserUTF8 extends Chooser
	{	
//		public var tideContext:Context = Spring.getInstance().getSpringContext();
//		
//		[Out]
//		public var dataType:String = new String();
//		
//		[Out]
//		public var dataField:String = new String();
//		
//		[Bindable] [In]
//		public var dataId:Number = 0;
		
		public function ChooserUTF8()
		{
			super();
//			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}
		
//		private function creationCompleteHandler(event:FlexEvent):void
//		{
//			tideContext.chooserUTF8 = this;
//		}
		
		override public function addEventListener(
            type:String, listener:Function, useCapture:Boolean = false,
            priority:int = 0, useWeakReference:Boolean = false):void  { 
            if (type == "textChange" && !hasEventListener(type))
            {
            	super.addEventListener(type, correctLinux, useCapture, priority, useWeakReference);
            }
//            if (type == KeyboardEvent.KEY_DOWN )
//            {
//            	super.addEventListener(type, shortCut, useCapture, priority, useWeakReference);
//            }     

            super.addEventListener(type, listener, useCapture, priority, useWeakReference);
        }
        
//        // short cut
//		public function shortCut(kv:KeyboardEvent):void
//		{
//			// giu phim Ctrl lua chon bang chuyen tab tren ban phim
//			if (kv.keyCode == 17)
//			{
//				if(dataType.length != 0)
//				{
//            		createPopUp(kv);			
//				}
//			}
//		}
//		
//		// create new popup
//		public function createPopUp(event:KeyboardEvent):void
//		{
//			// tao popup
//            var chooserUTF8PopUp:ChooserUTF8PopUpView = ChooserUTF8PopUpView(PopUpManager.createPopUp(this, ChooserUTF8PopUpView, false));
//            // transparent titlebar
//            chooserUTF8PopUp.setStyle("borderAlpha", 0.9);
//            // center
//            PopUpManager.centerPopUp(chooserUTF8PopUp); 
//		}
//		
//		public function setDataId(event:Event):void
//		{
//			event.currentTarget.currentTarget = dataId;
//		}
			
		 // correct text
		public function correctLinux(e:Event):void
		{
			var str:String;
			//Alert.show("current target: " + e.currentTarget);
			str=e.currentTarget.combo.text;
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
		
		  	e.currentTarget.combo.text=str;
		  	
		}

	}
}