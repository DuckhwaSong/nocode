package com.allthink.nocode.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;


@Component
public class NocodeDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*public Map<String, Object> servExec() throws Exception{
		return JdbcTemplate.queryForMap("SELECT * FROM `board` WHERE seq IN (1)");		
	}*/
	
	public List<Map<String,Object>> servExec() {	
		String sql = "SELECT * FROM `board` WHERE seq NOT IN (0)";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);	
		//System.out.println(list);
		return list;
	  }
	
	public List<Map<String,Object>> queryExec(String sql) {	
		System.out.println("sql : " + sql);
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);	
		System.out.println(list);
		return list;
	}
	public List<Map<String,Object>> queryExec(String sql, Object[] args) {
		List<Map<String,Object>> returnData = new ArrayList<>();
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, args);
		returnData.addAll(0, list);

		/*
		List<Map<String,Object>> returnData = new ArrayList<>();
		Map<String,Object> tmpMap = new HashMap<>();
		tmpMap.put("1", "2");
		returnData.add(0,tmpMap);
		*/
		return list;
	}
	
	// 업데이트 쿼리 실행
	public int queryExecUpdate(String sql) {	
		System.out.println("sql : " + sql);
		return jdbcTemplate.update(sql);	
	}
	
	// 쿼리 리턴 타입을 설정 list 형과 int 형 
	public String queryType(String sql) {
		if(Pattern.compile("^SELECT(.*)", Pattern.CASE_INSENSITIVE).matcher(sql).matches()) return "LIST";	// 셀렉트
		if(Pattern.compile("^SHOW(.*)", Pattern.CASE_INSENSITIVE).matcher(sql).matches()) return "LIST";	// 셀렉트
		return "INT";
	}
}