package sit.int222.poc.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sit.int222.poc.dto.SimpleBoardResponse;
import sit.int222.poc.project_management.Board;
import sit.int222.poc.project_management.BoardCollaboratorRepository;
import sit.int222.poc.project_management.BoardRepository;
import sit.int222.poc.user_account.User;
import sit.int222.poc.user_account.UserRepository;
import sit.int222.poc.utils.ListMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCollaboratorRepository boardCollaboratorRepository;
    private final UserRepository userRepository;
    private final ListMapper listMapper;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public List<SimpleBoardResponse> getAllPrivateBoards() {
        List<Board> boards = boardRepository.findAllByIsPublicFalse();
        return listMapper.map(boards, this::mapBoardToSimpleBoardResponse);
    }

    public List<SimpleBoardResponse> getAllAccessibleBoards() {
        User user = userService.getCurrentUser();
        List<Board> boards = boardRepository.findAllByIsPublicTrue();
        List<Board> ownedBoards = boardRepository.findAllByOwnerIdAndIsPublicFalse(user.getId());
        List<Board> collaboratorBoards = boardCollaboratorRepository.findAllByCollaboratorId(user.getId());
        boards.addAll(ownedBoards);
        boards.addAll(collaboratorBoards);
        return listMapper.map(boards, this::mapBoardToSimpleBoardResponse);
    }

    private SimpleBoardResponse mapBoardToSimpleBoardResponse(Board board) {
        SimpleBoardResponse response = new SimpleBoardResponse();
        modelMapper.map(board, response);

        User owner = userRepository.findById(board.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        response.setOwner(owner);

        return response;
    }
}
