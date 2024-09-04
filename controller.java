package com.dongbu.mobile.mobileaccident.biz.page.paymentGuarantee.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import alib.errormsg.ErrorMsg;
import alib.util.AMask;

import com.dongbu.mobile.mobileaccident.biz.common.BizCommon;
import com.dongbu.mobile.mobileaccident.biz.esb.request.CCLM0191Q;
import com.dongbu.mobile.mobileaccident.biz.esb.request.CCLM0770Q;
import com.dongbu.mobile.mobileaccident.biz.esb.request.CCLM1062Q;
import com.dongbu.mobile.mobileaccident.biz.esb.request.CCLM1082Q;
import com.dongbu.mobile.mobileaccident.biz.esb.response.CCLM0770;
import com.dongbu.mobile.mobileaccident.biz.esb.response.CCLM0770B;
import com.dongbu.mobile.mobileaccident.biz.esb.response.CCLM1082B;
import com.dongbu.mobile.mobileaccident.biz.page.claimadjustment.domain.ClaimAdjustmentReportVo;
import com.dongbu.mobile.mobileaccident.biz.page.main.domain.AccInfoVo;
import com.dongbu.mobile.mobileaccident.biz.page.main.mapper.MainMapper;
import com.dongbu.mobile.mobileaccident.biz.page.notice.service.NoticeService;
import com.dongbu.mobile.mobileaccident.biz.page.notice.vo.AccInfoForPGVo;
import com.dongbu.mobile.mobileaccident.biz.page.paymentGuarantee.service.PytGuService;
import com.dongbu.mobile.mobileaccident.biz.page.paymentGuarantee.vo.PytGuInfoVo;
import com.dongbu.mobile.mobileaccident.session.SessionMan;


@Controller
public class PytGuController {

	@Resource(name = "MainMapper")
	private MainMapper mainDao;

	private static final Logger LOGGER = LoggerFactory.getLogger(PytGuController.class);

	@Autowired
	PytGuService pytGuService;

	@RequestMapping(value = "/pytGuGps.do", method = RequestMethod.GET)
	public String pytGuGps(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		LOGGER.info("pytGuGps");

		boolean bRet = false;
		
			ErrorMsg errMsg = new ErrorMsg();
			String resultPage = "";
		
		try {
			LOGGER.info("pytGuGps");
			
			
			double lat1 = 37.506037;
			LOGGER.info("lat1 : "+ lat1);
			
			double lon1 = 127.054780;
			LOGGER.info("lon1 : "+ lon1);
			
			double lat2 = 37.508915;
			LOGGER.info("lat2 : "+ lat2);
			
			double lon2 = 127.062288;
			LOGGER.info("lon2 : "+ lon2);
			
			double distance = calculateHaversineDistance(lat1, lon1, lat2, lon2);
			LOGGER.info("두 좌표 사이의 거리 : "+ distance+"km");
			
			String hospLatitude = request.getParameter("hospLatitude");
			LOGGER.info("hospLatitude : "+hospLatitude);
			String hospLongitude = request.getParameter("hospLongitude");
			LOGGER.info("hospLongitude : "+hospLongitude);

			resultPage = "pages/pytGuGps";
			bRet = true;
		} catch (Exception e) {
			errMsg.appendMsg("내원여부 조회 중 오류가 발생했습니다.", e, LOGGER);
		} finally {
			if (!bRet) {
				System.out.println("!bRet");
				// 로그 프린트
				errMsg.logPrint(LOGGER);

				model.addAttribute("title", "오류가 발생했습니다!");
				model.addAttribute("errMsg", errMsg.getLastMsg());

				resultPage = "msg/errorMsg";
			}
		}
		LOGGER.info("resultPage : "+resultPage);
		return resultPage;
	}
	
	@RequestMapping(value = "/pytGuGpsEnd.do", method = RequestMethod.GET)
	public String pytGuGpsEnd(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		LOGGER.info("pytGuGpsEnd");

		boolean bRet = false;
		
			ErrorMsg errMsg = new ErrorMsg();
			String resultPage = "";

		try {
			LOGGER.info("pytGuGpsEnd");

			resultPage = "pages/pytGuGpsEnd";
			bRet = true;
		} catch (Exception e) {
			errMsg.appendMsg("내원여부 조회 중 오류가 발생했습니다.", e, LOGGER);
		} finally {
			if (!bRet) {
				System.out.println("!bRet");
				// 로그 프린트
				errMsg.logPrint(LOGGER);

				model.addAttribute("title", "오류가 발생했습니다!");
				model.addAttribute("errMsg", errMsg.getLastMsg());

				resultPage = "msg/errorMsg";
			}
		}
		LOGGER.info("resultPage : "+resultPage);
		return resultPage;
	}
	
