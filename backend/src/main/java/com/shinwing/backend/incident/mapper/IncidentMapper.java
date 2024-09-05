package com.shinwing.backend.incident.mapper;

import com.shinwing.backend.incident.model.Incident;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IncidentMapper {
    @Select("SELECT * FROM t_incident")
    List<Incident> findAll();

    @Select("<script>" +
            "SELECT count(*) FROM t_incident" +
            "<where>" +
            "AND deleted_at is null " +
            "<if test='type != null and type != \"\"'> AND incident_type = #{type}</if>" +
            "<if test='subType != null and subType != \"\"'> AND incident_sub_type = #{subType}</if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status}</if>" +
            "</where>" +
            "</script>")
    Integer count(@Param("type") String type, @Param("subType") String subType, @Param("status") String status);

    @Select("<script>" +
            "SELECT * FROM t_incident" +
            "<where>" +
            "AND deleted_at is null " +
            "<if test='type != null and type != \"\"'> AND incident_type = #{type}</if>" +
            "<if test='subType != null and subType != \"\"'> AND incident_sub_type = #{subType}</if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status}</if>" +
            "</where>" +
            "ORDER BY id ASC " +
            "LIMIT #{limit} OFFSET #{offset}" +
            "</script>")
    List<Incident> findByPage(@Param("offset") Long offset, @Param("limit") Long limit, @Param("type") String type, @Param("subType") String subType, @Param("status") String status);

    @Select("SELECT * FROM t_incident WHERE token = #{token} and deleted_at is null")
    Incident findByToken(String token);

    @Select("SELECT * FROM t_incident WHERE id = #{id}")
    Incident findById(Long id);

    @Insert("INSERT INTO t_incident(title, token, description, incident_type, incident_sub_type, status, priority, created_by) " +
            " VALUES (#{title}, #{token}, #{description}, #{incidentType}, #{incidentSubType}, #{status}, #{priority}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(Incident incident);

    @Update("UPDATE t_incident SET title = #{title}, description = #{description}, status = #{status}, priority = #{priority} " +
            " WHERE id = #{id}")
    Integer update(Incident incident);

    @Update("UPDATE t_incident SET deleted_at = #{deletedAt} WHERE id = #{id}")
    void delete(Long id, Long deletedAt);
}
