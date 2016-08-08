package com.wty.ution.data.model;

public class BasicDataResponseModel {

	public BasicDataInfoModel basicinfo;

	public BasicDataInfoModel getResponse_params() {
		return basicinfo;
	}

	public void setResponse_params(BasicDataInfoModel response_params) {
		this.basicinfo = response_params;
	}
}
