/*
 * Children Immunization Registry System (IRS). Copyright (C) 2011 PATH (www.path.org)
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
 * Email: htran282@gmail.com
 */

package org.hil.dao;

import java.io.IOException;
import java.util.List;

import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.Library;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.hil.children.service.impl.SMSManagerImpl.OutboundNotification;
import org.hil.core.dao.GenericDistrictDao;
import org.hil.core.dao.GenericProvinceDao;
import org.hil.core.model.District;
import org.hil.core.model.Province;

public class DistrictDaoTest extends BaseDaoTestCase {
	@Autowired
	private GenericDistrictDao genericDistrictDao;
	
	@Autowired
	private GenericProvinceDao genericProvinceDao;
	
	public void testFindDistrictByProvince() {
		assertNotNull("Cannot inject dao", genericDistrictDao);
		
	}

}
