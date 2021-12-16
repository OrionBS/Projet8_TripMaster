package tourGuide;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.CloserAttractions;
import tourGuide.model.Provider;
import tourGuide.model.User;
import tourGuide.model.VisitedLocation;
import tourGuide.proxy.GpsMicroServiceProxy;
import tourGuide.proxy.RewardCentralMicroServiceProxy;
import tourGuide.proxy.TripPricerMicroServiceProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestTourGuideService {

    @Autowired
    private GpsMicroServiceProxy gpsMicroServiceProxy;
    @Autowired
    private RewardCentralMicroServiceProxy rewardCentralMicroServiceProxy;
    @Autowired
    private TripPricerMicroServiceProxy tripPricerMicroServiceProxy;

    @Ignore
    @Test
    public void getUserLocation() {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
        tourGuideService.tracker.stopTracking();
        assertTrue(visitedLocation.userId.equals(user.getUserId()));
    }

    @Test
    public void addUser() {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        User retrivedUser = tourGuideService.getUser(user.getUserName());
        User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

        tourGuideService.tracker.stopTracking();

        assertEquals(user, retrivedUser);
        assertEquals(user2, retrivedUser2);
    }

    @Test
    public void getAllUsers() {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        List<User> allUsers = tourGuideService.getAllUsers();

        tourGuideService.tracker.stopTracking();

        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

    @Ignore
    @Test
    public void trackUser() {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(user.getUserId(), visitedLocation.userId);
    }

    @Ignore
    @Test
    public void getNearbyAttractions() {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        CloserAttractions closerAttractions = tourGuideService.getNearByAttractions(visitedLocation);

        tourGuideService.tracker.stopTracking();

        assertEquals(5, closerAttractions.getFiveCloserAttractions().size());
    }

    public void getTripDeals() {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        List<Provider> providers = tourGuideService.getTripDeals(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(10, providers.size());
    }


}
