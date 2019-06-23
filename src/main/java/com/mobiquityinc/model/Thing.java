package com.mobiquityinc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Thing {

    int index;

    double weight;

    int cost;

}
