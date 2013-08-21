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

package org.hil.webservice.mobile.impl;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.hil.core.dao.ChildrenDao;
import org.hil.core.dao.ChildrenVaccinationHistoryDao;
import org.hil.core.dao.CommuneDao;
import org.hil.core.dao.DistrictDao;
import org.hil.core.dao.GeneralReportDataDao;
import org.hil.core.dao.GenericChildrenDao;
import org.hil.core.dao.GenericChildrenVaccinationHistoryDao;
import org.hil.core.dao.GenericCommuneDao;
import org.hil.core.dao.GenericDistrictDao;
import org.hil.core.dao.GenericGeneralReportDataDao;
import org.hil.core.dao.GenericProvinceDao;
import org.hil.core.dao.GenericSystemAccountDao;
import org.hil.core.dao.GenericSystemUserDao;
import org.hil.core.dao.GenericVillageDao;
import org.hil.core.dao.ProvinceDao;
import org.hil.core.dao.VillageDao;
import org.hil.core.model.Children;
import org.hil.core.model.ChildrenVaccinationHistory;
import org.hil.core.model.Commune;
import org.hil.core.model.District;
import org.hil.core.model.GeneralReportData;
import org.hil.core.model.Province;
import org.hil.core.model.SystemAccount;
import org.hil.core.model.SystemUser;
import org.hil.core.model.Village;
import org.hil.core.model.vo.ChildrenDuePrintVO;
import org.hil.core.model.vo.RegionVaccinationReportData;
import org.hil.core.model.vo.search.ChildrenSearchVO;
import org.hil.core.service.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

@Path("/children")
@Produces({"text/plain","application/xml", "application/json"})
@Consumes({"text/plain","application/xml", "application/json"})
@Transactional
@Service("childrenWebService")
public class ChildrenWebServiceImpl extends BaseManager {
	@Qualifier("childrenDaoExt")
	@Autowired
	private ChildrenDao childrenDaoExt;
	
	@Autowired
	private GenericChildrenDao childrenDao;
	
	@Autowired
	private GenericSystemAccountDao systemAccountDao;
	
	@Autowired
	private GenericSystemUserDao systemUserDao;
	
	@Autowired
	private GenericProvinceDao provinceDao;
	
	@Autowired
	private GenericDistrictDao districtDao;
	
	@Autowired
	@Qualifier("districtDaoExt")
	private DistrictDao districtDaoExt;
	
	@Autowired
	@Qualifier("communeDaoExt")
	private CommuneDao communeDaoExt;
	
	@Autowired
	private GenericCommuneDao communeDao;
	
	@Autowired
	private GenericVillageDao villageDao;
	
	@Autowired
	@Qualifier("villageDaoExt")
	private VillageDao villageDaoExt;
	
	@Autowired
	@Qualifier("provinceDaoExt")
	private ProvinceDao provinceDaoExt;
	
	@Autowired
	private GenericChildrenVaccinationHistoryDao childrenVaccinationHistoryDao;
	
	@Qualifier("childrenVaccinationHistoryDaoExt")
	@Autowired
	private ChildrenVaccinationHistoryDao childrenVaccinationHistoryDaoExt;
	
	@Autowired
	private GenericGeneralReportDataDao generalReportDataDao; 
	
	@Autowired
	@Qualifier("generalReportDataDaoExt")
	private GeneralReportDataDao generalReportDataDaoExt;
	
	String sessionAuth = "";	
	List<Children> list = new ArrayList<Children>();
	String tmpAuth = "";
	String tmpAuthor = "";
	boolean force;
	
	@Path("login")
	@POST
	public String login(String usernamepassword) {
		log.debug("Mobile login: " + usernamepassword.toLowerCase());
		String[] up = usernamepassword.toLowerCase().split("&");
		String password = up[0].split("=")[1];
		String username = up[1].split("=")[1];
		List<SystemAccount> tmp = systemAccountDao.findByAccountNameAndPassword(username, password);
		String result = "";
		if (tmp == null || tmp.size() == 0) {
			result = "{\"ResultSet\":{\"totalResultsAvailable\":0,\"totalResultsReturned\":0}}";
		} else {
			List<SystemUser> sysuser = systemUserDao.findBySystemAccount(tmp.get(0));
			sessionAuth = java.util.UUID.randomUUID().toString();		
			result = "{\"ResultSet\":{\"totalResultsAvailable\":1,\"totalResultsReturned\":1"
					+ ",\"Result\":[{\"accountId\":\"" + sysuser.get(0).getId() + "\",\"accountName\":\"" + tmp.get(0).getAccountName() + "\",\"sessionAuth\":\"" + sessionAuth + "\""
					+ ",\"cId\":\"" + sysuser.get(0).getCommune().getId() + "\",\"communeId\":\"" + sysuser.get(0).getCommune().getCommuneId() + "\",\"communeName\":\"" + sysuser.get(0).getCommune().getCommuneName() + "\""
					+ ",\"dId\":\"" + sysuser.get(0).getCommune().getDistrict().getId() + "\",\"districtId\":\"" + sysuser.get(0).getCommune().getDistrict().getDistrictId() + "\",\"districtName\":\"" + sysuser.get(0).getCommune().getDistrict().getDistrictName() + "\""
					+ ",\"pId\":\"" + sysuser.get(0).getCommune().getDistrict().getProvince().getId() + "\",\"provinceId\":\"" + sysuser.get(0).getCommune().getDistrict().getProvince().getProvinceId() + "\",\"provinceName\":\"" + sysuser.get(0).getCommune().getDistrict().getProvince().getProvinceName() + "\"";
			
			
			List<Village> villages = villageDao.findByCommune(sysuser.get(0).getCommune());
			result += ",\"Villages\":[";
			for (int vi=0; vi<villages.size(); vi++) {
				Village v = villages.get(vi);
				result += "{\"vId\":\"" + v.getId() + "\",\"villageId\":\"" + v.getVillageId() + "\",\"villageName\":\"" + v.getVillageName() + "\"}";
				if (vi < villages.size()-1) {
					result += ",";
				}
			}
			
			List<Province> provinces = provinceDao.getAll(null, null);
			result += "],\"Provinces\":[";
			for (int pi=0; pi<provinces.size(); pi++) {
				Province p = provinces.get(pi);
				result += "{\"pId\":\"" + p.getId() + "\",\"provinceId\":\"" + p.getProvinceId() + "\",\"provinceName\":\"" + p.getProvinceName() + "\"}";
				if (pi < provinces.size()-1) {
					result += ",";
				}			
			}
			
			List<District> districts = districtDao.findByProvince(sysuser.get(0).getCommune().getDistrict().getProvince());
			result += "],\"Districts\":[";
			for (int di=0; di<districts.size(); di++) {
				District d = districts.get(di);
				result += "{\"dId\":\"" + d.getId() + "\",\"districtId\":\"" + d.getDistrictId() + "\",\"districtName\":\"" + d.getDistrictName() + "\"}";
				if (di < districts.size()-1) {
					result += ",";
				}
			}
			
			List<Commune> communes = communeDao.findByDistrict(sysuser.get(0).getCommune().getDistrict());
			result += "],\"Communes\":[";
			for (int ci=0; ci<communes.size(); ci++) {
				Commune cm = communes.get(ci);
				result += "{\"cId\":\"" + cm.getId() + "\",\"communeId\":\"" + cm.getCommuneId() + "\",\"communeName\":\"" + cm.getCommuneName() + "\"}";
				if (ci < communes.size()-1) {
					result += ",";
				}
			}
			
			result += "]}]}}";
		}
		log.debug(result);
		return result;
	}
	
