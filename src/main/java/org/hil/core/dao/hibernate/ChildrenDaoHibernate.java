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

package org.hil.core.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hil.core.dao.ChildrenDao;
import org.hil.core.dao.ChildrenVaccinationHistoryDao;
import org.hil.core.dao.GenericChildrenDao;
import org.hil.core.dao.GenericChildrenVaccinationHistoryDao;
import org.hil.core.dao.GenericVaccinationDao;
import org.hil.core.dao.GenericVaccinationDayDao;
import org.hil.core.model.Children;
import org.hil.core.model.ChildrenVaccinationHistory;
import org.hil.core.model.Commune;
import org.hil.core.model.District;
import org.hil.core.model.Vaccination;
import org.hil.core.model.VaccinationDay;
import org.hil.core.model.vo.ChildrenDuePrintVO;
import org.hil.core.model.vo.ChildrenPrintVO;
import org.hil.core.model.vo.ChildrenVaccinatedInLocationVO;
import org.hil.core.model.vo.RegionVaccinationReportData;
import org.hil.core.model.vo.search.ChildrenSearchVO;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("childrenDaoExt")
public class ChildrenDaoHibernate extends GenericDaoHibernateSupportExt<Children> implements ChildrenDao {
	
	@Autowired
	private GenericChildrenDao childrenDao;
	
	@Autowired
	private GenericChildrenVaccinationHistoryDao childrenVaccinationHistoryDao;
	
	@Autowired
	private GenericVaccinationDao vaccinationDao;
	
	@Qualifier("childrenVaccinationHistoryDaoExt")
	@Autowired
	private ChildrenVaccinationHistoryDao childrenVaccinationHistoryDaoExt;
	
	@Autowired
	private GenericVaccinationDayDao vaccinationDayDao;
	
	public List<Children> searchChildren(ChildrenSearchVO params) {
		String sql = " from Children c where 1=1 ";
				
		long locationId = 0;
		if (params.getVillageId() != null && params.getVillageId() > 0) {			
			sql += " and c.village.id =:locationId";
			locationId = params.getVillageId();
		} else if (params.getCommuneId() != null && params.getCommuneId() > 0) {
			sql += " and c.village.commune.id =:locationId";
			locationId = params.getCommuneId();
		} else if (params.getDistrictId() != null && params.getDistrictId() > 0) {
			sql += " and c.village.commune.district.id =:locationId";
			locationId = params.getDistrictId();
		} else if (params.getProvinceId() != null && params.getProvinceId() > 0) {
			sql += " and c.village.commune.district.province.id =:locationId";
			locationId = params.getProvinceId();
		}
		
		if (params.getDateOfBirthFrom() != null) {
			sql += " and DATE(c.dateOfBirth) >=DATE(:fromDate)";
		}
		if (params.getDateOfBirthTo() != null) {
			sql += " and DATE(c.dateOfBirth) <=DATE(:toDate)";
		}
		if (params.getChildCode() != null && params.getChildCode().trim().length() > 0) {
			sql += " and lower(c.childCode) like :childCode";
		}
		
		if (params.getChildName() != null && params.getChildName().trim().length() > 0)
			sql += " and lower(c.firstName) like :firstName ";
		if (params.getMotherName() != null && params.getMotherName().trim().length() > 0)
			sql += " and lower(c.motherFirstName) like :motherFirstName ";
		
		sql += 	" order by c.dateOfBirth desc";
		log.debug(sql);
		Query qry = getSession().createQuery(sql);
		if (locationId > 0)
			qry.setParameter("locationId", locationId);
		if (params.getDateOfBirthFrom() != null) {
			qry.setParameter("fromDate", params.getDateOfBirthFrom());
		}
		if (params.getDateOfBirthTo() != null) {
			qry.setParameter("toDate", params.getDateOfBirthTo());
		}
		if (params.getChildCode() != null && params.getChildCode().trim().length() > 0) {
			qry.setParameter("childCode", "%" + params.getChildCode() + "%");
		}
		if (params.getChildName() != null && params.getChildName().trim().length() > 0)
			qry.setParameter("firstName", params.getChildName().trim().toLowerCase() + "%");
		if (params.getMotherName() != null && params.getMotherName().trim().length() > 0)
			qry.setParameter("motherFirstName", params.getMotherName().trim().toLowerCase() + "%");
		
		List<Children> list = qry.list();
		log.debug("Count:" + list.size());
		return list;
	}
	
	public Children saveChild(Children child, String author, boolean force) {		
		boolean isNewChild = false;
		if (child.getFullName() != null && ! child.getFullName().trim().equalsIgnoreCase(""))
			child.setFirstName(child.getFullName().substring(child.getFullName().lastIndexOf(" ") + 1));
		if (child.getMotherName() != null && ! child.getMotherName().trim().equalsIgnoreCase(""))
			child.setMotherFirstName(child.getMotherName().substring(child.getMotherName().lastIndexOf(" ") + 1));
		if (child.getId() == null) {
			
			if (!force) {
				if (!child.getMotherName().trim().equalsIgnoreCase("")) {
					long total = checkDuplicate(child);
					if (total > 0)
						return null;		
				}
			}			
			isNewChild = true;
			child.setFinishedDate(null);
			child.setLocked(false);
			child.setCreationDate(new Date());
			child.setAuthor(author);
			child.setBarcodeDate(null);
		} else {
			child.setModifiedDate(new Date());
			child.setLastAuthor(author);
		}
		child = childrenDao.save(child);
		if (isNewChild) {
			String code = generateChildCode(child);
			child.setChildCode(code);
			log.debug("child code: " + code);
			child = childrenDao.save(child);
		}		
		
		if (!isNewChild)
			return child;
		
		// add all existed vaccinations record
		List<Vaccination> vaccinations = vaccinationDao.getAll(null, null);
		Vaccination vaccination = new Vaccination();
		ChildrenVaccinationHistory vaccinationHistory = new ChildrenVaccinationHistory();
		
		for (int c=0; c<vaccinations.size(); c++) {
			vaccination = vaccinations.get(c);
			vaccinationHistory = new ChildrenVaccinationHistory();
			vaccinationHistory.setChild(child);
			vaccinationHistory.setVaccination(vaccination);				
			vaccinationHistory.setVaccinated((short)0);
			childrenVaccinationHistoryDao.save(vaccinationHistory);
		}
		return child;		

	}
	
