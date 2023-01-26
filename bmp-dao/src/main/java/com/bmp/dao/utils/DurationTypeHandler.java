package com.bmp.dao.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

/**
 * convert {@link Duration} to nanos (long).
 */
public class DurationTypeHandler extends BaseTypeHandler<Duration> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Duration parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.toNanos());
    }

    @Override
    public Duration getNullableResult(ResultSet rs, String columnName) throws SQLException {
        long nanos = rs.getLong(columnName);
        return nanos == 0 && rs.wasNull() ? null : Duration.ofNanos(nanos);
    }

    @Override
    public Duration getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        long nanos = rs.getLong(columnIndex);
        return nanos == 0 && rs.wasNull() ? null : Duration.ofNanos(nanos);
    }

    @Override
    public Duration getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        long nanos = cs.getLong(columnIndex);
        return nanos == 0 && cs.wasNull() ? null : Duration.ofNanos(nanos);
    }
}
