package org.hil.core.service
{
	import org.hil.core.model.util.PageableList;
	
	public interface IRemoteListService
	{
		function loadData(param:PageableList,findEvent:String,findResult:Function,findFault:Function):void
	}
}