	public List<ChildrenDuePrintVO> getListChildrenDue(String dueTime, Commune commune) {
		List<VaccinationDay> vaccinationDays = vaccinationDayDao.findByCommune(commune);
		if (vaccinationDays == null || vaccinationDays.size() == 0)
			return null;		
				
		String[] tmpdueTime = dueTime.split("-");
		
		Date today = new Date();
		dueTime = tmpdueTime[1] + "-" + tmpdueTime[0] + "-" + vaccinationDays.get(0).getDateInMonth();
		
		List<Children> listChildren = childrenVaccinationHistoryDaoExt.getListChildrenDueByDueTimeCommune(dueTime, commune);
		List<ChildrenDuePrintVO> childrenDuePrintVOs = new ArrayList<ChildrenDuePrintVO>();		
		
		for (Children c : listChildren) {
			ChildrenDuePrintVO aCP = new ChildrenDuePrintVO();
			aCP.setFullName(c.getFullName());
			aCP.setResiden(c.getVillage().getVillageName());
			aCP.setChildCode(c.getChildCode());
			aCP.setMotherName(c.getMotherName() != null ? c.getMotherName() : "");
			aCP.setMotherBirthYear(c.getMotherBirthYear());
			aCP.setFatherName(c.getFatherName() != null ? c.getFatherName() : "");
			aCP.setGender(c.isGender());
			aCP.setChildId(c.getId());
			aCP.setpId(c.getVillage().getCommune().getDistrict().getProvince().getId());
			aCP.setProvinceName(c.getVillage().getCommune().getDistrict().getProvince().getProvinceName());
			aCP.setdId(c.getVillage().getCommune().getDistrict().getId());
			aCP.setDistrictName(c.getVillage().getCommune().getDistrict().getDistrictName());
			aCP.setcId(c.getVillage().getCommune().getId());
			aCP.setCommuneName(c.getVillage().getCommune().getCommuneName());
			aCP.setvId(c.getVillage().getId());
			aCP.setMotherID(c.getMotherID() != null ? c.getMotherID() : "");
			aCP.setMotherMobile(c.getMotherMobile() != null ? c.getMotherMobile() : "");
			aCP.setFatherBirthYear(c.getFatherBirthYear());
			aCP.setFatherID(c.getFatherID() != null ? c.getFatherID() : "");
			aCP.setFatherMobile(c.getFatherMobile() != null ? c.getFatherMobile() : "");
			aCP.setCaretakerBirthYear(c.getCaretakerBirthYear());
			aCP.setCaretakerID(c.getCaretakerID() != null ? c.getCaretakerID() : "");
			aCP.setCaretakerMobile(c.getCaretakerMobile() != null ? c.getCaretakerMobile() : "");
			aCP.setCaretakerName(c.getCaretakerName() != null ? c.getCaretakerName() : "");
			aCP.setCurrentCaretaker(c.getCurrentCaretaker());
			aCP.setDateOfBirth(c.getDateOfBirth());
			aCP.setCurrentCaretaker(c.getCurrentCaretaker());
			aCP.setLocked(c.isLocked());
			List<Vaccination> vaccines = childrenVaccinationHistoryDaoExt.getListVaccinationByChild(dueTime, c, null);
			aCP.setListVaccines(vaccines);
			String strVaccines = "";
			//log.debug("Size Due: " + c.getId() + " | " +  vaccines.size());
			for (Vaccination v : vaccines) {
				strVaccines += v.getName() + "\n";
			}			
			aCP.setVaccines(strVaccines);
			childrenDuePrintVOs.add(aCP);			
		}
		log.debug("Total children due: " + childrenDuePrintVOs.size() + " : " + dueTime);
		return childrenDuePrintVOs;
	}
	
	public String generateChildCode(Children child) {
		Long idbasic;
		String sql = "Select min(c.id)" + " from Children c"
				+ " where c.village.commune.id=:communeId and YEAR(c.dateOfBirth) =:BASE_YEAR";

		Query qry = getSession().createQuery(sql);
		qry.setParameter("communeId", child.getVillage().getCommune().getId());
		log.debug(child.getDateOfBirth().getYear() + 1900);
		qry.setParameter("BASE_YEAR", child.getDateOfBirth().getYear() + 1900);
		idbasic = (Long) qry.uniqueResult();
		if (idbasic == null)
			idbasic = new Long(0);
		log.debug("Id basic: " + idbasic);
		
		String sql2 = "Select count(*)" + " from Children c"
				+ " where (c.village.commune.id!=:communeId or YEAR(c.dateOfBirth) !=:BASE_YEAR) and c.id >" + idbasic + " and c.id < " + child.getId();
		Query qry2 = getSession().createQuery(sql2);
		qry2.setParameter("communeId", child.getVillage().getCommune().getId());
		qry2.setParameter("BASE_YEAR", child.getDateOfBirth().getYear() + 1900);
		Long delta = (Long) qry2.uniqueResult();
		log.debug(delta);
		String code = child.getChildCode();
		long seq = 1001 + child.getId() - idbasic - delta;
		
		
		code = code	+ "-" + String.valueOf(seq).substring(1);
		log.debug("Code: " + code);
		return code;
	}
	
	public List<Children> findByCommuneAndFinishedAndLocked(Commune commune, boolean finished, boolean locked) {
		String sql = " from Children c where c.locked=:locked ";
		
		if (finished) {
			sql += " and c.finishedDate is not null ";
		} else {
			sql += " and c.finishedDate is null ";
		}
		sql += "and c.village.commune=:commune order by id desc";
		log.debug(sql);
		Query qry = getSession().createQuery(sql);		
		qry.setParameter("locked", locked);
		qry.setParameter("commune", commune);
		List<Children> list = qry.list();

		log.debug("Count:" + list.size());
		return list;
	}
	
