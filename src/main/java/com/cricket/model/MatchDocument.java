package com.cricket.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("matches")
public class MatchDocument {
    @Id
    private String id;
    private String teamA;
    private String teamB;
    private String status = "LIVE";
    private String battingTeam;
    private int innings = 1;
    private int runs;
    private int wickets;
    private int legalBalls;
    private double runRate;
    private String lastEvent = "Match ready";
    private String createdAt = Instant.now().toString();
    private List<BallEvent> events = new ArrayList<>();
    private List<PlayerStat> players = new ArrayList<>();

    public MatchDocument() {}
    public MatchDocument(String teamA, String teamB) {
        this.teamA = teamA; this.teamB = teamB; this.battingTeam = teamA;
        players.add(new PlayerStat("Rohit Sharma", "BAT"));
        players.add(new PlayerStat("Virat Kohli", "BAT"));
        players.add(new PlayerStat("Jasprit Bumrah", "BOWL"));
        players.add(new PlayerStat("Hardik Pandya", "ALL"));
    }
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getTeamA(){return teamA;} public void setTeamA(String v){teamA=v;}
    public String getTeamB(){return teamB;} public void setTeamB(String v){teamB=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getBattingTeam(){return battingTeam;} public void setBattingTeam(String v){battingTeam=v;}
    public int getInnings(){return innings;} public void setInnings(int v){innings=v;}
    public int getRuns(){return runs;} public void setRuns(int v){runs=v;}
    public int getWickets(){return wickets;} public void setWickets(int v){wickets=v;}
    public int getLegalBalls(){return legalBalls;} public void setLegalBalls(int v){legalBalls=v;}
    public double getRunRate(){return runRate;} public void setRunRate(double v){runRate=v;}
    public String getLastEvent(){return lastEvent;} public void setLastEvent(String v){lastEvent=v;}
    public String getCreatedAt(){return createdAt;} public void setCreatedAt(String v){createdAt=v;}
    public List<BallEvent> getEvents(){return events;} public void setEvents(List<BallEvent> v){events=v;}
    public List<PlayerStat> getPlayers(){return players;} public void setPlayers(List<PlayerStat> v){players=v;}

    public String getOvers() { return (legalBalls / 6) + "." + (legalBalls % 6); }

    public static class BallEvent {
        private int sequence;
        private int runs;
        private String extrasType;
        private boolean wicket;
        private boolean legalBall;
        private String batter;
        private String bowler;
        private String description;
        private String timestamp = Instant.now().toString();
        public BallEvent() {}
        public BallEvent(int sequence,int runs,String extrasType,boolean wicket,boolean legalBall,String batter,String bowler,String description) {
            this.sequence=sequence;this.runs=runs;this.extrasType=extrasType;this.wicket=wicket;this.legalBall=legalBall;this.batter=batter;this.bowler=bowler;this.description=description;
        }
        public int getSequence(){return sequence;} public void setSequence(int v){sequence=v;}
        public int getRuns(){return runs;} public void setRuns(int v){runs=v;}
        public String getExtrasType(){return extrasType;} public void setExtrasType(String v){extrasType=v;}
        public boolean isWicket(){return wicket;} public void setWicket(boolean v){wicket=v;}
        public boolean isLegalBall(){return legalBall;} public void setLegalBall(boolean v){legalBall=v;}
        public String getBatter(){return batter;} public void setBatter(String v){batter=v;}
        public String getBowler(){return bowler;} public void setBowler(String v){bowler=v;}
        public String getDescription(){return description;} public void setDescription(String v){description=v;}
        public String getTimestamp(){return timestamp;} public void setTimestamp(String v){timestamp=v;}
    }
    public static class PlayerStat {
        private String name; private String role; private int runs; private int balls; private int fours; private int sixes; private int wickets;
        public PlayerStat() {}
        public PlayerStat(String name,String role){this.name=name;this.role=role;}
        public String getName(){return name;} public void setName(String v){name=v;}
        public String getRole(){return role;} public void setRole(String v){role=v;}
        public int getRuns(){return runs;} public void setRuns(int v){runs=v;}
        public int getBalls(){return balls;} public void setBalls(int v){balls=v;}
        public int getFours(){return fours;} public void setFours(int v){fours=v;}
        public int getSixes(){return sixes;} public void setSixes(int v){sixes=v;}
        public int getWickets(){return wickets;} public void setWickets(int v){wickets=v;}
        public double getStrikeRate(){return balls==0?0:Math.round((runs*1000.0/balls))/10.0;}
    }
}
