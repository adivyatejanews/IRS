package org.hil.core.components
{
	import flash.events.Event;
	import flash.events.FocusEvent;
	import org.hil.core.util.DateMath;
	import mx.controls.Alert;
	import mx.controls.DateField;
	import mx.validators.DateValidator;
	
	import org.hil.core.validator.CustomDateValidator;

	[ResourceBundle("Validator")]
	public class DateFieldAutoCorrect extends mx.controls.DateField
	{
		public var required:Boolean=false;
		public var _selectedDate2:Date = null;
		//check if input Date is later than Now
		public var checkLimitDate:Boolean=false;

		public function DateFieldAutoCorrect()
		{
			super();
		}

		override public function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void
		{

			if (type == FocusEvent.FOCUS_OUT && !hasEventListener(type))
			{
				super.addEventListener(type, handleAutoConvertDateTime, useCapture, priority, useWeakReference);
			}

			super.addEventListener(type, listener, useCapture, priority, useWeakReference);
		}

		// correct date input
		public function handleAutoConvertDateTime(event:Event):void
		{
			var dateTimeString:String=event.currentTarget.text;
			if (dateTimeString.length == 8)
			{
				var dayString:String=dateTimeString;
				var monthString:String=dateTimeString;
				var yearString:String=dateTimeString;
				var day:String=dayString.substr(0, 2);
				var month:String=monthString.substr(2, 2);
				var year:String=yearString.substr(4, 4);
				event.currentTarget.text=null;
				event.currentTarget.text=day + "/" + month + "/" + year;
			}
			hanldeValidDateTime(event);

		}

		// valid date time
		public function hanldeValidDateTime(event:Event):void
		{
			var dateValidator:CustomDateValidator=new CustomDateValidator();
			dateValidator.source=event.currentTarget;
			dateValidator.property="text";
			// if required
			if (required)
			{
				dateValidator.required=true;
			}
			if (!required)
			{
				dateValidator.required=false;
			}

//			if (checkLimitDate)
			dateValidator.checkLimitDate=checkLimitDate;
			dateValidator.formatString = formatString;

			dateValidator.inputFormat=event.currentTarget.formatString;
			dateValidator.invalidCharError=resourceManager.getString('Validator', 'invalid_char_error');
			dateValidator.formatError=resourceManager.getString('Validator', 'date_format_error');
			dateValidator.wrongLengthError=resourceManager.getString('Validator', 'wrong_length_error');
			dateValidator.wrongDayError=resourceManager.getString('Validator', 'wrong_day_error');
			dateValidator.wrongMonthError=resourceManager.getString('Validator', 'wrong_month_error');
			dateValidator.wrongYearError=resourceManager.getString('Validator', 'wrong_year_error');
			dateValidator.requiredFieldError=resourceManager.getString('Validator', 'required_field_error');
		}
		
		public function get selectedDate2():Date
		{	
			_selectedDate2 = DateMath.addHours(selectedDate, 12);
			return _selectedDate2;
		}
	}
}