	public boolean hasChildInCommnue(Long communeId) {		
		String sql = "Select c.id as cid from children c, village vl "
				+ " where c.id_village =vl.id and vl.id_commune=:communeId order by c.id desc limit 1";		
		SQLQuery qry = getSession().createSQLQuery(sql);		
		qry.setParameter("communeId", communeId);		
		log.debug("SQL: " + sql);		
		qry.addScalar("cid", Hibernate.LONG);

		int result = qry.list().size();
		if (result > 0)
			return true;
		else
			return false;		
	}
	
	public long checkDuplicate(Children child) {
		String sql = "Select count(*) from children c, village vl "
				+ " where c.id_village =vl.id and vl.id_commune=:communeId "
				+ " and YEAR(c.date_of_birth)=:yob and c.mother_name =:motherName ";
		SQLQuery qry = getSession().createSQLQuery(sql);		
		qry.setParameter("communeId", child.getVillage().getCommune().getId());
		int yob = child.getDateOfBirth().getYear() + 1900;
		qry.setParameter("yob", yob);
		qry.setParameter("motherName", child.getMotherName());
		log.debug("SQL: " + sql);		
		long result = ((Number)qry.uniqueResult()).longValue();
		log.debug(result);
		return result;
	}
	
	public List<RegionVaccinationReportData> getChildrenVaccinationReport(String timeFrom,String timeTo, Commune commune, District district) {

		List<RegionVaccinationReportData> statistics = new ArrayList<RegionVaccinationReportData>();
		
		String[] timeFromYM = timeFrom.split("/");
		String strTimeFrom = timeFromYM[1] + "-" + timeFromYM[0] + "-01 00:00:00";
		log.debug("From: " + strTimeFrom);
		
		Calendar calendar = Calendar.getInstance();	
		String[] timeToYM = timeTo.split("/");
		int year = Integer.parseInt(timeToYM[1]);
		String strTimeFromY = year + "-01-01 00:00:00";
		calendar.set(year,Integer.parseInt(timeToYM[0]),1);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		String strTimeTo = year + "-" + timeToYM[0] + "-" + maxDay + " 23:59:59";
		log.debug("To: " + strTimeTo);
		
		String queryLocation = "";
		if (commune != null)
			queryLocation = " AND cm.id = :communeId";
		else if (district != null)
			queryLocation = " AND cm.id_district = :districtId GROUP BY cm.id ORDER BY cm.id ASC";
		
		String sql = this.buidlSQLforReport("commune", queryLocation,"strTimeFrom");		
		log.debug(sql);
		
		SQLQuery qry = getSession().createSQLQuery(sql);
		if (commune != null)
			qry.setParameter("communeId", commune.getId());
		else if (district != null)
			qry.setParameter("districtId", district.getId());
		qry.setParameter("timeYear", year);
		qry.setParameter("strTimeFrom", strTimeFrom);
		qry.setParameter("strTimeTo", strTimeTo);
		
		qry.addScalar("regionName", Hibernate.STRING);
		qry.addScalar("childrenUnder1", Hibernate.INTEGER);
		qry.addScalar("VGBL24", Hibernate.INTEGER);
		qry.addScalar("VGBG24", Hibernate.INTEGER);
		qry.addScalar("BCG", Hibernate.INTEGER);
		qry.addScalar("DPT_VGB_Hib1", Hibernate.INTEGER);
		qry.addScalar("OPV1", Hibernate.INTEGER);
		qry.addScalar("DPT_VGB_Hib2", Hibernate.INTEGER);
		qry.addScalar("OPV2", Hibernate.INTEGER);
		qry.addScalar("DPT_VGB_Hib3", Hibernate.INTEGER);
		qry.addScalar("OPV3", Hibernate.INTEGER);
		qry.addScalar("measles1", Hibernate.INTEGER);
		qry.addScalar("eVGBL24", Hibernate.INTEGER);
		qry.addScalar("eVGBG24", Hibernate.INTEGER);
		qry.addScalar("eBCG", Hibernate.INTEGER);
		qry.addScalar("eDPT_VGB_Hib1", Hibernate.INTEGER);
		qry.addScalar("eOPV1", Hibernate.INTEGER);
		qry.addScalar("eDPT_VGB_Hib2", Hibernate.INTEGER);
		qry.addScalar("eOPV2", Hibernate.INTEGER);
		qry.addScalar("eDPT_VGB_Hib3", Hibernate.INTEGER);
		qry.addScalar("eOPV3", Hibernate.INTEGER);
		qry.addScalar("eMeasles1", Hibernate.INTEGER);
		qry.addScalar("protectedTetanusCases", Hibernate.INTEGER);
		qry.addScalar("reactionNormalCases", Hibernate.INTEGER);
		qry.addScalar("reactionSeriousCases", Hibernate.INTEGER);
		qry.addScalar("amountOfFinish", Hibernate.INTEGER);
		qry.setResultTransformer(Transformers.aliasToBean(RegionVaccinationReportData.class));
		
		statistics = qry.list();
		
		if (commune != null && statistics.size() > 0) {
			RegionVaccinationReportData same = new RegionVaccinationReportData();
			same.setAmountOfFinish(statistics.get(0).getAmountOfFinish());
			same.setAmountOfFinish(statistics.get(0).getAmountOfFinish());
			same.setBCG(statistics.get(0).getBCG());
			same.setChildrenUnder1(statistics.get(0).getChildrenUnder1());
			same.setDPT_VGB_Hib1(statistics.get(0).getDPT_VGB_Hib1());
			same.setDPT_VGB_Hib2(statistics.get(0).getDPT_VGB_Hib2());
			same.setDPT_VGB_Hib3(statistics.get(0).getDPT_VGB_Hib3());
			same.setMeasles1(statistics.get(0).getMeasles1());
			same.setOPV1(statistics.get(0).getOPV1());
			same.setOPV2(statistics.get(0).getOPV2());
			same.setOPV3(statistics.get(0).getOPV3());
			same.setVGBG24(statistics.get(0).getVGBG24());
			same.setVGBL24(statistics.get(0).getVGBL24());
			same.setProtectedTetanusCases(statistics.get(0).getProtectedTetanusCases());
			same.setReactionNormalCases(statistics.get(0).getReactionNormalCases());
			same.setReactionSeriousCases(statistics.get(0).getReactionSeriousCases());			
			same.seteBCG(statistics.get(0).geteBCG());			
			same.seteDPT_VGB_Hib1(statistics.get(0).geteDPT_VGB_Hib1());
			same.seteDPT_VGB_Hib2(statistics.get(0).geteDPT_VGB_Hib2());
			same.seteDPT_VGB_Hib3(statistics.get(0).geteDPT_VGB_Hib3());
			same.seteMeasles1(statistics.get(0).geteMeasles1());
			same.seteOPV1(statistics.get(0).geteOPV1());
			same.seteOPV2(statistics.get(0).geteOPV2());
			same.seteOPV3(statistics.get(0).geteOPV3());
			same.seteVGBG24(statistics.get(0).geteVGBG24());
			same.seteVGBL24(statistics.get(0).geteVGBL24());
			statistics.add(same);
				
			String sql2 = this.buidlSQLforReport("commune", queryLocation,"strTimeFromY");
			SQLQuery qry2 = getSession().createSQLQuery(sql2);
			qry2.setParameter("communeId", commune.getId());
			qry2.setParameter("timeYear", year);
			qry2.setParameter("strTimeTo", strTimeTo);			
			qry2.setParameter("strTimeFromY", strTimeFromY);			
			qry2.addScalar("regionName", Hibernate.STRING);
			qry2.addScalar("childrenUnder1", Hibernate.INTEGER);
			qry2.addScalar("VGBL24", Hibernate.INTEGER);
			qry2.addScalar("VGBG24", Hibernate.INTEGER);
			qry2.addScalar("BCG", Hibernate.INTEGER);
			qry2.addScalar("DPT_VGB_Hib1", Hibernate.INTEGER);
			qry2.addScalar("OPV1", Hibernate.INTEGER);
			qry2.addScalar("DPT_VGB_Hib2", Hibernate.INTEGER);
			qry2.addScalar("OPV2", Hibernate.INTEGER);
			qry2.addScalar("DPT_VGB_Hib3", Hibernate.INTEGER);
			qry2.addScalar("OPV3", Hibernate.INTEGER);
			qry2.addScalar("measles1", Hibernate.INTEGER);
			qry2.addScalar("eVGBL24", Hibernate.INTEGER);
			qry2.addScalar("eVGBG24", Hibernate.INTEGER);
			qry2.addScalar("eBCG", Hibernate.INTEGER);
			qry2.addScalar("eDPT_VGB_Hib1", Hibernate.INTEGER);
			qry2.addScalar("eOPV1", Hibernate.INTEGER);
			qry2.addScalar("eDPT_VGB_Hib2", Hibernate.INTEGER);
			qry2.addScalar("eOPV2", Hibernate.INTEGER);
			qry2.addScalar("eDPT_VGB_Hib3", Hibernate.INTEGER);
			qry2.addScalar("eOPV3", Hibernate.INTEGER);
			qry2.addScalar("eMeasles1", Hibernate.INTEGER);
			qry2.addScalar("protectedTetanusCases", Hibernate.INTEGER);
			qry2.addScalar("reactionNormalCases", Hibernate.INTEGER);
			qry2.addScalar("reactionSeriousCases", Hibernate.INTEGER);
			qry2.addScalar("amountOfFinish", Hibernate.INTEGER);
			qry2.setResultTransformer(Transformers.aliasToBean(RegionVaccinationReportData.class));
			statistics.add((RegionVaccinationReportData)qry2.list().get(0));
		} else if (district != null && statistics.size() > 0) {
			String sql2 = this.buidlSQLforReport("district", queryLocation, "strTimeFrom");
			//log.debug(sql2);
			SQLQuery qry2 = getSession().createSQLQuery(sql2);
			qry2.setParameter("districtId", district.getId());
			qry2.setParameter("timeYear", year);
			qry2.setParameter("strTimeFrom", strTimeFrom);
			qry2.setParameter("strTimeTo", strTimeTo);
			
			qry2.addScalar("regionName", Hibernate.STRING);
			qry2.addScalar("childrenUnder1", Hibernate.INTEGER);
			qry2.addScalar("VGBL24", Hibernate.INTEGER);
			qry2.addScalar("VGBG24", Hibernate.INTEGER);
			qry2.addScalar("BCG", Hibernate.INTEGER);
			qry2.addScalar("DPT_VGB_Hib1", Hibernate.INTEGER);
			qry2.addScalar("OPV1", Hibernate.INTEGER);
			qry2.addScalar("DPT_VGB_Hib2", Hibernate.INTEGER);
			qry2.addScalar("OPV2", Hibernate.INTEGER);
			qry2.addScalar("DPT_VGB_Hib3", Hibernate.INTEGER);
			qry2.addScalar("OPV3", Hibernate.INTEGER);
			qry2.addScalar("measles1", Hibernate.INTEGER);
			qry2.addScalar("eVGBL24", Hibernate.INTEGER);
			qry2.addScalar("eVGBG24", Hibernate.INTEGER);
			qry2.addScalar("eBCG", Hibernate.INTEGER);
			qry2.addScalar("eDPT_VGB_Hib1", Hibernate.INTEGER);
			qry2.addScalar("eOPV1", Hibernate.INTEGER);
			qry2.addScalar("eDPT_VGB_Hib2", Hibernate.INTEGER);
			qry2.addScalar("eOPV2", Hibernate.INTEGER);
			qry2.addScalar("eDPT_VGB_Hib3", Hibernate.INTEGER);
			qry2.addScalar("eOPV3", Hibernate.INTEGER);
			qry2.addScalar("eMeasles1", Hibernate.INTEGER);
			qry2.addScalar("protectedTetanusCases", Hibernate.INTEGER);
			qry2.addScalar("reactionNormalCases", Hibernate.INTEGER);
			qry2.addScalar("reactionSeriousCases", Hibernate.INTEGER);
			qry2.addScalar("amountOfFinish", Hibernate.INTEGER);
			qry2.setResultTransformer(Transformers.aliasToBean(RegionVaccinationReportData.class));
			statistics.add((RegionVaccinationReportData)qry2.list().get(0));
			
			String sql3 = this.buidlSQLforReport("district", queryLocation, "strTimeFromY");
			//log.debug(sql3);
			SQLQuery qry3 = getSession().createSQLQuery(sql3);
			qry3.setParameter("districtId", district.getId());
			qry3.setParameter("timeYear", year);
			qry3.setParameter("strTimeTo", strTimeTo);
			qry3.setParameter("strTimeFromY", strTimeFromY);
			qry3.addScalar("regionName", Hibernate.STRING);
			qry3.addScalar("childrenUnder1", Hibernate.INTEGER);
			qry3.addScalar("VGBL24", Hibernate.INTEGER);
			qry3.addScalar("VGBG24", Hibernate.INTEGER);
			qry3.addScalar("BCG", Hibernate.INTEGER);
			qry3.addScalar("DPT_VGB_Hib1", Hibernate.INTEGER);
			qry3.addScalar("OPV1", Hibernate.INTEGER);
			qry3.addScalar("DPT_VGB_Hib2", Hibernate.INTEGER);
			qry3.addScalar("OPV2", Hibernate.INTEGER);
			qry3.addScalar("DPT_VGB_Hib3", Hibernate.INTEGER);
			qry3.addScalar("OPV3", Hibernate.INTEGER);
			qry3.addScalar("measles1", Hibernate.INTEGER);
			qry3.addScalar("eVGBL24", Hibernate.INTEGER);
			qry3.addScalar("eVGBG24", Hibernate.INTEGER);
			qry3.addScalar("eBCG", Hibernate.INTEGER);
			qry3.addScalar("eDPT_VGB_Hib1", Hibernate.INTEGER);
			qry3.addScalar("eOPV1", Hibernate.INTEGER);
			qry3.addScalar("eDPT_VGB_Hib2", Hibernate.INTEGER);
			qry3.addScalar("eOPV2", Hibernate.INTEGER);
			qry3.addScalar("eDPT_VGB_Hib3", Hibernate.INTEGER);
			qry3.addScalar("eOPV3", Hibernate.INTEGER);
			qry3.addScalar("eMeasles1", Hibernate.INTEGER);
			qry3.addScalar("protectedTetanusCases", Hibernate.INTEGER);
			qry3.addScalar("reactionNormalCases", Hibernate.INTEGER);
			qry3.addScalar("reactionSeriousCases", Hibernate.INTEGER);
			qry3.addScalar("amountOfFinish", Hibernate.INTEGER);
			qry3.setResultTransformer(Transformers.aliasToBean(RegionVaccinationReportData.class));
			statistics.add((RegionVaccinationReportData)qry3.list().get(0));
		}	
		
		log.debug("Report: " + statistics.size());
		return statistics;
	}
	
