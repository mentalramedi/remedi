package com.example.remedi;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataSetFire {
    String fullname,username,profileimage,memberName,deviceName,deviceStatus;
    String date,details;
    private boolean isExpanded;

    public DataSetFire() {
    }

    public DataSetFire(String fullname, String profileimage, String username,String memberName,String deviceName,String deviceStatus,String date,String details) {
        this.fullname = fullname;
        this.profileimage = profileimage;
        this.username = username;
        this.memberName = memberName;
        this.deviceName = deviceName;
        this.deviceStatus = deviceStatus;
        this.isExpanded = false;
        this.date = date;
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}
