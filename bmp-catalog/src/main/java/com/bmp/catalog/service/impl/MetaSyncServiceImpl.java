package com.bmp.catalog.service.impl;

import com.bmp.catalog.dto.RefreshRequest;
import com.bmp.catalog.metasync.MetaSyncTask;
import com.bmp.catalog.service.MetaSyncService;
import com.bmp.commons.result.Result;
import com.bmp.cron.Poller;
import com.bmp.cron.broker.Broker;
import com.bmp.dao.entity.Datasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaSyncServiceImpl implements MetaSyncService {
    private static final Logger logger = LoggerFactory.getLogger(MetaSyncServiceImpl.class);

    public MetaSyncServiceImpl(Broker broker) {
        Poller.start(broker, this::retriever, this::build, this::postAdd);
    }

    @Override
    public Result<?> refresh(RefreshRequest request) {
        // TODO:
        //  1. select datasource
        //  2. build meta sync task.
        //  3. execute task or send to broker.
        throw new RuntimeException("unimplemented");
    }

    private List<Datasource> retriever() {
        // TODO: find all the scans whose scheduled execution time is in
        //  from 1970.1.1 to next five seconds in the future
        throw new RuntimeException("unimplemented");
    }

    private MetaSyncTask build(Datasource datasource) {
        // TODO: build meta sync task from datasource.
        throw new RuntimeException("unimplemented");
    }

    private void postAdd(MetaSyncTask task) {
        // TODO: update sync execute time in datasource.
    }
}