	public List<ChildrenPrintVO> searchChildrenForPrint(ChildrenSearchVO params) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDOBFrom = format.format(params.getDateOfBirthFrom());
		String strDOBTo = format.format(params.getDateOfBirthTo());
		
		String sql = "Select cvh.id_children as childId, c.full_name as fullName, c.date_of_birth as dateOfBirth, v.village_name as villageName, cm.commune_name as communeName,"
				+ " c.child_code as childCode, c.mother_name as motherName, c.mother_birth_year as motherBirthYear, c.gender as gender, c.father_name as fatherName, c.father_birth_year as fatherBirthYear,"
				+ " c.father_mobile as fatherMobile, c.mother_mobile as motherMobile, c.caretaker_name as caretakerName, c.caretaker_mobile as caretakerMobile, "
				+ " group_concat(if(cvh.id_vaccination=1, DATE_FORMAT(date_of_immunization, '%d/%m/%Y'), null)) AS VGB,"
				+ " group_concat(if(cvh.id_vaccination=2, DATE_FORMAT(date_of_immunization, '%d/%m/%Y'), null)) AS BCG,"
				+ " group_concat(if(cvh.id_vaccination=3, DATE_FORMAT(date_of_immunization, '%d/%m/%Y'), null)) AS DPT_VGB_Hib1,"
				+ " group_concat(if(cvh.id_vaccination=4, DATE_FORMAT(date_of_immunization, '%d/%m/%Y'), null)) AS OPV1,"
				+ " group_concat(if(cvh.id_vaccination=5, DATE_FORMAT(date_of_immunization, '%d/%m/%Y'), null)) AS DPT_VGB_Hib2,"
				+ " group_concat(if(cvh.id_vaccination=6, DATE_FORMAT(date_of_immunization, '%d/%m/%Y'), null)) AS OPV2,"
				+ " group_concat(if(cvh.id_vaccination=7, DATE_FORMAT(date_of_immunization, '%d/%m/%Y'), null)) AS DPT_VGB_Hib3,"
				+ " group_concat(if(cvh.id_vaccination=8, DATE_FORMAT(date_of_immunization, '%d/%m/%Y'), null)) AS OPV3,"
				+ " group_concat(if(cvh.id_vaccination=9, DATE_FORMAT(date_of_immunization, '%d/%m/%Y'), null)) AS measles1"				
				+ " FROM children c, children_vaccination_history cvh, village v, commune cm"
				+ " where cvh.id_children=c.id and DATE(c.date_of_birth) >=:strDOBFrom and DATE(c.date_of_birth) <=:strDOBTo  and "
				+ " c.id_village=v.id and v.id_commune=cm.id and cm.id=:communeId and (cvh.vaccinated=1 or cvh.vaccinated=0 or cvh.vaccinated=3)"
				+ " group by cvh.id_children  order by c.id asc";
		
