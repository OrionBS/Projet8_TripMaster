package tourGuide.proxy;

import feign.Param;
import feign.RequestLine;
import tourGuide.model.Attraction;
import tourGuide.model.VisitedLocation;

import java.util.List;
import java.util.UUID;

public interface GpsMicroServiceProxy {

    @RequestLine("GET /getAttractions")
    List<Attraction> getAttractions();

    @RequestLine("GET /getUserLocation?userId={userId}")
    VisitedLocation getUserLocation(@Param("userId") UUID userId);
}
