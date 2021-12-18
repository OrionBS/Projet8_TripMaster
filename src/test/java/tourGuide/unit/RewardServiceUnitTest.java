package tourGuide.unit;

import com.jsoniter.output.JsonStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.*;
import tourGuide.proxy.GpsMicroServiceProxy;
import tourGuide.proxy.RewardCentralMicroServiceProxy;
import tourGuide.service.RewardsService;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RewardServiceUnitTest {

    @InjectMocks
    RewardsService rewardsService;

    @Mock
    GpsMicroServiceProxy gpsMicroServiceProxy;
    @Mock
    RewardCentralMicroServiceProxy rewardCentralMicroServiceProxy;


    @Test
    public void testCalculateRewards() {
        //GIVEN
        User user = new User(UUID.randomUUID(),"userName","phoneNumber","emailAddress");
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(),new Location(125,124),Date.from(Instant.now()));
        user.addToVisitedLocations(visitedLocation);
        Attraction attraction = new Attraction("Tour Eiffel", "Paris","FRANCE",125,124);
        Mockito.when(gpsMicroServiceProxy.getAttractions()).thenReturn(Collections.singletonList(attraction));
        Mockito.when(rewardCentralMicroServiceProxy.getAttractionsRewardPoints(attraction.attractionId,user.getUserId())).thenReturn(12);
        //WHEN
        rewardsService.calculateRewards(user);

        //THEN
        Mockito.verify(gpsMicroServiceProxy, Mockito.times(1)).getAttractions();
        Assertions.assertEquals(JsonStream.serialize(Collections.singletonList(new UserReward(visitedLocation,attraction,12))), JsonStream.serialize(user.getUserRewards()));
    }
}
