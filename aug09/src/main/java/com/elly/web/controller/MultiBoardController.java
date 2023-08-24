package com.elly.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.elly.web.service.MultiBoardService;

@Controller
public class MultiBoardController {

	@Autowired
	private MultiBoardService multiBoardService;

	@GetMapping("/multiboard")
	public String multiboard(@RequestParam(value = "board", required = false, defaultValue = "1") int board,
			Model model) {

		
		//화면에 보여줄 게시판 목록도 가져오기
		List<Map<String, Object>> boardlist = multiBoardService.boardlist();
		List<Map<String, Object>> list = multiBoardService.list(board);
		model.addAttribute("boardlist", boardlist);
		model.addAttribute("list", list);
		System.out.println(list);

		return "multiboard";
	}

	@GetMapping("/mbwrite")
	public String mbwrite(@RequestParam(value = "board", required = false, defaultValue = "1") int board, Model model,
			HttpSession session) {
		// 로그인 사용자만 접근하도록
		if (session.getAttribute("mid") != null) {
			model.addAttribute("board", board);
			return "mbwrite";
		} else {
			return "redirect:/login?error=login";
			//에러를 같이 실어보냄
		}

	}
	
	@PostMapping("/mbwrite")
	public String mbwrite(@RequestParam Map<String, Object> map, HttpSession sessoin) {
		//{mb_del=true, mb_content=첫번째글, m_no=4, mb_read=1, mb_date=10:32, m_name=정재현, mb_board=1, mb_no=1, mb_title=첫글, m_id=jd0214}
		//로그인 유무
		if (sessoin.getAttribute("mid") != null) {
				//selectkey기법
		map.put("mid", sessoin.getAttribute("mid"));	
		
		
		//int result =  multiBoardService.mbwrite(map);
		multiBoardService.mbwrite(map);
		//mb_no라는 이름으로 마지막 게시판번호를 뽑아옴
		//System.out.println(map);
		//System.out.println(mb_no);
		return "redirect:/mbdetail?board="+map.get("board")+"&mbno="+map.get("mb_no");
		} else {
			return "redirect:/login?error=login";
		}
		
	}
	
	
	@GetMapping("/mbdetail")
	public String mbDtail(@RequestParam(value="mbno", required = true) int mbno, Model model) {
		System.out.println(mbno);
		Map<String, Object> detail = multiBoardService.mbdetail(mbno);
		model.addAttribute("detail", detail);
		return "mbdetail";
	}
	
}
	
	
	
	

