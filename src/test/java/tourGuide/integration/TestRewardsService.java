package tourGuide.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.model.Attraction;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.model.VisitedLocation;
import tourGuide.proxy.GpsMicroServiceProxy;
import tourGuide.proxy.RewardCentralMicroServiceProxy;
import tourGuide.proxy.TripPricerMicroServiceProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class TestRewardsService {

    @Autowired
    private TourGuideService tourGuideService;
    @Autowired
    private RewardsService rewardsService;
    @Autowired
    private GpsMicroServiceProxy gpsMicroServiceProxy;
    @Autowired
    private RewardCentralMicroServiceProxy rewardCentralMicroServiceProxy;
    @Autowired
    private TripPricerMicroServiceProxy tripPricerMicroServiceProxy;


    @Test
    public void userGetRewards() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = gpsMicroServiceProxy.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.tracker.stopTracking();
        Assertions.assertEquals(1, userRewards.size());
    }

    @Test
    public void isWithinAttractionProximity() {
        Attraction attraction = gpsMicroServiceProxy.getAttractions().get(0);
        Assertions.assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

}
