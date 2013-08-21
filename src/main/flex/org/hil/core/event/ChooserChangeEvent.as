package org.hil.core.event
{

	import flash.events.Event;

	public class ChooserChangeEvent extends Event
	{

		// Public constructor.
		public function ChooserChangeEvent(type:String)
		{
			// Call the constructor of the superclass.
			super(type);

		}

		// Define static constant.
		public static const CHANGE:String="Change";


	}

}