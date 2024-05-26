package bodyclasses.request;

import com.github.javafaker.Faker;

public class Event {
    String eventId;
    String resourceId;
    String userId;
    String amountUsd;
    int multiplier;
    String extra;
    Faker faker = new Faker();

    public Event() {
    }
    public Event(boolean common){
        if(common){
            this.eventId = faker.internet().uuid();
            this.resourceId = "33ebdfa9-9a99-4019-a237-04204e1f9ff6";
            this.userId = "1234";
            this.amountUsd = "1000";
            this.multiplier = 1;
            this.extra = "";
        }
    }

    public Event(String eventId, String resourceId, String userId, String amountUsd, int multiplier, String extra) {
        this.eventId = eventId;
        this.resourceId = resourceId;
        this.userId = userId;
        this.amountUsd = amountUsd;
        this.multiplier = multiplier;
        this.extra = extra;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmountUsd() {
        return amountUsd;
    }

    public void setAmountUsd(String amountUsd) {
        this.amountUsd = amountUsd;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
