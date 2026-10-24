package com.cricket.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import com.cricket.model.MatchDocument;
import com.cricket.service.MatchService;
import com.cricket.service.MatchService.BallRequest;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MatchController {
    private final MatchService service;
    public MatchController(MatchService service){this.service=service;}

    @GetMapping("/health")
    public Map<String,Object> health(){return Map.of("status","UP","application","Cricket Score System");}
    @GetMapping("/matches") public List<MatchDocument> all(){return service.all();}
    @GetMapping("/matches/live") public List<MatchDocument> live(){return service.live();}
    @GetMapping("/matches/{id}") public MatchDocument get(@PathVariable String id){return service.get(id);}
    @GetMapping("/matches/{id}/commentary") public List<MatchDocument.BallEvent> commentary(@PathVariable String id){return service.commentary(id);}
    @PostMapping("/matches") public MatchDocument create(@Valid @RequestBody CreateMatch request){return service.create(request.teamA(),request.teamB());}
    @PostMapping("/matches/{id}/balls") public MatchDocument ball(@PathVariable String id,@RequestBody BallRequest request){return service.recordBall(id,request);}
    @PostMapping("/matches/{id}/simulate") public MatchDocument simulate(@PathVariable String id){return service.simulate(id);}
    @PostMapping("/matches/{id}/finish") public MatchDocument finish(@PathVariable String id){return service.finish(id);}

    public record CreateMatch(@NotBlank String teamA,@NotBlank String teamB){}
}
