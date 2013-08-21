/*
* Children Immunization Registry System (IRS). Copyright (C) 2011 PATH (www.path.org)
* 
* IRS is based on HIL (http://code.google.com/p/hil)
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
* 
* Author: Tran Trung Hieu
* Email: hieutt24@gmail.com
*/

package org.hil.core.model.vo
{
	import mx.resources.ResourceManager;

	[Bindable]
	[ResourceBundle("MainApp")]
	public class MenuVO
	{
		private var _vMenuXML:XML;
		private var _roleUser:Array = ['ROLE_USER'];
		private var _roleAdmin:Array = ['ROLE_ADMIN'];
		private var _rolesChildrenInformationRegistration:Array=['ROLE_ADMIN','ROLE_DISTRICT','ROLE_COMMUNE'];
		private var _rolesPrintListOfChildrenDue:Array=['ROLE_ADMIN','ROLE_DISTRICT','ROLE_COMMUNE'];
		private var _rolesVaccinationReport:Array=['ROLE_ADMIN','ROLE_DISTRICT','ROLE_COMMUNE'];
		private var _rolesVaccinationDay:Array=['ROLE_ADMIN','ROLE_DISTRICT'];
		private var _roleReport:Array=['ROLE_ADMIN','ROLE_DISTRICT','ROLE_COMMUNE'];

		public function MenuVO() {
			_vMenuXML=<menutree>
					<node 
						label={ResourceManager.getInstance().getString('MainApp', 'menu.children_vaccination_information')} 
						position='-1' roles={_rolesChildrenInformationRegistration}>
						<node 
							label={ResourceManager.getInstance().getString('MainApp', 'menu.children_information')}  
							position='3' roles={_rolesChildrenInformationRegistration} />
						<node 
							label={ResourceManager.getInstance().getString('MainApp', 'menu.list_of_children_due_next_time')}  
							position='5' roles={_rolesPrintListOfChildrenDue} />
						<node 
							label={ResourceManager.getInstance().getString('MainApp', 'menu.children_vaccination_report')}  
							position='6' roles={_rolesVaccinationReport} />
						<node 
							label={ResourceManager.getInstance().getString('MainApp', 'menu.children_vaccination_in_location_report')}  
							position='9' roles={_rolesVaccinationReport} />
					</node>
					<node 
						label={ResourceManager.getInstance().getString('MainApp', 'menu.vaccination_day')} 
							position='-1' roles={_rolesVaccinationDay}>
							<node 
								label={ResourceManager.getInstance().getString('MainApp', 'menu.vaccination_day')}  
							position='4' roles={_rolesVaccinationDay} />
					</node>
					<node 
						label={ResourceManager.getInstance().getString('MainApp', 'menu.general_report_data')} 
						position='-1' roles={_roleReport} >
						<node 
							label={ResourceManager.getInstance().getString('MainApp', 'menu.general_report_data')} 
							position='7' roles={_roleReport} />						
					</node>
					<node 
						label={ResourceManager.getInstance().getString('MainApp', 'menu.list_management')} 
						position='-1' roles={_roleAdmin} >
						<node 
							label={ResourceManager.getInstance().getString('MainApp', 'menu.list_of_vaccinations')} 
							position='2' roles={_roleAdmin} />
						
						<node 
							label={ResourceManager.getInstance().getString('MainApp', 'menu.list_of_regions')} 
							position='1' roles={_roleAdmin} />						
					</node>
					<node 
						label={ResourceManager.getInstance().getString('MainApp', 'menu.system_administrator')} 
						position='-1' roles={_roleAdmin}>
						<node 
							label={ResourceManager.getInstance().getString('MainApp', 'menu.system_user_management')} 
							position='0' roles={_roleAdmin} />						
					</node>
					<node 
						label={ResourceManager.getInstance().getString('MainApp', 'menu.personal_information')} 
							position='-1' roles={_roleUser}>
						<node 
							label={ResourceManager.getInstance().getString('MainApp', 'menu.personal_information')} 
							position='8' roles={_roleUser} />						
					</node>
				</menutree>;
		}

		public function get vMenuXML():XML {
			return _vMenuXML;
		}

	}
}