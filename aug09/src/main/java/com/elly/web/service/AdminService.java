package com.elly.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elly.web.dao.AdminDAO;

@Service
public class AdminService {
	
	@Autowired
	private AdminDAO adminDAO;

	public Map<String, Object> adminLogin(Map<String, Object> map) {
		return adminDAO.adminLogin(map);
	

}

	public List<Map<String, Object>> list() {
		return  adminDAO.list();
	}

	public void noticeWrite(Map<String, Object> map) {
		adminDAO.noticeWrite(map);
	
		
	}

	public String detail(int nno) {
		return adminDAO.detail(nno);
	}

	public int noticeHide(int nno) {
		return adminDAO.noticeHide(nno);
	}

	public List<Map<String, Object>> setupList() {
		return adminDAO.setupList();
	}
	
	
	public int multiBoardInsert(Map<String, String> map) {
		return adminDAO.multiBoardInsert(map);
	}

	public List<Map<String, Object>> memberList() {
		return adminDAO.memberList();
	}

	public int gradeChange(Map<String, String> map) {
		return adminDAO.gradeChange(map);
	}

	public List<Map<String, Object>> post(Map<String, Object> map) {
		return adminDAO.post(map);
	}

	public List<Map<String, Object>> boardList() {
		return  adminDAO.boardList();
	}

	public List<Map<String, Object>> postDetail(int mbno) {
		return adminDAO.postDetail(mbno);
	}
	
}
