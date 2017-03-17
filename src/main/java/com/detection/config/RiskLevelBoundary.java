package com.detection.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @fileName RiskLevelBoundary.java
 * @author csk
 * @createTime 2017年3月8日 上午9:39:28
 * @version 1.0
 * @function
 */

@Component
@ConfigurationProperties(prefix="fire.default.riskLevel")
public class RiskLevelBoundary {
    
    private float firstLevelBoundary;
    private float secondLevelBoundary;
    private float thirdLevelBoundary;
    
    public float getFirstLevelBoundary() {
        return firstLevelBoundary;
    }
    public void setFirstLevelBoundary(float firstLevelBoundary) {
        this.firstLevelBoundary = firstLevelBoundary;
    }
    public float getSecondLevelBoundary() {
        return secondLevelBoundary;
    }
    public void setSecondLevelBoundary(float secondLevelBoundary) {
        this.secondLevelBoundary = secondLevelBoundary;
    }
    public float getThirdLevelBoundary() {
        return thirdLevelBoundary;
    }
    public void setThirdLevelBoundary(float thirdLevelBoundary) {
        this.thirdLevelBoundary = thirdLevelBoundary;
    }
    
    
}
