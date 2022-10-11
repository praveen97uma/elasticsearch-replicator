package com.phonepe.platform.es.client;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ESClientConfiguration {

    String host;

    int port;

}
