package com.mobiquityinc.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Package {

    private int capacity;

    private List<Thing> things;

}
