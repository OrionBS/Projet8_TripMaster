package tourGuide.unit;

import com.jsoniter.output.JsonStream;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.*;
import tourGuide.restController.TourGuideController;
import tourGuide.service.TourGuideService;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TourGuideControllerUnitTest {

    @InjectMocks
    TourGuideController tourGuideController;

    @Mock
    TourGuideService tourGuideService;

    @Test
    public void testIndex() {
        //GIVEN
        String message = "Greetings from TourGuide!";

        //WHEN
        String received = tourGuideController.index();

        //THEN
        Assertions.assertEquals(message,received);
    }

    @Test
    public void testGetLocation() {
        //GIVEN
        String userName = "John";
        User user = new User(UUID.randomUUID(),"John","0658799511","john@mail.com");
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(),new Location(145,23), Date.from(Instant.now()));
        Mockito.when(tourGuideService.getUser(userName)).thenReturn(user);
        Mockito.when(tourGuideService.getUserLocation(user)).thenReturn(visitedLocation);

        //WHEN
        String received = tourGuideController.getLocation(userName);

        //THEN
        Mockito.verify(tourGuideService, Mockito.times(1)).getUser(userName);
        Mockito.verify(tourGuideService, Mockito.times(1)).getUserLocation(user);
        Assertions.assertEquals(JsonStream.serialize(visitedLocation.location),received);
    }

    @Test
    public void testGetNearbyAttractions() {
        String userName = "John";
        User user = new User(UUID.randomUUID(),"John","0658799511","john@mail.com");
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(),new Location(145,23), Date.from(Instant.now()));
        CloserAttractions closerAttractions = new CloserAttractions(new Location(145,23), new ArrayList<>());
        Mockito.when(tourGuideService.getUser(userName)).thenReturn(user);
        Mockito.when(tourGuideService.getUserLocation(user)).thenReturn(visitedLocation);
        Mockito.when(tourGuideService.getNearByAttractions(visitedLocation)).thenReturn(closerAttractions);

        //WHEN
        String received = tourGuideController.getNearbyAttractions(userName);

        //THEN
        Mockito.verify(tourGuideService, Mockito.times(1)).getUser(userName);
        Mockito.verify(tourGuideService, Mockito.times(1)).getUserLocation(user);
        Mockito.verify(tourGuideService, Mockito.times(1)).getNearByAttractions(visitedLocation);
        Assertions.assertEquals(JsonStream.serialize(closerAttractions),received);
    }

    @Test
    public void testGetRewards() {
        String userName = "John";
        User user = new User(UUID.randomUUID(),"John","0658799511","john@mail.com");
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(),new Location(145,23), Date.from(Instant.now()));
        UserReward userReward = new UserReward(visitedLocation,new Attraction("Tour Eiffel","Paris","FRANCE",145,23));
        Mockito.when(tourGuideService.getUser(userName)).thenReturn(user);
        Mockito.when(tourGuideService.getUserRewards(user)).thenReturn(Collections.singletonList(userReward));

        //WHEN
        String received = tourGuideController.getRewards(userName);

        //THEN
        Mockito.verify(tourGuideService, Mockito.times(1)).getUser(userName);
        Mockito.verify(tourGuideService, Mockito.times(1)).getUserRewards(user);
        Assertions.assertEquals(JsonStream.serialize(Collections.singletonList(userReward)),received);
    }

    @Test
    public void testGetAllCurrentLocations() {
        User user = new User(UUID.randomUUID(),"John","0658799511","john@mail.com");
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(),new Location(145,23), Date.from(Instant.now()));
        String positions = visitedLocation.userId + " : {'longitude':" + visitedLocation.location.longitude + ",'latitude':" + visitedLocation.location.latitude + "}";

        Mockito.when(tourGuideService.getAllUsers()).thenReturn(Collections.singletonList(user));
        Mockito.when(tourGuideService.getUserLocation(user)).thenReturn(visitedLocation);

        //WHEN
        String received = tourGuideController.getAllCurrentLocations();

        //THEN
        Mockito.verify(tourGuideService, Mockito.times(1)).getAllUsers();
        Mockito.verify(tourGuideService, Mockito.times(1)).getUserLocation(user);
        Assertions.assertEquals(JsonStream.serialize(Collections.singletonList(positions)),received);
    }

    @Test
    public void testGetTripDeals() {
        String userName = "John";
        User user = new User(UUID.randomUUID(),"John","0658799511","john@mail.com");
        Provider provider = new Provider(UUID.randomUUID(),"John",125);
        Mockito.when(tourGuideService.getUser(userName)).thenReturn(user);
        Mockito.when(tourGuideService.getTripDeals(user)).thenReturn(Collections.singletonList(provider));

        //WHEN
        String received = tourGuideController.getTripDeals(userName);

        //THEN
        Mockito.verify(tourGuideService, Mockito.times(1)).getUser(userName);
        Mockito.verify(tourGuideService, Mockito.times(1)).getTripDeals(user);
        Assertions.assertEquals(JsonStream.serialize(Collections.singletonList(provider)),received);
    }

    @Test
    public void testGetUserPreferences() {
        String userName = "John";
        User user = new User(UUID.randomUUID(),"John","0658799511","john@mail.com");
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setAttractionProximity(12);
        userPreferences.setHighPricePoint(Money.of(50,"USD"));
        userPreferences.setLowerPricePoint(Money.of(10,"USD"));
        userPreferences.setNumberOfAdults(1);
        userPreferences.setTicketQuantity(1);
        userPreferences.setTripDuration(2);
        userPreferences.setNumberOfChildren(0);
        user.setUserPreferences(userPreferences);
        Mockito.when(tourGuideService.getUser(userName)).thenReturn(user);

        //WHEN
        String received = tourGuideController.getUserPreferences(userName);

        //THEN
        Mockito.verify(tourGuideService, Mockito.times(1)).getUser(userName);
        Assertions.assertEquals(JsonStream.serialize(user.getUserPreferences().toString()),received);
    }

    @Test
    public void testSetUserPreferences() {
        String userName = "John";
        User user = new User(UUID.randomUUID(),"John","0658799511","john@mail.com");
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setAttractionProximity(12);
        userPreferences.setHighPricePoint(Money.of(50,"USD"));
        userPreferences.setLowerPricePoint(Money.of(10,"USD"));
        userPreferences.setNumberOfAdults(1);
        userPreferences.setTicketQuantity(1);
        userPreferences.setTripDuration(2);
        userPreferences.setNumberOfChildren(0);
        Mockito.when(tourGuideService.getUser(userName)).thenReturn(user);

        //WHEN
        String received = tourGuideController.setUserPreferences(userName,userPreferences);

        //THEN
        Mockito.verify(tourGuideService, Mockito.times(1)).getUser(userName);
        Assertions.assertEquals(JsonStream.serialize(user.getUserPreferences().toString()),received);
    }

}
