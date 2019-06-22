package com.mobiquityinc.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Package {

    private int capacity;

    private Map<Integer, Thing> things;

}
