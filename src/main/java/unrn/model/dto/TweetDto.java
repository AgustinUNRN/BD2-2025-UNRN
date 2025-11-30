package unrn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class TweetDto {
    private int id;
    private String userName;
    private String text;
    private Date createdAt;
}
