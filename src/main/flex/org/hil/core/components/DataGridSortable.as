package org.hil.core.components
{
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.controls.AdvancedDataGrid;

	public class DataGridSortable extends DataGrid
	{
		public function DataGridSortable()
		{
			super();
		}

		public function get listRendererArray():Array
		{
			return listItems;
		}

		public var sortIndex:int = -1;
		public var sortColumn:DataGridColumn;
		public var sortDirection:String;
		public var lastSortIndex:int = -1;
		public function sortByColumn(index:int):void
		{
			var c:DataGridColumn = columns[index];
			var desc:Boolean = c.sortDescending;

			// do the sort if we’re allowed to
			if (c.sortable)
			{
				var s:Sort = collection.sort;
				var f:SortField;
				if (s)
				{
					s.compareFunction = null;
					// analyze the current sort to see what we’ve been given
					var sf:Array = s.fields;
					if (sf)
					{
						for (var i:int = 0; i < sf.length; i++)
						{
							if (sf[i].name == c.dataField)
							{
								// we’re part of the current sort
								f = sf[i]
								// flip the logic so desc is new desired order
								desc = !f.descending;
								break;
							}
						}
					}
				}
				else
					s = new Sort;

				if (!f)
					f = new SortField(c.dataField);

				c.sortDescending = desc;
				var dir:String = (desc) ? "DESC" : "ASC";
				sortDirection = dir;

				// set the grid’s sortIndex
				lastSortIndex = sortIndex;
				sortIndex = index;
				sortColumn = c;

				placeSortArrow();

				// if you have a labelFunction you must supply a sortCompareFunction
				f.name = c.dataField;
				if (c.sortCompareFunction != null)
				{
					f.compareFunction = c.sortCompareFunction;
				}
				else
				{
					f.compareFunction = null;
				}
				f.descending = desc;
				s.fields = [f];
			}
			collection.sort = s;
			collection.refresh();

		}

	/**
	 * @private
	 */

	}
}

