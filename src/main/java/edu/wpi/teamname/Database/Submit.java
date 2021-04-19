package edu.wpi.teamname.Database;

import edu.wpi.teamname.simplify.Config;
import org.json.JSONObject;

public class Submit {
    private static final Submit instance = new Submit();
    private Submit() {};

    public static synchronized Submit getInstance() {
        return instance;
    }

    String SERVER_URL = Config.getInstance().getServerUrl();

    public void UserRegistration(UserRegistration _form) {
        StringBuilder reasons = new StringBuilder();
        reasons.append("[");
        _form.getReasonsForVisit().forEach(r -> {
            reasons.append("'").append(r).append("', ");
        });
        reasons.setLength(reasons.length()-2);
        reasons.append("]");

        JSONObject data = new JSONObject();
        data.put("name", _form.getName());
        data.put("reasons", reasons.toString());
        data.put("date", _form.getDate());
        data.put("phone", _form.getPhoneNumber());
        data.put("ack", String.valueOf(_form.isAcknowledged()));
        data.put("ackAt", String.valueOf(_form.getAcknowledgedAt()));

        String url = SERVER_URL + "/api/submit-check-in";

        AsynchronousTask task = new AsynchronousTask(url, data, "POST");
        AsynchronousQueue.getInstance().add(task);
    }
}
