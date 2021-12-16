package tourGuide.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;

public class TrackerTask extends Thread {
    private Logger logger = LoggerFactory.getLogger(TrackerTask.class);
    private final TourGuideService tourGuideService;
    private final User user;

    public TrackerTask(TourGuideService tourGuideService, User user) {
        this.tourGuideService = tourGuideService;
        this.user = user;

    }

    @Override
    public void run() {
        tourGuideService.trackUserLocation(this.user);
        logger.info("Tracking " + user.getUserName());
    }
}
