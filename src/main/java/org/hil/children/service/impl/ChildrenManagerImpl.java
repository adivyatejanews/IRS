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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.hil.children.service.ChildrenManager;
import org.hil.core.dao.ChildrenDao;
import org.hil.core.dao.ChildrenVaccinationHistoryDao;
import org.hil.core.dao.GenericChildrenDao;
import org.hil.core.dao.GenericChildrenVaccinationHistoryDao;
import org.hil.core.dao.GenericCommuneDao;
import org.hil.core.dao.GenericVaccinationDao;
import org.hil.core.dao.GenericVaccinationDayDao;
import org.hil.core.dao.GenericVillageDao;
import org.hil.core.dao.VillageDao;
import org.hil.core.model.Children;
import org.hil.core.model.ChildrenVaccinationHistory;
import org.hil.core.model.Commune;
import org.hil.core.model.District;
import org.hil.core.model.Vaccination;
import org.hil.core.model.VaccinationDay;
import org.hil.core.model.Village;
import org.hil.core.model.vo.ChildrenDuePrintVO;
import org.hil.core.model.vo.ChildrenPrintVO;
import org.hil.core.model.vo.ChildrenVaccinatedInLocationVO;
import org.hil.core.model.vo.RegionVaccinationReportData;
import org.hil.core.model.vo.ListChildrenPrintVO;
import org.hil.core.model.vo.VaccinationReportPrintVO;
import org.hil.core.model.vo.search.ChildrenSearchVO;
import org.hil.core.service.BaseManager;
import org.hil.core.util.Config;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.granite.context.GraniteContext;
import org.granite.messaging.webapp.HttpGraniteContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("childrenManager")
public class ChildrenManagerImpl extends BaseManager implements ChildrenManager {
	
	@Qualifier("childrenDaoExt")
	@Autowired
	private ChildrenDao childrenDaoExt;
	
	@Autowired
	private GenericChildrenDao childrenDao;
	
	@Autowired
	private GenericCommuneDao communeDao;
	
	@Autowired
	private GenericVillageDao villageDao;
	
	@Qualifier("villageDaoExt")
	@Autowired	
	private VillageDao villageDaoExt;
	
	@Autowired
	private GenericVaccinationDao vaccinationDao;
	
	@Autowired
	private GenericChildrenVaccinationHistoryDao childrenVaccinationHistoryDao;
	
	@Qualifier("childrenVaccinationHistoryDaoExt")
	@Autowired
	private ChildrenVaccinationHistoryDao childrenVaccinationHistoryDaoExt;
	
	@Autowired
	private GenericVaccinationDayDao vaccinationDayDao;
	
	@Autowired
	private Config config;
	
	public List<Children> searchChildren(ChildrenSearchVO params) {
		return childrenDaoExt.searchChildren(params);
	}
	
