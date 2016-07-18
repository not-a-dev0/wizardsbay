package edu.neu.cs5500.wizards.core;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by susannaedens on 6/20/16.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "userId" })
@JsonPropertyOrder({ "rating", "feedbackDescription", "username", "time", "feedbackId"})
public class Feedback {

    @JsonProperty
    private Integer id;

    @JsonProperty
    private Integer userId;

    @JsonProperty
    private String username;

    @JsonProperty
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @JsonProperty
    @NotEmpty
    private String feedbackDescription;

    @JsonProperty
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd hh:mm:ss a", timezone="PST")
    private Timestamp time;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Feedback() {}

    @JsonProperty("feedbackId")
    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getUserId() {
        return userId;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feedback feedback = (Feedback) o;

        if (id != null ? !id.equals(feedback.id) : feedback.id != null) return false;
        if (userId != null ? !userId.equals(feedback.userId) : feedback.userId != null) return false;
        if (username != null ? !username.equals(feedback.username) : feedback.username != null) return false;
        if (!rating.equals(feedback.rating)) return false;
        if (!feedbackDescription.equals(feedback.feedbackDescription)) return false;
        return time != null ? time.equals(feedback.time) : feedback.time == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + rating.hashCode();
        result = 31 * result + feedbackDescription.hashCode();
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
