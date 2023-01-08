package com.allthink.nocode.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;
//import java.util.Arrays;

@Component
public class NocodeDao {
	
	@Autowired
	private static JdbcTemplate jdbcTemplate;
	
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
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);	
		//System.out.println(list);
		return list;
	}
	
	
	/*public List<Map<String,Object>> queryExec(String sql,List psArray) {	
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);	
		//System.out.println(list);
		return list;
	}*/
	
	// queryForList(String sql, Object[] args, int[] argTypes)
	  public static List<Map<String,Object>> queryExec(String sql, Object[] args) {
		
		//System.out.println("sql : " + sql);
		//System.out.println("args : " + Arrays.toString(args));
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, args);
		
		//int[] argTypes = { java.sql.Types.CHAR, java.sql.Types.INTEGER };		// 인자가 2개인 경우 타입 		
		//List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, args, argTypes);
		return list;
	  }
}