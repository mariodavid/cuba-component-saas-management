package de.diedavids.cuba.ccsm

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.global.UserSessionSource
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.sql.SQLException

abstract class IntegrationSpec extends Specification {

    @Shared
    @ClassRule
    public CcsmTestContainer cont = CcsmTestContainer.Common.INSTANCE

    protected Metadata metadata
    protected UserSessionSource userSessionSource
    protected DataManager dataManager

    void setup() {
        userSessionSource = AppBeans.get(UserSessionSource.NAME)
        dataManager = AppBeans.get(DataManager.NAME)
        metadata = cont.metadata()
    }


    protected void clearTable(String tableName) {
        String sql = "delete from $tableName"
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        try {
            runner.update(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}