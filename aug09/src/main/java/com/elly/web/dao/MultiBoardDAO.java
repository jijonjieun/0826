package com.elly.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MultiBoardDAO {

	List<Map<String, Object>> list(int board);

	int mbwrite(Map<String, Object> map);

	Map<String, Object> mbdetail(int mbno);

	List<Map<String, Object>> boardlist();



}
