package com.bmp.catalog.metasync;

import com.bmp.cron.Task;
import com.bmp.dao.entity.Datasource;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class MetaSyncTask extends Task {
    private static final Logger logger = LoggerFactory.getLogger(MetaSyncTask.class);
    @JsonInclude
    private Datasource datasource;

    @Override
    public String type() {
        return "meta_sync";
    }

    @Override
    public void run() {
        // TODO:
        //  1. build Lister from datasource.
        //  2. list sync path.
        //  3. convert assets and columns.
        //  4. save assets and columns.
    }

    @Transactional
    public void exampleTXN() {
        // do something...
    }
}
