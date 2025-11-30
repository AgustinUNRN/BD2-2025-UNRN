package unrn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ReTweetDto {
    private int id;
    private String userRetweeted;
    private TweetDto originalTweet;
    private Date dateRetweeted;
}
