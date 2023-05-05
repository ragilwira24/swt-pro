package com.ragilwiradiputra.sawitpro.records;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ResponseRecord<T>(int statusCode, String statusMessage, T body) {
}
