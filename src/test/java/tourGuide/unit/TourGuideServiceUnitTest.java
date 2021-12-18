package tourGuide.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.Provider;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.proxy.GpsMicroServiceProxy;
import tourGuide.proxy.TripPricerMicroServiceProxy;
import tourGuide.service.TourGuideService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TourGuideServiceUnitTest {

    @InjectMocks
    TourGuideService tourGuideService;

    @Mock
    TripPricerMicroServiceProxy tripPricerMicroServiceProxy;
    @Mock
    GpsMicroServiceProxy gpsMicroServiceProxy;


    @Test
    public void testGetUserRewards() {
        //GIVEN
        User user = new User(UUID.randomUUID(), "userName", "phoneNumber", "emailAddress");

        //WHEN
        List<UserReward> userRewards = tourGuideService.getUserRewards(user);

        //THEN
        Assertions.assertEquals(Collections.emptyList(), userRewards);
    }

    @Test
    public void testAddUser() {
        //GIVEN
        User user = new User(UUID.randomUUID(), "userName", "phoneNumber", "emailAddress");
        //WHEN
        tourGuideService.addUser(user);

        //THEN
    }

    @Test
    public void testGetAllUsers() {

    }

    @Test
    public void testGetUser() {
        //GIVEN
        User user = new User(UUID.randomUUID(), "userName", "phoneNumber", "emailAddress");

        //WHEN
        User userAnswer = tourGuideService.getUser(user.getUserName());

        //THEN
        Assertions.assertNull(userAnswer);
    }

    @Test
    public void testGetTripDeals() {
        //GIVEN
        User user = new User(UUID.randomUUID(), "userName", "phoneNumber", "emailAddress");

        Mockito.when(tripPricerMicroServiceProxy.getPrice("test-server-api-key", user.getUserId(), user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), 0)).thenReturn(Collections.emptyList());
        //WHEN
        List<Provider> providers = tourGuideService.getTripDeals(user);
        //THEN
        Mockito.verify(tripPricerMicroServiceProxy, Mockito.times(1)).getPrice("test-server-api-key", user.getUserId(), user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), 0);
        Assertions.assertEquals(Collections.emptyList(), providers);
    }
}