		SQLQuery qry = getSession().createSQLQuery(sql);
		qry.setParameter("communeId", params.getCommuneId());		
		qry.setParameter("strDOBFrom", strDOBFrom);
		qry.setParameter("strDOBTo", strDOBTo);
		
		qry.addScalar("fullName", Hibernate.STRING);
		qry.addScalar("dateOfBirth", Hibernate.DATE);
		qry.addScalar("childCode", Hibernate.STRING);
		qry.addScalar("gender", Hibernate.BOOLEAN);
		qry.addScalar("villageName", Hibernate.STRING);
		qry.addScalar("communeName", Hibernate.STRING);
		qry.addScalar("fatherName", Hibernate.STRING);
		qry.addScalar("fatherBirthYear", Hibernate.INTEGER);
		qry.addScalar("fatherMobile", Hibernate.STRING);
		qry.addScalar("motherName", Hibernate.STRING);
		qry.addScalar("motherBirthYear", Hibernate.INTEGER);
		qry.addScalar("motherMobile", Hibernate.STRING);
		qry.addScalar("caretakerName", Hibernate.STRING);
		qry.addScalar("caretakerMobile", Hibernate.STRING);
		qry.addScalar("VGB", Hibernate.STRING);
		qry.addScalar("BCG", Hibernate.STRING);
		qry.addScalar("DPT_VGB_Hib1", Hibernate.STRING);
		qry.addScalar("OPV1", Hibernate.STRING);
		qry.addScalar("DPT_VGB_Hib2", Hibernate.STRING);
		qry.addScalar("OPV2", Hibernate.STRING);
		qry.addScalar("DPT_VGB_Hib3", Hibernate.STRING);
		qry.addScalar("OPV3", Hibernate.STRING);
		qry.addScalar("measles1", Hibernate.STRING);
		
