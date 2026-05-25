package com.example.techcorp.api;

import com.example.techcorp.Company;
import com.example.techcorp.GameService;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private GameService gameService;

    public GameController() {

        gameService = new GameService();
    }

    @GetMapping("/game/state")
    public Map<String, Object> gameState() {

        Company company =
            gameService.getCompany();

        return Map.of(

            "turn",
                gameService.getTurn(),

            "company",
                company.getName(),

            "cash",
                company.getCash(),

            "employees",
                company.getEmployees().size(),

            "productivity",
                company.calculateTotalProductivity(),

            "activeProjects",
                company.getProjects(),

            "availableProjects",
                company.getAvailableProjects()
        );
    }

    @GetMapping("/game/accept/{projectName}")
    public Map<String, Object> acceptProject(
            @PathVariable("projectName") String projectName) {
                
        String result =
            gameService.acceptProject(
                projectName
            );
            
        Company company =
            gameService.getCompany();

        return Map.of(
            "message",
                result,

            "activeProjectsCount",
                company.getProjects().size(),

            "availableProjectsCount",
                company.getAvailableProjects().size()
        );
    }

    @GetMapping("/game/endTurn")
    public Map<String, Object> endTurn() {
        
        String result =
            gameService.endTurn();

        Company company =
            gameService.getCompany();

        return Map.of(
            "message",
                result,
                
            "cash",
                company.getCash(),
                
            "productivity",
                company.calculateTotalProductivity(),
                
            "activeProjects",
                company.getProjects()
        );
    }

    @GetMapping("/game/hire/intern")
    public Map<String, Object> hireIntern() {
        
        String result =
            gameService.hireIntern();
            
        Company company =
            gameService.getCompany();
            
        return Map.of(
            "message",
                result,

            "cash",
                company.getCash(),

            "employees",
                company.getEmployees().size(),

            "productivity",
                company.calculateTotalProductivity()
        );
    }

    @GetMapping("/game/hire/freelancer")
    public Map<String, Object> hireFreelancer() {
        
        String result =
            gameService.hireFreelancerBot();

        Company company =
            gameService.getCompany();

        return Map.of(
            "message",
                result,

            "cash",
                company.getCash(),

            "freelancerBots",
                company.getFreelancerBots().size(),

            "productivity",
                company.calculateTotalProductivity()
        );
    }

    @GetMapping("/game/buy/tool")
    public Map<String, Object> buyTool() {
        
        String result =
            gameService.buyAutomatedTool();

        Company company =
            gameService.getCompany();
            
        return Map.of(
            "message",
                result,

            "cash",
                company.getCash(),

            "automatedTools",
                company.getAutomatedTools().size(),

            "productivity",
                company.calculateTotalProductivity()
        );
    }
}