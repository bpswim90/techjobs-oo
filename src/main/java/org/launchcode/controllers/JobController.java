package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.data.JobData;
import org.launchcode.models.forms.JobForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam int id) {

        Job job = jobData.findById(id);
        model.addAttribute(job);

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute(jobForm);
            return "new-job";
        }

        Employer newEmployer = jobData.getEmployers().findById(jobForm.getEmployerId());
        Location newLocation = jobData.getLocations().findById(jobForm.getLocationId());
        CoreCompetency newCoreCompetency = jobData.getCoreCompetencies().findById(jobForm.getCoreCompetencyId());
        PositionType newPositionType = jobData.getPositionTypes().findById(jobForm.getPositionTypeId());

        Job newJob = new Job(jobForm.getName(), newEmployer, newLocation, newPositionType, newCoreCompetency);

        jobData.add(newJob);

        return "redirect:/job?id=" + newJob.getId();

    }
}
