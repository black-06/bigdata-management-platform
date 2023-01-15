package com.bmp.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.bmp.commons.enums.DatasourceType;
import com.bmp.commons.enums.Status;
import com.bmp.commons.enums.SubjectType;
import com.bmp.connector.api.ConnectorInfo;
import com.bmp.connector.api.ConnectorManager;
import com.bmp.connector.api.list.AssetPath;
import com.bmp.dao.utils.BaseEntity;
import com.bmp.dao.utils.DurationTypeHandler;
import com.bmp.dao.utils.JacksonListTypeHandler;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Duration;
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
    @TableField(value = "sync_paths", typeHandler = AssetPathListHandlerList.class)
    private List<AssetPath> syncPaths;
    @TableField(value = "sync_execute_time", updateStrategy = FieldStrategy.IGNORED)
    private Instant syncExecuteTime;
    @TableField(value = "sync_interval", typeHandler = DurationTypeHandler.class)
    private Duration syncInterval;

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

    public static class AssetPathListHandlerList extends JacksonListTypeHandler<AssetPath> {
        @Override
        protected TypeReference<List<AssetPath>> getTypeReference() {
            return new TypeReference<List<AssetPath>>() {
            };
        }
    }
}