	@Path("getChild/{childCode}")
	@GET
	public String getChild(@PathParam("childCode") String childCode) {
		return "testws";
	}
	
	@Path("saveChild")
	@POST
	public String saveChild(String childInfo) {
		//fatherName=&password=45853e0a110321665cace9e7ce46e31f&motherBirthYear=&force=false&fatherID=&motherMobile=&caretakerName=&fatherBirthYear=&villageId=20&username=huongmy&currentCaretaker=0&childCode=BTR0401-2011&caretakerMobile=&gender=true&motherName=Md adm&motherID=&caretakerBirthYear=&childId=&dateOfBirth=6/12/2011&locked=false&caretakerID=&fatherMobile=&sessionAuth=cd5f3e7c-63d7-48f7-bebb-ce5aab96f75a&fullName=Test adm
		log.debug("Save child: " + sessionAuth );		
		log.debug(childInfo);
		//this.parseXml(childInfo);
		
		String[] up = childInfo.split("&");
		String fatherName = up[0].replaceAll("\\w*=", "");
		String password = up[1].replaceAll("\\w*=", "");
		String motherBirthYear = up[2].replaceAll("\\w*=", "");
		String sforce = up[3].replaceAll("\\w*=", "");
		String fatherID = up[4].replaceAll("\\w*=", "");
		String motherMobile = up[5].replaceAll("\\w*=", "");
		String caretakerName = up[6].replaceAll("\\w*=", "");
		String fatherBirthYear = up[7].replaceAll("\\w*=", "");		
		String villageId = up[8].replaceAll("\\w*=", "");
		String username = up[9].replaceAll("\\w*=", "");
		tmpAuthor = username;
		String currentCaretaker = up[10].replaceAll("\\w*=", "");
		String childCode = up[11].replaceAll("\\w*=", "");
		String caretakerMobile = up[12].replaceAll("\\w*=", "");
		String gender = up[13].replaceAll("\\w*=", "");
		String motherName = up[14].replaceAll("\\w*=", "");
		String motherID = up[15].replaceAll("\\w*=", "");
		String caretakerBirthYear = up[16].replaceAll("\\w*=", "");
		String childId = up[17].replaceAll("\\w*=", "");
		String dateOfBirth = up[18].replaceAll("\\w*=", "");
		String strLocked = up[19].replaceAll("\\w*=", "");
		String caretakerID = up[20].replaceAll("\\w*=", "");
		String fatherMobile = up[21].replaceAll("\\w*=", "");
		tmpAuth = up[22].replaceAll("\\w*=", "");
		String fullName = up[23].replaceAll("\\w*=", "");

		String result = "";
//		if (!tmpAuth.equalsIgnoreCase(sessionAuth))
//			return "{\"ResultSet\":{\"totalResultsReturned\":0}}";
		
		
		log.debug("Saving: " + childCode + " - " + childId);
		Children c;
		if (childId == null || childId.equalsIgnoreCase("")) {
			c = new Children();
		} else {
			c = childrenDao.get(Long.parseLong(childId));
		}
		
		force = Boolean.parseBoolean(sforce);
		c.setFatherName(fatherName);
		c.setMotherBirthYear(motherBirthYear.length() > 0 ? Integer.parseInt(motherBirthYear) : null);
		c.setFatherID(fatherID);
		c.setMotherMobile(motherMobile);
		c.setCaretakerName(caretakerName);
		c.setFatherBirthYear(fatherBirthYear.length() > 0 ? Integer.parseInt(fatherBirthYear) : null);
		c.setCurrentCaretaker(Short.parseShort(currentCaretaker));
		c.setVillage(villageDao.get(Long.parseLong(villageId)));
		c.setChildCode(childCode.toUpperCase());
		c.setCaretakerMobile(caretakerMobile);
		c.setGender(Boolean.parseBoolean(gender));
		c.setMotherName(motherName);
		c.setMotherID(motherID);
		c.setCaretakerBirthYear(caretakerBirthYear.length() > 0 ? Integer.parseInt(caretakerBirthYear) : null);
		c.setFromMobile(true);
		c.setLocked(Boolean.parseBoolean(strLocked));
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date bdate = format.parse(dateOfBirth);
			c.setDateOfBirth(bdate);
		} catch (ParseException e) {		
			e.printStackTrace();
		}
		c.setCaretakerID(caretakerID);
		c.setFatherMobile(fatherMobile);
		c.setFullName(fullName);
		List<Children> newChildren = new ArrayList<Children>();
		newChildren.add(c);
		if (newChildren.size() > 0) {
			Children nChild = childrenDaoExt.saveChild(newChildren.get(0), tmpAuthor, force);
			if (nChild == null)
				result = "{\"ResultSet\":{\"totalResultsReturned\":0}}";
			else
				result = "{\"ResultSet\":{\"totalResultsReturned\":1}}";
		} else
			result = "{\"ResultSet\":{\"totalResultsReturned\":0}}";
		return result;
	}
	
	@Path("listOfChildrenDue")
	@POST
	public String listOfChildrenDue(String query) {
		//startOffset=0&cId=10432&password=134460152ceb3c44bf92d0f6155d907e&sessionAuth=e15ff2fd-6972-4dac-af8a-ebdb790414ec&timeDue=12-2011&offset=10&username=ngaidang
		log.debug(query);
		String result = "";
		List<Children> listChildrenDue = new ArrayList<Children>();
		String[] up = query.toLowerCase().split("&");
		int page = Integer.parseInt(up[0].replaceAll("\\w*=", ""));
		String cId = up[1].replaceAll("\\w*=", "");
		String password = up[2].replaceAll("\\w*=", "");
		tmpAuth = up[3].replaceAll("\\w*=", "");
		String timeDue = up[4].replaceAll("\\w*=", "");
		int offset = Integer.parseInt(up[5].replaceAll("\\w*=", ""));
		String username = up[6].replaceAll("\\w*=", "");

////		if (!tmpAuth.equalsIgnoreCase(sessionAuth))
////			return "{\"ResultSet\":{\"totalResultsReturned\":0}}";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		List<ChildrenDuePrintVO> tmpList = childrenDaoExt.getListChildrenDue(timeDue, communeDao.get(Long.parseLong(cId)));
		if (tmpList.size() > 0) {			
			int totalResultsReturned = tmpList.size() > offset*page ? offset : tmpList.size() - offset*(page-1); 
			result = "{\"ResultSet\":{\"totalResultsAvailable\":" +tmpList.size() + ",\"totalResultsReturned\":" + totalResultsReturned + ",\"Result\":[";
			for (int i=offset*(page-1); i< offset*(page-1) + totalResultsReturned; i++) {
				ChildrenDuePrintVO v = tmpList.get(i);
				String name = v.getFullName().equalsIgnoreCase("") ? "(M)" + v.getMotherName() : v.getFullName();
				String motherYOB = v.getMotherBirthYear() != null ? v.getMotherBirthYear().toString() : "";
				String fatherYOB = v.getFatherBirthYear() != null ? v.getFatherBirthYear().toString() : "";
				String otherYOB = v.getCaretakerBirthYear() != null ? v.getCaretakerBirthYear().toString() : "";
				result += "{\"childId\":\"" + v.getChildId() + "\",\"childName\":\"" + name + "\",\"childCode\":\"" + v.getChildCode() + "\",\"childDOB\":\"" + format.format(v.getDateOfBirth()) + "\",\"childGender\":\"" + v.isGender() + "\",\"locked\":\"" + v.isLocked() + "\",\"childMother\":\"" + v.getMotherName() + "\",\"childMotherYOB\":\"" + v.getMotherBirthYear() + "\",\"childPId\":\"" + v.getpId() + "\",\"childProvinceName\":\"" + v.getProvinceName() + "\",\"childDId\":\"" + v.getdId() + "\",\"childDistrictName\":\"" + v.getDistrictName() + "\",\"childCId\":\"" + v.getcId() + "\",\"childCommuneName\":\"" + v.getCommuneName() + "\",\"childVId\":\"" + v.getvId() + "\",\"childVillageName\":\"" + v.getResiden() + "\",\"vaccines\":\"" + v.getVaccines().replaceAll("\n", ". ") + "\"";
				result += ",\"motherID\":\"" + v.getMotherID() + "\",\"motherYOB\":\"" + motherYOB + "\",\"motherMobile\":\"" + v.getMotherMobile() + "\",\"fatherName\":\"" + v.getFatherName() + "\",\"fatherID\":\"" + v.getFatherID() + "\",\"fatherYOB\":\"" + fatherYOB + "\",\"fatherMobile\":\"" + v.getFatherMobile() + "\",\"careID\":\"" + v.getCaretakerID() + "\",\"careName\":\"" + v.getCaretakerName() + "\",\"careYOB\":\"" + otherYOB + "\",\"careMobile\":\"" + v.getCaretakerMobile() + "\",\"currentCare\":\"" + v.getCurrentCaretaker()  + "\"";
				List<ChildrenVaccinationHistory> events = childrenVaccinationHistoryDaoExt.findByChildOrderbyDueDate(v.getChildId());
				result += ",\"VaccinationEvent\":[";
				for (int j=0; j< events.size(); j++) {
					ChildrenVaccinationHistory e = events.get(j);
					String strDateOfImmunization = "";
					if (e.getDateOfImmunization() != null)
						strDateOfImmunization = format.format(e.getDateOfImmunization());
					Commune cm = e.getVaccinatedLocation();
					String pId = "";
				    String provinceName = "";
				    String dId = "";
				    String districtName = "";
				    String scId = "";
				    String communeName = ""; 
					if (cm != null) {
						pId = cm.getDistrict().getProvince().getId() + "";
						provinceName = cm.getDistrict().getProvince().getProvinceName();
						dId = cm.getDistrict().getId() + "";
						districtName = cm.getDistrict().getDistrictName();
						scId = cm.getId() + "";
						communeName = cm.getCommuneName();
					}
					String otherLocation = "0";
					if (e.getOtherVaccinatedLocation() != null && e.getOtherVaccinatedLocation() > 0) {
						otherLocation = "" + e.getOtherVaccinatedLocation();
					}
					String missingReason = "";
					if (e.getReasonIfMissed() != null)
						missingReason = e.getReasonIfMissed();
					result += "{\"eventId\":\"" + e.getId() + "\",\"vaccinationId\":\"" + e.getVaccination().getId() + "\",\"vaccinationName\":\"" + e.getVaccination().getName() + "\",\"dateOfImmunization\":\"" + strDateOfImmunization + "\",\"vaccinated\":\"" + e.getVaccinated() + "\",\"reasonIfMissed\":\"" + missingReason + "\",\"pId\":\"" + pId + "\",\"provinceName\":\"" + provinceName + "\",\"dId\":\"" + dId + "\",\"districtName\":\"" + districtName + "\",\"cId\":\"" + scId + "\",\"communeName\":\"" + communeName + "\",\"otherLocation\":\"" + otherLocation + "\",\"overdue\":\"" + e.isOverdue() + "\"}";
					if (j < events.size()-1) {
						result += ",";
					}
				}
				result += "]}";
				if (i<tmpList.size() - 1) {
					result += ",";
				}
			}
			result += "]}}";
		} else
			result = "{\"ResultSet\":{\"totalResultsAvailable\":0,\"totalResultsReturned\":0}}";
		log.debug(result);
		return result;
	}
	
	@Path("saveVaccinationEvent")
	@POST
	public String saveVaccinationEvent(String query) {
		String result = "";
		log.debug(query);
		String[] up = query.split("&");
		String eventId = up[0].replaceAll("\\w*=", "");
		String reasonIfMissed = up[1].replaceAll("\\w*=", "");
		short otherLocation = Short.parseShort(up[2].replaceAll("\\w*=", ""));
		String childId = up[3].replaceAll("\\w*=", "");
		String vaccinatedLocation = up[4].replaceAll("\\w*=", "");
		String username = up[5].replaceAll("\\w*=", "");
		tmpAuth = up[6].replaceAll("\\w*=", "");		
		String vaccinated = up[7].replaceAll("\\w*=", "");
		String dateOfImmunization = up[8].replaceAll("\\w*=", "");		
		String password = up[9].replaceAll("\\w*=", "");
		tmpAuthor = username;
		
		ChildrenVaccinationHistory vaccinationEvent = childrenVaccinationHistoryDao.get(Long.parseLong(eventId));
		vaccinationEvent.setReasonIfMissed(reasonIfMissed);
		vaccinationEvent.setOtherVaccinatedLocation(otherLocation);
		if (otherLocation==0) {
			vaccinationEvent.setVaccinatedLocation(communeDao.get(Long.parseLong(vaccinatedLocation)));
		}
		vaccinationEvent.setVaccinated(Short.parseShort(vaccinated));
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			if (dateOfImmunization != null && !dateOfImmunization.equalsIgnoreCase("")) {
				Date bdate = format.parse(dateOfImmunization);
				vaccinationEvent.setDateOfImmunization(bdate);
			} else
				vaccinationEvent.setDateOfImmunization(null);
		} catch (ParseException e) {		
			e.printStackTrace();
		}
		vaccinationEvent.setFromMobile(true);
		ChildrenVaccinationHistory childVH = childrenVaccinationHistoryDaoExt.saveVaccinationHistory(vaccinationEvent);
		if (childVH == null)
			result = "{\"ResultSet\":{\"totalResultsReturned\":0}}";
		else
			result = "{\"ResultSet\":{\"totalResultsReturned\":1}}";
		return result;
	}
	
	@Path("provinces")
	@POST
	public String provinces(String query) {
		log.debug(query);
		List<Province> provinces = provinceDaoExt.getAllOrderbyName("asc");
		String result = "{\"ResultSet\":{\"totalResultsAvailable\":" + provinces.size() + ",\"totalResultsReturned\":" + provinces.size() + ",\"Result\":[";
		
		for (int pi=0; pi<provinces.size(); pi++) {
			Province p = provinces.get(pi);
			result += "{\"pId\":\"" + p.getId() + "\",\"provinceId\":\"" + p.getProvinceId() + "\",\"provinceName\":\"" + p.getProvinceName() + "\"";
			if (pi < provinces.size()-1) {
				result += ",";
			}			
		}
		result += "]}}";	
		
		log.debug(result);
		return result;
	}
	
	@Path("districts")
	@POST
	public String districts(String query) {
		log.debug(query);
		//password=Huongmy&pId=5&sessionAuth=2ef9285c-e3e1-46ea-9e6c-ea8b65e46579&username=huongmy
		String[] up = query.split("&");
		String password = up[0].replaceAll("\\w*=", "");
		long pId = Long.parseLong(up[1].replaceAll("\\w*=", ""));
		tmpAuth= up[2].replaceAll("\\w*=", "");
		String username = up[3].replaceAll("\\w*=", "");		
		
		List<District> districts = districtDaoExt.findByProvinceId(pId);
		String result = "{\"ResultSet\":{\"totalResultsAvailable\":" + districts.size() + ",\"totalResultsReturned\":" + districts.size() + ",\"Result\":[";
		
		for (int di=0; di<districts.size(); di++) {
			District d = districts.get(di);
			result += "{\"dId\":\"" + d.getId() + "\",\"districtId\":\"" + d.getDistrictId() + "\",\"districtName\":\"" + d.getDistrictName() + "\"}";
			if (di < districts.size()-1) {
				result += ",";
			}			
		}
		result += "]}}";	
		
		log.debug(result);
		return result;
	}
	
	@Path("communes")
	@POST
	public String communes(String query) {
		log.debug(query);
		//password=D&sessionAuth=b685e1a6-8fc8-4bcf-b1c0-97b302ab83d5&dId=Hồng Bàng&username=huongmy
		String[] up = query.split("&");
		String password = up[0].replaceAll("\\w*=", "");
		tmpAuth= up[1].replaceAll("\\w*=", "");
		long dId = Long.parseLong(up[2].replaceAll("\\w*=", ""));		
		String username = up[3].replaceAll("\\w*=", "");
		
		List<Commune> communes = communeDaoExt.findByDistrictId(dId);
		String result = "{\"ResultSet\":{\"totalResultsAvailable\":" + communes.size() + ",\"totalResultsReturned\":" + communes.size() + ",\"Result\":[";
		
		for (int di=0; di<communes.size(); di++) {
			Commune c = communes.get(di);
			result += "{\"cId\":\"" + c.getId() + "\",\"communeId\":\"" + c.getCommuneId() + "\",\"communeName\":\"" + c.getCommuneName() + "\"}";
			if (di < communes.size()-1) {
				result += ",";
			}			
		}
		result += "]}}";	
		
		log.debug(result);
		return result;
	}
	
	@Path("villages")
	@POST
	public String villages(String query) {
		log.debug(query);
		//cId=10409&password=D&sessionAuth=6bd6be3e-2fb4-4c87-af56-d05999f3e5ea&username=huongmy
		String[] up = query.split("&");
		long cId = Long.parseLong(up[0].replaceAll("\\w*=", ""));
		String password = up[1].replaceAll("\\w*=", "");		
		tmpAuth= up[2].replaceAll("\\w*=", "");
		String username = up[3].replaceAll("\\w*=", "");
		
		List<Village> villages = villageDaoExt.findByCommuneId(cId);
		String result = "{\"ResultSet\":{\"totalResultsAvailable\":" + villages.size() + ",\"totalResultsReturned\":" + villages.size() + ",\"Result\":[";
		
		for (int di=0; di<villages.size(); di++) {
			Village v = villages.get(di);
			result += "{\"vId\":\"" + v.getId() + "\",\"villageId\":\"" + v.getVillageId() + "\",\"villageName\":\"" + v.getVillageName() + "\"}";
			if (di < villages.size()-1) {
				result += ",";
			}			
		}
		result += "]}}";	
		
		log.debug(result);
		return result;
	}
	
	@Path("locations")
	@POST
	public String locations(String query) {
		log.debug("Get locations: " + query);
		//cId=59&password=Huongmy&pId=59&sessionAuth=28e39595-7b2f-4a81-910f-358ab8fa6984&dId=59&username=huongmy
		String[] up = query.split("&");
		long cId = Long.parseLong(up[0].replaceAll("\\w*=", ""));
		String password = up[1].replaceAll("\\w*=", "");
		long pId = Long.parseLong(up[2].replaceAll("\\w*=", ""));
		tmpAuth= up[3].replaceAll("\\w*=", "");
		long dId = Long.parseLong(up[4].replaceAll("\\w*=", ""));
		String username = up[5].replaceAll("\\w*=", "");	
		
		List<District> districts = districtDaoExt.findByProvinceId(pId);
		String result = "{\"ResultSet\":{\"totalResultsAvailable\":" + districts.size() + ",\"totalResultsReturned\":" + districts.size() + ",";
		result += "\"Districts\":[";
		for (int di=0; di<districts.size(); di++) {
			District d = districts.get(di);
			result += "{\"dId\":\"" + d.getId() + "\",\"districtId\":\"" + d.getDistrictId() + "\",\"districtName\":\"" + d.getDistrictName() + "\"}";
			if (di < districts.size()-1) {
				result += ",";
			}			
		}
		
		List<Commune> communes = communeDaoExt.findByDistrictId(dId);
		result += "],\"Communes\":[";
		for (int ci=0; ci<communes.size(); ci++) {
			Commune cm = communes.get(ci);
			result += "{\"cId\":\"" + cm.getId() + "\",\"communeId\":\"" + cm.getCommuneId() + "\",\"communeName\":\"" + cm.getCommuneName() + "\"}";
			if (ci < communes.size()-1) {
				result += ",";
			}
		}
		
		List<Village> villages = villageDaoExt.findByCommuneId(cId);
		result += "],\"Villages\":[";
		for (int vi=0; vi<villages.size(); vi++) {
			Village v = villages.get(vi);
			result += "{\"vId\":\"" + v.getId() + "\",\"villageId\":\"" + v.getVillageId() + "\",\"villageName\":\"" + v.getVillageName() + "\"}";
			if (vi < villages.size()-1) {
				result += ",";
			}
		}
		
		result += "]}}";	
		
		log.debug(result);
		return result;
	}
	
