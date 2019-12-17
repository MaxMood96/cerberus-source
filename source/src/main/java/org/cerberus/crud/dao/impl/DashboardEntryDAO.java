/* Cerberus Copyright (C) 2013 - 2017 cerberustesting
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

 This file is part of Cerberus.

 Cerberus is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Cerberus is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Cerberus.  If not, see <http://www.gnu.org/licenses/>.*/
package org.cerberus.crud.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cerberus.crud.dao.IDashboardEntryDAO;
import org.cerberus.crud.entity.DashboardEntry;
import org.cerberus.crud.entity.DashboardGroupEntries;
import org.cerberus.crud.factory.IFactoryDashboardEntry;
import org.cerberus.database.DatabaseSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cDelage
 */
@Repository
public class DashboardEntryDAO implements IDashboardEntryDAO {

    private static final Logger LOG = LogManager.getLogger(DashboardEntryDAO.class);

    @Autowired
    private DatabaseSpring databaseSpring;

    @Autowired
    private IFactoryDashboardEntry factoryDashboardEntry;

    @Override
    public List<DashboardEntry> readByGroupEntries(DashboardGroupEntries dashboardGroupEntries) {
        List<DashboardEntry> response = new ArrayList();
        StringBuilder query = new StringBuilder();
        query.append("SELECT`reportItemCode`,`param1`,`param2` FROM `dashboardEntry`WHERE `idGroupEntries` = ?;");
        try {
            Connection connection = databaseSpring.connect();
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            preStat.setInt(1, dashboardGroupEntries.getId());
            ResultSet rs = preStat.executeQuery();
            while (rs.next()) {
                response.add(this.loadFromResultSet(rs));
            }
        } catch (SQLException exception) {
            LOG.error("Catch exception during read dashboard entry by group", exception);
        }
        return response;
    }

    /**
     * Create a dashboard entry
     *
     * @param reportItemCode report item code string
     * @param param1 first param of the dashboard
     * @param param2 second param of the dashboard
     * @return the new id group entries
     */
    @Override
    public String create(int idGroupEntries, String reportItemCode, String param1, String param2) {
        String result = new String();
        final String query = "INSERT INTO dashboardEntry(idGroupEntries, reportItemCode, param1, param2) VALUES (?,?,?,?)";

        try {
            Connection connection = databaseSpring.connect();
            try {
                PreparedStatement preStat = connection.prepareStatement(query);

                preStat.setInt(1, idGroupEntries);
                preStat.setString(2, reportItemCode);
                preStat.setString(3, param1);
                preStat.setString(4, param2);
                preStat.execute();
                preStat.close();
                result = "Insert entry successfully";
            } catch (SQLException exception) {
                LOG.error("Unable to execute query : " + exception.toString());
                result = "Insert entry failed";
            } finally {
                try {

                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    LOG.warn(e.toString());
                }
            }
        } catch (Exception exception) {
            LOG.error("Failed to connect to database, catched Exception : ", exception);
        }

        return result;
    }

    @Override
    public DashboardEntry loadFromResultSet(ResultSet rs) throws SQLException {
        String reportItemCode = rs.getString("reportItemCode");
        String param1 = rs.getString("param1");
        String param2 = rs.getString("param2");
        return factoryDashboardEntry.create(reportItemCode, null, param1, param2);
    }
}