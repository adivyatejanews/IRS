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

package org.hil.core.model.vo;

import java.util.List;

public class ListChildrenPrintVO {
	private String communeName;
	private String dueDate;
	private List<ChildrenDuePrintVO> children;

	/**
	 * @return the children
	 */
	public List<ChildrenDuePrintVO> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<ChildrenDuePrintVO> children) {
		this.children = children;
	}

	/**
	 * @return the communeName
	 */
	public String getCommuneName() {
		return communeName;
	}
	
	public void setCommuneName(String communeName) {
		this.communeName = communeName;
	}
	
	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
}
