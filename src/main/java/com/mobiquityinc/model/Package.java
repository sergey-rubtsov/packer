package com.mobiquityinc.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Package {

    private int capacity;

    private List<Thing> things;

}
