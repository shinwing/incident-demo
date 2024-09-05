package com.shinwing.backend.incident.mapper;

import com.shinwing.backend.incident.model.IncidentPriority;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidentPriorityTypeHandler extends BaseTypeHandler<IncidentPriority> {

        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, IncidentPriority parameter, JdbcType jdbcType) throws SQLException {
            ps.setString(i, parameter.getPriorityValue());
        }

        @Override
        public IncidentPriority getNullableResult(ResultSet rs, String columnName) throws SQLException {
            String priorityValue = rs.getString(columnName);
            return getIncidentPriority(priorityValue);
        }

        @Override
        public IncidentPriority getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            String priorityValue = rs.getString(columnIndex);
            return getIncidentPriority(priorityValue);
        }

        @Override
        public IncidentPriority getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            String priorityValue = cs.getString(columnIndex);
            return getIncidentPriority(priorityValue);
        }

        private IncidentPriority getIncidentPriority(String priorityValue) {
            for (IncidentPriority priority : IncidentPriority.values()) {
                if (priority.getPriorityValue().equals(priorityValue)) {
                    return priority;
                }
            }
            return null;
        }
}