package unrn.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTweetRequest {

    @NotBlank
    private String userName;

    @NotBlank
    @Size(max = 280)
    private String text;

    public CreateTweetRequest() {
    }

    public CreateTweetRequest(String userName, String text) {
        this.userName = userName;
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}