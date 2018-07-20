package com.zucc.ccm31501396.mdays.data;

public class Schedule {

    /**
     * userName : admin
     * sId : admin2201807171459085
     * sTitle : 完成短学期安卓项目日程模块
     * sDate : 2018-7-18
     * sType : 工作
     * startTime : 08:00
     * endTime : 21:00
     * priority : 5
     * place : 理4-220
     * sNote : 再不写来不及啦！
     * sStatus : 未完成
     * clock : 1
     * clockId :
     */

    private String userName;
    private String sId;
    private String sTitle;
    private String sDate;
    private String sType;
    private String startTime;
    private String endTime;
    private String priority;
    private String place;
    private String sNote;
    private String sStatus;
    private String clock;
    private String clockId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSId() {
        return sId;
    }

    public void setSId(String sId) {
        this.sId = sId;
    }

    public String getSTitle() {
        return sTitle;
    }

    public void setSTitle(String sTitle) {
        this.sTitle = sTitle;
    }

    public String getSDate() {
        return sDate;
    }

    public void setSDate(String sDate) {
        this.sDate = sDate;
    }

    public String getSType() {
        return sType;
    }

    public void setSType(String sType) {
        this.sType = sType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSNote() {
        return sNote;
    }

    public void setSNote(String sNote) {
        this.sNote = sNote;
    }

    public String getSStatus() {
        return sStatus;
    }

    public void setSStatus(String sStatus) {
        this.sStatus = sStatus;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    public Integer getClockId() {
        return Integer.parseInt(clockId.trim());
    }

    public void setClockId(String clockId) {
        this.clockId = clockId;
    }

    //    * sDate : 2018-7-18
    public int getYear(){
        return Integer.parseInt(sDate.substring(0,4));
    }
    public int getMonth(){
        return Integer.parseInt(sDate.substring(5,6));
    }
    public int getDay(){
        return Integer.parseInt(sDate.substring(7,9));
    }
//    * startTime : 08:00
    public int getHour(){
        if(startTime.length()==4){
            return Integer.parseInt(startTime.substring(0,1));
        }else{
            return Integer.parseInt(startTime.substring(0,2));
        }
    }
    public int getMin(){
        if(startTime.length()==4){
            return Integer.parseInt(startTime.substring(2,4));
        }else{
            return Integer.parseInt(startTime.substring(3,5));
        }
    }
}