	@RequestMapping(value = "/pytGuGpsError.do", method = RequestMethod.GET)
	public String pytGuGpsError(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		LOGGER.info("pytGuGpsError");

		boolean bRet = false;
		
			ErrorMsg errMsg = new ErrorMsg();
			String resultPage = "";
		
		try {

			resultPage = "pages/pytGuGpsError";
			bRet = true;
		} catch (Exception e) {
			errMsg.appendMsg("내원여부 조회 중 오류가 발생했습니다.", e, LOGGER);
		} finally {
			if (!bRet) {
				System.out.println("!bRet");
				// 로그 프린트
				errMsg.logPrint(LOGGER);

				model.addAttribute("title", "오류가 발생했습니다!");
				model.addAttribute("errMsg", errMsg.getLastMsg());

				resultPage = "msg/errorMsg";
			}
		}
		LOGGER.info("resultPage : "+resultPage);
		return resultPage;
	}
	
	@RequestMapping(value = "/pytGu.do", method = RequestMethod.GET)
	public String pytGu(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		System.out.println("pytGu.do START");
		boolean bRet = false;

		ErrorMsg errMsg = new ErrorMsg();
		String resultPage = "";
		String no = request.getParameter("no") == null ? "" : (String)request.getParameter("no");
		System.out.println("pyt_no : "+no);
		
		String system_send_yn = request.getParameter("system_send_yn") == null ? "" : (String)request.getParameter("system_send_yn");
		
		
		String dmg_ojt_nm = request.getParameter("dmg_ojt_nm") == null ? "" : (String)request.getParameter("dmg_ojt_nm");
		System.out.println("dmg_ojt_nm : "+dmg_ojt_nm);
		
		String dmg_tlno = request.getParameter("dmg_tlno") == null ? "" : (String)request.getParameter("dmg_tlno");
		System.out.println("dmg_tlno : "+dmg_tlno);
		
		String accd_rpt_no = request.getParameter("accd_rpt_no") == null ? "" : (String)request.getParameter("accd_rpt_no");
		System.out.println("accd_rpt_no : "+accd_rpt_no);
		
		String brth = request.getParameter("brth") == null ? "" : (String)request.getParameter("brth");
		System.out.println("brth : "+brth);
		
		
		try {
			//QR코드 Test
//			if (no.length() < 9)
//				throw new Exception("접수번호 오류입니다.");
			
			
			String sAcc_No = "";
			String sAcc_Dambo = "";
			String sAcc_Seyoul = "";

			String[] ss = no.split("-");
			System.out.println("pyt_ss : "+ss);

			sAcc_No = ss[0];
			sAcc_No = BizCommon.convertAccNoToNew(sAcc_No);
			if (ss.length == 3) {
				sAcc_Dambo = ss[1];
				sAcc_Seyoul = ss[2];
			}
			
			System.out.println("pyt_sAcc_No : "+sAcc_No);
			System.out.println("pyt_sAcc_Dambo : "+sAcc_Dambo);
			System.out.println("pyt_sAcc_Seyoul : "+sAcc_Seyoul);
			//모바일사고조회 접속이력 DB저장
			Map paramMap = new HashMap();
			paramMap.put("USE_ACC_NO", sAcc_No);
			paramMap.put("USE_ACC_DAMBO", sAcc_Dambo);
			paramMap.put("USE_ACC_SEYOUL", sAcc_Seyoul);
			paramMap.put("USE_USER_NM", "");
			paramMap.put("USE_REQ_JOB_ID", "pytGu.do");
			
			mainDao.insertUserLog(paramMap);
			System.out.println("pyt_insert_MOACC_USE_LOG");
			
			//지불보증정보 조회
			System.out.println("pytGuService.getPytGu START");
			HashMap<String, String> pytGuInfo = pytGuService.getPytGu(sAcc_No, sAcc_Dambo, sAcc_Seyoul);
			System.out.println("pytGuService.getPytGu END");
			
			if(pytGuInfo.isEmpty()) {
				throw new Exception("조회데이터가 없습니다.");
			}
			
			pytGuInfo.put("accd_rpt_no", sAcc_No);				//사고번호
			pytGuInfo.put("accd_oj_rnk", sAcc_Seyoul);			//서열
			pytGuInfo.put("dambo", sAcc_Dambo);					//담보 21,22:정비공장, 그 외 병원 DISPLAY
			pytGuInfo.put("system_send_yn", system_send_yn);	//시스템 발송여부
			
			
			resultPage = "pages/pytGu";
			
			HttpSession session = request.getSession(true);
			session.setAttribute("pytGuInfo", pytGuInfo);
			System.out.println("pytGuInfo : "+pytGuInfo);
			System.out.println("pyt_HttpSession SET = pytGuInfo");
			System.out.println("pyt_resultPage : "+resultPage);
			bRet = true;
		} catch (Exception e) {
			errMsg.appendMsg("지불보증 정보 조회 중 오류가 발생헀습니다.", e, LOGGER);
		} finally {
			if (!bRet) {
				// 로그 프린트
				errMsg.logPrint(LOGGER);

				model.addAttribute("title", "오류가 발생했습니다!");
				model.addAttribute("msg", errMsg.getLastMsg());

				resultPage = "msg/endMsgCommon";
			}
		}

		return resultPage;
	}
	
