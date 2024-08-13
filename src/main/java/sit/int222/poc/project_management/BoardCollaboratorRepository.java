package sit.int222.poc.project_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardCollaboratorRepository extends JpaRepository<BoardCollaborator, Long> {
    @Query("SELECT b FROM Board b JOIN BoardCollaborator bc ON b.id = bc.board.id WHERE bc.collaboratorId = ?1")
    List<Board> findAllByCollaboratorId(String oid);
}