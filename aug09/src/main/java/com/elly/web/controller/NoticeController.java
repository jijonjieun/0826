package com.elly.web.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elly.web.service.NoticeService;
import com.elly.web.util.Util;

@Controller
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private Util util;
	
	@GetMapping("/notice")
	public String notice(Model model) {
		//필요한거? model + map + service + DAO + Mapper
	// 리스트를 jsp에 찍어주기위해 모델을 이용함
		
		
		List<Map<String, Object>> list = noticeService.list();
		model.addAttribute("list", list);
		System.out.println(list);
		
		return "notice";
	
	}
	
	
	//2023-08-22
	//노티스디테일
	@GetMapping("/noticeDetail")
	public String noticeDetail(@RequestParam("nno") int nno, Model model) {
		System.out.println(nno);
		Map<String, Object> detail =  noticeService.detail(nno);
		model.addAttribute("detail",detail);
		return "noticeDetail";
		
	}
	
	
	//다운로드처리하기 /download@파일명
	@ResponseBody
	@GetMapping("/download@{fileName}")
	public void download(@PathVariable("fileName")String filename, HttpServletResponse response) {
		//System.out.println(filename);
		String path = util.uploadPath();
		//System.out.println(path);
		String oriFileName = noticeService.getOriFileName(filename);
			
		//8비트로 데이터를 내보내겠다?...
		File serverSideFile = new File(path, filename);	
		try {
			byte[] fileByte = FileCopyUtils.copyToByteArray(serverSideFile);
			response.setContentType("application/octet-stream");
	          response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(oriFileName, "UTF-8")+"\";");
	          response.setHeader("Content-Transfer-Encoding", "binary");
	         response.getOutputStream().write(fileByte);
	         response.getOutputStream().flush();
	         response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	
	}
	
	
	
	
	
	
	
	
}
