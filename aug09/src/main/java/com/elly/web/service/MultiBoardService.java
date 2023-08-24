package com.elly.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elly.web.dao.MultiBoardDAO;

@Service
public class MultiBoardService {

	@Autowired
	private MultiBoardDAO mbDAO;

	public List<Map<String, Object>> list(int board) {
		return mbDAO.list(board);
	}

	public int mbwrite(Map<String, Object> map) {
		return mbDAO.mbwrite(map);
	}

	public Map<String, Object> mbdetail(int mbno) {
		return mbDAO.mbdetail(mbno);
	}

	public List<Map<String, Object>> boardlist() {
		return mbDAO.boardlist();
	}
	
	
}
