package unrn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {
    private String userName;
    private String email;
    private List<TweetDto> tweets;
    private List<ReTweetDto> reTweets;

}
