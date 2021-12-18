package tourGuide.restController;

import com.jsoniter.output.JsonStream;
import org.springframework.web.bind.annotation.*;
import tourGuide.model.Provider;
import tourGuide.model.User;
import tourGuide.model.UserPreferences;
import tourGuide.model.VisitedLocation;
import tourGuide.service.TourGuideService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TourGuideController {

    private final TourGuideService tourGuideService;

    public TourGuideController(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * Transmet la dernière position de l'utilisateur.
     * @param userName le nom de l'utilisateur.
     * @return la latitude et la longitude de l'utilisateur.
     */
    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }

    /**
     * Transmet les 5 attractions les plus proches de la positions de l'utilisateur.
     * @param userName le nom de l'utilisateur.
     * @return les 5 attractions avec leurs détails.
     */
    @GetMapping("/getNearByAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
    }

    /**
     * Transmet les gains de l'utilisateur.
     * @param userName le nom de l'utilisateur.
     * @return les gains de l'utilisateur.
     */
    @GetMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    /**
     * Transmet la dernière position de la totalité des utilisateurs.
     * @return la liste des positions avec l'id de l'utilisateur.
     */
    @GetMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {

        List<String> userIdsMapping = new ArrayList<>();
        for (User user : tourGuideService.getAllUsers()) {
            VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
            userIdsMapping.add(visitedLocation.userId + " : {'longitude':" + visitedLocation.location.longitude + ",'latitude':" + visitedLocation.location.latitude + "}");
        }

        return JsonStream.serialize(userIdsMapping);
    }

    /**
     * Transmet le meilleur séjour de voyage en fonction de l'utilisateur.
     * @param userName le nom de l'utilisateur.
     * @return la liste de Provider personnalisés.
     */
    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    /**
     * Transmet les préférences d'un utilisateur.
     * @param userName le nom de l'utilisateur.
     * @return les préférences de l'utilisateur.
     */
    @GetMapping(path = "/getUserPreferences")
    public String getUserPreferences(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUser(userName).getUserPreferences().toString());
    }

    /**
     * Modifie les préférences d'un utilisateur.
     * @param userName le nom de l'utilisateur.
     * @param userPreferences les nouvelles préférences.
     * @return les nouvelles préférences.
     */
    @PostMapping(path = "/setUserPreferences")
    public String setUserPreferences(@RequestParam String userName, @RequestBody UserPreferences userPreferences) {
        tourGuideService.getUser(userName).setUserPreferences(userPreferences);
        return JsonStream.serialize(userPreferences.toString());
    }

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }


}