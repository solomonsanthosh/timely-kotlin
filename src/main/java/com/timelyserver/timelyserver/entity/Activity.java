package com.timelyserver.timelyserver.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private ArrayList<Long> userid;

    private String file = null;


    private String assignedBy = "self";
    private String type = "self";
    private Boolean pin = false;


    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date date = null;

    public Activity(String title, String content, ArrayList<Long> userid, Date date,String assignedBy,String file,String type) {

        this.title = title;
        this.content = content;
        this.userid = userid;
        this.assignedBy = assignedBy;
        this.file = file;
        this.date = date;
        this.type = type;

    }

    public Activity() {
    }




    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    private String status = "Not Completed";


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public ArrayList<Long> getUserid() {
        return userid;
    }


    public void setUserid(ArrayList<Long> userid) {
        this.userid = userid;
    }


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }


    public String getAssignedBy() {
        return assignedBy;
    }


    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public Boolean getPin() {
        return pin;
    }


    public void setPin(Boolean pin) {
        this.pin = pin;
    }


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
  

}
