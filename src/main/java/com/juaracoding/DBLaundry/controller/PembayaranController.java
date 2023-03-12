package com.juaracoding.DBLaundry.controller;/*
IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author User a.k.a. Safril Efendi Lubis
Java Developer
Created on 12/03/2023 14:33
@Last Modified 12/03/2023 14:33
Version 1.1
*/

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.PembayaranDTO;
import com.juaracoding.DBLaundry.model.Pembayaran;
import com.juaracoding.DBLaundry.service.PembayaranService;
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
public class PembayaranController {

    private PembayaranService pembayaranService;
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();

    private List<Pembayaran> lsCPUpload = new ArrayList<Pembayaran>();

    private String [] strExceptionArr = new String[2];

    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public PembayaranController(PembayaranService pembayaranService) {
        strExceptionArr[0] = "PembayaranController";
        mapSorting();
        this.pembayaranService = pembayaranService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","idPembayaran");
        mapSorting.put("nama","namaPembayaran");
    }

    @GetMapping("/v1/pembayaran/new")
    public String createPembayaran(Model model, WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("pembayaran", new PembayaranDTO());
        return "pembayaran/create_pembayaran";
    }

    @GetMapping("/v1/pembayaran/edit/{id}")
    public String editPembayaran(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = pembayaranService.findById(id,request);
        PembayaranDTO pembayaranDTO = (objectMapper.get("data")==null?null:(PembayaranDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success"))
        {
            PembayaranDTO pembayaranDTOForSelect = (PembayaranDTO) objectMapper.get("data");
            model.addAttribute("pembayaran", pembayaranDTO);
            return "pembayaran/edit_Pembayaran";
        }
        else
        {
            model.addAttribute("pembayaran", new PembayaranDTO());
            return "redirect:/api/usrmgmnt/v1/pembayaran/default";
        }
    }
    @PostMapping("/v1/pembayaran/new")
    public String newPembayaran(@ModelAttribute(value = "pembayaran")
                            @Valid PembayaranDTO pembayaranDTO
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
            model.addAttribute("pembayaran",pembayaranDTO);
            model.addAttribute("status","error");

            return "pembayaran/create_pembayaran";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("pembayaran",pembayaranDTO);
            return "pembayaran/create_pembayaran";
        }
        /* END OF VALIDATION */

        Pembayaran pembayaran = modelMapper.map(pembayaranDTO, new TypeToken<Pembayaran>() {}.getType());
        objectMapper = pembayaranService.savePembayaran(pembayaran,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/usrmgmnt/v1/pembayaran/fbpsb/0/asc/idPembayaran?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("pembayaran",new PembayaranDTO());
            model.addAttribute("status","error");
            return "pembayaran/create_pembayaran";
        }
    }

    @PostMapping("/v1/pembayaran/edit/{id}")
    public String editPembayaran(@ModelAttribute("pembayaran")
                             @Valid PembayaranDTO pembayaranDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("pembayaran",pembayaranDTO);
            return "pembayaran/edit_pembayaran";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("pembayaran",pembayaranDTO);
            return "pembayaran/edit_pembayaran";
        }
        /* END OF VALIDATION */

        Pembayaran pembayaran = modelMapper.map(pembayaranDTO, new TypeToken<Pembayaran>() {}.getType());
        objectMapper = pembayaranService.updatePembayaran(id,pembayaran,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("pembayaran",new PembayaranDTO());
            return "redirect:/api/usrmgmnt/v1/pembayaran/fbpsb/0/asc/idPembayaran?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("pembayaran",new PembayaranDTO());
            return "pembayaran/edit_pembayaran";
        }
    }


    @GetMapping("/v1/pembayaran/default")
    public String getDefaultData(Model model,WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        Pageable pageable = PageRequest.of(0,5, Sort.by("idPembayaran"));
        objectMapper = pembayaranService.findAllPembayaran(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper,request);

        model.addAttribute("pembayaran",new PembayaranDTO());
        model.addAttribute("sortBy","idPembayaran");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/pembayaran/pembayaran";
    }

    @GetMapping("/v1/pembayaran/fbpsb/{page}/{sort}/{sortby}")
    public String findByPembayaran(
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
        sortzBy = sortzBy==null?"idPembayaran":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = pembayaranService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("pembayaran",new PembayaranDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/pembayaran/pembayaran";
    }


    @GetMapping("/v1/pembayaran/delete/{id}")
    public String deletePembayaran(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = pembayaranService.deletePembayaran(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set session
        model.addAttribute("pembayaran", new PembayaranDTO());
        return "redirect:/api/usrmgmnt/v1/pembayaran/default";
    }
}


