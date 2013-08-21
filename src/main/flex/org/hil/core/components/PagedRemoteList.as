package org.hil.core.components
{

	import org.hil.core.model.util.PageableList;
	import org.hil.core.service.IRemoteListService;
	
	import mx.logging.ILogger;
	import mx.logging.Log;
	import mx.rpc.events.ResultEvent;
	
	import org.granite.rpc.remoting.mxml.SecureRemoteObject;
	import org.granite.tide.collections.PagedCollection;
	import org.granite.tide.events.TideResultEvent;

	[Bindable]
	public class PagedRemoteList extends PagedCollection
	{
		
		private var log:ILogger = Log.getLogger("org.hil.core.components.PagedRemoteList");
		private var _remoteListService: IRemoteListService;
		private var _findEvent:String;
		
		
		public function PagedRemoteList(p_remoteListService:IRemoteListService,p_findEvent:String)
		{
			super();
			_findEvent = p_findEvent;
			_remoteListService = p_remoteListService;

		}

		/**
		 *	Trigger a results query for the current filter
		 *	@param first	: index of first required result
		 *  @param last     : index of last required result
		 */
		protected override function find(first:int, last:int):void {
//			log.debug("find from {0} to {1}", first, last);
			trace("send message:" + _findEvent +" to find from:" + first +" to:" + last);
			// Force evaluation of max, results and count
			var order:String = sort != null && sort.fields.length > 0 ? sort.fields[0].name : null;
			var desc:Boolean = sort != null && sort.fields.length > 0 ? sort.fields[0].descending : false;
			var _pageableList:PageableList = new PageableList();
			_pageableList.firstResult=first;
			_pageableList.maxResults=last-first;
			_pageableList.orderedField=order;
			_pageableList.desc= desc;
			_remoteListService.loadData(_pageableList,_findEvent,findResult,findFault);

		}
		
		
		
		protected override function getResult(event:TideResultEvent):Object {
//			trace("got result from:" + _result.firstResult.toString() +" size:" + _result.maxResults);
			var _pagedResult:PageableList = event.result as PageableList;
		    return _pagedResult;
		}

	
		
	}
}