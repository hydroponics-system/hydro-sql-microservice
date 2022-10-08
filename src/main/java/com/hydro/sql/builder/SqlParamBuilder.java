package com.hydro.sql.builder;

import static com.hydro.common.datetime.DateTimeMapper.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.hydro.common.dictionary.enums.TextEnum;

/**
 * Sql builder to create all query binding parameters for making querys to the
 * database.
 * 
 * @author Sam Butler
 * @since Februrary 2, 2022
 */
public class SqlParamBuilder {
    private MapSqlParameterSource sqlParams;

    /**
     * Private Constructor for static class to initilize its components.
     * 
     * @param sqlParams The params to be set.
     */
    private SqlParamBuilder(MapSqlParameterSource sqlParams) {
        this.sqlParams = sqlParams == null ? new MapSqlParameterSource() : sqlParams;
    }

    /**
     * Initialize the {@link SqlParamBuilder} with an empty param set.
     * 
     * @return {@link SqlParamBuilder} for an empty object.
     */
    public static SqlParamBuilder with() {
        return new SqlParamBuilder(null);
    }

    /**
     * Initialize the {@link SqlParamBuilder} with the give sql params.
     * 
     * @param sqlParams The sql params to be set.
     * @return {@link SqlParamBuilder} for an empty object.
     */
    public static SqlParamBuilder with(MapSqlParameterSource sqlParams) {
        return new SqlParamBuilder(sqlParams);
    }

    /**
     * Add a parameter to the sql map.
     * 
     * @param name  The name of the parameter.
     * @param value The value of the parameter.
     * @return this builder object {@link SqlParamBuilder}
     */
    public SqlParamBuilder withParam(String name, Object value) {
        this.sqlParams.addValue(name, value);
        return this;
    }

    /**
     * Add parameter to sql map and check that the text enum is not null, if not get
     * the text id.
     * 
     * @param name  The name of the parameter.
     * @param value The value of the parameter
     * @return this builder object {@link SqlParamBuilder}
     */
    public SqlParamBuilder withParam(String name, TextEnum value) {
        return withParam(name, value == null ? null : value.getTextId());
    }

    /**
     * Add parameter to sql map for the given {@link Boolean} value.
     * 
     * @param name  The name of the parameter.
     * @param value The value of the parameter
     * @return this builder object {@link SqlParamBuilder}
     */
    public SqlParamBuilder withParam(String name, Boolean value) {
        return withParam(name, value == null ? null : value ? 1 : 0);
    }

    /**
     * Add parameter to sql map for the given {@link LocalDateTime} object.
     * 
     * @param name  The name of the parameter.
     * @param value The value of the parameter
     * @return this builder object {@link SqlParamBuilder}
     */
    public SqlParamBuilder withParam(String name, LocalDateTime value) {
        return withParam(name, printDate(value));
    }

    /**
     * Add parameter to sql map and check that the text enum is not null, if not get
     * the text id.
     * 
     * @param name  The name of the parameter.
     * @param value The value of the parameter
     * @return this builder object {@link SqlParamBuilder}
     */
    public SqlParamBuilder withParam(String name, Date value) {
        return withParam(name, printDate(value));
    }

    /**
     * Add parameter to sql map for an enum collection and check that the text enum
     * is not null, if not get the text id.
     * 
     * @param name  The name of the parameter.
     * @param value The value of the parameter
     * @return this builder object {@link SqlParamBuilder}
     */
    public <T> SqlParamBuilder withParamTextEnumCollection(String name, Collection<? extends TextEnum> values) {
        return withParam(name,
                         values == null ? null : values.stream().map(TextEnum::getTextId).collect(Collectors.toList()));
    }

    /**
     * Retrieve {@link MapSqlParameterSource} object for the given builder.
     * 
     * @return {@link MapSqlParameterSource}
     */
    public MapSqlParameterSource build() {
        return sqlParams;
    }
}
