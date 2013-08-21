package org.hil.core.model.util;

import java.util.List;

public class PageableList<T> {

	public PageableList() {
	}

	/**
	 * @param desc
	 * @param firstResult
	 * @param maxResults
	 * @param orderedField
	 */
	public PageableList(Integer firstResult,Integer maxResults,String orderedField,Boolean desc)
	{
		this.desc = desc;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
		this.orderedField = orderedField;
	}

	private String orderedField;
	 public String getOrderedField() {
		return orderedField;
	}

	public void setOrderedField(String orderedField) {
		this.orderedField = orderedField;
	}

	private Boolean desc;
	
	 public Boolean getDesc() {
		return desc;
	}

	public void setDesc(Boolean desc) {
		this.desc = desc;
	}

	private Integer firstResult;
		private Integer maxResults;
	    public Integer getFirstResult() {
			return firstResult;
		}

		public void setFirstResult(Integer firstResult) {
			this.firstResult = firstResult;
		}

		public Integer getMaxResults() {
			return maxResults;
		}

		public void setMaxResults(Integer maxResults) {
			this.maxResults = maxResults;
		}


	
	public PageableList(List<T> values, Integer firstResult, Integer maxResults, Integer rowCount) {
        this.resultList = values;
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.resultCount = rowCount;
    }

    private List<T> resultList;
   
    private Integer resultCount;

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> values) {
        this.resultList = values;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer rowCount) {
        this.resultCount = rowCount;
    }

}