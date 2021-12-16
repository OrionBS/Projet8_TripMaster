package tourGuide.proxy;

import feign.Param;
import feign.RequestLine;

import java.util.UUID;

public interface RewardCentralMicroServiceProxy {

    @RequestLine("GET /getAttractionRewardPoints?attractionId={attractionId}&userId={userId}")
    int getAttractionsRewardPoints(@Param("attractionId") UUID attractionId, @Param("userId") UUID userId);
}
