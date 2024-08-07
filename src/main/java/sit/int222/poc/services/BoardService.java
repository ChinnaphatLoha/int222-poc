package sit.int222.poc.config.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sit.int222.poc.project_management.BoardRepository;

@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
}
