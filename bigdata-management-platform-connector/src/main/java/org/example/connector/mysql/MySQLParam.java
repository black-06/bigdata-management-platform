package org.example.connector.mysql;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MySQLParam {
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    @JsonIgnore
    private final String jdbcUrl;

    @JsonCreator
    public MySQLParam(
            @JsonProperty("host") String host,
            @JsonProperty("port") String port,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password
    ) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.jdbcUrl = String.format("jdbc:mysql://%s:%s", host, port);
    }
}
