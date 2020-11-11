package com.example.pta.result;

public class ResultClass {

    private String id, name,matchId, category,date_time,totalPrize,total_time, entryFee,
            type, routine,syllabus,winnerPrice;

    public ResultClass() {}

    public ResultClass(String id, String name, String matchId, String category, String date_time,
                       String totalPrize, String total_time, String entryFee,
                       String type, String routine, String syllabus, String winnerPrice) {
        this.id = id;
        this.name = name;
        this.matchId = matchId;
        this.category = category;
        this.date_time = date_time;
        this.totalPrize = totalPrize;
        this.total_time = total_time;
        this.entryFee = entryFee;
        this.type = type;
        this.routine = routine;
        this.syllabus = syllabus;
        this.winnerPrice = winnerPrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMatchId() {
        return matchId;
    }

    public String getCategory() {
        return category;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getTotalPrize() {
        return totalPrize;
    }

    public String getTotal_time() {
        return total_time;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public String getType() {
        return type;
    }

    public String getRoutine() {
        return routine;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public String getWinnerPrice() {
        return winnerPrice;
    }
}
