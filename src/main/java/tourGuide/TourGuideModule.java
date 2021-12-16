package tourGuide;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tourGuide.proxy.GpsMicroServiceProxy;
import tourGuide.proxy.RewardCentralMicroServiceProxy;
import tourGuide.proxy.TripPricerMicroServiceProxy;
import tourGuide.service.RewardsService;

import java.util.Locale;

@Configuration
public class TourGuideModule {

    @Value("${url.gps}")
    private String gpsUrl;
    @Value("${url.reward}")
    private String rewardUrl;
    @Value("${url.trip.pricer}")
    private String tripPricerUrl;

    @Bean
    public RewardsService getRewardsService() {
        return new RewardsService(getGpsMicroServiceProxy(), getRewardCentralMicroServiceProxy());
    }

    @Bean
    public GpsMicroServiceProxy getGpsMicroServiceProxy() {
        Locale.setDefault(Locale.ENGLISH);
        GpsMicroServiceProxy gpsMicroServiceProxy = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(GpsMicroServiceProxy.class, gpsUrl);
        return gpsMicroServiceProxy;
    }

    @Bean
    public TripPricerMicroServiceProxy getTripPricerMicroServiceProxy() {
        TripPricerMicroServiceProxy tripPricerMicroServiceProxy = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(TripPricerMicroServiceProxy.class, tripPricerUrl);
        return tripPricerMicroServiceProxy;
    }

    @Bean
    public RewardCentralMicroServiceProxy getRewardCentralMicroServiceProxy() {
        RewardCentralMicroServiceProxy rewardCentralMicroServiceProxy = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(RewardCentralMicroServiceProxy.class, rewardUrl);
        return rewardCentralMicroServiceProxy;
    }

}
