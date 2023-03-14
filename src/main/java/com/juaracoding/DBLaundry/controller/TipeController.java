package com.juaracoding.DBLaundry.controller;/*
IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author User a.k.a. Safril Efendi Lubis
Java Developer
Created on 14/03/2023 13:26
@Last Modified 14/03/2023 13:26
Version 1.1
*/

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.TipeDTO;
import com.juaracoding.DBLaundry.model.Tipe;
import com.juaracoding.DBLaundry.service.TipeService;
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
@RequestMapping("/api/mgmnt")
public class TipeController {

    private TipeService tipeService;

    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();

    private List<Tipe> lsCPUpload = new ArrayList<Tipe>();

    private String [] strExceptionArr = new String[2];

    private MappingAttribute mappingAttribute = new MappingAttribute();

    public TipeController(TipeService tipeService) {
        strExceptionArr[0] = "TipeController";
        mapSorting();
        this.tipeService = tipeService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","idTipe");
        mapSorting.put("nama","namaTipe");
    }

    @GetMapping("/v1/tipe/new")
    public String createTipe(Model model, WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("tipe", new TipeDTO());
        return "tipe/create_tipe";
    }

    @GetMapping("/v1/Tipe/edit/{id}")
    public String editTipe(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = tipeService.findById(id,request);
        TipeDTO tipeDTO = (objectMapper.get("data")==null?null:(TipeDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success"))
        {
            TipeDTO tipeDTOForSelect = (TipeDTO) objectMapper.get("data");
            model.addAttribute("tipe", tipeDTO);
            return "tipe/edit_tipe";
        }
        else
        {
            model.addAttribute("tipe", new TipeDTO());
            return "redirect:/api/mgmnt/v1/tipe/default";
        }
    }
    @PostMapping("/v1/tipe/new")
    public String newTipe(@ModelAttribute(value = "tipe")
                            @Valid TipeDTO tipeDTO
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
            model.addAttribute("tipe",tipeDTO);
            model.addAttribute("status","error");

            return "tipe/create_tipe";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("tipe",tipeDTO);
            return "tipe/create_tipe";
        }
        /* END OF VALIDATION */

        Tipe tipe = modelMapper.map(tipeDTO, new TypeToken<Tipe>() {}.getType());
        objectMapper = tipeService.saveTipe(tipe,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/mgmnt/v1/tipe/fbpsb/0/asc/idTipe?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("tipe",new TipeDTO());
            model.addAttribute("status","error");
            return "tipe/create_tipe";
        }
    }

    @PostMapping("/v1/tipe/edit/{id}")
    public String editTipe(@ModelAttribute("tipe")
                             @Valid TipeDTO tipeDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("tipe",tipeDTO);
            return "tipe/edit_tipe";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("tipe",tipeDTO);
            return "tipe/edit_tipe";
        }
        /* END OF VALIDATION */

        Tipe tipe = modelMapper.map(tipeDTO, new TypeToken<Tipe>() {}.getType());
        objectMapper = tipeService.updateTipe(id,tipe,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("tipe",new TipeDTO());
            return "redirect:/api/mgmnt/v1/tipe/fbpsb/0/asc/idTipe?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("tipe",new TipeDTO());
            return "tipe/edit_tipe";
        }
    }


    @GetMapping("/v1/tipe/default")
    public String getDefaultData(Model model,WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        Pageable pageable = PageRequest.of(0,5, Sort.by("idTipe"));
        objectMapper = tipeService.findAllTipe(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper,request);

        model.addAttribute("tipe",new TipeDTO());
        model.addAttribute("sortBy","idtipe");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/tipe/tipe";
    }

    @GetMapping("/v1/tipe/fbpsb/{page}/{sort}/{sortby}")
    public String findByTipe(
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
        sortzBy = sortzBy==null?"idTipe":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = tipeService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("tipe",new TipeDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/tipe/tipe";
    }


    @GetMapping("/v1/tipe/delete/{id}")
    public String deleteTipe(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = tipeService.deleteTipe(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set session
        model.addAttribute("tipe", new TipeDTO());
        return "redirect:/api/mgmnt/v1/tipe/default";
    }
}