		qry.setResultTransformer(Transformers
				.aliasToBean(ChildrenPrintVO.class));
		
		List<ChildrenPrintVO> list = qry.list();
		
		return list;
	}
	
	public List<ChildrenVaccinatedInLocationVO> getChildrenVaccinatedInLocationReport(String timeFrom,String timeTo, Commune commune, District district, Vaccination vaccine) {
		List<ChildrenVaccinatedInLocationVO> statistics = new ArrayList<ChildrenVaccinatedInLocationVO>();
		
		String[] timeFromYM = timeFrom.split("/");
		String strTimeFrom = timeFromYM[1] + "-" + timeFromYM[0] + "-01 00:00:00";
		log.debug("From: " + strTimeFrom);
		
		Calendar calendar = Calendar.getInstance();	
		String[] timeToYM = timeTo.split("/");
		int year = Integer.parseInt(timeToYM[1]);
		String strTimeFromY = year + "-01-01 00:00:00";
		calendar.set(year,Integer.parseInt(timeToYM[0]),1);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		String strTimeTo = year + "-" + timeToYM[0] + "-" + maxDay + " 23:59:59";
		log.debug("To: " + strTimeTo);
		
		String sql = "";

		if (commune != null) {
			sql = "(SELECT c.id as id, cm.commune_name as communeName, v.village_name as villageName, c.child_code as childCode, "
				+ " c.full_name as fullName, c.gender as gender, c.date_of_birth as dateOfBirth, c.mother_name as motherName, "
				+ "	cvh.date_of_immunization as dateOfImmunization, cvh.other_vaccinated_location as otherLocation, null as vaccinatedCommune "
				+ " FROM children_vaccination_history cvh, children c, village v, commune cm "
				+ " WHERE cvh.id_vaccination=:vaccine AND cvh.vaccinated=1 AND cvh.other_vaccinated_location BETWEEN 1 AND 4 AND cvh.id_children=c.id "
				+ " AND c.id_village=v.id AND v.id_commune=cm.id AND cm.id=:cId "
				+ " AND DATE(cvh.date_of_immunization) BETWEEN :beginTime AND :endTime) "
				+ " UNION ALL "
				+ " (SELECT c.id as id, cm2.commune_name as communeName, v.village_name as villageName, c.child_code as childCode, "
				+ " c.full_name as fullName, c.gender as gender, c.date_of_birth as dateOfBirth, c.mother_name as motherName, "
				+ " cvh.date_of_immunization as dateOfImmunization, cvh.other_vaccinated_location as otherLocation, cm.commune_name as vaccinatedCommune "
				+ " FROM children_vaccination_history cvh, children c, village v,commune cm, commune cm2 "
				+ " WHERE cvh.id_vaccination=:vaccine AND cvh.vaccinated=1 AND cvh.id_vaccinated_location=cm.id AND cm.id=:cId "
				+ " AND DATE(cvh.date_of_immunization) BETWEEN :beginTime AND :endTime "
				+ " And cvh.id_children=c.id and v.id=c.id_village and cm2.id=v.id_commune) order by id";			
		} else if (district != null) {
			sql = "(SELECT c.id as id, cm.commune_name as communeName, v.village_name as villageName, c.child_code as childCode, "
					+ " c.full_name as fullName, c.gender as gender, c.date_of_birth as dateOfBirth, c.mother_name as motherName, "
					+ " cvh.date_of_immunization as dateOfImmunization, cvh.other_vaccinated_location as otherLocation, null as vaccinatedCommune"
					+ " FROM children_vaccination_history cvh, children c, village v, commune cm, district d "
					+ " WHERE cvh.id_vaccination=:vaccine AND cvh.vaccinated=1 AND cvh.other_vaccinated_location BETWEEN 1 AND 4 AND cvh.id_children=c.id "
					+ " AND c.id_village=v.id AND v.id_commune=cm.id AND cm.id_district=d.id AND d.id=:dId "
					+ " AND DATE(cvh.date_of_immunization) BETWEEN :beginTime AND :endTime)"
					+ " UNION ALL "
					+ " (SELECT c.id as id, cm2.commune_name as communeName, v.village_name as villageName, c.child_code as childCode, "
					+ " c.full_name as fullName, c.gender as gender, c.date_of_birth as dateOfBirth, c.mother_name as motherName, "
					+ " cvh.date_of_immunization as dateOfImmunization, cvh.other_vaccinated_location as otherLocation, cm.commune_name as vaccinatedCommune "
					+ " FROM children_vaccination_history cvh, children c, village v,commune cm, district d, commune cm2 "
					+ " WHERE cvh.id_vaccination=:vaccine AND cvh.vaccinated=1 AND cvh.id_vaccinated_location=cm.id AND cm.id_district=d.id AND d.id=:dId"
					+ " AND DATE(cvh.date_of_immunization) BETWEEN :beginTime AND :endTime "
					+ " AND cvh.id_children=c.id and v.id=c.id_village and cm2.id=v.id_commune) ORDER BY id";		
		}		
		
		log.debug(sql);
		
		SQLQuery qry = getSession().createSQLQuery(sql);
		if (commune != null)
			qry.setParameter("cId", commune.getId());
		else if (district != null)
			qry.setParameter("dId", district.getId());
		qry.setParameter("vaccine", vaccine.getId());
		qry.setParameter("beginTime", strTimeFrom);
		qry.setParameter("endTime", strTimeTo);
		
		qry.addScalar("id", Hibernate.LONG);
		qry.addScalar("communeName", Hibernate.STRING);
		qry.addScalar("villageName", Hibernate.STRING);
		qry.addScalar("childCode", Hibernate.STRING);
		qry.addScalar("fullName", Hibernate.STRING);
		qry.addScalar("gender", Hibernate.BOOLEAN);
		qry.addScalar("dateOfBirth", Hibernate.DATE);
		qry.addScalar("motherName", Hibernate.STRING);
		qry.addScalar("dateOfImmunization", Hibernate.DATE);
		qry.addScalar("otherLocation", Hibernate.SHORT);
		qry.addScalar("vaccinatedCommune", Hibernate.STRING);
		
		qry.setResultTransformer(Transformers
				.aliasToBean(ChildrenVaccinatedInLocationVO.class));
		
		statistics = qry.list();		
		
		log.debug("Report: " + statistics.size());
		
		return statistics;
	}
	
	private String buidlSQLforReport(String groupBy, String queryLocation, String strTimeFrom) {
		String sql = "";		
		sql = "SELECT vData.locationId AS locationId,vData.locationName AS regionName, vData.VGBL24 AS VGBL24, vData.eVGBL24 AS eVGBL24 "
		+ ",vData.VGBG24 AS VGBG24, vData.eVGBG24 AS eVGBG24, vData.BCG AS BCG, vData.eBCG AS eBCG"
		+ ",vData.DPT_VGB_Hib1 AS DPT_VGB_Hib1, vData.eDPT_VGB_Hib1 AS eDPT_VGB_Hib1,vData.OPV1 AS OPV1, vData.eOPV1 AS eOPV1"
		+ ",vData.DPT_VGB_Hib2 AS DPT_VGB_Hib2, vData.eDPT_VGB_Hib2 AS eDPT_VGB_Hib2,vData.OPV2 AS OPV2, vData.eOPV2 AS eOPV2"
		+ ",vData.DPT_VGB_Hib3 AS DPT_VGB_Hib3, vData.eDPT_VGB_Hib3 AS eDPT_VGB_Hib3,vData.OPV3 AS OPV3, vData.eOPV3 AS eOPV3"
		+ ",vData.measles1 AS measles1,vData.eMeasles1 AS eMeasles1,vData.amountOfFinish AS amountOfFinish"
		+ ",rData.protectedTetanusCases AS protectedTetanusCases,rData.reactionNormalCases AS reactionNormalCases,rData.reactionSeriousCases AS reactionSeriousCases"
		+ ",vData.childrenUnder1 AS childrenUnder1"
		+ " FROM ";
		if (groupBy.equalsIgnoreCase("commune")) {
			sql += "(SELECT cm.id AS locationId, cm.commune_name AS locationName,";
		} else if (groupBy.equalsIgnoreCase("district")) {
			sql += "(SELECT d.id AS locationId, d.district_name AS locationName,";
		}		
		sql += "SUM(cvh.id_vaccination=1 AND c.id_village=v.id AND v.id_commune=cm.id AND cvh.overdue=false) AS VGBL24,"
		+ "SUM(cvh.id_vaccination=1 AND cvh.id_vaccinated_location=cm.id AND cvh.overdue=false) AS eVGBL24,"
		+ "SUM(cvh.id_vaccination=1 AND c.id_village=v.id AND v.id_commune=cm.id AND cvh.overdue=true) AS VGBG24,"
		+ "SUM(cvh.id_vaccination=1 AND cvh.id_vaccinated_location=cm.id AND cvh.overdue=true) AS eVGBG24,"
		+ "SUM(cvh.id_vaccination=2 AND c.id_village=v.id AND v.id_commune=cm.id) AS BCG,"
		+ "SUM(cvh.id_vaccination=2 AND cvh.id_vaccinated_location=cm.id) AS eBCG,"
		+ "SUM(cvh.id_vaccination=3 AND c.id_village=v.id AND v.id_commune=cm.id) AS DPT_VGB_Hib1,"
		+ "SUM(cvh.id_vaccination=3 AND cvh.id_vaccinated_location=cm.id) AS eDPT_VGB_Hib1,"
		+ "SUM(cvh.id_vaccination=4 AND c.id_village=v.id AND v.id_commune=cm.id) AS OPV1,"
		+ "SUM(cvh.id_vaccination=4 AND cvh.id_vaccinated_location=cm.id) AS eOPV1,"
		+ "SUM(cvh.id_vaccination=5 AND c.id_village=v.id AND v.id_commune=cm.id) AS DPT_VGB_Hib2,"
		+ "SUM(cvh.id_vaccination=5 AND cvh.id_vaccinated_location=cm.id) AS eDPT_VGB_Hib2,"
		+ "SUM(cvh.id_vaccination=6 AND c.id_village=v.id AND v.id_commune=cm.id) AS OPV2,"
		+ "SUM(cvh.id_vaccination=6 AND cvh.id_vaccinated_location=cm.id) AS eOPV2,"
		+ "SUM(cvh.id_vaccination=7 AND c.id_village=v.id AND v.id_commune=cm.id) AS DPT_VGB_Hib3,"
		+ "SUM(cvh.id_vaccination=7 AND cvh.id_vaccinated_location=cm.id) AS eDPT_VGB_Hib3,"
		+ "SUM(cvh.id_vaccination=8 AND c.id_village=v.id AND v.id_commune=cm.id) AS OPV3,"
		+ "SUM(cvh.id_vaccination=8 AND cvh.id_vaccinated_location=cm.id) AS eOPV3,"
		+ "SUM(cvh.id_vaccination=9 AND c.id_village=v.id AND v.id_commune=cm.id) AS measles1," 
		+ "SUM(cvh.id_vaccination=9 AND cvh.id_vaccinated_location=cm.id) AS eMeasles1,"
		+ "SUM(c.id_village=v.id AND v.id_commune=cm.id AND c.finished_date IS NOT NULL AND DATE(c.finished_date) >=:" + strTimeFrom + " AND DATE(c.finished_date) <=:strTimeTo) AS amountOfFinish,";
		if (groupBy.equalsIgnoreCase("commune")) {
			sql += "(SELECT cu1.total_children_under_1 FROM children_under_1 cu1 WHERE cu1.id_commune=cm.id AND DATE_FORMAT(cu1.time, '%Y')=:timeYear AND (cu1.total_children_under_1 IS NOT NULL AND cu1.total_children_under_1 > 0) ORDER BY time DESC LIMIT 1) AS childrenUnder1 ";
		} else if (groupBy.equalsIgnoreCase("district")) {
			sql += "(SELECT SUM(cu1.total_children_under_1) FROM children_under_1 cu1 WHERE cu1.id IN (SELECT cu1.id FROM children_under_1 cu1, commune cm2 WHERE cu1.id_commune=cm2.id AND cm2.id_district=:districtId AND DATE_FORMAT(cu1.time, '%Y')=:timeYear AND (cu1.total_children_under_1 IS NOT NULL AND cu1.total_children_under_1 > 0) GROUP BY cu1.id_commune)) AS childrenUnder1";
		}		
		sql += " FROM commune cm, children_vaccination_history cvh, children c, village v";
		if (groupBy.equalsIgnoreCase("district")) {
			sql += ",district d ";
		}
		sql += " WHERE DATE(cvh.date_of_immunization) >=:" + strTimeFrom + " AND DATE(cvh.date_of_immunization) <=:strTimeTo" 
		+ " AND cvh.vaccinated=1 AND  cvh.id_children=c.id AND v.id_commune = cm.id AND c.id_village=v.id ";
		if (groupBy.equalsIgnoreCase("commune")) {
			sql += queryLocation + ") AS vData ";
		} else if (groupBy.equalsIgnoreCase("district")) {
			sql += " AND cm.id_district=d.id and d.id=:districtId GROUP BY d.id ORDER BY d.id ASC) AS vData ";
		}		
		sql += " LEFT JOIN ";
		if (groupBy.equalsIgnoreCase("commune")) {
			sql += "(SELECT cm.id AS locationId,";
		} else if (groupBy.equalsIgnoreCase("district")) {
			sql += "(SELECT d.id AS locationId,";
		}
		sql += " SUM(rd.amount_of_tetanus_protection) AS protectedTetanusCases"
		+ ",SUM(rd.total_normal_cases) AS reactionNormalCases,SUM(rd.total_serious_cases) AS reactionSeriousCases "
		+ " FROM general_report_data rd, commune cm ";
		if (groupBy.equalsIgnoreCase("district")) {
			sql += ",district d ";
		}
		sql += " WHERE rd.id_commune = cm.id and DATE(rd.time) >=:" + strTimeFrom + " AND DATE(rd.time) <=:strTimeTo ";
		if (groupBy.equalsIgnoreCase("commune")) {
			sql += queryLocation + ") AS rData ";
		} else if (groupBy.equalsIgnoreCase("district")) {
			sql += " AND cm.id_district=d.id and d.id=:districtId GROUP BY d.id ORDER BY d.id ASC) AS rData ";
		}		
		sql += " ON vData.locationId = rData.locationId ";
//		+ " LEFT JOIN ";
//		if (groupBy.equalsIgnoreCase("commune")) {
//			sql += "(SELECT cm.id AS locationId,";
//		} else if (groupBy.equalsIgnoreCase("district")) {
//			sql += "(SELECT d.id AS locationId,";
//		}
//		sql += " SUM(MAX(cu1.total_children_under_1)) AS childrenUnder1 "
//		+ " FROM children_under_1 cu1,commune cm ";
//		if (groupBy.equalsIgnoreCase("district")) {
//			sql += ",district d ";
//		}		
//		sql += " WHERE cu1.id_commune=cm.id AND DATE_FORMAT(cu1.time, '%Y')=:timeYear "
//		+ " AND (cu1.total_children_under_1 IS NOT NULL AND  cu1.total_children_under_1 > 0) ";
//		if (groupBy.equalsIgnoreCase("commune")) {
//			sql += queryLocation + ") AS cData ";
//		} else if (groupBy.equalsIgnoreCase("district")) {
//			sql += " AND cm.id_district=d.id and d.id=:districtId GROUP BY d.id ORDER BY d.id ASC) AS cData ";
//		}
//		sql += " ON vData.locationId=cData.locationId";
		return sql;
	}
}