//	@Path("locations")
//	@POST
//	public String locations(String auth) {
//		log.debug("Get locations: " + auth + " | " + sessionAuth);
//		List<Province> provinces = provinceDao.getAll(null, null);
//		String result = "{\"ResultSet\":{\"totalResultsAvailable\":" + provinces.size() + ",\"totalResultsReturned\":" + provinces.size() + ",\"Result\":[";
//		
//		for (int pi=0; pi<provinces.size(); pi++) {
//			Province p = provinces.get(pi);
//			List<District> districts = districtDao.findByProvince(p);
//			if (pi < provinces.size()-1) {
//				result += "{\"pId\":\"" + p.getId() + "\",\"provinceId\":\"" + p.getProvinceId() + "\",\"provinceName\":\"" + p.getProvinceName() + "\",";
//			} else {
//				result += "{\"pId\":\"" + p.getId() + "\",\"provinceId\":\"" + p.getProvinceId() + "\",\"provinceName\":\"" + p.getProvinceName() + "\"";
//			}
//			
//			result += ",\"District\":[";
//			for (int di=0; di<districts.size(); di++) {
//				District d = districts.get(di);
//				if (di < districts.size()-1) {
//					result += "{\"dId\":\"" + d.getId() + "\",\"districtId\":\"" + d.getDistrictId() + "\",\"districtName\":\"" + d.getDistrictName() + "\",";
//				} else {
//					result += "{\"dId\":\"" + d.getId() + "\",\"districtId\":\"" + d.getDistrictId() + "\",\"districtName\":\"" + d.getDistrictName() + "\"";
//				}
//				List<Commune> communes = communeDao.findByDistrict(d);
//				result += ",\"Commune\":[";
//				for (int ci=0; ci<communes.size(); ci++) {
//					Commune c = communes.get(ci);
//					if (ci < communes.size()-1) {
//						result += "{\"cId\":\"" + c.getId() + "\",\"communeId\":\"" + c.getCommuneId() + "\",\"communeName\":\"" + c.getCommuneName() + "\",";
//					} else {
//						result += "{\"cId\":\"" + c.getId() + "\",\"communeId\":\"" + c.getCommuneId() + "\",\"communeName\":\"" + c.getCommuneName() + "\"";
//					}
//					List<Village> villages = villageDao.findByCommune(c);
//					result += ",\"Village\":[";
//					for (int vi=0; vi<villages.size(); vi++) {
//						Village v = villages.get(vi);
//						if (vi < villages.size()-1) {
//							result += "{\"vId\":\"" + v.getId() + "\",\"villageId\":\"" + v.getVillageId() + "\",\"villageName\":\"" + v.getVillageName() + "\"},";
//						} else {
//							result += "{\"vId\":\"" + v.getId() + "\",\"villageId\":\"" + v.getVillageId() + "\",\"villageName\":\"" + v.getVillageName() + "\"}";
//						}
//					}
//					result += "]}";
//				}				
//				result += "]}";
//			}			
//			result += "]}";
//		}
//		result += "]}}";	
//		
//		log.debug(result);
//		return result;
//	}
	
	private void parseXml(String xml){
		list = new ArrayList<Children>();
		tmpAuth = "";
		tmpAuthor = "";
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
				 
				Children tempChild;
				String tempVal = "";
			 
				public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
					if(qName.equalsIgnoreCase("Children")) {
						tmpAuth = attributes.getValue("sessionAuth");
						
						if (!tmpAuth.equalsIgnoreCase("sessionAuth"))
							return;
						tmpAuthor = attributes.getValue("author");
						force = Boolean.parseBoolean(attributes.getValue("force"));
					}
					tempVal = "";
					if(qName.equalsIgnoreCase("Child")) {
						tempChild = new Children();
					}
				}
			 
				public void endElement(String uri, String localName, String qName) throws SAXException {			 
					if(qName.equalsIgnoreCase("Child")) {
						//add it to the list
						list.add(tempChild);
					} else if (qName.equalsIgnoreCase("id")) {
						if (tempVal.length() > 0)
							tempChild.setId(Long.parseLong(tempVal));
					} else if (qName.equalsIgnoreCase("fullName")) {
						if (tempVal.length() > 0)
							tempChild.setFullName(tempVal);						
					} else if (qName.equalsIgnoreCase("dateOfBirth")) {
						if (tempVal.length() > 0) {
							SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
							try {
								Date bdate = format.parse(tempVal);
								tempChild.setDateOfBirth(bdate);
							} catch (ParseException e) {		
								e.printStackTrace();
							}	
						}							
					} else if (qName.equalsIgnoreCase("gender")) {
						if (tempVal.length() > 0)
							tempChild.setGender(Boolean.parseBoolean(tempVal));
					} else if (qName.equalsIgnoreCase("childCode")) {
						if (tempVal.length() > 0)
							tempChild.setChildCode(tempVal);	
					} else if (qName.equalsIgnoreCase("fatherName")) {
						if (tempVal.length() > 0)
							tempChild.setFatherName(tempVal);
					} else if (qName.equalsIgnoreCase("fatherBirthYear")) {
						if (tempVal.length() > 0)
							tempChild.setFatherBirthYear(Integer.parseInt(tempVal));
					} else if (qName.equalsIgnoreCase("fatherID")) {
						if (tempVal.length() > 0)
							tempChild.setFatherID(tempVal);
					} else if (qName.equalsIgnoreCase("fatherMobile")) {
						if (tempVal.length() > 0)
							tempChild.setFatherMobile(tempVal);
					} else if (qName.equalsIgnoreCase("motherName")) {
						if (tempVal.length() > 0)
							tempChild.setMotherName(tempVal);
					} else if (qName.equalsIgnoreCase("motherBirthYear")) {
						if (tempVal.length() > 0)
							tempChild.setMotherBirthYear(Integer.parseInt(tempVal));
					} else if (qName.equalsIgnoreCase("motherID")) {
						if (tempVal.length() > 0)
							tempChild.setMotherID(tempVal);
					} else if (qName.equalsIgnoreCase("motherMobile")) {
						if (tempVal.length() > 0)
							tempChild.setMotherMobile(tempVal);
					} else if (qName.equalsIgnoreCase("caretakerName")) {
						if (tempVal.length() > 0)
							tempChild.setCaretakerName(tempVal);
					} else if (qName.equalsIgnoreCase("caretakerBirthYear")) {
						if (tempVal.length() > 0)
							tempChild.setCaretakerBirthYear(Integer.parseInt(tempVal));
					} else if (qName.equalsIgnoreCase("caretakerID")) {
						if (tempVal.length() > 0)
							tempChild.setCaretakerID(tempVal);
					} else if (qName.equalsIgnoreCase("caretakerMobile")) {
						if (tempVal.length() > 0)
							tempChild.setCaretakerMobile(tempVal);
					} else if (qName.equalsIgnoreCase("currentCaretaker")) {
						if (tempVal.length() > 0)
							tempChild.setCurrentCaretaker(Short.parseShort(tempVal));
					} else if (qName.equalsIgnoreCase("villageId")) {
						if (tempVal.length() > 0) {
							tempChild.setVillage(villageDao.get(Long.parseLong(tempVal)));
						}
					}			 
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {			 
					tempVal = new String(ch,start,length);
				}
			 
		     };
			saxParser.parse(new InputSource(new StringReader(xml)), handler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
				
	}
	
	@Path("search")
	@POST
	public String search(String query) {
		//offset=10&username=ngaidang&sessionAuth=950ee67d-076c-42bf-b767-20fbef7bd441&dobTo=11/12/2011&startOffset=1&dobFrom=1/11/2011&communeId=10432&password=134460152ceb3c44bf92d0f6155d907e&childCode=&villageId=
		//offset=10&username=huongmy&sessionAuth=36f11e61-ecb1-41e5-8d68-353ce5c876e8&dobTo=21/12/2011&startOffset=1&childName=H&dobFrom=1/11/2011&communeId=10434&password=45853e0a110321665cace9e7ce46e31f&childCode=&villageId=&motherName=
		log.debug(query);
		String result = "";
		List<Children> listChildrenDue = new ArrayList<Children>();
		String[] up = query.toLowerCase().split("&");
		int offset = Integer.parseInt(up[0].replaceAll("\\w*=", ""));
		String username = up[1].replaceAll("\\w*=", "");
		tmpAuth = up[2].replaceAll("\\w*=", "");
		String dobTo = up[3].replaceAll("\\w*=", "");
		int page = Integer.parseInt(up[4].replaceAll("\\w*=", ""));
		String childName = up[5].replaceAll("\\w*=", "");
		String dobFrom = up[6].replaceAll("\\w*=", "");
		String communeId = up[7].replaceAll("\\w*=", "");		
		String password = up[8].replaceAll("\\w*=", "");
		String childCode = up[9].replaceAll("\\w*=", "");
		String villageId = up[10].replaceAll("\\w*=", "");	
		String motherName = up[11].replaceAll("\\w*=", "");
		
		ChildrenSearchVO params = new ChildrenSearchVO();
		params.setChildCode(childCode);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			if (!dobFrom.equalsIgnoreCase("")) {
				Date fdate = format.parse(dobFrom);
				params.setDateOfBirthFrom(fdate);
			}
			if (!dobTo.equalsIgnoreCase("")) {
				Date tdate = format.parse(dobTo);
				params.setDateOfBirthTo(tdate);
			}
			
		} catch (ParseException e) {		
			e.printStackTrace();
		}
		if (!communeId.equalsIgnoreCase(""))
			params.setCommuneId(Long.parseLong(communeId));
		if (!villageId.equalsIgnoreCase(""))
			params.setVillageId(Long.parseLong(villageId));
		
		params.setChildName(childName);
		params.setMotherName(motherName);

