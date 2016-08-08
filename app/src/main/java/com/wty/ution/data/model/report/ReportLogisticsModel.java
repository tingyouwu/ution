package com.wty.ution.data.model.report;

import com.wty.ution.util.CommonUtil;
import com.xtion.sheet.model.ISheetRowModel;

import java.util.HashMap;
import java.util.Map;

public class ReportLogisticsModel implements ISheetRowModel {

	private String logisticsid;                             // 物流信息id
	private String xwcontactid;                             // 联系人id
	private int xwsource;                          //出发地
	private int xwdestination;               //目标地址
	private String xwprice;               //价格
	private String xwcreatetime;                //创建时间
	private String xwdate;                //发货时间

	@Override
	public String getCellText() {
		return "";
	}

	@Override
	public Map<String, String> getCellValue() {
		Map<String, String> result = new HashMap<String,String>();

		result.put("logisticsid",""+logisticsid);
		result.put("xwsource",""+xwsource);
		result.put("xwdestination",""+xwdestination);

		result.put("xwprice",""+xwprice);
		result.put("xwdate", CommonUtil.dateToYYYYMMdd(xwdate));

		return result;
	}
}
