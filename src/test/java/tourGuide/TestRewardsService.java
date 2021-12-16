package tourGuide;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.helper.InternalTestHelper;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRewardsService {

    @Autowired
    private GpsMicroServiceProxy gpsMicroServiceProxy;
    @Autowired
    private RewardCentralMicroServiceProxy rewardCentralMicroServiceProxy;
    @Autowired
    private TripPricerMicroServiceProxy tripPricerMicroServiceProxy;

    @Ignore
    @Test
    public void userGetRewards() {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);

        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = gpsMicroServiceProxy.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.tracker.stopTracking();
        assertEquals(1, userRewards.size());
    }

    @Ignore
    @Test
    public void isWithinAttractionProximity() {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);
        Attraction attraction = gpsMicroServiceProxy.getAttractions().get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

    @Ignore // Needs fixed - can throw ConcurrentModificationException
    @Test
    public void nearAllAttractions() {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
        List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
        tourGuideService.tracker.stopTracking();

        assertEquals(gpsMicroServiceProxy.getAttractions().size(), userRewards.size());
    }

}
