package com.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmp.catalog.dto.RefreshRequest;
import com.bmp.catalog.metasync.MetaSyncTask;
import com.bmp.catalog.service.AssetService;
import com.bmp.catalog.service.ColumnService;
import com.bmp.catalog.service.MetaSyncService;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.cron.Poller;
import com.bmp.cron.Task;
import com.bmp.cron.broker.Broker;
import com.bmp.dao.entity.Datasource;
import com.bmp.dao.mapper.DatasourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MetaSyncServiceImpl implements MetaSyncService {
    private static final Logger logger = LoggerFactory.getLogger(MetaSyncServiceImpl.class);
    private final DatasourceMapper datasourceMapper;
    private final AssetService assetService;
    private final ColumnService columnService;
    private final Broker broker;

    public MetaSyncServiceImpl(DatasourceMapper datasourceMapper, AssetService assetService, ColumnService columnService, Broker broker) {
        this.datasourceMapper = datasourceMapper;
        this.assetService = assetService;
        this.columnService = columnService;
        this.broker = broker;
        Poller.start(broker, this::retriever, this::build, this::postAdd);
    }

    @Override
    public Result<?> refresh(RefreshRequest request) {
        Datasource datasource = datasourceMapper.selectById(request.getDatasourceID());
        datasource.setSyncPaths(request.getPaths());
        MetaSyncTask task = build(datasource);
        if (request.isAsync()) {
            Task.execute(task);
            return Result.success(null);
        }
        try {
            this.broker.send(task);
            return Result.success(null);
        } catch (Exception e) {
            logger.warn("send meta sync task failed", e);
            return Result.error(Status.META_SYNC_ERROR_ARGS, e.getMessage());
        }
    }

    private List<Datasource> retriever() {
        return datasourceMapper.selectList(new QueryWrapper<Datasource>()
                .lambda()
                // find all the scans whose scheduled execution time is
                // in from 1970.1.1 to next five seconds in the future
                .lt(Datasource::getSyncExecuteTime, Instant.now().plus(Poller.INTERVAL))
                .orderByAsc(Datasource::getSyncExecuteTime)
        );
    }

    private MetaSyncTask build(Datasource datasource) {
        return MetaSyncTask.build(datasource, assetService, columnService);
    }

    private void postAdd(MetaSyncTask task) {
        Datasource datasource = task.getDatasource();
        if (datasource.getSyncInterval() == null) {
            // it's a onetime sync.
            datasource.setSyncExecuteTime(null);
        } else {
            // set next execute time.
            datasource.setSyncExecuteTime(Instant.now().plus(datasource.getSyncInterval()));
        }
        datasourceMapper.updateById(datasource);
    }
}
