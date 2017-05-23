package color.measurement.com.from_cp20.module.other;

/**
 * Created by wpc on 2017/3/30.
 */

class FAQ {

    String question;
    String response;

    public FAQ(String question, String response) {
        this.question = question;
        this.response = response;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
