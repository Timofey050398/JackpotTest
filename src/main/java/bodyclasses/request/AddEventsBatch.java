package bodyclasses.request;

import bodyclasses.builders.HeaderBuilder;
import constants.APIRoutes;
import io.qameta.allure.Step;
import io.restassured.http.Headers;
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
        Headers headers = HeaderBuilder.basicHeadersWithList(events);
        Response response =given()
                .headers(headers)
                .and()
                .body(events)
                .when()
                .post(APIRoutes.ADD_EVENTS_BATCH);
        return response;
    }
    @Step("Send correct POST add_events_batch request")
    public static Response addEventsBatch(){
        Event event = new Event(true);
        List<Event> events_request = new ArrayList<>();
        events_request.add(event);
        Headers headers = HeaderBuilder.basicHeadersWithList(events_request);
        Response response =given()
                .headers(headers)
                .and()
                .body(events_request)
                .when()
                .post(APIRoutes.ADD_EVENTS_BATCH);
        return response;
    }
    @Step("Send correct POST add_events_batch request")
    public static Response addEventsBatch(int size){
        List<Event> events_request = new ArrayList<>();
        for(int i =0;i < size;i++) {
            Event event = new Event(true);
            events_request.add(event);
        }
        Headers headers = HeaderBuilder.basicHeadersWithList(events_request);
        Response response =given()
                .headers(headers)
                .and()
                .body(events_request)
                .when()
                .post(APIRoutes.ADD_EVENTS_BATCH);
        return response;
    }
}
