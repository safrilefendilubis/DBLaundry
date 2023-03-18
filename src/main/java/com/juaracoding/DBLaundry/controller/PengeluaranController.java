package com.juaracoding.DBLaundry.controller;/*
Created By IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author Syarifudin a.k.a. Muhamad Syarifuidn
Java Developer
Created on 09/03/2023 12:57
@Last Modified 09/03/2023 12:57
Version 1.1
*/

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.PengeluaranDTO;
import com.juaracoding.DBLaundry.model.Pengeluaran;
import com.juaracoding.DBLaundry.service.PengeluaranService;
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
public class PengeluaranController {

    private PengeluaranService pengeluaranService;

    @Autowired
    private ModelMapper modelMapper;

    private Map<String, Object> objectMapper = new HashMap<String, Object>();
    private Map<String, String> mapSorting = new HashMap<String, String>();

    private List<Pengeluaran> lsCPUpload = new ArrayList<Pengeluaran>();

    private String[] strExceptionArr = new String[2];

    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public PengeluaranController(PengeluaranService pengeluaranService) {
        strExceptionArr[0] = "PengeluaranController";
        mapSorting();
        this.pengeluaranService = pengeluaranService;
    }

    private void mapSorting() {
        mapSorting.put("id", "idPengeluaran");
        mapSorting.put("nama", "namaPengeluaran");
        mapSorting.put("biaya", "biaya");
    }

    @GetMapping("/v1/pengeluaran/new")
    public String createPengeluaran(Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("pengeluaran", new PengeluaranDTO());
        return "pengeluaran/create_pengeluaran";
    }

    @GetMapping("/v1/pengeluaran/edit/{id}")
    public String editPengeluaran(Model model, WebRequest request, @PathVariable("id") Long id) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = pengeluaranService.findById(id, request);
        PengeluaranDTO pengeluaranDTO = (objectMapper.get("data") == null ? null : (PengeluaranDTO) objectMapper.get("data"));
        if ((Boolean) objectMapper.get("success")) {
            PengeluaranDTO pengeluaranDTOForSelect = (PengeluaranDTO) objectMapper.get("data");
            model.addAttribute("pengeluaran", pengeluaranDTO);
            return "pengeluaran/edit_pengeluaran";

        } else {
            model.addAttribute("pengeluaran", new PengeluaranDTO());
            return "redirect:/api/mgmnt/pengeluaran/default";
        }
    }

    @PostMapping("/v1/pengeluaran/new")
    public String newPengeluaran(@ModelAttribute(value = "pengeluaran")
                                 @Valid PengeluaranDTO pengeluaranDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
    ) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }

        /* START VALIDATION */
        if (bindingResult.hasErrors()) {
            model.addAttribute("pengeluaran", pengeluaranDTO);
            model.addAttribute("status", "error");

            return "pengeluaran/create_pengeluaran";
        }
        Boolean isValid = true;

        if (!isValid) {
            model.addAttribute("pengeluaran", pengeluaranDTO);
            return "pengeluaran/create_pengeluaran";
        }
        /* END OF VALIDATION */

        Pengeluaran pengeluaran = modelMapper.map(pengeluaranDTO, new TypeToken<Pengeluaran>() {
        }.getType());
        objectMapper = pengeluaranService.savePengeluaran(pengeluaran, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("message", "DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave") == null ? 1 : Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/mgmnt/v1/pengeluaran/fbpsb/0/asc/idPengeluaran?columnFirst=idPengeluaran&valueFirst=" + idDataSave + "&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("pengeluaran", new PengeluaranDTO());
            model.addAttribute("status", "error");
            return "pengeluaran/create_pengeluaran";
        }
    }

    @PostMapping("/v1/pengeluaran/edit/{id}")
    public String doRegis(@ModelAttribute("pengeluaran")
                          @Valid PengeluaranDTO pengeluaranDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    ) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        /* START VALIDATION */
        if (bindingResult.hasErrors()) {
            model.addAttribute("pengeluaran", pengeluaranDTO);
            return "pengeluaran/edit_pengeluaran";
        }
        Boolean isValid = true;

        if (!isValid) {
            model.addAttribute("pengeluaran", pengeluaranDTO);
            return "pengeluaran/edit_pengeluaran";
        }
        /* END OF VALIDATION */

        Pengeluaran pengeluaran = modelMapper.map(pengeluaranDTO, new TypeToken<Pengeluaran>() {
        }.getType());
        objectMapper = pengeluaranService.updatePengeluaran(id, pengeluaran, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("pengeluaran", new PengeluaranDTO());
            return "redirect:/api/mgmnt/v1/pengeluaran/fbpsb/0/asc/idPengeluaran?columnFirst=idPengeluaran&valueFirst=" + id + "&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("pengeluaran", new PengeluaranDTO());
            return "pengeluaran/edit_pengeluaran";
        }
    }


    @GetMapping("/v1/pengeluaran/default")
    public String getDefaultData(Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        Pageable pageable = PageRequest.of(0, 5, Sort.by("idPengeluaran"));
        objectMapper = pengeluaranService.findAllPengeluaran(pageable, request);
        mappingAttribute.setAttribute(model, objectMapper, request);

        model.addAttribute("pengeluaran", new PengeluaranDTO());
        model.addAttribute("sortBy", "idPengeluaran");
        model.addAttribute("currentPage", 1);
        model.addAttribute("asc", "asc");
        model.addAttribute("columnFirst", "");
        model.addAttribute("valueFirst", "");
        model.addAttribute("sizeComponent", 5);
        return "/pengeluaran/pengeluaran";
    }

    @GetMapping("/v1/pengeluaran/fbpsb/{page}/{sort}/{sortby}")
    public String findByPengeluaran(
            Model model,
            @PathVariable("page") Integer pagez,
            @PathVariable("sort") String sortz,
            @PathVariable("sortby") String sortzBy,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            @RequestParam String sizeComponent,
            WebRequest request
    ) {
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy == null ? "idPengeluaran" : sortzBy;
        Pageable pageable = PageRequest.of(pagez == 0 ? pagez : pagez - 1, Integer.parseInt(sizeComponent.equals("") ? "5" : sizeComponent), sortz.equals("asc") ? Sort.by(sortzBy) : Sort.by(sortzBy).descending());
        objectMapper = pengeluaranService.findByPage(pageable, request, columnFirst, valueFirst);
        mappingAttribute.setAttribute(model, objectMapper, request);
        model.addAttribute("pengeluaran", new PengeluaranDTO());
        model.addAttribute("currentPage", pagez == 0 ? 1 : pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting, sortzBy));
        model.addAttribute("columnFirst", columnFirst);
        model.addAttribute("valueFirst", valueFirst);
        model.addAttribute("sizeComponent", sizeComponent);

        return "/pengeluaran/pengeluaran";
    }

    @GetMapping("/v1/pengeluaran/delete/{id}")
    public String deletePengeluaran(Model model
            , WebRequest request
            , @PathVariable("id") byte id
    ) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = pengeluaranService.deletePengeluaran(id,request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("pengeluaran", new PengeluaranDTO());
            return "redirect:/api/mgmnt/v1/pengeluaran/fbpsb/0/asc/idPengeluaran?columnFirst=idPengeluaran&valueFirst=" + id + "&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        } else {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("pengeluaran", new PengeluaranDTO());
            return "pengeluaran/pengeluaran";
        }
    }
}