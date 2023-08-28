package com.elly.web.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elly.web.service.AdminService;
import com.elly.web.util.Util;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/admin") // 어드민폴더아래 있는 애들은 이쪽으로 옴
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private Util util;

	@GetMapping("/")
	public String adminIndex2() {
		return "redirect:/admin/admin";
	}

	// adminservice /admindao / adminMapper

	// 어드민 중복으로 사용하지 않아도됨.
	@GetMapping("/admin")
	public String adminIndex() {

		return "admin/index";
		// 폴더안에 abmin폴더안에 index.jsp (jsp파일은 영향안받아서 폴더이름작아줘ㅏ야함)
	}

	@PostMapping("/login")
	public String adminLogin(@RequestParam Map<String, Object> map, HttpSession session) {
		System.out.println(map);

		Map<String, Object> result = adminService.adminLogin(map);

		System.out.println(result);

		if (util.obj2Int(result.get("count")) == 1 && util.obj2Int(result.get("m_grade")) > 5) {
			// 세션올리기
			session.setAttribute("mid", map.get("id"));
			session.setAttribute("mname", result.get("m_name"));
			session.setAttribute("mgrade", result.get("m_grade"));
			// 메인으로 이동하기
			return "redirect:/admin/main";
		} else {
			return "redirect:/admin/admin?error=login";
		}

	}

	@GetMapping("/main")
	public String main() {
		return "admin/main"; // jsp경로라서 폴더를 적어주야함.
	}

	@GetMapping("/notice")
	public String notice(Model model) {
		// 1데이터베이스까지 연결하기
		// 2데이터 불러오기
		List<Map<String, Object>> list = adminService.list();
		// 3데이터 jsp로보내기
		model.addAttribute("list", list);
		return "admin/notice";
	}

	@PostMapping("/noticeWrite")
	public String noticeWrite(@RequestParam("upFile") MultipartFile upfile, @RequestParam Map<String, Object> map) {

		// System.out.println(map);
		// {title=ddd, content=dddddd, upFile=}

		// 요구사항확인
		if (!upfile.isEmpty()) {
			// 저장할 경로명 뽑기 실제 경로를 뽑아준다.request 뽑기
			HttpServletRequest request = 
		((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			String path =  request.getServletContext().getRealPath("/upload");
			//실제업로드파일주소.
			//System.out.println("실제 경로 : " + path);
			// upfile정보보기
			//System.out.println(upfile.getOriginalFilename());
			//실제파일이름
			//System.out.println(upfile.getSize());
			//System.out.println(upfile.getContentType());
			// 진짜로 파일 업로드하기 경로 + 저장할 파일명
			//String타입의 경로를 file형태로 바꾸어줍니다.
			//File filepath = new File(path);
			//중복이 발생할 수 있기때문에 
//uuid+파일명+.확장자
			//uuid+파일명+아이디/확장자
			
			UUID uuid =  UUID.randomUUID();
			//String realFileName = uuid + upfile.getOriginalFilename();
			
			//날짜뽑기
			LocalDateTime ldt = LocalDateTime.now();
			String format = ldt.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss"));
			String realFileName = format+ uuid.toString()+upfile.getOriginalFilename();
			//날짜, uuid, 실제 사용자가 저장한 파일이름
			
			File newFileName = new File(path, realFileName);
			//파일올리기
			try {
				upfile.transferTo(newFileName);
				//파일을 실제 내서버로 복사하기. 사용자가 파일을 업로드했다면 ()안에있는 경로로 복사해.
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("저장끝");
			
		
/*			//FileCopyUtils를 사용하기 위해서는 오리지널 파일을  byte[]로 만들어야 합니다.
			try {
				FileCopyUtils.copy(upfile.getBytes(), newFileName);
				//사용자의실제파일을 바이트로 저장합니다, newFileName의 이름으로 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
			
		
		//http://localhost/admin/notice

		map.put("upFile", upfile.getOriginalFilename());
		map.put("realFile", realFileName );
		
		}
		
		map.put("mno", 1);
		adminService.noticeWrite(map);
		System.out.println(map);
	

		return "redirect:/admin/notice";
}
	
	//메일보내기
	@GetMapping("/mail")	
	public String mail() {
		return "admin/mail";
	}
	
	
	@PostMapping("/mail")
	public String mail(@RequestParam Map<String, Object> map) throws EmailException {
		// util.simpleMailSender(map);
		util.htmlMailSender(map);
		
		return "admin/mail";
	}
	
	
	//noticeDetail
	@ResponseBody
	@PostMapping("/noticeDetail")
	public String noticeDetail(@RequestParam("nno") int nno) {
		System.out.println(nno);
		
		
		ObjectNode json = JsonNodeFactory.instance.objectNode();
		String result = adminService.detail(nno);
		json.put("content", result);
		

		return json.toString();
	}
	
	
	//noticeHide
	@ResponseBody
	@PostMapping("/noticeHide")
	public String noticeHide(@RequestParam("nno") int nno) {
	int result = adminService.noticeHide(nno);
	ObjectNode json = JsonNodeFactory.instance.objectNode();
	json.put("result", result);
		return json.toString();
		
	}
	
	
	//20230824 어플리케이션 테스트 수행
	//밸류만있다면 겟과 포스트 동시에 받을거라는 뜻이다.
	@RequestMapping(value = "/multiBoard", method = RequestMethod.GET)
	public String multiBoard(Model model) {
		//setup보드 내용을 가져와서 아래 jsp에 찍어주세요.
		List<Map<String, Object>> setupList = adminService.setupList();
		model.addAttribute("setupList", setupList);
		
		return "admin/multiBoard";
	}
	
	
	// /multiboard 2023-08-25 어플리케이션 테스트 수행
		@RequestMapping(value="/multiBoard", method = RequestMethod.POST)
		public String multiBoard(@RequestParam Map<String, String> map) {
			//DB에 저장하기
			int result = adminService.multiBoardInsert(map);
			System.out.println("result : " + result);
			return "redirect:/admin/multiBoard";
		}
		
		//member
		@RequestMapping(value="/member", method = RequestMethod.GET)
		public ModelAndView member() {
			ModelAndView mv = new ModelAndView("admin/member");
			mv.addObject("memberList", adminService.memberList());
			return mv;
		}
		
		//gradeChange	
		@RequestMapping(value="/gradeChange", method = RequestMethod.GET)
		public String gradeChange(@RequestParam Map<String, String> map) {
			int result = adminService.gradeChange(map);
			System.out.println(result);
			return "redirect:/admin/member";
		}
		
		
		@GetMapping("/post")
		public String post(Model model, @RequestParam Map<String, Object> map) {
			//name=cate가 있기때문에 cate다음 값만 변수cate로 들어오고 나머지는 변수 map으로 들어옴
			//게시판 번호가 들어올 수 있습니다. 추후처리
			List<Map<String, Object>> boardlist = adminService.boardList();
			//게시판 관리번호를 다 불러옵니다.
			//게시글을 다 불러옵니다.
			List<Map<String, Object>> list = adminService.post(map);

			
			if(!(map.containsKey("cate")) || map.get("cate").equals(null) || map.get("cate").equals("")) {
				map.put("cate",0);
		
			}
			System.out.println("뽑아오나요"+map.get("cate"));
			System.out.println("cate:" + map);
			System.out.println("검색:" + map);
			
			model.addAttribute("list", list);
			model.addAttribute("boardlist" , boardlist);
			return "/admin/post";
		}
		
		
		@ResponseBody
		@PostMapping("/postDetail")
		public String postDetail(@RequestParam("mbno") int mbno, Model model, Map<String, Object> map) {
			List<Map<String, Object>> result2 = adminService.postDetail(mbno);
			JSONObject json = new JSONObject();
			json.put("result2", result2);
			return json.toString();
			
		}
		
	

	
	
	
	
}
