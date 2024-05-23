package bodyclasses.request;

import constants.APIConstants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class AddEventsBatch {
    List<Event> events = new ArrayList<>();

    public List<Event> getEventBatches() {
        return events;
    }

    public void setEventBatches(List<Event> events) {
        this.events = events;
    }

    public AddEventsBatch(List<Event> events) {
        this.events = events;
    }
    @Step("Send correct POST add_events_batch request")
    public static Response addEventsBatch(List<Event> events){
        Response response =given()
                .header("Content-type", "application/json")
                .header("x-rewind-api","M3jKf30r8KWrlLrlVd6VPfEYpFmmErxP")
                .header("x-rewind-signature","84e695d6dbc2f1cdf57bd1c9660504b954b4e73f38f9fded4574001b044877ad")
                .and()
                .body(events)
                .when()
                .post(APIConstants.ADD_EVENTS_BATCH);
        return response;
    }
    @Step("Send correct POST add_events_batch request")
    public static Response addEventsBatch(){
        Event event = new Event(true);
        List<Event> events_request = new ArrayList<>();
        events_request.add(event);
        Response response =given()
                .header("Content-type", "application/json")
                .header("x-rewind-api","M3jKf30r8KWrlLrlVd6VPfEYpFmmErxP")
                .header("x-rewind-signature","84e695d6dbc2f1cdf57bd1c9660504b954b4e73f38f9fded4574001b044877ad")
                .and()
                .body(events_request)
                .when()
                .post(APIConstants.ADD_EVENTS_BATCH);
        return response;
    }
}
