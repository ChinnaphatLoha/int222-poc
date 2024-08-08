package sit.int222.poc.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import sit.int222.poc.dto.PrivateBoardResponse;
import sit.int222.poc.project_management.Board;
import sit.int222.poc.project_management.BoardRepository;
import sit.int222.poc.user_account.User;
import sit.int222.poc.user_account.UserRepository;
import sit.int222.poc.utils.ListMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ListMapper listMapper;
    private final ModelMapper modelMapper;

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public List<PrivateBoardResponse> getAllPrivateBoards() {
        List<Board> boards = boardRepository.findAllByIsPublicFalse();
        return listMapper.map(boards, this::mapBoardToPrivateBoardResponse);
    }

    private PrivateBoardResponse mapBoardToPrivateBoardResponse(Board board) {
        PrivateBoardResponse response = new PrivateBoardResponse();
        modelMapper.map(board, response);

        User owner = userRepository.findById(board.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        response.setOwner(owner);

        return response;
    }
}
