package tourGuide.model;

import java.util.List;

public class CloserAttractions {

    Location userLocation;
    List<AttractionDetails> fiveCloserAttractions;

    public CloserAttractions(Location userLocation, List<AttractionDetails> fiveCloserAttractions) {
        this.userLocation = userLocation;
        this.fiveCloserAttractions = fiveCloserAttractions;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public List<AttractionDetails> getFiveCloserAttractions() {
        return fiveCloserAttractions;
    }
}
