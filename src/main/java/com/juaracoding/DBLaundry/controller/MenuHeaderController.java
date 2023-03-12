package com.juaracoding.DBLaundry.controller;


import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.MenuHeaderDTO;
import com.juaracoding.DBLaundry.model.MenuHeader;
import com.juaracoding.DBLaundry.service.MenuHeaderService;
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
public class MenuHeaderController {

    private MenuHeaderService menuHeaderService;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();

    private List<MenuHeader> lsCPUpload = new ArrayList<MenuHeader>();

    private String [] strExceptionArr = new String[2];

    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public MenuHeaderController(MenuHeaderService menuHeaderService) {
        strExceptionArr[0] = "MenuHeaderController";
        mapSorting();
        this.menuHeaderService = menuHeaderService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","idMenuHeader");
        mapSorting.put("nama","namaMenuHeader");
        mapSorting.put("deskripsi","deskripsiMenuHeader");
    }

    @GetMapping("/v1/menuheader/new")
    public String createMenuHeader(Model model, WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("menuHeader", new MenuHeaderDTO());
        return "menuheader/create_menuheader";
    }

    @GetMapping("/v1/menuheader/edit/{id}")
    public String editMenuHeader(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = menuHeaderService.findById(id,request);
        MenuHeaderDTO menuHeaderDTO = (objectMapper.get("data")==null?null:(MenuHeaderDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success"))
        {
            MenuHeaderDTO menuHeaderDTOForSelect = (MenuHeaderDTO) objectMapper.get("data");
            model.addAttribute("menuHeader", menuHeaderDTO);
            return "menuheader/edit_menuheader";
        }
        else
        {
            model.addAttribute("menuHeader", new MenuHeaderDTO());
            return "redirect:/api/usrmgmnt/v1/menuheader/default";
        }
    }

    @PostMapping("/v1/menuheader/new")
    public String newMenuHeader(@ModelAttribute(value = "menuHeader")
                            @Valid MenuHeaderDTO menuHeaderDTO
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
            model.addAttribute("menuHeader",menuHeaderDTO);
            model.addAttribute("status","error");

            return "menuheader/create_menuheader";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("menuHeader",menuHeaderDTO);
            return "menuheader/create_menuheader";
        }
        /* END OF VALIDATION */

        MenuHeader menuHeader = modelMapper.map(menuHeaderDTO, new TypeToken<MenuHeader>() {}.getType());
        objectMapper = menuHeaderService.saveMenuHeader(menuHeader,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/usrmgmnt/v1/menuheader/fbpsb/0/asc/id?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("menuHeader",new MenuHeaderDTO());
            model.addAttribute("status","error");
            return "menuheader/create_menuheader";
        }
    }

    @PostMapping("/v1/menuheader/edit/{id}")
    public String editMenuHeader(@ModelAttribute("menuHeader")
                           @Valid MenuHeaderDTO menuHeaderDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("menuHeader",menuHeaderDTO);
            return "menuheader/edit_menuheader";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("menuHeader",menuHeaderDTO);
            return "menuheader/edit_menuheader";
        }
        /* END OF VALIDATION */

        MenuHeader menuHeader = modelMapper.map(menuHeaderDTO, new TypeToken<MenuHeader>() {}.getType());
        objectMapper = menuHeaderService.updateMenuHeader(id,menuHeader,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("menuHeader",new MenuHeaderDTO());
            return "redirect:/api/usrmgmnt/v1/menuheader/fbpsb/0/asc/id?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("menuHeader",new MenuHeaderDTO());
            return "menuheader/edit_menuheader";
        }
    }


    @GetMapping("/v1/menuheader/default")
    public String getDefaultData(Model model,WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        Pageable pageable = PageRequest.of(0,5, Sort.by("idMenuHeader"));
        objectMapper = menuHeaderService.findAllMenuHeader(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper,request);

        model.addAttribute("menuHeader",new MenuHeaderDTO());
        model.addAttribute("sortBy","idMenuHeader");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/menuheader/menuheader";
    }

    @GetMapping("/v1/menuheader/fbpsb/{page}/{sort}/{sortby}")
    public String findByMenuHeader(
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
        sortzBy = sortzBy==null?"idMenuHeader":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = menuHeaderService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("menuHeader",new MenuHeaderDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/menuheader/menuheader";
    }

    @GetMapping("/v1/menuheader/delete/{id}")
    public String deleteMenuHeader(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = menuHeaderService.deleteMenuHeader(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set session
        model.addAttribute("menuHeader", new MenuHeaderDTO());
        return "redirect:/api/usrmgmnt/v1/menuheader/default";
    }

}