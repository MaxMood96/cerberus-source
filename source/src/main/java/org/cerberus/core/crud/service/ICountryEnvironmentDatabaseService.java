/**
 * Cerberus Copyright (C) 2013 - 2025 cerberustesting
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of Cerberus.
 *
 * Cerberus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cerberus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cerberus.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.cerberus.core.crud.service;

import java.util.List;
import org.cerberus.core.crud.entity.CountryEnvironmentDatabase;
import org.cerberus.core.exception.CerberusException;
import org.cerberus.core.util.answer.Answer;
import org.cerberus.core.util.answer.AnswerItem;
import org.cerberus.core.util.answer.AnswerList;

/**
 *
 * @author bcivel
 */
public interface ICountryEnvironmentDatabaseService {

    /**
     *
     * @param system
     * @param country
     * @param environment
     * @param database
     * @return
     */
    public AnswerItem<CountryEnvironmentDatabase> readByKey(String system, String country, String environment, String database);

    /**
     *
     * @param system
     * @param country
     * @param environment
     * @return
     */
    public AnswerList<CountryEnvironmentDatabase> readByVarious(String system, String country, String environment);

    /**
     *
     * @param system
     * @param country
     * @param environment
     * @param start
     * @param amount
     * @param column
     * @param dir
     * @param searchTerm
     * @param individualSearch
     * @return
     */
    public AnswerList<CountryEnvironmentDatabase> readByVariousByCriteria(String system, String country, String environment, int start, int amount, String column, String dir, String searchTerm, String individualSearch);

    /**
     *
     * @param object
     * @return
     */
    public Answer create(CountryEnvironmentDatabase object);

    /**
     *
     * @param object
     * @return
     */
    public Answer delete(CountryEnvironmentDatabase object);

    /**
     *
     * @param object
     * @return
     */
    public Answer update(CountryEnvironmentDatabase object);

    /**
     *
     * @param objectList
     * @return
     */
    public Answer createList(List<CountryEnvironmentDatabase> objectList);

    /**
     *
     * @param objectList
     * @return
     */
    public Answer deleteList(List<CountryEnvironmentDatabase> objectList);

    /**
     * Update all CountryEnvironmentDatabase from the sourceList to the
     * perimeter of system, country and environment list. All existing databases
     * from newList will be updated, the new ones added and missing ones
     * deleted.
     *
     * @param system
     * @param country
     * @param environement
     * @param newList
     * @return
     */
    public Answer compareListAndUpdateInsertDeleteElements(String system, String country, String environement, List<CountryEnvironmentDatabase> newList);

    /**
     *
     * @param system
     * @param country
     * @param environment
     * @param database
     * @return true is application exist or false is application does not exist
     * in database.
     */
    boolean exist(String system, String country, String environment, String database);

    /**
     *
     * @param answerItem
     * @return
     * @throws CerberusException
     */
    CountryEnvironmentDatabase convert(AnswerItem<CountryEnvironmentDatabase> answerItem) throws CerberusException;

    /**
     *
     * @param answerList
     * @return
     * @throws CerberusException
     */
    List<CountryEnvironmentDatabase> convert(AnswerList<CountryEnvironmentDatabase> answerList) throws CerberusException;

    /**
     *
     * @param answer
     * @throws CerberusException
     */
    void convert(Answer answer) throws CerberusException;
}
