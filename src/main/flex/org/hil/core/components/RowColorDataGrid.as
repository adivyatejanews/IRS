package org.hil.core.components
{
	import flash.display.Sprite;

	public class RowColorDataGrid extends DataGridSortable
	{
		/**
		 *  @private
		 *  Storage for rowColorFunction property.
		 */
		private var _rowColorFunction:Function;

		public function RowColorDataGrid()
		{
			super();
		}


		[Inspectable(category="General")]
		/**
		 *  A user-defined function that determines the color of the row.
		 *
		 *  <p>The method signature has the following form:</p>
		 *
		 *  <pre>labelFunction(datagrid:DataGrid, rowIndex:int, color:uint):uint</pre>
		 *
		 *  <p>Where <code>datagrid</code> specify the datagrid,
		 *  <code>rowIndex</code> specifies the index of the item for the row in the dataprovider,
		 *  <code>color</code> specifies the suggested color for the row.
		 *  Return the color for the row.
		 **/

		public function get rowColorFunction():Function
		{
			return _rowColorFunction;
		}

		public function set rowColorFunction(value:Function):void
		{
			_rowColorFunction=value;
			invalidateList();
		}


		override protected function drawRowBackground(s:Sprite, rowIndex:int, y:Number, height:Number, color:uint, dataIndex:int):void
		{
			if (rowColorFunction != null && dataProvider != null)
			{
				if (dataIndex < dataProvider.length)
				{
					color=rowColorFunction(this, dataIndex, color);
				}
			}
			super.drawRowBackground(s, rowIndex, y, height, color, dataIndex);
		}

	}
}