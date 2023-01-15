package com.github.bmp.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bmp.commons.enums.DatasourceType;
import com.github.bmp.commons.enums.Status;
import com.github.bmp.commons.enums.SubjectType;
import com.github.bmp.connector.api.ConnectorInfo;
import com.github.bmp.connector.api.ConnectorManager;
import com.github.bmp.connector.api.list.AssetPath;
import com.github.bmp.dao.utils.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "catalog_datasource", autoResultMap = true)
public class Datasource extends BaseEntity implements Subject {
    @TableField("name")
    private String name;
    @TableField("description")
    private String description;
    @TableField("status")
    private Status status;
    @TableField("type")
    private DatasourceType type;
    @TableField(value = "connection_info", typeHandler = ConnectionInfoHandler.class)
    private ConnectorInfo connectorInfo;
    @TableField("collection_id")
    @JsonAlias("collection_id")
    private Integer collectionID;

    /* meta sync info */
    @TableField(value = "sync_paths", typeHandler = AssetPathListHandler.class)
    private List<AssetPath> syncPaths;
    @TableField(value = "sync_execute_time", updateStrategy = FieldStrategy.IGNORED)
    private Instant syncExecuteTime;
    @TableField("sync_interval")
    private Long syncInterval; // unit is nanos.

    @Override
    public SubjectType type() {
        return SubjectType.DATASOURCE;
    }

    @Override
    public Datasource setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public Datasource setCreateTime(Instant createTime) {
        super.setCreateTime(createTime);
        return this;
    }

    @Override
    public Datasource setUpdateTime(Instant updateTime) {
        super.setUpdateTime(updateTime);
        return this;
    }

    /**
     * ConnectionInfoHandler is a {@link org.apache.ibatis.type.TypeHandler}
     * that parses json to {@link ConnectorInfo}
     * and write {@link ConnectorInfo} to json.
     */
    public static class ConnectionInfoHandler extends AbstractJsonTypeHandler<ConnectorInfo> {
        @Override
        protected ConnectorInfo parse(String json) {
            try {
                return ConnectorManager.unmarshalConnectorInfo(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected String toJson(ConnectorInfo obj) {
            try {
                return ConnectorManager.marshalConnectorInfo(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class AssetPathListHandler extends AbstractJsonTypeHandler<List<AssetPath>> {
        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        protected List<AssetPath> parse(String json) {
            try {
                return mapper.readValue(json, new TypeReference<List<AssetPath>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected String toJson(List<AssetPath> list) {
            try {
                return mapper.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
