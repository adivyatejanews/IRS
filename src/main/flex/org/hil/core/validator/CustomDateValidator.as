package org.hil.core.validator
{
	import mx.validators.DateValidator;
	import mx.validators.ValidationResult;
	import mx.controls.DateField;

	[ResourceBundle("Validator")]
	public class CustomDateValidator extends DateValidator
	{

		//check if input Date is later than Now
		public var checkLimitDate:Boolean=false;
		public var formatString:String;

		public function CustomDateValidator()
		{
			//TODO: implement function
			super();
		}

		override protected function doValidation(value:Object):Array
		{

			// create an array to return.
			var validatorResults:Array=new Array();
			// Call base class doValidation().
			validatorResults=super.doValidation(value);

			// Return if there are errors.

			if (validatorResults.length > 0)
				return validatorResults;

			if (String(value).length == 0)
				return validatorResults;
			if (checkLimitDate)
			{
				var myDate:Date=DateField.stringToDate(String(value), formatString);
				var currentDate:Date=new Date();
				if (myDate > currentDate)
				{
					validatorResults.push(new ValidationResult(true, null, "", resourceManager.getString('Validator', 'loi_ngay_lon')));

				}
			}

			return validatorResults;



		}






	}
}