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
	
	@Autowired
	private NocodeLib nocodeLib;
	
	
	// application.properties 값을 가져옴
	@Value("${nocode.service.uri}")
    private String serviceUri;

	
	private String stringTest2(Map<String, Object> datas, String tmpKey){
		String[] tmpKeys=tmpKey.split("\\.");
		
		if(tmpKeys.length == 1 ) return  datas.get(tmpKeys[0]).toString();
		else if(tmpKeys.length > 1 && datas.get(tmpKeys[0]).getClass().getName()=="java.util.HashMap") {
			Map<String, Object> reData = (Map<String, Object>) datas.get(tmpKeys[0]);
			String reKey=tmpKey.replaceAll(tmpKeys[0]+"\\.", "");			
			//System.out.println("reKey : " + reKey);
			//System.out.println("reData : " + reData);
			return stringTest2( reData , reKey);			
		}
		return "";
	}
	
	// DB테스트
	@RequestMapping("/stringTest1")
	@ResponseBody
	String stringTest1(HttpServletRequest request) throws Exception{
		String returnData;
		returnData = "[{\"seq\":1,\"content\":\"test1\",\"regDate\":\"2022-11-25T22:00:21.000+00:00\"}]";
		return returnData;
	}
	
	// DB테스트
	@RequestMapping("/stringTest")
	@ResponseBody
	public Map<String, Object> stringTest(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();
		
		// datas 샘플
		Map<String, Object> datas = new HashMap<>();
		Map<String, Object> paramData = new HashMap<>();
		Map<String, Object> varData = new HashMap<>();
		paramData.put("seq", "1");
		paramData.put("seq2", "2");
		paramData.put("seq3", "3");
		
		varData.put("abc", "test1");
		datas.put("params", paramData);
		datas.put("varData", varData);
		
		System.out.println("datas : " + datas.get("params"));

		
		// 쿼리 문자열을 가공하여 ? 표시 변경
		String queryOrigin="SELECT * FROM board WHERE seq IN ({:params.seq},{:params.seq2},{:params.seq3})";
		String queryReplace=queryOrigin.replaceAll("\\{:[^\\}]*\\}","?");
		System.out.println("result : " + queryReplace);
		
		// 리스트에 패턴을 담는다
		List<String> preparedStatement = new ArrayList<String>();
		Matcher m = Pattern.compile("\\{:[^\\}]*\\}").matcher(queryOrigin);
		String tmpKey="";
		String tmpValue="";
		while (m.find()) { 
			tmpKey=m.group().toString().replaceAll("\\{:","").replaceAll("\\}","");
			tmpValue=stringTest2(datas,tmpKey);
			
			//System.out.println("tmpValue : " + tmpValue);
			preparedStatement.add(tmpValue);
			
		}
		
		System.out.println("queryArgs : " + preparedStatement);
		System.out.println("toArray1 : " + Arrays.toString(preparedStatement.toArray()));
		List<Object> objectList = new ArrayList<Object>(preparedStatement);

		
		System.out.println("queryExec1 : " + nocodeDao.queryExec(queryReplace,objectList.toArray()));
		returnData.put("queryExec1",nocodeDao.queryExec(queryReplace,objectList.toArray()));
		//System.out.println("queryExec2 : " + nocodeDao.queryExec(queryReplace,[1,2]));
		//System.out.println("queryExec2 : " + nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (?,?)",preparedStatement.toArray()));
		//returnData.put("Servdb-sql00", nocodeDao.queryExec(queryReplace,preparedStatement.toArray()));

		
		
		
	
		
		//returnData.put("Servdb-sql", Collections.singletonMap("test", ServDao.servExec()));		
		returnData.put("Servdb-sql2", nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (0)"));
		//int i=0;
		//Object[] args = { 1, 2 };
		
		List<String> argsList = new ArrayList<String>();
		argsList.add("3");
		argsList.add("2");
		
		System.out.println("toArray2 : " + Arrays.toString(argsList.toArray()));

		//returnData.put("Servdb-sql3", nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (?,?)",argsList.toArray()));
		returnData.put("Chibumps", serviceUri );
		
		return returnData;
		//return Collections.singletonMap("test", ServDao.servExec());
	}	
	
	// DB테스트
	@RequestMapping("/Servdb")
	@ResponseBody
	public Map<String, Object> Servdb(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();
		
		//returnData.put("Servdb-sql", Collections.singletonMap("test", nocodeDao.servExec()));		
		//returnData.put("Servdb-sql1", nocodeDao.servExec());
		//returnData.put("Servdb-sql2", nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (0)"));
		int i=0;
		Object[] args = { 1, 2 };
		System.out.println("args1 : " + args);
		returnData.put("Servdb-sql3", nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (?,?)",args));
		//returnData.put("Chibumps", serviceUri );
		
		return returnData;
		//return Collections.singletonMap("test", ServDao.servExec());
	}
	

	/*
	// 파라메터 메핑어노테이션 테스트
	@RequestMapping("/paramTest")
	@ResponseBody
	public Map<String, Object> serv(HttpServletRequest request,@RequestParam Map<String, Object> paramMap) throws Exception{		
		System.out.println(paramMap.get("abc"));
		return this.serv_(request);
	}
	
	
	// 테스트!!
	@RequestMapping("/myApi2/*")
	@ResponseBody
	public Map<String, Object> serv_(HttpServletRequest request) throws Exception{

		Map<String, Object> returnData = new HashMap<>();

		switch(request.getRequestURI()) {
			case "/myApi2/httpInfo":
				returnData = nocodeLib.httpInfo(request);
				break;
			case "/myApi2/testReq":
				returnData = this.testReq(request);
				break;
			case "/myApi2/mapTest":
				returnData = this.mapTest(request);
				break;
			case "/myApi2/mapTest2":
				//returnData = this.mapTest(request);
				returnData.put("test", "123");
				//returnData.put("direct query select", nocodeDao.queryExec("SELECT * FROM `board` WHERE seq NOT IN (0)"));
				returnData.put("direct query update", nocodeDao.queryExecUpdate("UPDATE board SET content='test1' WHERE seq=8"));
				break;				
			default:
				returnData = nocodeLib.httpInfo(request);
				break;
		}
		return returnData;

	}
	// 테스트!!
	@RequestMapping("/myApi2/mapTest2")
	@ResponseBody
	public Map<String, Object> mapTest2(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();
		returnData.put("direct query update", nocodeDao.queryExecUpdate("UPDATE board SET content='test1' WHERE seq=8"));
		return returnData;

	}
	
	// 테스트 - 차후 삭제필요
	public Map<String, Object> testReq(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();

		returnData.put("httpInfo", "123");
		return returnData;
	}
	// 테스트 - 차후 삭제필요
	public Map<String, Object> mapTest(HttpServletRequest request) throws Exception{
		Map<String, Object> returnData = new HashMap<>();
		Map<String, Object> processData = new HashMap<>();
		processData.put("var1", "SELECT * FROM board");
		processData.put("var2", "SELECT * FROM board");
		returnData.put("version", "1");
		returnData.put("process", processData);
		returnData.put("return", "SELECT * FROM borad WHERE seq={:params.seq}");
		
		System.out.println(processData.getClass());
		
		return returnData;
	}*/
	
	
	
	// 서비스콜 함수 - 얘만사용!!
	@RequestMapping(path="/myApi/{serviceID}")
	//@RequestMapping(value=this.serviceUri)
	//https://backendcode.tistory.com/228
	//@RequestMapping("/"+serviceUri+"/{serviceID}")
	//@RequestMapping("/*api*/{serviceID}")
	@ResponseBody
	public List<Map<String,Object>> serviceCall(HttpServletRequest request,@PathVariable("serviceID") String serviceID) throws Exception{		
		Map<String, Object> requestData = nocodeLib.httpInfo(request);
		requestData.put("serviceID", serviceID);		
		List<Map<String,Object>> returnData = nocodeLib.serviceCall(requestData);
		/*List<Map<String,Object>> returnData = new ArrayList<>();
		Map<String,Object> tmpMap = new HashMap<>();
		tmpMap.put("1", "2");
		returnData.add(0,tmpMap);*/		
		return returnData;
	}
	/*public Map<String, Object> serviceCall(HttpServletRequest request,@PathVariable("myApi") String myApi,@PathVariable("serviceID") String serviceID) throws Exception{		
		Map<String, Object> requestData = nocodeLib.httpInfo(request);
		requestData.put("myApi", myApi);		
		requestData.put("serviceID", serviceID);		
		Map<String, Object> returnData = nocodeLib.serviceCall(requestData);		
		return returnData;
	}*/
}
