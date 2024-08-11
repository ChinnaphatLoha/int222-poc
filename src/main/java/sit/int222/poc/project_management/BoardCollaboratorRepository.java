package sit.int222.poc.project_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardCollaboratorRepository extends JpaRepository<BoardCollaborator, Long> {

    @Query("SELECT bc.board FROM BoardCollaborator bc WHERE bc.collaboratorId = ?1")
    List<Board> findAllByCollaboratorId(Integer collaboratorId);
}