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

    @GetMapping("/game/difficulty/{difficulty}")
    public Map<String, Object> setDifficulty(
            @PathVariable("difficulty")
            String difficulty)  {
                
        gameService.setAiDifficulty(
            difficulty
        );
        
        return Map.of(
            "difficulty",
            difficulty
        );
    }

    @GetMapping("/game/state")
    public Map<String, Object> gameState() {
        
        Company company =
            gameService.getCompany();
            
        Company aiCompany =
            gameService.getAiCompany();
            
        return Map.ofEntries(
            
            Map.entry(
                "turn",
                gameService.getTurn()
            ),

            Map.entry(
                "gameStarted",
                gameService.isGameStarted()
            ),

            Map.entry(
                "turnLog",
                gameService.getTurnLog()
            ),

            Map.entry(
                "difficulty",
                gameService.getAiDifficulty()
            ),
            
            Map.entry(
                "company",
                company.getName()
            ),
            
            Map.entry(
                "cash",
                company.getCash()
            ),
            
            Map.entry(
                "employees",
                company.getEmployees().size()
            ),
            
            Map.entry(
                "productivity",
                company.calculateTotalProductivity()
            ),
            
            Map.entry(
                "availableProjects",
                company.getAvailableProjects()
            ),
            
            Map.entry(
                "activeProjects",
                company.getProjects()
            ),
            
            Map.entry(
                "aiCash",
                aiCompany.getCash()
            ),
            
            Map.entry(
                "aiProductivity",
                aiCompany.calculateTotalProductivity()
            ),
            
            Map.entry(
                "aiProjects",
                gameService.countActiveProjects(
                    aiCompany)
            ),
            
            Map.entry(
                "aiActiveProjects",
                aiCompany.getProjects()
            ),

            Map.entry(
                "totalSalaries",
                company.calculateTotalSalaries()
            ),

            Map.entry(
                "freelancerBots",
                company.getFreelancerBots().size()
            ),

            Map.entry(
                "automatedTools",
                company.getAutomatedTools().size()
            ),

            Map.entry(
                "activeProjectCount",
                gameService.countActiveProjects(company)
            ),

            Map.entry(
                "gameOver",
                gameService.isGameOver()
            ),
            
            Map.entry(
                "winner",
                gameService.getWinner()
            )
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

    @GetMapping("/game/hold/{projectName}")
    public Map<String, Object> holdProject(
        @PathVariable("projectName") String projectName) {
            
        String result =
            gameService.holdProject(
                projectName
            );
            
        return Map.of(
            "message",
            result
        );
    }

    @GetMapping("/game/resume/{projectName}")
    public Map<String, Object> resumeProject(
        @PathVariable("projectName") String projectName){
            
        String result =
            gameService.resumeProject(
                projectName
            );
            
        return Map.of(
            "message",
            result
        );
    }

    @GetMapping("/game/cancel/{projectName}")
    public Map<String, Object> cancelProject(
            @PathVariable("projectName")
            String projectName) {

        String result =
            gameService.cancelProject(
                projectName
            );

        return Map.of(
            "message",
            result
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