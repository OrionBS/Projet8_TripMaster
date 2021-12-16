package tourGuide.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.Attraction;
import tourGuide.model.User;
import tourGuide.proxy.GpsMicroServiceProxy;
import tourGuide.proxy.RewardCentralMicroServiceProxy;
import tourGuide.service.RewardsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RewardServiceUnitTest {

    @InjectMocks
    RewardsService rewardsService;

    @Mock
    GpsMicroServiceProxy gpsMicroServiceProxy;
    @Mock
    RewardCentralMicroServiceProxy rewardCentralMicroServiceProxy;
    @Mock
    RewardsService rewardsServiceMock;

    @Test
    public void testCalculateRewards() {
        //GIVEN
        User user = new User(UUID.randomUUID(),"userName","phoneNumber","emailAddress");
        Attraction attraction = new Attraction("Tour Eiffel", "Paris","FRANCE",125,124);
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(attraction);
        Mockito.when(gpsMicroServiceProxy.getAttractions()).thenReturn(attractions);
        //WHEN
        rewardsService.calculateRewards(user);

        //THEN
        Mockito.verify(gpsMicroServiceProxy, Mockito.times(1)).getAttractions();
    }
}
