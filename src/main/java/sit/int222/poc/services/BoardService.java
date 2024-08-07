package sit.int222.poc.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sit.int222.poc.project_management.Board;
import sit.int222.poc.project_management.BoardRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
}
