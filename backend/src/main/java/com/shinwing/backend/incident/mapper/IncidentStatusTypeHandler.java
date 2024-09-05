package com.shinwing.backend.incident.mapper;

import com.shinwing.backend.incident.model.IncidentStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidentStatusTypeHandler  extends BaseTypeHandler<IncidentStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IncidentStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getStatusValue());
    }

    @Override
    public IncidentStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String statusValue = rs.getString(columnName);
        return getIncidentStatus(statusValue);
    }

    @Override
    public IncidentStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String statusValue = rs.getString(columnIndex);
        return getIncidentStatus(statusValue);
    }

    @Override
    public IncidentStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String statusValue = cs.getString(columnIndex);
        return getIncidentStatus(statusValue);
    }

    private IncidentStatus getIncidentStatus(String statusValue) {
        for (IncidentStatus status : IncidentStatus.values()) {
            if (status.getStatusValue().equals(statusValue)) {
                return status;
            }
        }
        return null;
    }
}