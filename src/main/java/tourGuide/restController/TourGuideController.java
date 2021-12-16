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

    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }

    @GetMapping("/getNearByAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
    }

    @GetMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @GetMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {

        List<String> userIdsMapping = new ArrayList<>();
        for (User user : tourGuideService.getAllUsers()) {
            VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
            userIdsMapping.add(visitedLocation.userId + " : {'longitude':" + visitedLocation.location.longitude + ",'latitude':" + visitedLocation.location.latitude + "}");
        }

        return JsonStream.serialize(userIdsMapping);
    }

    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    @GetMapping(path = "/getUserPreferences")
    public String getUserPreferences(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUser(userName).getUserPreferences().toString());
    }

    @PostMapping(path = "/setUserPreferences")
    public String setUserPreferences(@RequestParam String userName, @RequestBody UserPreferences userPreferences) {
        tourGuideService.getUser(userName).setUserPreferences(userPreferences);
        return JsonStream.serialize(userPreferences.toString());
    }

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }


}