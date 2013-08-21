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

package org.hil.children.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.hil.children.service.SMSManager;
import org.hil.core.dao.ChildrenDao;
import org.hil.core.dao.VaccinationDayDao;
import org.hil.core.model.VaccinationDay;
import org.hil.core.model.vo.ChildrenDuePrintVO;
import org.hil.core.service.BaseManager;
import org.hil.core.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.Library;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

@Service("smsManager")
public class SMSManagerImpl extends BaseManager implements SMSManager {

	@Qualifier("vaccinationDayDaoExt")
	@Autowired
	private VaccinationDayDao vaccinationDayDaoExt;

	@Autowired
	private Config config;

	@Qualifier("childrenDaoExt")
	@Autowired
	private ChildrenDao childrenDaoExt;

	public void sendSMStoCaretakers() {
		
		Integer smsBeforeDays = Integer.valueOf(config.getSmsBeforeDays());
		String smsMsg = config.getSmsMsg();		
		String smsModemPort = config.getSmsModemPort();		
		
		List<VaccinationDay> vaccinatationDays = vaccinationDayDaoExt.getDaySMS(smsBeforeDays);
		String phones = "";
		Integer duedate = 25;
		Integer month = 1;
		Integer year;
		String duetime = "";
		 for (VaccinationDay vd : vaccinatationDays) {
			 duedate = vd.getDateInMonth();
			 month = (new Date()).getMonth() + 1;
			 year = (new Date()).getYear() + 1900;
			 duetime = month + "-" + year;
			 List<ChildrenDuePrintVO> children = childrenDaoExt.getListChildrenDue(duetime, vd.getCommune()) ;
		
			 for (ChildrenDuePrintVO child : children) {
				 String phone = "";
				 if (child.getCurrentCaretaker() == 0 && child.getMotherMobile() != null 
						 && !child.getMotherMobile().equalsIgnoreCase("") && !child.getMotherMobile().equalsIgnoreCase("null"))
					 phone = child.getMotherMobile();
				 if (child.getCurrentCaretaker() == 1 && child.getFatherMobile() != null 
						 && !child.getFatherMobile().equalsIgnoreCase("") && !child.getFatherMobile().equalsIgnoreCase("null"))
					 phone = child.getFatherMobile();
				 if (child.getCurrentCaretaker() == 2 && child.getCaretakerMobile() != null 
						 && !child.getFatherMobile().equalsIgnoreCase("") && !child.getFatherMobile().equalsIgnoreCase("null"))
					 phone = child.getCaretakerMobile();
//				 if (phone != null && !phone.equalsIgnoreCase("") && !phone.equalsIgnoreCase("null") 
//						 && !phone.startsWith("075") && phone.startsWith("0") && phones.indexOf(phone) < 0) {
				 if (phone != null && !phone.equalsIgnoreCase("") && !phone.equalsIgnoreCase("null") 
						 && phone.startsWith("0") && phones.indexOf(phone) < 0) {
					 //phones += " " + phone; // uncomment if not use 8100
					 //add 84 send via 8100  remoce if not use 8100
					 phone = phone.substring(1);
					 phone = "84" + phone;
					 phones += " " + phone;
				 }
			 }
		 }
		
		 log.debug(phones);
		 
		 if (phones.equalsIgnoreCase(""))
			 return;
		
		try {			
			
			OutboundNotification outboundNotification = new OutboundNotification();
			log.debug("Send message from a serial gsm modem.");
			log.debug(Library.getLibraryDescription());
			log.debug("Version: " + Library.getLibraryVersion());

			SerialModemGateway gateway = new SerialModemGateway("modem.com3", smsModemPort, 115200, "Wave", "");
			gateway.setInbound(true);
			gateway.setOutbound(true);

			org.smslib.Service.getInstance().setOutboundMessageNotification(outboundNotification);

			org.smslib.Service.getInstance().addGateway(gateway);
			org.smslib.Service.getInstance().startService();
			
			log.debug("Modem Information:");
			log.debug("  Manufacturer: " + gateway.getManufacturer());
			log.debug("  Model: " + gateway.getModel());
			log.debug("  Serial No: " + gateway.getSerialNo());
			log.debug("  SIM IMSI: " + gateway.getImsi());
			log.debug("  Signal Level: " + gateway.getSignalLevel() + " dBm");
			log.debug("  Battery Level: " + gateway.getBatteryLevel() + "%");

			org.smslib.Service.getInstance().createGroup("PATHNEPIIRS");
			
			String[] arrPhone = phones.split(" ");
			
			for (String phone : arrPhone) {
				if (!phone.trim().equalsIgnoreCase(""))
					org.smslib.Service.getInstance().addToGroup("PATHNEPIIRS", phone);
			}

			// Send a message synchronously.
			OutboundMessage msg = new OutboundMessage();
			String msgText = smsMsg;
			msgText = msgText.replace("$date$", duedate + "-" + duetime);
			msg.setText(msgText);
			msg.setRecipient("PATHNEPIIRS");
			org.smslib.Service.getInstance().sendMessage(msg);
			log.debug(msgText + " to " + arrPhone.length + " phones.");

			org.smslib.Service.getInstance().stopService();

		} catch (GatewayException e) {

		} catch (TimeoutException e) {

		} catch (SMSLibException e) {

		} catch (IOException e) {

		} catch (InterruptedException e) {

		}

	}

	public class OutboundNotification implements IOutboundMessageNotification {
		public void process(AGateway gateway, OutboundMessage msg) {
			log.debug("Outbound handler called from Gateway: "
					+ gateway.getGatewayId());
			log.debug(msg);
		}
	}

}