	@RequestMapping(value = "/pytGuInfo.do")
	public String goPage_pytGuInfo(HttpServletRequest request, ModelMap model) 
			throws Exception 
	{		
		LOGGER.info("### pytGuInfo.do ###");
		return getPytGuInfo(request, model, "pages/pytGuInfo", "/mobileacc/pytGuInfo.do");
	}
	
	public String getPytGuInfo(HttpServletRequest request, ModelMap model, String resultPage, String errPage) throws Exception {
		System.out.println("/pytGuInfo.do");
		boolean bRet = false;

		ErrorMsg errMsg = new ErrorMsg();
		HttpSession session = request.getSession(true);
		System.out.println("session : "+session);
		
		HashMap<String, String> dataMap = (HashMap<String, String>)session.getAttribute("dataMap");
		System.out.println("dataMap : "+dataMap);
		
		List<PytGuInfoVo> result = new ArrayList<PytGuInfoVo>();
	
		
		String dmg_ojt_nm = request.getParameter("dmg_ojt_nm") == null ? "" : (String)request.getParameter("dmg_ojt_nm");
		System.out.println("dmg_ojt_nm : "+dmg_ojt_nm);
		
		String dmg_tlno = request.getParameter("dmg_tlno") == null ? "" : (String)request.getParameter("dmg_tlno");
		System.out.println("dmg_tlno : "+dmg_tlno);
		
		String accd_rpt_no = request.getParameter("accd_rpt_no") == null ? "" : (String)request.getParameter("accd_rpt_no");
		System.out.println("accd_rpt_no : "+accd_rpt_no);
		
		String brth = request.getParameter("brth") == null ? "" : (String)request.getParameter("brth");
		System.out.println("brth : "+brth);
		
		try {
			//QR코드 지불보증 TEST
//			if(dataMap == null) {
//				throw new Exception("세션이 만료되었습니다. 받으신 URL로 재접속해 주시기 바랍니다.");
//			}

			CCLM1082Q req = new CCLM1082Q();
			
			req.setBz_dvcd("06");
			req.setAccd_rpt_no(dataMap.get("accd_rpt_no"));
			System.out.println("dataMap_accd_rpt_no : "+dataMap.get("accd_rpt_no"));
			
			req.setAccd_oj_rnk(dataMap.get("accd_oj_rnk"));
			req.setAccd_oj_dvcd(dataMap.get("dambo"));
			req.setCust_tlno(dataMap.get("cust_tlno"));
			
//			req.setBzaq_no(bzaq_no);
//			req.setSx_cd(sx_cd);
			req.setBrth(brth);
//			req.setBzaq_knd_cd(bzaq_knd_cd);
//			req.setCre_dvcd(cre_dvcd);
//			req.setCrny_pss_tm(crny_pss_tm);
			
			req.setDmg_ojt_nm(dmg_ojt_nm);
			req.setAccd_rpt_no(accd_rpt_no);
			System.out.println("parameter_accd_rpt_no : "+accd_rpt_no);

			
//			CCLM1082B res = pytGuService.savePytGuInfo(req);
			System.out.println("pytGuService.savePytGuInfo START");
			result = pytGuService.savePytGuInfo(req);
			System.out.println("pytGuService.savePytGuInfo END");		
			
			if(result == null) {
				throw new Exception("저장에 실패하였습니다.");
			}
			resultPage = "pages/pytGuInfo";
			System.out.println("resultPage : "+resultPage);	
			bRet = true;
		} catch (Exception e) {
			errMsg.appendMsg("지불보증정보 처리 중 오류가 발생헀습니다.", e, LOGGER);
		} finally {
			if (!bRet) {
				// 로그 프린트
				errMsg.logPrint(LOGGER);
				model.addAttribute("result", "N");
				model.addAttribute("errorMsg", errMsg.getLastMsg());
				
				model.addAttribute("url", errPage);
				
				resultPage = "msg/errorMsg";
			}else {
				model.addAttribute("result", "Y");
				model.addAttribute("pytGuInfo", result);
				LOGGER.info("model = "+model);
			}
		}
		LOGGER.info("resultPage = "+resultPage);
//		return "jsonView";
		return resultPage;
	}
	
