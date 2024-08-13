package sit.int222.poc.project_management;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
  List<Board> findAllByIsPublicFalse();

  List<Board> findAllByIsPublicTrue();

  List<Board> findAllByOwnerIdAndIsPublicFalse(String oid);
}