////		if (!tmpAuth.equalsIgnoreCase(sessionAuth))
////			return "{\"ResultSet\":{\"totalResultsReturned\":0}}";
		
		List<Children> tmpList = childrenDaoExt.searchChildren(params);
		
		if (tmpList.size() > 0) {			
			int totalResultsReturned = tmpList.size() > offset*page ? offset : tmpList.size() - offset*(page-1);
			result = "{\"ResultSet\":{\"totalResultsAvailable\":" +tmpList.size() + ",\"totalResultsReturned\":" + totalResultsReturned + ",\"Result\":[";
			for (int i=offset*(page-1); i< offset*(page-1) + totalResultsReturned; i++) {
				Children v = tmpList.get(i);
				String name = v.getFullName().equalsIgnoreCase("") ? "(M)" + v.getMotherName() : v.getFullName();
				String motherYOB = v.getMotherBirthYear() != null ? v.getMotherBirthYear().toString() : "";
				String fatherYOB = v.getFatherBirthYear() != null ? v.getFatherBirthYear().toString() : "";
				String otherYOB = v.getCaretakerBirthYear() != null ? v.getCaretakerBirthYear().toString() : "";
				result += "{\"childId\":\"" + v.getId() + "\",\"childName\":\"" + name + "\",\"childCode\":\"" + v.getChildCode() + "\",\"childDOB\":\"" + format.format(v.getDateOfBirth()) + "\",\"childGender\":\"" + v.isGender() + "\",\"locked\":\"" + v.isLocked() + "\",\"childMother\":\"" + v.getMotherName() + "\",\"childMotherYOB\":\"" + v.getMotherBirthYear() + "\",\"childPId\":\"" + v.getVillage().getCommune().getDistrict().getProvince().getId() + "\",\"childProvinceName\":\"" + v.getVillage().getCommune().getDistrict().getProvince().getProvinceName() + "\",\"childDId\":\"" + v.getVillage().getCommune().getDistrict().getId() + "\",\"childDistrictName\":\"" + v.getVillage().getCommune().getDistrict().getDistrictName() + "\",\"childCId\":\"" + v.getVillage().getCommune().getId() + "\",\"childCommuneName\":\"" + v.getVillage().getCommune().getCommuneName() + "\",\"childVId\":\"" + v.getVillage().getId() + "\",\"childVillageName\":\"" + v.getVillage().getVillageName() + "\"";
				result += ",\"motherID\":\"" + v.getMotherID() + "\",\"motherYOB\":\"" + motherYOB + "\",\"motherMobile\":\"" + v.getMotherMobile() + "\",\"fatherName\":\"" + v.getFatherName() + "\",\"fatherID\":\"" + v.getFatherID() + "\",\"fatherYOB\":\"" + fatherYOB + "\",\"fatherMobile\":\"" + v.getFatherMobile() + "\",\"careID\":\"" + v.getCaretakerID() + "\",\"careName\":\"" + v.getCaretakerName() + "\",\"careYOB\":\"" + otherYOB + "\",\"careMobile\":\"" + v.getCaretakerMobile() + "\",\"currentCare\":\"" + v.getCurrentCaretaker()  + "\"";
				List<ChildrenVaccinationHistory> events = childrenVaccinationHistoryDaoExt.findByChildOrderbyDueDate(v.getId());
				result += ",\"VaccinationEvent\":[";
				for (int j=0; j< events.size(); j++) {
					ChildrenVaccinationHistory e = events.get(j);
					String strDateOfImmunization = "";
					if (e.getDateOfImmunization() != null)
						strDateOfImmunization = format.format(e.getDateOfImmunization());
					Commune cm = e.getVaccinatedLocation();
					String pId = "";
				    String provinceName = "";
				    String dId = "";
				    String districtName = "";
				    String scId = "";
				    String communeName = ""; 
					if (cm != null) {
						pId = cm.getDistrict().getProvince().getId() + "";
						provinceName = cm.getDistrict().getProvince().getProvinceName();
						dId = cm.getDistrict().getId() + "";
						districtName = cm.getDistrict().getDistrictName();
						scId = cm.getId() + "";
						communeName = cm.getCommuneName();
					}
					String otherLocation = "0";
					if (e.getOtherVaccinatedLocation() != null && e.getOtherVaccinatedLocation() > 0) {
						otherLocation = "" + e.getOtherVaccinatedLocation();
					}
					String missingReason = "";
					if (e.getReasonIfMissed() != null)
						missingReason = e.getReasonIfMissed();
					result += "{\"eventId\":\"" + e.getId() + "\",\"vaccinationId\":\"" + e.getVaccination().getId() + "\",\"vaccinationName\":\"" + e.getVaccination().getName() + "\",\"dateOfImmunization\":\"" + strDateOfImmunization + "\",\"vaccinated\":\"" + e.getVaccinated() + "\",\"reasonIfMissed\":\"" + missingReason + "\",\"pId\":\"" + pId + "\",\"provinceName\":\"" + provinceName + "\",\"dId\":\"" + dId + "\",\"districtName\":\"" + districtName + "\",\"cId\":\"" + scId + "\",\"communeName\":\"" + communeName + "\",\"otherLocation\":\"" + otherLocation + "\",\"overdue\":\"" + e.isOverdue() + "\"}";
					if (j < events.size()-1) {
						result += ",";
					}
				}
				result += "]}";
				if (i<tmpList.size() - 1) {
					result += ",";
				}
			}
			result += "]}}";
		} else
			result = "{\"ResultSet\":{\"totalResultsAvailable\":0,\"totalResultsReturned\":0}}";
		log.debug(result);
		return result;
	}
		
	@Path("generalReportData")
	@POST
	public String generalReportData(String query) {
		//cId=10434&password=Huongmy&sessionAuth=13e3c5bb-c58a-4e9e-b7cc-55a7c95c3f8b&timeData=12/2011&username=huongmy
		log.debug(query);
		String[] up = query.toLowerCase().split("&");
		String communeId = up[0].replaceAll("\\w*=", "");
		String password = up[1].replaceAll("\\w*=", "");
		tmpAuth = up[2].replaceAll("\\w*=", "");
		String time = up[3].replaceAll("\\w*=", "");
		String username = up[4].replaceAll("\\w*=", "");
		
		String result = "";
		GeneralReportData grd = generalReportDataDaoExt.findGeneralReportDataByCommuneAndTime(time, Long.parseLong(communeId));
		
		if (grd == null) {
			result = "{\"ResultSet\":{\"totalResultsReturned\":0}}";
		} else {
			String normal = grd.getTotalNormalCases() == null ? "" : String.valueOf(grd.getTotalNormalCases());
			String serious = grd.getTotalSeriousCases() == null ? "" : String.valueOf(grd.getTotalSeriousCases());
			String tetanus = grd.getAmountOfTetanusProtection() == null ? "" : String.valueOf(grd.getAmountOfTetanusProtection());
			result = "{\"ResultSet\":{\"totalResultsReturned\":1,\"Result\":["
					+ "{\"reportId\":\"" + grd.getId() + "\",\"normal\":\"" + normal + "\",\"serious\":\"" + serious + "\",\"tetanus\":\"" + tetanus + "\"}]}}";
		}
		
		return result;
	}
	
	@Path("saveGeneralReportData")
	@POST
	public String saveGeneralReportData(String query) {
		//serious=668&username=huongmy&sessionAuth=52ce4ae0-a3fb-4fdd-9dca-6409420c4b50&tatanus=366&normal=255&timeData=12/2011&reportId=15&cId=10434&password=Huongmy
		log.debug(query);
		String[] up = query.toLowerCase().split("&");
		String serious = up[0].replaceAll("\\w*=", "");
		String username = up[1].replaceAll("\\w*=", "");
		tmpAuth = up[2].replaceAll("\\w*=", "");
		String tetanus = up[3].replaceAll("\\w*=", "");
		String normal = up[4].replaceAll("\\w*=", "");
		String timeData = up[5].replaceAll("\\w*=", "");
		String reportId = up[6].replaceAll("\\w*=", "");
		String communeId = up[7].replaceAll("\\w*=", "");
		String password = up[8].replaceAll("\\w*=", "");
		
		String result = "";
		GeneralReportData grd = null;
		if (reportId.equalsIgnoreCase("")) {			
			Commune commune = communeDao.get(Long.parseLong(communeId));
			generalReportDataDaoExt.createGeneralReportDataInCommunesByDistrict(timeData, null, commune.getDistrict());
			grd = generalReportDataDaoExt.findGeneralReportDataByCommuneAndTime(timeData, commune.getId());
		} else {
			grd = generalReportDataDao.get(Long.parseLong(reportId));
		}
		
		if (!tetanus.equalsIgnoreCase(""))
			grd.setAmountOfTetanusProtection(Integer.parseInt(tetanus));
		if (!normal.equalsIgnoreCase(""))
			grd.setTotalNormalCases(Integer.parseInt(normal));
		if (!serious.equalsIgnoreCase(""))
			grd.setTotalSeriousCases(Integer.parseInt(serious));
		
		grd = generalReportDataDao.save(grd);	
	
		if (grd == null) {
			result = "{\"ResultSet\":{\"totalResultsReturned\":0}}";
		} else {
			String strnormal = grd.getTotalNormalCases() == null ? "" : String.valueOf(grd.getTotalNormalCases());
			String strserious = grd.getTotalSeriousCases() == null ? "" : String.valueOf(grd.getTotalSeriousCases());
			String strtetanus = grd.getAmountOfTetanusProtection() == null ? "" : String.valueOf(grd.getAmountOfTetanusProtection());
			result = "{\"ResultSet\":{\"totalResultsReturned\":1,\"Result\":["
					+ "{\"reportId\":\"" + grd.getId() + "\",\"normal\":\"" + strnormal + "\",\"serious\":\"" + strserious + "\",\"tetanus\":\"" + strtetanus + "\"}]}}";
		}
		
		return result;
	}
	
	@Path("vaccinationStatistic")
	@POST
	public String vaccinationStatistic(String query) {
		//cId=10434&password=Huongmy&sessionAuth=13e3c5bb-c58a-4e9e-b7cc-55a7c95c3f8b&timeData=12/2011&username=huongmy
		log.debug(query);
		String[] up = query.toLowerCase().split("&");
		String communeId = up[0].replaceAll("\\w*=", "");
		String password = up[1].replaceAll("\\w*=", "");
		tmpAuth = up[2].replaceAll("\\w*=", "");
		String time = up[3].replaceAll("\\w*=", "");
		String username = up[4].replaceAll("\\w*=", "");
		
		String result = "";
		Commune commune = communeDao.get(Long.parseLong(communeId));
		List<RegionVaccinationReportData> rep = childrenDaoExt.getChildrenVaccinationReport(time, time, commune, null);
		
		if (rep == null || rep.size() == 0) {
			result = "{\"ResultSet\":{\"totalResultsReturned\":0}}";
		} else {
			RegionVaccinationReportData vr = rep.get(0);
			String under1 = vr.getChildrenUnder1() != null ? vr.getChildrenUnder1() + "" : "";
			String tetanus = vr.getProtectedTetanusCases() != null ? vr.getProtectedTetanusCases() + "" : "";
			String normal = vr.getReactionNormalCases() != null ? vr.getReactionNormalCases() + "" : "";
			String serious = vr.getReactionSeriousCases() != null ? vr.getReactionSeriousCases() + "" : "";
			result = "{\"ResultSet\":{\"totalResultsReturned\":1,\"Result\":["
					+ "{\"childrenUnder1\":\"" + under1 + "\",\"bcg\":\"" + vr.getBCG() + "\",\"vgbl24h\":\"" + vr.getVGBL24() + "\",\"vgbg24h\":\"" + vr.getVGBG24() + "\",\"penta1\":\"" + vr.getDPT_VGB_Hib1() + "\",\"penta2\":\"" + vr.getDPT_VGB_Hib2() + "\",\"penta3\":\"" + vr.getDPT_VGB_Hib3() + "\",\"opv1\":\"" + vr.getOPV1() + "\",\"opv2\":\"" + vr.getOPV2() + "\",\"opv3\":\"" + vr.getOPV3() + "\",\"measles1\":\"" + vr.getMeasles1() + "\",\"finished\":\"" + vr.getAmountOfFinish() + "\",\"tetanus\":\"" + tetanus + "\",\"normal\":\"" + normal + "\",\"serious\":\"" + serious + "\"}," ;
			vr = rep.get(2);		
			under1 = vr.getChildrenUnder1() != null ? vr.getChildrenUnder1() + "" : "";
			tetanus = vr.getProtectedTetanusCases() != null ? vr.getProtectedTetanusCases() + "" : "";
			normal = vr.getReactionNormalCases() != null ? vr.getReactionNormalCases() + "" : "";
			serious = vr.getReactionSeriousCases() != null ? vr.getReactionSeriousCases() + "" : "";
			result += "{\"childrenUnder1\":\"" + under1 + "\",\"bcg\":\"" + vr.getBCG() + "\",\"vgbl24h\":\"" + vr.getVGBL24() + "\",\"vgbg24h\":\"" + vr.getVGBG24() + "\",\"penta1\":\"" + vr.getDPT_VGB_Hib1() + "\",\"penta2\":\"" + vr.getDPT_VGB_Hib2() + "\",\"penta3\":\"" + vr.getDPT_VGB_Hib3() + "\",\"opv1\":\"" + vr.getOPV1() + "\",\"opv2\":\"" + vr.getOPV2() + "\",\"opv3\":\"" + vr.getOPV3() + "\",\"measles1\":\"" + vr.getMeasles1() + "\",\"finished\":\"" + vr.getAmountOfFinish() + "\",\"tetanus\":\"" + tetanus + "\",\"normal\":\"" + normal + "\",\"serious\":\"" + serious + "\"}]}}";
		}
		
		return result;
	}
	
	@Path("changePassword")
	@POST
	public String changePassword(String query) {
		//password=45853e0a110321665cace9e7ce46e31f&sessionAuth=bed8a586-3665-4a50-8feb-9aef4b18472d&newpassword=&accountId=51&username=huongmy
		log.debug(query);
		String[] up = query.toLowerCase().split("&");
		String password = up[0].replaceAll("\\w*=", "");
		tmpAuth = up[1].replaceAll("\\w*=", "");
		String newpassword = up[2].replaceAll("\\w*=", "");		
		String username = up[3].replaceAll("\\w*=", "");
		
		String result = "";
		
		SystemAccount sa = systemAccountDao.findByAccountName(username).get(0);
		sa.setPassword(newpassword);
		sa = systemAccountDao.save(sa);
		if (sa.getPassword().equalsIgnoreCase(newpassword))
			result = "{\"ResultSet\":{\"totalResultsReturned\":1}}";
		else
			result = "{\"ResultSet\":{\"totalResultsReturned\":0}}";
		return result;
	}
}