	@RequestMapping(value = "/pytGuEnd.do", method = RequestMethod.GET)
	public String pytGuEnd(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		LOGGER.info("pytGuEnd");

		boolean bRet = false;
		
		//사고접수번호, *이름, *전화번호, 생년월일 (* = 필수값), 위-경도
			ErrorMsg errMsg = new ErrorMsg();
			String resultPage = "";
			//String no = request.getParameter("no");
			
			//String cust_lat = request.getParameter("lat");
			//String cust_lon = request.getParameter("lon"); 
			
		try {
			LOGGER.info("pytGuEnd");
			
			resultPage = "pages/pytGuEnd";
			bRet = true;
		} catch (Exception e) {
			errMsg.appendMsg("완료처리 중 오류가 발생했습니다.", e, LOGGER);
		} finally {
			if (!bRet) {
				System.out.println("!bRet");
				// 로그 프린트
				errMsg.logPrint(LOGGER);

				model.addAttribute("title", "오류가 발생했습니다!");
				model.addAttribute("errMsg", errMsg.getLastMsg());

				resultPage = "msg/errorMsg";
			}
		}
		LOGGER.info("resultPage : "+resultPage);
		return resultPage;
	}
	
	@RequestMapping(value = "/pytGuExpiry.do", method = RequestMethod.GET)
	public String pytGuExpiry(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		LOGGER.info("pytGuExpiry");

		boolean bRet = false;
		
		//사고접수번호, *이름, *전화번호, 생년월일 (* = 필수값), 위-경도
			ErrorMsg errMsg = new ErrorMsg();
			String resultPage = "";
			//String no = request.getParameter("no");
			
			//String cust_lat = request.getParameter("lat");
			//String cust_lon = request.getParameter("lon"); 
			
		try {
			LOGGER.info("pytGuExpiry");
			
			resultPage = "pages/pytGuExpiry";
			bRet = true;
		} catch (Exception e) {
			errMsg.appendMsg("사고조회 중 오류가 발생했습니다.", e, LOGGER);
		} finally {
			if (!bRet) {
				System.out.println("!bRet");
				// 로그 프린트
				errMsg.logPrint(LOGGER);

				model.addAttribute("title", "오류가 발생했습니다!");
				model.addAttribute("errMsg", errMsg.getLastMsg());

				resultPage = "msg/errorMsg";
			}
		}
		LOGGER.info("resultPage : "+resultPage);
		return resultPage;
	}
	
	public static double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
		// 지구 반지름(km)
		final int R = 6371;
		
		double latDistance = Math.toRadians(lat2 - lat1);
		
		double lonDistance = Math.toRadians(lon2 - lon1);
		
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance /2);
		System.out.println("distance(a) : "+ a);
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		System.out.println("distance(c) : "+ c);
		
		System.out.println("R*c = "+ R * c);
		return R * c;
	}

	public static double deg2rad(double degrees) {
		return degrees * (Math.PI / 180.0);
	}
	
	public static double rad2deg(double radians) {
		return radians * (180.0 / Math.PI);
	}
	
}
