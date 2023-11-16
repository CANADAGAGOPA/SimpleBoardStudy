package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller //  Controller는 화면(View)과 비즈니스 로직(Model)를 연결시키는 다리 역할
public class BoardController {

    @Autowired // BoardRepository와 연결
    private BoardService boardService;

    @GetMapping("/board/write") // local:8090/board/write 요청이 들어오면 아래의 함수를 실행
    public String boardWriteForm() {

        return "boardwrite"; // templates 에서 "boardwrite"라는 이름의 View 를 찾아서 반환
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model) { //

        boardService.write(board); // BoardService 와 연결되어 있다.

        model.addAttribute("message", "게시글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/list") // local:8090/board/list 요청이 들어오면 아래의 함수를 실행
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {

        Page<Board> list = null;
        // 객체는 페이징된 데이터를 나타내며, 여기에서는 게시판(Board) 엔터티에 대한 페이지를 나타내기 위한 변수를 초기화

        if(searchKeyword == null) {
            list = boardService.boardList(pageable);
        }else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

     // model.addAttribute("list", boardService.boardList());
        model.addAttribute("list", list); // 뷰로 전달할 데이터를 "list"란 이름으로 모델에 추가
        // 스프링 MVC에서 뷰에 데이터를 전달하기 위한 코드
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        // 뷰에게 보여줄 정보들을 model.addAttribute 메서드를 통해 모델에 추가

        return "boardlist";
    }

    @GetMapping("/board/view") // localhost:8090/board/view?id=1
    public String boardView(Model model, Integer id) {

        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model) {

        boardService.boardDelete(id);

        model.addAttribute("message", "게시글 삭제가 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model) throws IOException {

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp);

        model.addAttribute("message", "게시글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }
}
