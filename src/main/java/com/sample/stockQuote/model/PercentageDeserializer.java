package com.sample.stockQuote.model;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Removing % from the response
 */
public class PercentageDeserializer extends JsonDeserializer<BigDecimal> {

	@Override
	public BigDecimal deserialize(JsonParser string, DeserializationContext txt) throws IOException {
		String str = string.getText();
		BigDecimal newVal = null;
		if (str != null && str.endsWith("%")) {
			str = str.substring(0, str.length() - 1);
		}
		newVal = new BigDecimal(str);
		return newVal;
	}

}
