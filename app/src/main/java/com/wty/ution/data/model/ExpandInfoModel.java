package com.wty.ution.data.model;

import com.wty.ution.data.dalex.ExpandinfoDALEx;

public class ExpandInfoModel {

	public ExpandinfoDALEx[] expandfieldinfo;

	public ExpandinfoDALEx[] getResponse_params() {
		return expandfieldinfo;
	}

	public void setResponse_params(ExpandinfoDALEx[] response_params) {
		this.expandfieldinfo = response_params;
	}
}
