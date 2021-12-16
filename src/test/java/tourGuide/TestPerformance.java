package tourGuide;

import feign.Feign;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.User;
import tourGuide.model.VisitedLocation;
import tourGuide.proxy.GpsMicroServiceProxy;
import tourGuide.proxy.RewardCentralMicroServiceProxy;
import tourGuide.proxy.TripPricerMicroServiceProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@ImportAutoConfiguration(Feign.class)
public class TestPerformance {

    @Autowired
    private GpsMicroServiceProxy gpsMicroServiceProxy;
    private RewardCentralMicroServiceProxy rewardCentralMicroServiceProxy;
    private TripPricerMicroServiceProxy tripPricerMicroServiceProxy;

    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */

    @Ignore
    @Test
    public void highVolumeTrackLocation() {

        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        List<User> allUsers = new ArrayList<>();
        allUsers = tourGuideService.getAllUsers();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (User user : allUsers) {
            tourGuideService.trackUserLocation(user);
        }
        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    @Test
    public void highVolumeGetRewards() throws InterruptedException {
        RewardsService rewardsService = new RewardsService(gpsMicroServiceProxy, rewardCentralMicroServiceProxy);

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TourGuideService tourGuideService = new TourGuideService(gpsMicroServiceProxy, rewardsService, tripPricerMicroServiceProxy, rewardCentralMicroServiceProxy);

        Attraction attraction = gpsMicroServiceProxy.getAttractions().get(0);
        List<User> allUsers = new ArrayList<>();
        allUsers = tourGuideService.getAllUsers();
        allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

        rewardsService.calculateAllRewards(allUsers);

        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);
        }
        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
