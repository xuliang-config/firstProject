package com.example.clockIn.source;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "plan")
public class FilePlace {
    public String place;

    public String getPlace(){
        return this.place;
    }

    public void setPlace(String place){
        this.place = place;
    }

}
