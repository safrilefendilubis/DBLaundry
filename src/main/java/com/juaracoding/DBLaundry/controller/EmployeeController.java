package com.juaracoding.DBLaundry.controller;

import com.juaracoding.DBLaundry.model.Employee;
import com.juaracoding.DBLaundry.service.EmployeeService;
import com.juaracoding.DBLaundry.utils.MappingAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class EmployeeController {

    private EmployeeService employeeService;
    private MappingAttribute mappingAttribute = new MappingAttribute();
    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // display list of employees
    @GetMapping("/")
    public String viewHomePage(Model model, WebRequest request) {
        mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
        if(request.getAttribute("USR_ID",1)==null){
            return "redirect:/api/check/logout";
        }
        return findPaginated(1, "firstName", "asc", model,request);
    }

    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model, WebRequest request) {
        mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
        if(request.getAttribute("USR_ID",1)==null){
            return "redirect:/api/check/logout";
        }

        // create model attribute to bind form data
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee,Model model, WebRequest request) {
        mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
        if(request.getAttribute("USR_ID",1)==null){
            return "redirect:/api/check/logout";
        }
        // save employee to database
        employeeService.saveEmployee(employee);
        return "redirect:/api/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable( value = "id") long id, Model model,WebRequest request) {
        mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
        if(request.getAttribute("USR_ID",1)==null){
            return "redirect:/api/check/logout";
        }
        // get employee from the service
        Employee employee = employeeService.getEmployeeById(id);
        // set employee as a model attribute to pre-populate the form
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable (value = "id") long id,Model model,WebRequest request) {
        mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
        if(request.getAttribute("USR_ID",1)==null){
            return "redirect:/api/check/logout";
        }
        // call delete employee method
        employeeService.deleteEmployeeById(id);
        return "redirect:/";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model,WebRequest request) {
        mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
        if(request.getAttribute("USR_ID",1)==null){
            return "redirect:/api/check/logout";
        }
        int pageSize = 5;

        Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Employee> listEmployees = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listEmployees", listEmployees);
        return "index";
    }
}
