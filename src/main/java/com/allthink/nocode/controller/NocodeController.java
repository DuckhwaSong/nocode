package com.allthink.nocode.controller;

import java.util.HashMap;
import java.util.List;
//import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
//import java.io.DataInputStream;

//import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.allthink.nocode.model.NocodeDao;
import com.allthink.nocode.library.NocodeLib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

@Controller
public class NocodeController {
	//private Logger logger = LoggerFactory.getLogger(ServiceController.class);

	
	@Autowired
	private NocodeDao nocodeDao;
	//private NocodeLib nocodeLib;
	
	
	// application.properties 값을 가져옴
	@Value("${nocode.service.uri}")
    private String serviceUri;

	
	// DB테스트
	@RequestMapping("/stringTest")
	@ResponseBody
	public Map<String, Object> stringTest(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();
		
		// 쿼리 문자열을 가공하여 ? 표시 변경
		String queryOrigin="SELECT * FROM board WHERE seq IN ({:params.seq},{:params.seq2})";
		//String queryReplace=queryOrigin.replaceAll("\\{:*\\}","?");
		String queryReplace=queryOrigin.replaceAll("\\{:.*\\}","?");
		System.out.println("result : " + queryReplace);
		
		// 리스트에 패턴을 담는다
		List<String> allMatches = new ArrayList<String>();		
		Matcher m = Pattern.compile("\\{:.*\\}").matcher(queryOrigin);
		while (m.find()) { allMatches.add(m.group()); }
		
		System.out.println("queryArgs : " + allMatches);

		
		//returnData.put("Servdb-sql", Collections.singletonMap("test", ServDao.servExec()));		
		returnData.put("Servdb-sql2", nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (0)"));
		//int i=0;
		//Object[] args = { 1, 2 };
		
		List<String> argsList = new ArrayList<String>();
		argsList.add("3");
		argsList.add("2");
		
		returnData.put("Servdb-sql3", nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (?,?)",argsList.toArray()));
		returnData.put("Chibumps", serviceUri );
		
		return returnData;
		//return Collections.singletonMap("test", ServDao.servExec());
	}	
	
	// DB테스트
	@RequestMapping("/Servdb")
	@ResponseBody
	public Map<String, Object> Servdb(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();
		
		//returnData.put("Servdb-sql", Collections.singletonMap("test", ServDao.servExec()));		
		returnData.put("Servdb-sql2", nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (0)"));
		int i=0;
		Object[] args = { 1, 2 };
		returnData.put("Servdb-sql3", nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (?,?)",args));
		returnData.put("Chibumps", serviceUri );
		
		return returnData;
		//return Collections.singletonMap("test", ServDao.servExec());
	}
	

	
	// 파라메터 메핑어노테이션 테스트
	@RequestMapping("/paramTest")
	@ResponseBody
	public Map<String, Object> serv(HttpServletRequest request,@RequestParam Map<String, Object> paramMap) throws Exception{		
		System.out.println(paramMap.get("abc"));
		return this.serv_(request);
	}
	
	
	// 테스트!!
	@RequestMapping("/myApi/*")
	@ResponseBody
	public Map<String, Object> serv_(HttpServletRequest request) throws Exception{

		Map<String, Object> returnData = new HashMap<>();

		switch(request.getRequestURI()) {
			case "/myApi/httpInfo":
				returnData = NocodeLib.httpInfo(request);
				break;
			case "/myApi/testReq":
				returnData = this.testReq(request);
				break;
			default:
				returnData = NocodeLib.httpInfo(request);
				break;
		}
		return returnData;

	}
	
	// 테스트 - 차후 삭제필요
	public Map<String, Object> testReq(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();

		returnData.put("httpInfo", "123");
		return returnData;
	}

	
	
	
	// 서비스콜 함수 - 얘만사용!!
	@RequestMapping(path="/myApi/{serviceID}")
	//@RequestMapping(value=this.serviceUri)
	@ResponseBody
	public Map<String, Object> serviceCall(HttpServletRequest request,@PathVariable("serviceID") String serviceID) throws Exception{		
		Map<String, Object> requestData = NocodeLib.httpInfo(request);		
		requestData.put("serviceID", serviceID);		
		Map<String, Object> returnData = NocodeLib.serviceCall(requestData);		
		return returnData;
	}
}