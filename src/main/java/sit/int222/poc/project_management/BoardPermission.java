package sit.int222.poc.project_management;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "board_permissions", schema = "project_management")
public class BoardPermission {
    @Id
    @Column(name = "id", nullable = false)
    private Byte id;

    @Size(max = 45)
    @NotNull
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Size(max = 200)
    @Column(name = "description", length = 200)
    private String description;

}