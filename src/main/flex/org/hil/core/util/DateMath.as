package org.hil.core.util
{

	public class DateMath
	{

		public function DateMath()
		{
			//TODO: implement function
		}

		public static function addWeeks(date:Date, weeks:Number):Date
		{
			return addDays(date, weeks * 7);
		}

		public static function addDays(date:Date, days:Number):Date
		{
			return addHours(date, days * 24);
		}

		public static function addHours(date:Date, hrs:Number):Date
		{
			return addMinutes(date, hrs * 60);
		}

		public static function addMinutes(date:Date, mins:Number):Date
		{
			return addSeconds(date, mins * 60);
		}

		public static function addSeconds(date:Date, secs:Number):Date
		{
			var mSecs:Number=secs * 1000;
			var sum:Number=mSecs + date.getTime();
			return new Date(sum);
		}

		public static function diffDate(beginDate:Date, endDate:Date):uint
		{
			return (endDate.getTime() - beginDate.getTime()) / 1000 / 60 / 60 / 24;
		}

		public static function matchDay(day1:Date, day2:Date):Boolean
		{
			if ((day1.date == day2.date) && (day1.month == day2.month) && (day1.fullYear == day2.fullYear))
				return true;
			return false;
		}
		
		public static function clearTime(date:Date):Date
		{
			var oldDate:Date= new Date(date.fullYear,date.month,date.date);
			
			return oldDate;
		}

	}
}