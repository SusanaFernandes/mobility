/*
 * @(#)SubmitCandidacyActivity.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Susana Fernandes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Internal Mobility Module.
 *
 *   The Internal Mobility Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Internal Mobility  Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Internal Mobility  Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.mobility.domain.activity;

import module.mobility.domain.JobOffer;
import module.mobility.domain.JobOfferCandidacy;
import module.mobility.domain.JobOfferProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class SubmitCandidacyActivity extends WorkflowActivity<JobOfferProcess, SubmitCandidacyInformation> {

    @Override
    public boolean isActive(JobOfferProcess process, User user) {
        JobOffer jobOffer = process.getJobOffer();
        return jobOffer.isApproved() && jobOffer.isInCandidacyPeriod() && jobOffer.getHasAllNeededInfoForSubmitCancidacy()
                && !jobOffer.hasCandidacy(user);
    }

    @Override
    protected void process(SubmitCandidacyInformation activityInformation) {
        Person person = Authenticate.getUser().getPerson();
        JobOfferCandidacy jobOfferCandidacy =
                new JobOfferCandidacy(person.getPersonalPortfolio().getLastPersonalPortfolioInfo(),
                        activityInformation.getFiles());
        activityInformation.getProcess().getJobOffer().addJobOfferCandidacy(jobOfferCandidacy);
    }

    @Override
    public ActivityInformation<JobOfferProcess> getActivityInformation(JobOfferProcess process) {
        return new SubmitCandidacyInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/MobilityResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
