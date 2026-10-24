package com.cricket.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.cricket.model.MatchDocument;
import com.cricket.repository.MatchRepository;

@Component
public class SeedData implements CommandLineRunner {
    private final MatchRepository repository;
    public SeedData(MatchRepository repository){this.repository=repository;}
    @Override public void run(String... args){
        if(repository.count()==0){
            MatchDocument m=new MatchDocument("India","Australia");
            m.setRuns(126);m.setWickets(3);m.setLegalBalls(86);m.setRunRate(8.79);m.setLastEvent("FOUR");
            repository.save(m);
        }
    }
}
