package color.measurement.com.from_cp20.module.main.obsolete.message_board;

/**
 * Created by wpc on 2017/3/27.
 */

public class Message {

    Integer userId;
    String portraitURL;
    String name;

    String content;
    Integer responseMessageId;

    public Message(Integer userId, String portraitURL, String name, String content) {
        this.userId = userId;
        this.portraitURL = portraitURL;
        this.name = name;
        this.content = content;
    }

    public Message(String content) {
        userId = null;
        this.content = content;
    }
}
