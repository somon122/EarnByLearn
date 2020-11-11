package com.example.pta.match;

public class MatchClass {

    private String id, name, category,candidateNo,date_time, total_time, entryFee,
            type, Syllabus,Routine,winnerPrice;

    public MatchClass() {}

    public MatchClass(String id, String name, String category, String candidateNo,
                      String date_time, String total_time, String entryFee,
                      String type, String syllabus, String routine, String winnerPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.candidateNo = candidateNo;
        this.date_time = date_time;
        this.total_time = total_time;
        this.entryFee = entryFee;
        this.type = type;
        Syllabus = syllabus;
        Routine = routine;
        this.winnerPrice = winnerPrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getCandidateNo() {
        return candidateNo;
    }

    public String getDate_time() {
        return date_time;
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

    public String getSyllabus() {
        return Syllabus;
    }

    public String getRoutine() {
        return Routine;
    }

    public String getWinnerPrice() {
        return winnerPrice;
    }
}


