package com.hydro.sql.builder;

import static com.hydro.common.datetime.DateTimeMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydro.common.dictionary.enums.WebRole;

/**
 * Test class for the Sql Param Builder.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
public class SqlParamBuilderTest {

    @Test
    public void testBuildDefaultParameterSource() {
        MapSqlParameterSource params = SqlParamBuilder.with().build();
        assertNotNull(params, "Params should be defined");
    }

    @Test
    public void testBuildObjectParamsString() {
        MapSqlParameterSource params = SqlParamBuilder.with().withParam("TEST", "Value").build();

        assertNotNull(params, "Params should be defined");
        assertEquals("Value", params.getValue("TEST"), "String Value");
    }

    @Test
    public void testBuildObjectParamsTextEnum() {
        MapSqlParameterSource params = SqlParamBuilder.with().withParam("TEST", WebRole.ADMIN).build();

        assertNotNull(params, "Params should be defined");
        assertEquals("ADMIN", params.getValue("TEST"), "Text Enum Value");
    }

    @Test
    public void testBuildObjectParamsBoolean() {
        MapSqlParameterSource params = SqlParamBuilder.with().withParam("TEST_TRUE", true)
                .withParam("TEST_FALSE", false).build();

        assertNotNull(params, "Params should be defined");
        assertEquals(1, params.getValue("TEST_TRUE"), "True Boolean Value");
        assertEquals(0, params.getValue("TEST_FALSE"), "False Boolean Value");
    }

    @Test
    public void testBuildObjectParamsLocalDateTime() {
        LocalDateTime nowValue = LocalDateTime.now();
        MapSqlParameterSource params = SqlParamBuilder.with().withParam("TEST", nowValue).build();

        assertNotNull(params, "Params should be defined");
        assertEquals(printDate(nowValue), params.getValue("TEST"), "Local Date Time Value");
    }

    @Test
    public void testBuildObjectParamsDate() {
        Date dateNow = new Date();
        MapSqlParameterSource params = SqlParamBuilder.with().withParam("TEST", dateNow).build();

        assertNotNull(params, "Params should be defined");
        assertEquals(printDate(dateNow), params.getValue("TEST"), "Date Value");
    }

    @Test
    public void testBuildObjectParamsTextEnumCollection() {
        List<WebRole> roles = new ArrayList<>();
        roles.add(WebRole.ADMIN);
        roles.add(WebRole.DEVELOPER);

        MapSqlParameterSource params = SqlParamBuilder.with().withParamTextEnumCollection("TEST", roles).build();

        List<WebRole> returnedRoles = convertToList(params.getValue("TEST"));
        assertNotNull(params, "Params should be defined");
        assertEquals(2, returnedRoles.size(), "Size should be 2");
        assertEquals("ADMIN", returnedRoles.get(0), "Should be role ADMIN");
        assertEquals("DEVELOPER", returnedRoles.get(1), "Should be role DEVELOPER");
    }

    private <T> List<T> convertToList(Object v) {
        return new ObjectMapper().convertValue(v, new TypeReference<List<T>>() {});
    }

}
