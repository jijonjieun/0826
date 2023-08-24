package com.elly.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elly.web.dto.BoardDTO;
import com.elly.web.service.BoardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller	
public class BoardController {
	
	@Autowired
	private BoardService boardservice;
	
	
	@GetMapping("/board")
	public String board(Model model) {
	List<BoardDTO> list =  boardservice.boardList();
		
	model.addAttribute("list",list);
		return "board";
	}	
	
	
	@ResponseBody
	@PostMapping("/detail")
	public String detail(@RequestParam("bno") int bno) {
		// System.out.println(bno);
		
		BoardDTO dto = boardservice.detail(bno);


		//JSONObject json = new JSONObject();
		// JSONObject e = new JSONObject();
		ObjectNode json = JsonNodeFactory.instance.objectNode();
		json.put("content", dto.getBcontent());
		json.put("uuid", dto.getUuid());
		json.put("ip", dto.getBip());


		
		
		return json.toString();
	}

	
	
	@GetMapping("/write")
	public String write() {
		return "write";
	}
	
	
	@PostMapping("/write")
	public String write(HttpServletRequest request, HttpSession session) {
		//System.out.println(request.getParameter("title"));
		//System.out.println(request.getParameter("content"));
		BoardDTO dto = new BoardDTO();
		dto.setBtitle(request.getParameter("title"));
		dto.setBcontent(request.getParameter("content"));
	dto.setM_id(String.valueOf(session.getAttribute("mid")));
		//세션은 오브젝트상태로있기때문에 스트링밸류오브로 스트링으로바꿔줌
		
		int result = boardservice.write(dto);
		System.out.println(result);
		
		
		return "redirect:/board";
		
	}
	
	
	
	@PostMapping("/delete")
	public String delete(BoardDTO dto) {
		System.out.println(dto.getBno());
		return "redirect:/board";
	}
	
	
	@ResponseBody
	@PostMapping("/detail2")
	public String detail2(@RequestParam("bno") int bno) throws JsonProcessingException {
		BoardDTO detail = boardservice.detail2(bno);
		ObjectMapper mapp = new ObjectMapper();
		String json = mapp.writeValueAsString(detail);
		

		return json;
	}
	
	
	
	
	
	
	
	
	

}
