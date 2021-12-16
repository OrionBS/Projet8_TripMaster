package tourGuide.model;

public class AttractionDetails {

    String touristAttractionName;
    Location touristAttractionLocation;
    double touristAttractionDistanceToUser;
    int touristAttractionRewardPoints;

    public AttractionDetails(String touristAttractionName, Location touristAttractionLocation, double touristAttractionDistanceToUser, int touristAttractionRewardPoints) {
        this.touristAttractionName = touristAttractionName;
        this.touristAttractionLocation = touristAttractionLocation;
        this.touristAttractionDistanceToUser = touristAttractionDistanceToUser;
        this.touristAttractionRewardPoints = touristAttractionRewardPoints;
    }

    public double getTouristAttractionDistanceToUser() {
        return touristAttractionDistanceToUser;
    }
}
