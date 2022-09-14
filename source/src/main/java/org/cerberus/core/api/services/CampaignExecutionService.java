/**
 * Cerberus Copyright (C) 2013 - 2017 cerberustesting
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
package org.cerberus.core.api.services;

import lombok.AllArgsConstructor;
import org.cerberus.core.api.exceptions.EntityNotFoundException;
import org.cerberus.core.api.exceptions.FailedReadOperationException;
import org.cerberus.core.crud.entity.Invariant;
import org.cerberus.core.crud.entity.Tag;
import org.cerberus.core.crud.service.IInvariantService;
import org.cerberus.core.crud.service.ITagService;
import org.cerberus.core.crud.service.ITestCaseExecutionService;
import org.cerberus.core.exception.CerberusException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * @author lucashimpens
 */
@AllArgsConstructor
@Service
public class CampaignExecutionService {

    private final ITagService tagService;
    private final ITestCaseExecutionService testCaseExecutionService;
    private final IInvariantService invariantService;

    public Tag findByExecutionIdWithExecutions(String campaignExecutionId) {
        Optional<Tag> campaignExecution;
        try {
            List<Invariant> priorities = invariantService.readByIdName("PRIORITY");
            List<Invariant> countries = invariantService.readByIdName("COUNTRY");
            List<Invariant> environments = invariantService.readByIdName("ENVIRONMENT");
            campaignExecution = Optional.ofNullable(tagService.convert(tagService.readByKey(campaignExecutionId)));
            if (campaignExecution.isPresent()) {
                campaignExecution.get().setExecutionsNew(
                        testCaseExecutionService.readLastExecutionAndExecutionInQueueByTag(campaignExecution.get().getTag()));
                campaignExecution.get().getExecutionsNew()
                        .forEach(execution -> {
                            execution.setCountryObj(invariantService.findCountryInvariantFromCountries(execution.getCountry(), countries));
                            execution.setEnvironmentObj(invariantService.findEnvironmentInvariantFromEnvironments(execution.getEnvironment(), environments));
                            execution.setPriorityObj(invariantService.findPriorityInvariantFromPriorities(execution.getTestCasePriority(), priorities));
                        });
            } else {
                throw new EntityNotFoundException(Tag.class, "campaignExecutionId", campaignExecutionId);
            }
        } catch (CerberusException | ParseException e) {
            throw new FailedReadOperationException("An error occurred when retrieving the campaign execution.");
        }
        return campaignExecution.get();
    }
}
