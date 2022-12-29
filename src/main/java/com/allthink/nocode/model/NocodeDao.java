package com.allthink.nocode.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;

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
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);	
		//System.out.println(list);
		return list;
	  }
}