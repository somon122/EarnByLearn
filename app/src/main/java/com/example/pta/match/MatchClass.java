package com.example.pta.match;

public class MatchClass {

    private String id, name, category,candidateNo,start_date_time,end_date_time, total_time, entryFee,
            type, Syllabus,winnerPrice;

    public MatchClass() {}

    public MatchClass(String id, String name, String category, String candidateNo, String start_date_time,
                      String end_date_time, String total_time, String entryFee, String type, String syllabus, String winnerPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.candidateNo = candidateNo;
        this.start_date_time = start_date_time;
        this.end_date_time = end_date_time;
        this.total_time = total_time;
        this.entryFee = entryFee;
        this.type = type;
        Syllabus = syllabus;
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

    public String getStart_date_time() {
        return start_date_time;
    }

    public String getEnd_date_time() {
        return end_date_time;
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

    public String getWinnerPrice() {
        return winnerPrice;
    }
}


