package com.cricket.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.cricket.model.MatchDocument;
import com.cricket.model.MatchDocument.BallEvent;
import com.cricket.model.MatchDocument.PlayerStat;
import com.cricket.repository.MatchRepository;

@Service
public class MatchService {
    private final MatchRepository repository;
    private final Random random = new Random();
    public MatchService(MatchRepository repository){this.repository=repository;}

    public List<MatchDocument> all(){return repository.findAll();}
    public List<MatchDocument> live(){return repository.findByStatusOrderByCreatedAtDesc("LIVE");}
    public MatchDocument get(String id){return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Match not found"));}

    public MatchDocument create(String teamA,String teamB){
        if(teamA==null||teamA.isBlank()||teamB==null||teamB.isBlank()||teamA.equalsIgnoreCase(teamB))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Enter two different team names.");
        return repository.save(new MatchDocument(teamA.trim(),teamB.trim()));
    }

    public MatchDocument recordBall(String id, BallRequest request){
        MatchDocument m=get(id);
        if(!"LIVE".equals(m.getStatus())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Match is not live.");
        if(request.runs()<0 || request.runs()>6) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Runs must be between 0 and 6.");
        String extras=request.extrasType()==null?"NONE":request.extrasType().toUpperCase(Locale.ROOT);
        Set<String> allowed=Set.of("NONE","WIDE","NO_BALL","BYE","LEG_BYE");
        if(!allowed.contains(extras)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unsupported extrasType.");
        boolean legal=!extras.equals("WIDE")&&!extras.equals("NO_BALL");
        int total=request.runs()+((extras.equals("WIDE")||extras.equals("NO_BALL"))?1:0);
        if(extras.equals("WIDE")||extras.equals("NO_BALL")) legal=false;
        String batter=blank(request.batter(),"Rohit Sharma"), bowler=blank(request.bowler(),"Jasprit Bumrah");
        String desc;
        if(request.wicket()) desc="WICKET";
        else if(extras.equals("WIDE")) desc="Wide";
        else if(extras.equals("NO_BALL")) desc="No-ball";
        else if(extras.equals("BYE")) desc="Bye";
        else if(extras.equals("LEG_BYE")) desc="Leg bye";
        else if(total==4) desc="FOUR";
        else if(total==6) desc="SIX";
        else if(total==0) desc="Dot ball";
        else desc=total+" run"+(total==1?"":"s");
        m.setRuns(m.getRuns()+total);
        if(request.wicket()) m.setWickets(m.getWickets()+1);
        if(legal) m.setLegalBalls(m.getLegalBalls()+1);
        m.setRunRate(m.getLegalBalls()==0?0:Math.round((m.getRuns()*6.0/m.getLegalBalls())*100.0)/100.0);
        m.setLastEvent(desc);
        PlayerStat batterStat=m.getPlayers().stream().filter(p->p.getName().equalsIgnoreCase(batter)).findFirst().orElseGet(()->{PlayerStat p=new PlayerStat(batter,"BAT");m.getPlayers().add(p);return p;});
        if(!extras.equals("WIDE")&&!extras.equals("BYE")&&!extras.equals("LEG_BYE")) {
            batterStat.setRuns(batterStat.getRuns()+request.runs());
            if(legal) batterStat.setBalls(batterStat.getBalls()+1);
            if(request.runs()==4) batterStat.setFours(batterStat.getFours()+1);
            if(request.runs()==6) batterStat.setSixes(batterStat.getSixes()+1);
        }
        if(request.wicket()) {
            PlayerStat bowlerStat=m.getPlayers().stream().filter(p->p.getName().equalsIgnoreCase(bowler)).findFirst().orElseGet(()->{PlayerStat p=new PlayerStat(bowler,"BOWL");m.getPlayers().add(p);return p;});
            bowlerStat.setWickets(bowlerStat.getWickets()+1);
        }
        m.getEvents().add(0,new BallEvent(m.getEvents().size()+1,total,extras,request.wicket(),legal,batter,bowler,desc));
        if(m.getWickets()>=10 || m.getLegalBalls()>=120) m.setStatus("COMPLETED");
        return repository.save(m);
    }
    public MatchDocument simulate(String id){
        int[] options={0,0,1,1,1,2,2,3,4,4,6};
        int value=options[random.nextInt(options.length)];
        boolean wicket=random.nextInt(18)==0;
        return recordBall(id,new BallRequest(value,"NONE",wicket,"Rohit Sharma","Jasprit Bumrah"));
    }
    public MatchDocument finish(String id){MatchDocument m=get(id);m.setStatus("COMPLETED");m.setLastEvent("Match completed");return repository.save(m);}
    public List<BallEvent> commentary(String id){return get(id).getEvents();}
    private String blank(String v,String fallback){return v==null||v.isBlank()?fallback:v.trim();}
    public record BallRequest(int runs,String extrasType,boolean wicket,String batter,String bowler){}
}
