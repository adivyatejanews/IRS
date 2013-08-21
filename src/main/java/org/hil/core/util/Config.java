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

package org.hil.core.util;

public class Config {
	
	private String baseReportDir;	
	private String smsBeforeDays;
	private String smsMsg;
	private String javaLibPath;
	private String smsjar;
	private String smsModemPort;
	private String smsModemBaudRate;
	private String contextPath;
	
	/**
	 * @return the baseReportDir
	 */
	public String getBaseReportDir() {
		return baseReportDir;
	}

	public String getJavaLibPath() {
		return javaLibPath;
	}

	public void setJavaLibPath(String javaLibPath) {
		this.javaLibPath = javaLibPath;
	}

	public String getSmsjar() {
		return smsjar;
	}

	public void setSmsjar(String smsjar) {
		this.smsjar = smsjar;
	}

	public String getSmsModemPort() {
		return smsModemPort;
	}

	public void setSmsModemPort(String smsModemPort) {
		this.smsModemPort = smsModemPort;
	}

	public String getSmsModemBaudRate() {
		return smsModemBaudRate;
	}

	public void setSmsModemBaudRate(String smsModemBaudRate) {
		this.smsModemBaudRate = smsModemBaudRate;
	}

	/**
	 * @param baseReportDir the baseReportDir to set
	 */
	public void setBaseReportDir(String baseReportDir) {
		this.baseReportDir = baseReportDir;
	}

	public String getSmsBeforeDays() {
		return smsBeforeDays;
	}

	public void setSmsBeforeDays(String smsBeforeDays) {
		this.smsBeforeDays = smsBeforeDays;
	}

	public String getSmsMsg() {
		return smsMsg;
	}

	public void setSmsMsg(String smsMsg) {
		this.smsMsg = smsMsg;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}	
	
}