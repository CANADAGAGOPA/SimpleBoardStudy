package com.study.board.reposiory;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    // Jpa레포지토리를 상속, 제네릭<>으로 board엔티티와 pk로 지정한 값의 타입을 지정해준다.

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
    // 메서드는 title 속성에 특정 키워드가 포함된 게시물을 검색하기 위한 메서드

    // Page<Board> findByContentContaining(String searchKeyword, Pageable pageable);
}

// 이 인터페이스는 Board 엔티티와 관련된 CRUD (Create, Read, Update, Delete) 작업을 수행하기 위한 메서드를 제공