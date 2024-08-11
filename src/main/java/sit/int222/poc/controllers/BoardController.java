package sit.int222.poc.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int222.poc.dto.SimpleBoardResponse;
import sit.int222.poc.project_management.Board;
import sit.int222.poc.services.BoardService;

import java.util.List;

@RestController
@RequestMapping("/poc/api/boards")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<Board>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    @GetMapping("/test/private")
    public ResponseEntity<List<SimpleBoardResponse>> getAllPrivateBoards() {
        return ResponseEntity.ok(boardService.getAllPrivateBoards());
    }

    @GetMapping("/test/accessible")
    public ResponseEntity<List<SimpleBoardResponse>> getAllAccessibleBoards() {
        return ResponseEntity.ok(boardService.getAllAccessibleBoards());
    }
}
