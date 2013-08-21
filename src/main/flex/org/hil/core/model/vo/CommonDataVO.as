package org.hil.core.model.vo
{
	import mx.collections.ArrayCollection;
	import mx.resources.ResourceBundle;
	import mx.resources.ResourceManager;	
	import org.hil.core.model.AbstractEntity;	
	import mx.controls.Alert;
	[Bindable]
	[ResourceBundle("CommonData")]
	public class CommonDataVO extends AbstractEntity {		
		[ArrayElementType("org.hil.core.model.SystemRole")]
		public var allRoles:ArrayCollection=new ArrayCollection();
		
		public var arrayOtherVaccinatedLocation:ArrayCollection;
		
		public function CommonDataVO() {
			arrayOtherVaccinatedLocation = new ArrayCollection( [
				{name:""},  
				{name:ResourceManager.getInstance().getString('CommonData', 'national_hospital')},
				{name:ResourceManager.getInstance().getString('CommonData', 'province_hospital')},
				{name:ResourceManager.getInstance().getString('CommonData', 'district_hospital')},
				{name:ResourceManager.getInstance().getString('CommonData', 'clinic_private_hospital')},
				{name:ResourceManager.getInstance().getString('CommonData', 'other')}
			]);			
		}
		
	}
}