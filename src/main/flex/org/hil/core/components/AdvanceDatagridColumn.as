package org.hil.core.components
{
    import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	public class AdvanceDatagridColumn extends AdvancedDataGridColumn
	{
		public function AdvanceDatagridColumn(columnName:String = null) {
			super(columnName);
			this.sortCompareFunction = compareProperties;
			this.labelFunction = labelize;
		}

		private function labelize(item:Object, column:AdvancedDataGridColumn):String {
			return getPropertyValue(item);
		}

		private function getPropertyValue(data:Object):String {
			var properties:Array = dataField.split(/\./);
			var label:Object = data;
			for (var i:uint = 0; i < properties.length && label != null; i++)
				label = label[properties[i]];
			return (label != null ? String(label) : ' ');
		}

		public function compareProperties(obj1:Object, obj2:Object):int {
			var s1:String = getPropertyValue(obj1);
			var s2:String = getPropertyValue(obj2);
			return (s1 < s2 ? -1 : (s1 == s2 ? 0 : 1));
		}

	}
}