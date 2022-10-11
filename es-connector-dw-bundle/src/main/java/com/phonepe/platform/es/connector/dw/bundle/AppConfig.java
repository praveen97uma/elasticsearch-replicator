package com.phonepe.platform.es.connector.dw.bundle;

import io.dropwizard.Configuration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppConfig extends Configuration {
    @NotEmpty
    @NotNull
    String name;
}
