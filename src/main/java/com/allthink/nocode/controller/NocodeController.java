package com.allthink.nocode.controller;

import java.util.HashMap;
//import java.util.List;
import java.util.Map;
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
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.allthink.nocode.model.NocodeDao;
import com.allthink.nocode.library.NocodeLib;


@Controller
public class NocodeController {

	//private Logger logger = LoggerFactory.getLogger(ServiceController.class);

	
	@Autowired
	private NocodeDao nocodeDao;
	
	// application.properties 값을 가져옴
	@Value("${nocode.service.uri}")
    private String serviceUri;

	
	@RequestMapping("/Servdb")
	@ResponseBody
	public Map<String, Object> Servdb(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();
		
		//returnData.put("Servdb-sql", Collections.singletonMap("test", ServDao.servExec()));		
		returnData.put("Servdb-sql2", nocodeDao.servExec());
		returnData.put("Chibumps", serviceUri );
		
		return returnData;
		//return Collections.singletonMap("test", ServDao.servExec());
	}
	
	
	
	@RequestMapping("/serv")
	@ResponseBody
	public Map<String, Object> serv(HttpServletRequest request) throws Exception{
		return this.serv_(request);
	}
	
	@RequestMapping("/serv/*")
	@ResponseBody
	public Map<String, Object> serv_(HttpServletRequest request) throws Exception{

		Map<String, Object> returnData = new HashMap<>();

		switch(request.getRequestURI()) {
			case "/serv/httpInfo":
				returnData = NocodeLib.httpInfo(request);
				break;
			case "/serv/testReq":
				returnData = this.testReq(request);
				break;
			default:
				returnData = NocodeLib.httpInfo(request);
				break;
		}
		return returnData;

	}
	
	public Map<String, Object> testReq(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();

		returnData.put("httpInfo", "123");
		return returnData;
	}

	
}