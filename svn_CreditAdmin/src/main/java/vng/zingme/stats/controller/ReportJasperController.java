package vng.zingme.stats.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vng.zingme.stats.model.ReportDetail;
import vng.zingme.stats.model.ReportSummary;
import vng.zingme.stats.model.TranByApp;
import vng.zingme.stats.service.AppService;
import vng.zingme.stats.service.CommonService;
import vng.zingme.stats.service.ReportService;
import au.com.bytecode.opencsv.CSVWriter;

@Controller
@RequestMapping("/")
public class ReportJasperController {
	@Autowired
	private ReportService reportService;
	@Autowired
	private CommonService common;
	@Autowired
	private AppService appService;
	private static final Logger log = Logger
			.getLogger(ReportJasperController.class);

	@RequestMapping(value = "ajax/reportsummaryjasper", method = RequestMethod.GET)
	public ModelAndView getPdfReportSummary(HttpServletResponse response,
			@RequestParam("startDate") String startDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("type") String type) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();

		List<ReportSummary> reportSummaryList = reportService.getReportSummary(
				startDate, toDate);

		JRDataSource JRdataSource = new JRBeanCollectionDataSource(
				reportSummaryList);
		parameterMap.put("datasource", JRdataSource);

		ModelAndView modelAndView = null;
		// xlsReport bean has ben declared in the jasper-views.xml file
		if ("csv".equals(type)) {
			modelAndView = new ModelAndView("csvReport", parameterMap);
		} else if ("pdf".equals(type)) {
			modelAndView = new ModelAndView("pdfReport", parameterMap);
		} else if ("xls".equals(type)) {
			modelAndView = new ModelAndView("xlsReport", parameterMap);
		}
		return modelAndView;
	}

	/**
	 * @param response
	 * @param startDate
	 * @param toDate
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "ajax/reportdetailjasper", method = RequestMethod.GET)
	public ModelAndView getFullData(HttpServletResponse response,
			@RequestParam("startDate") String startDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("type") String type) throws IOException {
		List<TranByApp> appListGet = appService.getListAppOfLoggingUser();
		List<String> appList = new ArrayList<>();
		for (TranByApp tranByApp : appListGet) {
			if (!"zing".equals(tranByApp.getAppId())
					&& !"admin".equals(tranByApp.getAppId())) {
				appList.add(tranByApp.getAppId());
			}
		}

		List<ReportDetail> reportSummaryList = reportService.getReportDetail(
				startDate, toDate, appList);
		response.setContentType("text/csv;charset=utf-8");
		response.setHeader("Content-Disposition",
				"inline; filename=reportDetail.csv");

		OutputStream resOs = response.getOutputStream();
		OutputStream buffOs = new BufferedOutputStream(resOs);
		OutputStreamWriter outputwriter = new OutputStreamWriter(buffOs);
		CSVWriter writer = new CSVWriter(outputwriter, ',');
		try {
			int size = appListGet.size();
			String[] columnName = new String[size + 5];
			columnName[0] = "ngay";
			columnName[1] = "ton dau ky";
			columnName[2] = "tong nhap";
			// String [] columnName = {"ngay", "ton dau ky","tong nhap",
			// "xuat trong ky","tong xuat","ton cuoi ky" };
			int i = 3;
			for (String appID : appList) {
				columnName[i] = appID;
				i++;
			}
			columnName[i] = "tong xuat";
			columnName[i + 1] = "ton cuoi ky";

			writer.writeNext(columnName);
			for (int j = 0; j < reportSummaryList.size(); j++) {
				String[] data = new String[size + 5];
				data[0] = reportSummaryList.get(j).getDate();
				data[1] = reportSummaryList.get(j).getOpeningBalance();
				data[2] = reportSummaryList.get(j).getIncome();
				List<TranByApp> appAmount = reportSummaryList.get(j).getApps();
				int k = 3;
				for (TranByApp tranByApp : appAmount) {
					data[k] = tranByApp.getAmount();
					k++;
				}
				data[k] = reportSummaryList.get(j).getClosingBalance();
				data[k + 1] = reportSummaryList.get(j).getPayroll();
				writer.writeNext(data);
			}

			buffOs.flush();
			outputwriter.flush();
			writer.flush();

		} catch (IOException e) {
			Log.error(e.getMessage());
		} finally {
			writer.close();
			outputwriter.close();
			buffOs.close();
		}
		return null;
	}

	/**
	 * @param response
	 * @param startDate
	 * @param toDate
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "ajax/reportzingxu", method = RequestMethod.GET)
	public ModelAndView getReportZingXu(HttpServletResponse response,
			@RequestParam("startDate") String startDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("type") String type,
			@RequestParam("appId") List<String> appIdList) throws IOException {

		/*
		 * List<TranByApp> appListGet = appService.getListAppOfLoggingUser();
		 * List<String> appList = new ArrayList<>(); for (TranByApp tranByApp :
		 * appListGet) { appList.add(tranByApp.getAppId()); }
		 */
		List<String> appList = new ArrayList<>();
		for (String appId : appIdList) {
			if ("0".equals(appId)) {
				List<TranByApp> appListGet = appService
						.getListAppOfLoggingUser();
				for (TranByApp tranByApp : appListGet) {
					if (!"zing".equals(tranByApp.getAppId())
							&& !"admin".equals(tranByApp.getAppId())) {
						appList.add(tranByApp.getAppId());
					}
				}
				break;
			} else {
				appList.add(appId);
			}
		}		

		List<ReportDetail> reportSummaryList = reportService.getReportDetail(
				startDate, toDate, appList);
		response.setContentType("text/csv;charset=utf-8");
		response.setHeader("Content-Disposition",
				"inline; filename=reportZingxu.csv");

		OutputStream resOs = response.getOutputStream();

		OutputStream buffOs = new BufferedOutputStream(resOs);
		OutputStreamWriter outputwriter = new OutputStreamWriter(buffOs);

		CSVWriter writer = new CSVWriter(outputwriter, ',');
		try {
			// List<TranByApp> list = appService.getListAppOfLoggingUser();
			int size = appList.size();
			String[] columnName = new String[size + 2];
			columnName[0] = "ngay";
			columnName[1] = "tong ZingXu Nap";
			// String [] columnName = {"ngay", "ton dau ky","tong nhap",
			// "xuat trong ky","tong xuat","ton cuoi ky" };
			int i = 2;
			for (String app : appList) {
				columnName[i] = app;
				i++;
			}

			writer.writeNext(columnName);
			for (int j = 0; j < reportSummaryList.size(); j++) {
				String[] data = new String[size + 2];
				data[0] = reportSummaryList.get(j).getDate();
				data[1] = reportSummaryList.get(j).getIncome();
				List<TranByApp> appAmount = reportSummaryList.get(j).getApps();
				int k = 2;
				for (TranByApp tranByApp : appAmount) {
					data[k] = tranByApp.getAmount();
					k++;
				}
				writer.writeNext(data);
			}

			buffOs.flush();
			outputwriter.flush();
			writer.flush();
		} catch (IOException e) {
			Log.error(e.getMessage());
		} finally {
			writer.close();
			outputwriter.close();
			buffOs.close();
		}
		return null;
	}

}
