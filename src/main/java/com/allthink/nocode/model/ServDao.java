package com.allthink.nocode.model;

//import java.util.List;
//import org.apache.ibatis.annotations.Mapper;
//import org.springframework.stereotype.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ServDao {
	
	@Autowired
	private JdbcTemplate JdbcTemplate;
	
	public Map<String, Object> servExec() throws Exception{
		return JdbcTemplate.queryForMap("SELECT * FROM `board` WHERE seq=1");
	}

}