package com.cloud.common.util;

import java.io.Serializable;

public class BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * @fieldName: id
	 * @fieldType: Integer
	 * @Description: 用户Id
	 */
	private Integer id;
	/**
	 * @fieldName: orgId
	 * @fieldType: String
	 * @Description: 组织Id
	 */
	private Integer orgId;
	/**
	 * @fieldName: pageNum
	 * @fieldType: Integer
	 * @Description: 请求第几页
	 */
	private Integer pageNum=1;
	/**
	 * @fieldName: limitSize
	 * @fieldType: Integer
	 * @Description: 每页条数
	 */
	private Integer limitSize=10;

	public BaseDTO() {

		super();
	}

	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {

		this.id = id;
	}


	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getPageNum() {

		return pageNum;
	}

	public void setPageNum(Integer pageNum) {

		this.pageNum = pageNum;
	}

	public Integer getLimitSize() {

		return limitSize;
	}

	public void setLimitSize(Integer limitSize) {

		this.limitSize = limitSize;
	}
}