	public Children saveChild(Children child, boolean force) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return childrenDaoExt.saveChild(child, name, force);
	}
	
	public List<ChildrenVaccinationHistory> getVaccinationHistory(Children child) {		
		return childrenVaccinationHistoryDaoExt.findByChildOrderbyDueDate(child.getId());
	}
	
	public List<ChildrenDuePrintVO> getListChildrenDue(String dueTime, Commune commune) {
		return childrenDaoExt.getListChildrenDue(dueTime, commune);
	}
	
	public List<ChildrenVaccinationHistory> getVaccinationHistoryByVaccines(Long childId, Short vaccinated, List<Vaccination> vaccines, boolean asc) {
		return childrenVaccinationHistoryDaoExt.findByChildAndVaccineAndVaccinatedAndOderbyVaccinationId(childId, vaccinated, vaccines, asc);
	}
	
	public String printListChildrenDue(String dueTime, Commune commune, List<ChildrenDuePrintVO> childrenDue) {		
		String path = "";
		JasperPrint reportPrint = createListChildrenPrintVOReportPrint(childrenDue, dueTime, commune);
		String prefixFileName = commune.getDistrict().getProvince().getProvinceId() + commune.getDistrict().getDistrictId() + commune.getCommuneId();
		try {
			long currentTime = System.currentTimeMillis();

			GraniteContext gc = GraniteContext.getCurrentInstance();
			ServletContext sc = ((HttpGraniteContext) gc).getServletContext();

			String reportDir = sc.getRealPath(config.getBaseReportDir());
			
			String pdfPath = reportDir + "/" + prefixFileName + "_" + currentTime + ".pdf";
			log.debug("path to pdf report:" + pdfPath);
			JasperExportManager.exportReportToPdfFile(reportPrint, pdfPath);
			path = "/reports/" + prefixFileName + "_" + currentTime + ".pdf";
		} catch (Exception ex) {
			String connectMsg = "Could not create the report "
					+ ex.getMessage() + " " + ex.getLocalizedMessage();
			log.debug(connectMsg);
		}
		return path;
	}
	
	@SuppressWarnings( { "unchecked", "rawtypes" })
	public JasperPrint createListChildrenPrintVOReportPrint(List<ChildrenDuePrintVO> childrenDue, String dueTime, Commune commune) {

		JasperPrint childrenVOReportPrint = new JasperPrint();
		try {
			Map parameters = new HashMap();
			GraniteContext gc = GraniteContext.getCurrentInstance();
			ServletContext sc = ((HttpGraniteContext) gc).getServletContext();
			String reportDir = sc.getRealPath(config.getBaseReportDir());

			parameters.put("SUBREPORT_DIR", reportDir + "/jrxml/");
			
			String jasperPath = reportDir + "/jrxml/ListChildrenImmunization.jasper";
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperPath);
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(getListChildrenPrintVO(childrenDue,dueTime, commune));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
			childrenVOReportPrint = jasperPrint;
			
		} catch (Exception ex) {
			String connectMsg = "Could not create the report "
					+ ex.getMessage() + " " + ex.getLocalizedMessage();
			log.debug(connectMsg);
		}
		return childrenVOReportPrint;
	}
	
	public List<ListChildrenPrintVO> getListChildrenPrintVO(List<ChildrenDuePrintVO> childrenDue, String dueTime, Commune commune) {
		List<ListChildrenPrintVO> listChildrenPrintVOs = new ArrayList<ListChildrenPrintVO>();
		ListChildrenPrintVO aListChildrenPrintVO = new ListChildrenPrintVO();	
		aListChildrenPrintVO.setChildren(childrenDue);
		aListChildrenPrintVO.setCommuneName(commune.getCommuneName());
		List<VaccinationDay> vaccinatationDays = vaccinationDayDao.findByCommune(commune);
		dueTime = dueTime.replaceAll("-", "/");
		aListChildrenPrintVO.setDueDate(vaccinatationDays.get(0).getDateInMonth() + "/" + dueTime);
		listChildrenPrintVOs.add(aListChildrenPrintVO);
		
		return listChildrenPrintVOs;

	}
	
	public ChildrenVaccinationHistory saveVaccinationHistory(ChildrenVaccinationHistory vaccinationEvent) {
		vaccinationEvent.setFromMobile(false);
		return childrenVaccinationHistoryDaoExt.saveVaccinationHistory(vaccinationEvent);		
	}
	
	public List<ChildrenVaccinationHistory> saveVaccinationHistory(ChildrenVaccinationHistory vaccinationEvent,Long childId, Short vaccinated, List<Vaccination> vaccines, boolean asc) {
		vaccinationEvent.setFromMobile(false);
		childrenVaccinationHistoryDaoExt.saveVaccinationHistory(vaccinationEvent);
		return childrenVaccinationHistoryDaoExt.findByChildAndVaccineAndVaccinatedAndOderbyVaccinationId(childId, vaccinated, vaccines, asc);
	}
	
	public void deleteChild(Children child) {
		childrenVaccinationHistoryDaoExt.removeByChildId(child.getId());
		childrenDao.remove(child);
	}
	
	public List<RegionVaccinationReportData> getChildrenVaccinationReport(String timeFrom,String timeTo, Commune commune, District district) {
		return childrenDaoExt.getChildrenVaccinationReport(timeFrom, timeTo, commune, district);
	}
	
	public String printListVaccinationReport(String type, String timeFrom,String timeTo, Commune commune, District district, List<RegionVaccinationReportData> statistics) {
		String path = "";
		String prefixFileName = "";
		if (commune != null) 
			prefixFileName = commune.getDistrict().getProvince().getProvinceId() + commune.getDistrict().getDistrictId() + commune.getCommuneId();
		else
			prefixFileName = district.getProvince().getProvinceId() + district.getDistrictId();
		GraniteContext gc = GraniteContext.getCurrentInstance();
		ServletContext sc = ((HttpGraniteContext) gc).getServletContext();
		String reportDir = sc.getRealPath(config.getBaseReportDir());
		long currentTime = System.currentTimeMillis();
		String filePath = reportDir + "/" + prefixFileName + "_Report_" + currentTime;		
		
		if (type.equalsIgnoreCase("pdf")) {
			JasperPrint reportPrint = createListVaccinationReportPrint(timeFrom, timeTo, commune, district, statistics);			
			try {			
				filePath += ".pdf";				
				JasperExportManager.exportReportToPdfFile(reportPrint, filePath);
				path = "/reports/" + prefixFileName + "_Report_" + currentTime + ".pdf";		
				log.debug("path to pdf report:" + path);
			} catch (Exception ex) {
				String connectMsg = "Could not create the report "
						+ ex.getMessage() + " " + ex.getLocalizedMessage();
				log.debug(connectMsg);
			}
		} else {
			filePath += ".xls";
			POIFSFileSystem fs;
			String regionName = "";
			String provinceName = "";
			String districtName = "";
			String communeName = "";
			String timeData = "";
			Short rId = 0;
			if (commune != null) {
				communeName = commune.getCommuneName();
				provinceName = commune.getDistrict().getProvince().getProvinceName();
				districtName = commune.getDistrict().getDistrictName();
				rId = commune.getDistrict().getProvince().getRegionId();											
			} else if (district != null) {
				provinceName = district.getProvince().getProvinceName();
				districtName = district.getDistrictName();				
				rId = district.getProvince().getRegionId();
			}
			if (rId == 1)
				regionName = "Miền Bắc";
			else if (rId == 2)
				regionName = "Miền Trung";
			else
				regionName = "Miền Nam";
			if (timeFrom.equalsIgnoreCase(timeTo)) {
				timeData = timeTo;
			} else {			
				timeData = timeFrom + " - " + timeTo;
			}
			try {
				fs = new POIFSFileSystem(new FileInputStream(reportDir + "/excel/TCMR_Report_Template.xls"));
				HSSFWorkbook wb = new  HSSFWorkbook(fs, true);
				
				HSSFSheet s = wb.getSheetAt(0);
				
				HSSFRow r = null;
				HSSFCell c = null;
				
				r = s.getRow(4);
				c = r.getCell(1);
				c.setCellValue(c.getStringCellValue() + " " + regionName.toUpperCase());
				
				r = s.getRow(5);
				c = r.getCell(1);
				c.setCellValue(c.getStringCellValue() + " " + provinceName.toUpperCase());
				
				r = s.getRow(6);
				c = r.getCell(1);
				c.setCellValue(c.getStringCellValue() + " " + districtName.toUpperCase());
				
				r = s.getRow(7);
				c = r.getCell(1);
				c.setCellValue(c.getStringCellValue() + " " + communeName.toUpperCase());
				
				r = s.getRow(4);
				c = r.getCell(15);
				c.setCellValue(timeData);
				
				HSSFCellStyle cs = wb.createCellStyle();
				cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
				
				HSSFCellStyle cs1 = wb.createCellStyle();
				cs1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cs1.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cs1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cs1.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				
				int rownum=13;
				for (rownum=13;rownum<statistics.size() + 11;rownum++) {
					if (rownum<statistics.size() + 10)
						copyRow(wb, s, rownum, rownum+1);
					r = s.getRow(rownum);					
					r.setHeight((short)270);
					c = r.getCell(1);
					c.setCellValue(rownum-12);					
					c = r.getCell(3);
					c.setCellValue(statistics.get(rownum-13).getRegionName());					
					c = r.getCell(6);
					c.setCellValue(statistics.get(rownum-13).getChildrenUnder1() == null ? 0 : statistics.get(rownum-13).getChildrenUnder1());
					c = r.getCell(8);					
					c.setCellValue(statistics.get(rownum-13).getBCG() + " (" + statistics.get(rownum-13).geteBCG() + ")");					
					c = r.getCell(10);
					c.setCellValue(statistics.get(rownum-13).getVGBL24() + " (" + statistics.get(rownum-13).geteVGBL24() + ")");					
					c = r.getCell(11);
					c.setCellValue(statistics.get(rownum-13).getVGBG24() + " (" + statistics.get(rownum-13).geteVGBG24() + ")");					
					c = r.getCell(14);
					c.setCellValue(statistics.get(rownum-13).getDPT_VGB_Hib1() + " (" + statistics.get(rownum-13).geteDPT_VGB_Hib1() + ")");					
					c = r.getCell(16);
					c.setCellValue(statistics.get(rownum-13).getDPT_VGB_Hib2() + " (" + statistics.get(rownum-13).geteDPT_VGB_Hib2() + ")");					
					c = r.getCell(18);
					c.setCellValue(statistics.get(rownum-13).getDPT_VGB_Hib3() + " (" + statistics.get(rownum-13).geteDPT_VGB_Hib3() + ")");					
					c = r.getCell(19);
					c.setCellValue(statistics.get(rownum-13).getOPV1() + " (" + statistics.get(rownum-13).geteOPV1() + ")");					
					c = r.getCell(20);
					c.setCellValue(statistics.get(rownum-13).getOPV2() + " (" + statistics.get(rownum-13).geteOPV2() + ")");					
					c = r.getCell(21);
					c.setCellValue(statistics.get(rownum-13).getOPV3() + " (" + statistics.get(rownum-13).geteOPV3() + ")");					
					c = r.getCell(23);
					c.setCellValue(statistics.get(rownum-13).getMeasles1() + " (" + statistics.get(rownum-13).geteMeasles1() + ")");					
					c = r.getCell(25);
					c.setCellValue(statistics.get(rownum-13).getAmountOfFinish());					
					c = r.getCell(27);
					c.setCellValue(statistics.get(rownum-13).getProtectedTetanusCases() == null ? 0 : statistics.get(rownum-13).getProtectedTetanusCases());
					c = r.getCell(28);
					c.setCellValue(statistics.get(rownum-13).getReactionNormalCases() == null ? 0 : statistics.get(rownum-13).getReactionNormalCases());
					c = r.getCell(30);
					c.setCellValue(statistics.get(rownum-13).getReactionSeriousCases() == null ? 0 : statistics.get(rownum-13).getReactionSeriousCases());
				}				
				if (statistics != null && statistics.size() > 0) {
					for (;rownum<statistics.size()+13;rownum++) {
						r = s.getRow(rownum);					
						c = r.getCell(6);
						c.setCellValue(statistics.get(rownum-13).getChildrenUnder1() == null ? 0 : statistics.get(rownum-13).getChildrenUnder1());					
						c = r.getCell(8);
						c.setCellValue(statistics.get(rownum-13).getBCG() + "\n(" + statistics.get(rownum-13).geteBCG() + ")");					
						c = r.getCell(10);
						c.setCellValue(statistics.get(rownum-13).getVGBL24() + "\n(" + statistics.get(rownum-13).geteVGBL24() + ")");					
						c = r.getCell(11);
						c.setCellValue(statistics.get(rownum-13).getVGBG24() + "\n(" + statistics.get(rownum-13).geteVGBG24() + ")");					
						c = r.getCell(14);
						c.setCellValue(statistics.get(rownum-13).getDPT_VGB_Hib1() + "\n(" + statistics.get(rownum-13).geteDPT_VGB_Hib1() + ")");					
						c = r.getCell(16);
						c.setCellValue(statistics.get(rownum-13).getDPT_VGB_Hib2() + "\n(" + statistics.get(rownum-13).geteDPT_VGB_Hib2() + ")");					
						c = r.getCell(18);
						c.setCellValue(statistics.get(rownum-13).getDPT_VGB_Hib3() + "\n(" + statistics.get(rownum-13).geteDPT_VGB_Hib3() + ")");					
						c = r.getCell(19);
						c.setCellValue(statistics.get(rownum-13).getOPV1() + "\n(" + statistics.get(rownum-13).geteOPV1() + ")");					
						c = r.getCell(20);
						c.setCellValue(statistics.get(rownum-13).getOPV2() + "\n(" + statistics.get(rownum-13).geteOPV2() + ")");					
						c = r.getCell(21);
						c.setCellValue(statistics.get(rownum-13).getOPV3() + "\n(" + statistics.get(rownum-13).geteOPV3() + ")");					
						c = r.getCell(23);
						c.setCellValue(statistics.get(rownum-13).getMeasles1() + "\n(" + statistics.get(rownum-13).geteMeasles1() + ")");					
						c = r.getCell(25);
						c.setCellValue(statistics.get(rownum-13).getAmountOfFinish());					
						c = r.getCell(27);
						c.setCellValue(statistics.get(rownum-13).getProtectedTetanusCases() == null ? 0 : statistics.get(rownum-13).getProtectedTetanusCases());
						c = r.getCell(28);
						c.setCellValue(statistics.get(rownum-13).getReactionNormalCases() == null ? 0 : statistics.get(rownum-13).getReactionNormalCases());
						c = r.getCell(30);
						c.setCellValue(statistics.get(rownum-13).getReactionSeriousCases() == null ? 0 : statistics.get(rownum-13).getReactionSeriousCases());
						r.setHeight((short)500);
					}					
				} else {
					for (;rownum<16;rownum++) {						
						r = s.getRow(rownum);
						c = r.getCell(6);
						c.setCellValue("");					
						c = r.getCell(8);
						c.setCellValue("");					
						c = r.getCell(10);
						c.setCellValue("");					
						c = r.getCell(11);
						c.setCellValue("");					
						c = r.getCell(14);
						c.setCellValue("");					
						c = r.getCell(16);
						c.setCellValue("");					
						c = r.getCell(18);
						c.setCellValue("");					
						c = r.getCell(19);
						c.setCellValue("");					
						c = r.getCell(20);
						c.setCellValue("");					
						c = r.getCell(21);
						c.setCellValue("");					
						c = r.getCell(23);
						c.setCellValue("");					
						c = r.getCell(25);
						c.setCellValue("");					
						c = r.getCell(27);
						c.setCellValue("");
						c = r.getCell(28);
						c.setCellValue("");
						c = r.getCell(30);
						c.setCellValue("");
					}					
				}
				
				FileOutputStream fileOut = new FileOutputStream(filePath); 
				wb.write(fileOut);
				fileOut.close();
				path = "/reports/" + prefixFileName + "_Report_" + currentTime + ".xls";
				log.debug("Excel: " + path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return path;
	}
	
	private static void copyRow(HSSFWorkbook workbook, HSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        HSSFRow newRow = worksheet.getRow(destinationRowNum);
        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            HSSFCellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (newCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getFirstRow() -
                                        cellRangeAddress.getLastRow())),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }

	
	@SuppressWarnings( { "unchecked", "rawtypes" })
	public JasperPrint createListVaccinationReportPrint(String timeFrom,String timeTo, Commune commune, District district, List<RegionVaccinationReportData> statistics) {
		JasperPrint vaccinationReportPrint = new JasperPrint();
		try {
			Map parameters = new HashMap();
			GraniteContext gc = GraniteContext.getCurrentInstance();
			ServletContext sc = ((HttpGraniteContext) gc).getServletContext();
			String reportDir = sc.getRealPath(config.getBaseReportDir());

			parameters.put("SUBREPORT_DIR", reportDir + "/jrxml/");
			
			String jasperPath = reportDir + "/jrxml/ImmunizationReport.jasper";
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(getListVaccinationReportPrintVO(timeFrom, timeTo, commune, district, statistics));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
			vaccinationReportPrint = jasperPrint;
			
		} catch (Exception ex) {
			String connectMsg = "Could not create the report "
					+ ex.getMessage() + " " + ex.getLocalizedMessage();
			log.debug(connectMsg);
		}
		return vaccinationReportPrint;
	}
	
	public List<VaccinationReportPrintVO> getListVaccinationReportPrintVO(String timeFrom,String timeTo, Commune commune, District district, List<RegionVaccinationReportData> statistics) {
		List<VaccinationReportPrintVO> listVaccinationReportPrintVOs = new ArrayList<VaccinationReportPrintVO>();
		VaccinationReportPrintVO aVaccinationReportPrintVO = new VaccinationReportPrintVO();
		
		RegionVaccinationReportData sum = statistics.get(statistics.size()-2);
		RegionVaccinationReportData total = statistics.get(statistics.size()-1);
		
		aVaccinationReportPrintVO.setSumAmountOfFinish(sum.getAmountOfFinish());
		aVaccinationReportPrintVO.setSumBCG(sum.getBCG() + "\n(" + sum.geteBCG() + ")");		
		aVaccinationReportPrintVO.setSumChildrenUnder1(sum.getChildrenUnder1());
		aVaccinationReportPrintVO.setSumDPT_VGB_Hib1(sum.getDPT_VGB_Hib1() + "\n(" + sum.geteDPT_VGB_Hib1() + ")");
		aVaccinationReportPrintVO.setSumDPT_VGB_Hib2(sum.getDPT_VGB_Hib2() + "\n(" + sum.geteDPT_VGB_Hib2() + ")");
		aVaccinationReportPrintVO.setSumDPT_VGB_Hib3(sum.getDPT_VGB_Hib3() + "\n(" + sum.geteDPT_VGB_Hib3() + ")");
		aVaccinationReportPrintVO.setSumMeasles1(sum.getMeasles1() + " (" + sum.geteMeasles1() + ")");
		aVaccinationReportPrintVO.setSumOPV1(sum.getOPV1() + "\n(" + sum.geteOPV1() + ")");
		aVaccinationReportPrintVO.setSumOPV2(sum.getOPV2() + "\n(" + sum.geteOPV2() + ")");
		aVaccinationReportPrintVO.setSumOPV3(sum.getOPV3() + "\n(" + sum.geteOPV3() + ")");
		aVaccinationReportPrintVO.setSumProtectedTetanusCases(sum.getProtectedTetanusCases());
		aVaccinationReportPrintVO.setSumReactionNormalCases(sum.getReactionNormalCases());
		aVaccinationReportPrintVO.setSumReactionSeriousCases(sum.getReactionSeriousCases());
		aVaccinationReportPrintVO.setSumVGBG24(sum.getVGBG24() + "\n(" + sum.geteVGBG24() + ")");
		aVaccinationReportPrintVO.setSumVGBL24(sum.getVGBL24() + "\n(" + sum.geteVGBL24() + ")");
		
		aVaccinationReportPrintVO.setTotalAmountOfFinish(total.getAmountOfFinish());
		aVaccinationReportPrintVO.setTotalBCG(total.getBCG() + "\n(" + total.geteBCG() + ")");
		aVaccinationReportPrintVO.setTotalChildrenUnder1(total.getChildrenUnder1());
		aVaccinationReportPrintVO.setTotalDPT_VGB_Hib1(total.getDPT_VGB_Hib1() + "\n(" + total.geteDPT_VGB_Hib1() + ")");
		aVaccinationReportPrintVO.setTotalDPT_VGB_Hib2(total.getDPT_VGB_Hib2() + "\n(" + total.geteDPT_VGB_Hib2() + ")");
		aVaccinationReportPrintVO.setTotalDPT_VGB_Hib3(total.getDPT_VGB_Hib3() + "\n(" + total.geteDPT_VGB_Hib3() + ")");
		aVaccinationReportPrintVO.setTotalMeasles1(total.getMeasles1() + "\n(" + total.geteMeasles1() + ")");
		aVaccinationReportPrintVO.setTotalOPV1(total.getOPV1() + "\n(" + total.geteOPV1() + ")");
		aVaccinationReportPrintVO.setTotalOPV2(total.getOPV2() + "\n(" + total.geteOPV2() + ")");
		aVaccinationReportPrintVO.setTotalOPV3(total.getOPV3() + "\n(" + total.geteOPV3() + ")");
		aVaccinationReportPrintVO.setTotalProtectedTetanusCases(total.getProtectedTetanusCases());
		aVaccinationReportPrintVO.setTotalReactionNormalCases(total.getReactionNormalCases());
		aVaccinationReportPrintVO.setTotalReactionSeriousCases(total.getReactionSeriousCases());
		aVaccinationReportPrintVO.setTotalVGBG24(total.getVGBG24() + "\n(" + total.geteVGBG24() + ")");
		aVaccinationReportPrintVO.setTotalVGBL24(total.getVGBL24() + "\n(" + total.geteVGBL24() + ")");
		
		statistics.remove(statistics.size()-1);
		statistics.remove(statistics.size()-1);

		aVaccinationReportPrintVO.setStatistics(statistics);
		String regionName = "";
		if (commune != null) {
			aVaccinationReportPrintVO.setCommuneName(commune.getCommuneName().toUpperCase());
			aVaccinationReportPrintVO.setProvinceName(commune.getDistrict().getProvince().getProvinceName().toUpperCase());
			aVaccinationReportPrintVO.setDistrictName(commune.getDistrict().getDistrictName().toUpperCase());
			Short rId = commune.getDistrict().getProvince().getRegionId();
			aVaccinationReportPrintVO.setRegionId(commune.getDistrict().getProvince().getRegionId());
			if (rId == 1)
				regionName = "Miền Bắc";
			else if (rId == 2)
				regionName = "Miền Trung";
			else
				regionName = "Miền Nam";
			aVaccinationReportPrintVO.setRegionName(regionName.toUpperCase());			
		} else if (district != null) {
			aVaccinationReportPrintVO.setCommuneName("");
			aVaccinationReportPrintVO.setProvinceName(district.getProvince().getProvinceName().toUpperCase());
			aVaccinationReportPrintVO.setDistrictName(district.getDistrictName().toUpperCase());
			aVaccinationReportPrintVO.setRegionId(district.getProvince().getRegionId());
			Short rId = district.getProvince().getRegionId();
			if (rId == 1)
				regionName = "Miền Bắc";
			else if (rId == 2)
				regionName = "Miền Trung";
			else
				regionName = "Miền Nam";
			aVaccinationReportPrintVO.setRegionName(regionName.toUpperCase());
		}
		if (timeFrom.equalsIgnoreCase(timeTo)) {
			aVaccinationReportPrintVO.setTimeData(timeTo);
		} else {			
			aVaccinationReportPrintVO.setTimeData(timeFrom + " - " + timeTo);
		}
			
		listVaccinationReportPrintVOs.add(aVaccinationReportPrintVO);
		log.debug(aVaccinationReportPrintVO.getRegionName());
		return listVaccinationReportPrintVOs;

	}
	
	public String printListChildren(ChildrenSearchVO params) {
		List<ChildrenPrintVO> children = childrenDaoExt.searchChildrenForPrint(params);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String strDOBFrom = format.format(params.getDateOfBirthFrom());
		String strDOBTo = format.format(params.getDateOfBirthTo());
		String path = "";
		String prefixFileName = "";
		Commune commune = communeDao.get(params.getCommuneId());
		prefixFileName = commune.getDistrict().getProvince().getProvinceId() + commune.getDistrict().getDistrictId() + commune.getCommuneId();
		
		GraniteContext gc = GraniteContext.getCurrentInstance();
		ServletContext sc = ((HttpGraniteContext) gc).getServletContext();
		String reportDir = sc.getRealPath(config.getBaseReportDir());
		long currentTime = System.currentTimeMillis();
		String filePath = reportDir + "/" + prefixFileName + "_List_Children_Excel_" + currentTime;
		
		POIFSFileSystem fs;
		try {
			filePath += ".xls";
			fs = new POIFSFileSystem(new FileInputStream(reportDir + "/excel/ListOfChildrenInCommune.xls"));
			HSSFWorkbook wb = new  HSSFWorkbook(fs, true);
			
			HSSFSheet s = wb.getSheetAt(0);
			
			HSSFRow r = null;
			HSSFCell c = null;
			
			r = s.getRow(0);
			c = r.getCell(1);
			c.setCellValue(commune.getCommuneName());
			
			c = r.getCell(2);
			c.setCellValue("(" + strDOBFrom + " - " + strDOBTo + ")");
			
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			HSSFCellStyle cs1 = wb.createCellStyle();
			cs1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs1.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			HSSFCellStyle cs2 = wb.createCellStyle();
			cs2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs2.setBorderRight(HSSFCellStyle.BORDER_THIN);					
			CreationHelper createHelper = wb.getCreationHelper();
			cs2.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			
			int rownum=3;
			for (rownum=3;rownum<children.size() + 3;rownum++) {			
				r = s.createRow(rownum);
								
				c = r.createCell(0);
				c.setCellStyle(cs1);
				c.setCellValue(rownum-2);
				
				c = r.createCell(1);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getFullName());
								
				c = r.createCell(2);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getDateOfBirth());
				
				c = r.createCell(3);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).isGender() == true ? "Nữ" : "Nam");
				
				c = r.createCell(4);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getVillageName());
				
				c = r.createCell(5);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getMotherName());
				
				c = r.createCell(6);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getMotherBirthYear() != null ? children.get(rownum-3).getMotherBirthYear() : 0);
				
				c = r.createCell(7);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getMotherMobile());
				
				c = r.createCell(8);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getFatherName());
				
				c = r.createCell(9);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getFatherBirthYear() != null ? children.get(rownum-3).getFatherBirthYear() : 0);
				
				c = r.createCell(10);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getFatherMobile());
				
				c = r.createCell(11);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getCaretakerName());
				
				c = r.createCell(12);
				c.setCellStyle(cs);
				c.setCellValue(children.get(rownum-3).getCaretakerMobile());
				
				c = r.createCell(13);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getVGB());
				
				c = r.createCell(14);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getBCG());
				
				c = r.createCell(15);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getDPT_VGB_Hib1());
				
				c = r.createCell(16);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getDPT_VGB_Hib2());
				
				c = r.createCell(17);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getDPT_VGB_Hib3());
				
				c = r.createCell(18);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getOPV1());
				
				c = r.createCell(19);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getOPV2());
				
				c = r.createCell(20);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getOPV3());
				
				c = r.createCell(21);
				c.setCellStyle(cs2);
				c.setCellValue(children.get(rownum-3).getMeasles1());
			}
			
					
			FileOutputStream fileOut = new FileOutputStream(filePath); 
			wb.write(fileOut);
			fileOut.close();
			path = "/reports/" + prefixFileName + "_List_Children_Excel_" + currentTime + ".xls";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	
	public List<ChildrenVaccinatedInLocationVO> getChildrenVaccinatedInLocationReport(String timeFrom,String timeTo, Commune commune, District district, Vaccination vaccine) {
		return childrenDaoExt.getChildrenVaccinatedInLocationReport(timeFrom, timeTo, commune, district, vaccine);		
	}
	
	public String printListVaccinatedInLocationReport(String type, String timeFrom,String timeTo, Commune commune, District district, Vaccination vaccine, List<ChildrenVaccinatedInLocationVO> statistics) {
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		String path = "";
		String prefixFileName = "";
		if (commune != null)
			prefixFileName = district.getDistrictId() + "_" + commune.getCommuneId();
		else if (district != null)
			prefixFileName = district.getDistrictId();
		GraniteContext gc = GraniteContext.getCurrentInstance();
		ServletContext sc = ((HttpGraniteContext) gc).getServletContext();
		String reportDir = sc.getRealPath(config.getBaseReportDir());
		long currentTime = System.currentTimeMillis();
		String filePath = reportDir + "/" + prefixFileName + "_DanhSachTreDenTiem_" + vaccine.getName() + "_" + currentTime;
		
		POIFSFileSystem fs;
		try {
			filePath += ".xls";
			fs = new POIFSFileSystem(new FileInputStream(reportDir + "/excel/ListOfChildrenVaccinatedInLocation.xls"));
			HSSFWorkbook wb = new  HSSFWorkbook(fs, true);
			
			HSSFSheet s = wb.getSheetAt(0);
			
			HSSFRow r = null;
			HSSFCell c = null;
			
			r = s.getRow(0);
			c = r.getCell(0);
			c.setCellValue("Danh sách trẻ đến tiêm chủng " + vaccine.getName() + " (bao gồm cả trẻ tiêm ở bệnh viện/phòng khám)");
			
			r = s.getRow(1);
			c = r.getCell(1);
			c.setCellValue(district.getDistrictName());
			if (commune != null) {
				c = r.getCell(2);
				c.setCellValue("Xã");
				c = r.getCell(3);
				c.setCellValue(commune.getCommuneName());
				c = r.getCell(4);
				c.setCellValue("(" + timeFrom + " - " + timeTo + ")");
			} else { 
				c = r.getCell(2);
				c.setCellValue("(" + timeFrom + " - " + timeTo + ")");
			}
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			HSSFCellStyle cs1 = wb.createCellStyle();
			cs1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs1.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			HSSFCellStyle cs2 = wb.createCellStyle();
			cs2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs2.setBorderRight(HSSFCellStyle.BORDER_THIN);					
			CreationHelper createHelper = wb.getCreationHelper();
			cs2.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			
			int rownum=3;
			for (rownum=3;rownum<statistics.size() + 3;rownum++) {			
				r = s.createRow(rownum);
								
				c = r.createCell(0);
				c.setCellStyle(cs1);
				c.setCellValue(rownum-2);
				
				c = r.createCell(1);
				c.setCellStyle(cs);
				c.setCellValue(statistics.get(rownum-3).getCommuneName());
								
				c = r.createCell(2);
				c.setCellStyle(cs);
				c.setCellValue(statistics.get(rownum-3).getVillageName());
				
				c = r.createCell(3);
				c.setCellStyle(cs);
				c.setCellValue(statistics.get(rownum-3).getChildCode());
				
				c = r.createCell(4);
				c.setCellStyle(cs);
				c.setCellValue(statistics.get(rownum-3).getFullName());
				
				c = r.createCell(5);
				c.setCellStyle(cs1);
				c.setCellValue(statistics.get(rownum-3).getGender() == true ? "Nữ" : "Nam");
				
				c = r.createCell(6);
				c.setCellStyle(cs2);
				c.setCellValue(statistics.get(rownum-3).getDateOfBirth());
				
				c = r.createCell(7);
				c.setCellStyle(cs);
				c.setCellValue(statistics.get(rownum-3).getMotherName());
				
				c = r.createCell(8);
				c.setCellStyle(cs2);
				c.setCellValue(statistics.get(rownum-3).getDateOfImmunization());
				
				c = r.createCell(9);
				c.setCellStyle(cs);
				String vaccinatedLocation = "";
				if (statistics.get(rownum-3).getOtherLocation() != null
						&& statistics.get(rownum-3).getOtherLocation() >= 1
						&& statistics.get(rownum-3).getOtherLocation() <= 4) {
					if (statistics.get(rownum-3).getOtherLocation() == 1)
						vaccinatedLocation = "Bệnh viện TW";
					else if (statistics.get(rownum-3).getOtherLocation() == 2)
						vaccinatedLocation = "Bệnh viện tỉnh";
					else if (statistics.get(rownum-3).getOtherLocation() == 3)
						vaccinatedLocation = "Bệnh viện huyện";
					else if (statistics.get(rownum-3).getOtherLocation() == 4)
						vaccinatedLocation = "Phòng khám/Bệnh viện tư nhân";
				} else
					vaccinatedLocation = statistics.get(rownum-3).getVaccinatedCommune();
				c.setCellValue(vaccinatedLocation);				
			}
			
					
			FileOutputStream fileOut = new FileOutputStream(filePath); 
			wb.write(fileOut);
			fileOut.close();
			path = "/reports/" + prefixFileName + "_DanhSachTreDenTiem_" + vaccine.getName() + "_" + currentTime + ".xls";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	
	public void importExcel() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug("Start import...");
		String fileToBeRead="/home/hieu/DKTC-2011_Hieu.xls";
		try{			
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileToBeRead));			
			
			HSSFSheet sheet = workbook.getSheetAt(1);
			
			Vaccination vvgb = vaccinationDao.get((long)1);
			Vaccination vbcg = vaccinationDao.get((long)2);
			Vaccination vdpt1 = vaccinationDao.get((long)3);
			Vaccination vopv1 = vaccinationDao.get((long)4);
			Vaccination vdpt2 = vaccinationDao.get((long)5);
			Vaccination vopv2 = vaccinationDao.get((long)6);
			Vaccination vdpt3 = vaccinationDao.get((long)7);
			Vaccination vopv3 = vaccinationDao.get((long)8);
			Vaccination vmeasles = vaccinationDao.get((long)9);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			for (int r=1;r<sheet.getPhysicalNumberOfRows();r++) {
				log.debug("--------------------------------------" + r);
				HSSFRow row = sheet.getRow(r);
				Children child = new Children();
				Date bcg = null;
				Date opv1 = null;
				Date opv2 = null;
				Date opv3 = null;
				Date dpt1 = null;
				Date dpt2 = null;
				Date dpt3 = null;
				Date measles = null;
				Date helpb1 = null;
				Boolean helpb1ontime = null;
				Date finishedDate = null;
				String villageName = "";
				long communeId = 0;
				for (int c=0; c< 20;c++) {
					
					HSSFCell cell = row.getCell(c);					
					// Type the content
					Date tmpDate = null;
					int tmpInt = 0;
					String tmpStr = "";
					Boolean tmpboolean = null;
					if (cell != null) {
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								tmpDate = cell.getDateCellValue();	
			                } else 
			                	tmpInt = (int)cell.getNumericCellValue();
						} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							tmpStr = cell.getStringCellValue();							
						} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
							tmpInt = (int)cell.getNumericCellValue();
						} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
							tmpboolean = cell.getBooleanCellValue();
						}
					}

					if (c == 0) {
						communeId = (long)tmpInt;						
					} else if (c==1) {
						child.setFullName(tmpStr);
					} else if (c==2) {
						if (tmpInt == 2)
							child.setGender(true);
						else
							child.setGender(false);
					} else if (c==3) {
//						if (cell==null)
//							child.setDateOfBirth(null);
//						else 
//							child.setDateOfBirth(tmpDate);
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							child.setDateOfBirth(null);
						else {
							child.setDateOfBirth(format.parse(tmpStr));
						}
					} else if (c==4) {
						child.setFatherName(tmpStr);
					} else if (c==5) {
						child.setMotherName(tmpStr);
					} else if (c==6) {
						//village
						villageName = tmpStr;
					} else if (c==7) {
//						if (cell != null)
//							bcg = tmpDate;
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							bcg = null;
						else {
							bcg = format.parse(tmpStr);
						}
					} else if (c==8) {
//						if (cell != null)
//							opv1 = tmpDate;
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							opv1 = null;
						else {
							opv1 = format.parse(tmpStr);
						}
					} else if (c==9) {
//						if (cell != null)
//							opv2 = tmpDate;
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							opv2 = null;
						else {
							opv2 = format.parse(tmpStr);
						}
					} else if (c==10) {
//						if (cell != null)
//							opv3 = tmpDate;
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							opv3 = null;
						else {
							opv3 = format.parse(tmpStr);
						}
					} else if (c==11) {
//						if (cell != null)
//							dpt1 = tmpDate; 
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							dpt1 = null;
						else {
							dpt1 = format.parse(tmpStr);
						}
					} else if (c==12) {
//						if (cell != null)
//							dpt2 = tmpDate;
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							dpt2 = null;
						else {
							dpt2 = format.parse(tmpStr);
						}
					} else if (c==13) {
//						if (cell != null)
//							dpt3 = tmpDate; 
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							dpt3 = null;
						else {
							dpt3 = format.parse(tmpStr);
						}
					} else if (c==14) {
//						if (cell != null)
//							measles = tmpDate;
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							measles = null;
						else {
							measles = format.parse(tmpStr);
						}
					} else if (c==15) {
//						if (cell != null) {
//							helpb1 = tmpDate;
//							helpb1ontime = true;
//						}
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							helpb1 = null;							
						else {
							helpb1 = format.parse(tmpStr);
							helpb1ontime = true;
						}
					} else if (c==16 && helpb1 == null) {
//						if (cell != null) {
//							helpb1 = tmpDate;
//							helpb1ontime = false;
//						}						
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							helpb1 = null;							
						else {
							helpb1 = format.parse(tmpStr);
							helpb1ontime = false;
						}
					} else if (c==19) {
//						if (cell != null)
//							finishedDate = tmpDate;
						if (tmpStr == null || tmpStr.equalsIgnoreCase(""))
							finishedDate = null;							
						else {
							finishedDate = format.parse(tmpStr);							
						}
					}
				}
				if (child.getDateOfBirth() != null) {
					log.debug(child.getFullName() + " - " + child.getDateOfBirth() + " - " + child.isGender());
					Commune commune = communeDao.get(communeId);
					
					if (villageName.equalsIgnoreCase("")) 
						child.setVillage(villageDao.findByCommune(commune).get(0));
					else {
 						List<Village> vl = villageDaoExt.findByCommuneIdAndVillageName(communeId, villageName);
 						if (vl != null && vl.size() > 0) 
 							child.setVillage(vl.get(0));
 						else {
 							child.setVillage(villageDao.findByCommune(commune).get(0));
 							child.setAddress(villageName);
 						} 						
					}
					
					child.setCurrentCaretaker((short)0);
					child.setCreationDate(new Date());
					child.setAuthor(name);
					child.setBarcodeDate(null);
					child.setLocked(false);					
					if (finishedDate != null) {
						child.setFinishedDate(finishedDate);
					} else if (bcg != null && dpt1 != null && dpt2 != null && dpt3 != null && opv1 != null && opv2 != null && opv3 != null && measles != null) {
						Date maxDate = dpt3.getTime() > opv3.getTime() ? dpt3 : opv3;
						maxDate = maxDate.getTime() > measles.getTime() ? maxDate : measles;
						child.setFinishedDate(maxDate);
					} else
						child.setFinishedDate(null);
					int year = child.getDateOfBirth().getYear() + 1900;
					if (child.getFullName() != null && !child.getFullName().equalsIgnoreCase(""))
						child.setFirstName(child.getFullName().substring(child.getFullName().lastIndexOf(" ") + 1));
					if (child.getMotherName() != null && !child.getMotherName().equalsIgnoreCase(""))
						child.setMotherFirstName(child.getMotherName().substring(child.getMotherName().lastIndexOf(" ") + 1));
					child.setChildCode(commune.getDistrict().getProvince().getProvinceId() + commune.getDistrict().getDistrictId() +  commune.getCommuneId() + "-" + year);
					child = childrenDao.save(child);
					String code = childrenDaoExt.generateChildCode(child);
					child.setChildCode(code);
					log.debug("child code: " + code);
					child = childrenDao.save(child);
					
					ChildrenVaccinationHistory 	newVH = new ChildrenVaccinationHistory();
					
					if (helpb1 != null) {
						log.debug("VGB " + helpb1);
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setReasonIfMissed("");
						if (helpb1ontime != null)
							newVH.setOverdue(helpb1ontime);
						newVH.setVaccinated((short)1);			
						newVH.setVaccination(vvgb);
						newVH.setDateOfImmunization(helpb1);
						newVH.setVaccinatedLocation(child.getVillage().getCommune());
						childrenVaccinationHistoryDao.save(newVH);
					} else {
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setOverdue(false);
						newVH.setReasonIfMissed("");
						newVH.setVaccinated((short)0);			
						newVH.setVaccination(vvgb);
						childrenVaccinationHistoryDao.save(newVH);
					}
					
					if (bcg != null) {
						log.debug("BCG " + bcg);
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setReasonIfMissed("");
						//newVH.setOverdue(false);
						newVH.setVaccinated((short)1);			
						newVH.setVaccination(vbcg);
						newVH.setDateOfImmunization(bcg);
						newVH.setVaccinatedLocation(child.getVillage().getCommune());
						childrenVaccinationHistoryDao.save(newVH);
					} else {
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setOverdue(false);
						newVH.setReasonIfMissed("");
						newVH.setVaccinated((short)0);			
						newVH.setVaccination(vbcg);
						childrenVaccinationHistoryDao.save(newVH);
					}
					
					if (dpt1 != null) {
						log.debug("DPT1 " + dpt1);
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setReasonIfMissed("");
						//newVH.setOverdue(false);
						newVH.setVaccinated((short)1);			
						newVH.setVaccination(vdpt1);
						newVH.setDateOfImmunization(dpt1);
						newVH.setVaccinatedLocation(child.getVillage().getCommune());
						childrenVaccinationHistoryDao.save(newVH);
					} else {
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setOverdue(false);
						newVH.setReasonIfMissed("");
						newVH.setVaccinated((short)0);			
						newVH.setVaccination(vdpt1);
						childrenVaccinationHistoryDao.save(newVH);
					}
					
					if (opv1 != null) {
						log.debug("OPV1 " + opv1);
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setReasonIfMissed("");
						//newVH.setOverdue(false);
						newVH.setVaccinated((short)1);			
						newVH.setVaccination(vopv1);
						newVH.setDateOfImmunization(opv1);
						newVH.setVaccinatedLocation(child.getVillage().getCommune());
						childrenVaccinationHistoryDao.save(newVH);
					} else {
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setOverdue(false);
						newVH.setReasonIfMissed("");
						newVH.setVaccinated((short)0);			
						newVH.setVaccination(vopv1);
						childrenVaccinationHistoryDao.save(newVH);
					}
					
					if (dpt2 != null) {
						log.debug("DPT2 " + dpt2);
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setReasonIfMissed("");
						//newVH.setOverdue(false);
						newVH.setVaccinated((short)1);			
						newVH.setVaccination(vdpt2);
						newVH.setDateOfImmunization(dpt2);
						newVH.setVaccinatedLocation(child.getVillage().getCommune());
						childrenVaccinationHistoryDao.save(newVH);
					} else {
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setOverdue(false);
						newVH.setReasonIfMissed("");
						newVH.setVaccinated((short)0);			
						newVH.setVaccination(vdpt2);
						childrenVaccinationHistoryDao.save(newVH);
					}
					
					if (opv2 != null) {
						log.debug("OPV2 " + opv2);
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setReasonIfMissed("");
						//newVH.setOverdue(false);
						newVH.setVaccinated((short)1);			
						newVH.setVaccination(vopv2);
						newVH.setDateOfImmunization(opv2);
						newVH.setVaccinatedLocation(child.getVillage().getCommune());
						childrenVaccinationHistoryDao.save(newVH);
					} else {
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setOverdue(false);
						newVH.setReasonIfMissed("");
						newVH.setVaccinated((short)0);			
						newVH.setVaccination(vopv2);
						childrenVaccinationHistoryDao.save(newVH);
					}
					
					if (dpt3 != null) {
						log.debug("DPT3 " + dpt3);
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setReasonIfMissed("");
						//newVH.setOverdue(false);
						newVH.setVaccinated((short)1);			
						newVH.setVaccination(vdpt3);
						newVH.setDateOfImmunization(dpt3);
						newVH.setVaccinatedLocation(child.getVillage().getCommune());
						childrenVaccinationHistoryDao.save(newVH);
					} else {
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setOverdue(false);
						newVH.setReasonIfMissed("");
						newVH.setVaccinated((short)0);			
						newVH.setVaccination(vdpt3);
						childrenVaccinationHistoryDao.save(newVH);
					}
					
					if (opv3 != null) {
						log.debug("OPV3 " + opv1);
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setReasonIfMissed("");
						//newVH.setOverdue(false);
						newVH.setVaccinated((short)1);			
						newVH.setVaccination(vopv3);
						newVH.setDateOfImmunization(opv3);
						newVH.setVaccinatedLocation(child.getVillage().getCommune());
						childrenVaccinationHistoryDao.save(newVH);
					} else {
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setOverdue(false);
						newVH.setReasonIfMissed("");
						newVH.setVaccinated((short)0);			
						newVH.setVaccination(vopv3);
						childrenVaccinationHistoryDao.save(newVH);
					}
					
					if (measles != null) {
						log.debug("Measles " + measles);
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setReasonIfMissed("");
						//newVH.setOverdue(false);
						newVH.setVaccinated((short)1);			
						newVH.setVaccination(vmeasles);
						newVH.setDateOfImmunization(measles);
						newVH.setVaccinatedLocation(child.getVillage().getCommune());
						childrenVaccinationHistoryDao.save(newVH);
					} else {
						newVH = new ChildrenVaccinationHistory();
						newVH.setChild(child);
						newVH.setOverdue(false);
						newVH.setReasonIfMissed("");
						newVH.setVaccinated((short)0);			
						newVH.setVaccination(vmeasles);
						childrenVaccinationHistoryDao.save(newVH);
					}										
					log.debug("--------------------------------------");
				}
				
			}			
        
	    } catch(Exception e) {

	    }
	}
}
