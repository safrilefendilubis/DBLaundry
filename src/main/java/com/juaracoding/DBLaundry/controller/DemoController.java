package com.juaracoding.DBLaundry.controller;


import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.DemoDTO;
import com.juaracoding.DBLaundry.model.Demo;
import com.juaracoding.DBLaundry.service.DemoService;
import com.juaracoding.DBLaundry.utils.ConstantMessage;
import com.juaracoding.DBLaundry.utils.ManipulationMap;
import com.juaracoding.DBLaundry.utils.MappingAttribute;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/usrmgmnt")
public class DemoController {

    private DemoService demoService;

    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();

    private List<Demo> lsCPUpload = new ArrayList<Demo>();

    private String [] strExceptionArr = new String[2];

    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public DemoController(DemoService demoService) {
        strExceptionArr[0] = "DemoController";
        mapSorting();
        this.demoService = demoService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","idDemo");
        mapSorting.put("nama","namaDemo");
    }

    @GetMapping("/v1/demo/new")
    public String createDemo(Model model, WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("demo", new DemoDTO());
        return "demo/create_demo";
    }

    @GetMapping("/v1/demo/edit/{id}")
    public String editDemo(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = demoService.findById(id,request);
        DemoDTO demoDTO = (objectMapper.get("data")==null?null:(DemoDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success"))
        {
            DemoDTO demoDTOForSelect = (DemoDTO) objectMapper.get("data");
            model.addAttribute("demo", demoDTO);
            return "demo/edit_demo";

        }
        else
        {
            model.addAttribute("demo", new DemoDTO());
            return "redirect:/api/usrmgmnt/v1/demo/default";
        }
    }
    @PostMapping("/v1/demo/new")
    public String newDemo(@ModelAttribute(value = "demo")
                          @Valid DemoDTO demoDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
    )
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }

        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("demo",demoDTO);
            model.addAttribute("status","error");

            return "demo/create_demo";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("demo",demoDTO);
            return "demo/create_demo";
        }
        /* END OF VALIDATION */

        Demo demo = modelMapper.map(demoDTO, new TypeToken<Demo>() {}.getType());
        objectMapper = demoService.saveDemo(demo,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/usrmgmnt/v1/demo/fbpsb/0/asc/idDemo?columnFirst=idDemo&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("demo",new DemoDTO());
            model.addAttribute("status","error");
            return "demo/create_demo";
        }
    }

    @PostMapping("/v1/demo/edit/{id}")
    public String editDemo(@ModelAttribute("demo")
                          @Valid DemoDTO demoDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("demo",demoDTO);
            return "demo/edit_demo";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("demo",demoDTO);
            return "demo/edit_demo";
        }
        /* END OF VALIDATION */

        Demo demo = modelMapper.map(demoDTO, new TypeToken<Demo>() {}.getType());
        objectMapper = demoService.updateDemo(id,demo,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("demo",new DemoDTO());
            return "redirect:/api/usrmgmnt/v1/demo/fbpsb/0/asc/idDemo?columnFirst=idDemo&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("demo",new DemoDTO());
            return "demo/edit_demo";
        }
    }


    @GetMapping("/v1/demo/default")
    public String getDefaultData(Model model,WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        Pageable pageable = PageRequest.of(0,5, Sort.by("idDemo"));
        objectMapper = demoService.findAllDemo(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper,request);

        model.addAttribute("demo",new DemoDTO());
        model.addAttribute("sortBy","idDemo");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/demo/demo";
    }

    @GetMapping("/v1/demo/fbpsb/{page}/{sort}/{sortby}")
    public String findByDemo(
            Model model,
            @PathVariable("page") Integer pagez,
            @PathVariable("sort") String sortz,
            @PathVariable("sortby") String sortzBy,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            @RequestParam String sizeComponent,
            WebRequest request
    ){
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"idDemo":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = demoService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("demo",new DemoDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/demo/demo";
    }

    @GetMapping("/v1/demo/delete/{id}")
    public String deleteDemo(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = demoService.deleteDemo(id,request);
        model.addAttribute("demo", new DemoDTO());
        return "redirect:/api/usrmgmnt/v1/demo/default";
    }

//    @GetMapping("/v1/demo/del/{id}")
//    public String getDeleteDemo(Model model, WebRequest request, @PathVariable("id") Long id)
//    {
//        if(OtherConfig.getFlagSessionValidation().equals("y"))
//        {
//            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
//            if(request.getAttribute("USR_ID",1)==null){
//                return "redirect:/api/check/logout";
//            }
//        }
//        objectMapper = demoService.findById(id,request);
//        DemoDTO demoDTO = (objectMapper.get("data")==null?null:(DemoDTO) objectMapper.get("data"));
//        if((Boolean) objectMapper.get("success"))
//        {
//            DemoDTO demoDTOForSelect = (DemoDTO) objectMapper.get("data");
//            model.addAttribute("demo", demoDTO);
//            return "demo/delete_demo";
//        }
//        else
//        {
//            model.addAttribute("demo", new DemoDTO());
//            return "redirect:/api/usrmgmnt/v1/demo/default";
//        }
//    }

//    @PutMapping("/v1/demo/delete/{id}")
//    public String deleteDemo(Model model, WebRequest request, @PathVariable("id") Long id)
//    {
//        if(OtherConfig.getFlagSessionValidation().equals("y"))
//        {
//            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
//            if(request.getAttribute("USR_ID",1)==null){
//                return "redirect:/api/check/logout";
//            }
//        }
//        objectMapper = demoService.deleteDemo(id,request);
//        model.addAttribute("demo", new DemoDTO());
//        if((Boolean) objectMapper.get("success"))
//        {
//            return "redirect:/api/usrmgmnt/v1/demo/default";
//
//        }
//        else
//        {
//            return "demo/delete_demo";
//        }
//    }

}
