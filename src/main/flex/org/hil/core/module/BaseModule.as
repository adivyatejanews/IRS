package org.hil.core.module
{
	import flash.system.ApplicationDomain;	
	import mx.modules.Module;

	public class BaseModule extends Module
	{
		public static var appDomain:ApplicationDomain;
		public function BaseModule() {
			super();
		}
		public function addModule(_appDomain:ApplicationDomain):void {
			appDomain=_appDomain;
		} 
		
	}
}