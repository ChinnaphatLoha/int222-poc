package sit.int222.poc.dto;

import lombok.Data;
import sit.int222.poc.user_account.User;

@Data
public class SimpleBoardResponse {
    private Long id;
    private String title;
    private String description;
    private Boolean isPublic;
    private User owner;

}
