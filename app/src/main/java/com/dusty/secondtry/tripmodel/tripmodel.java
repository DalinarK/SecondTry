package com.dusty.secondtry.tripmodel;

import java.util.List;

/**
 * Created by Dustin on 2/8/2016.
 */
public class tripmodel {

    private String _id;
    private String name;
    private String location;
    private String days;
    private String demographic;
    private String cost;
    private List<activities> activitiesList;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDemographic() {
        return demographic;
    }

    public void setDemographic(String demographic) {
        this.demographic = demographic;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public List<activities> getActivitiesList() {
        return activitiesList;
    }

    public void setActivitiesList(List<activities> activitiesList) {
        this.activitiesList = activitiesList;
    }

    public static class activities
    {
        private String Title;
        private String Description;
        private int Duration;
        private int Cost;
        private String Picture;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public int getDuration() {
            return Duration;
        }

        public void setDuration(int duration) {
            Duration = duration;
        }

        public int getCost() {
            return Cost;
        }

        public void setCost(int cost) {
            Cost = cost;
        }

        public String getPicture() {
            return Picture;
        }

        public void setPicture(String picture) {
            Picture = picture;
        }
    }
}


