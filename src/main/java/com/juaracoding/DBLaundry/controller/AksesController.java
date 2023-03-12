package com.juaracoding.DBLaundry.controller;

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.AksesDTO;
import com.juaracoding.DBLaundry.dto.DivisiDTO;
import com.juaracoding.DBLaundry.dto.MenuDTO;
import com.juaracoding.DBLaundry.model.Akses;
import com.juaracoding.DBLaundry.model.Demo;
import com.juaracoding.DBLaundry.service.AksesService;
import com.juaracoding.DBLaundry.service.DivisiService;
import com.juaracoding.DBLaundry.service.MenuService;
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
import java.util.*;

@Controller
@RequestMapping("/api/usrmgmnt")
public class AksesController {

    private AksesService aksesService;

    private MenuService menuService;
    private DivisiService divisiService;

    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();

    private List<Demo> lsCPUpload = new ArrayList<Demo>();

    private String [] strExceptionArr = new String[2];

    private MappingAttribute mappingAttribute = new MappingAttribute();

    public AksesController(AksesService aksesService, MenuService menuService, DivisiService divisiService) {
        strExceptionArr[0] = "DemoController";
        mapSorting();
        this.aksesService = aksesService;
        this.menuService = menuService;
        this.divisiService = divisiService;
    }

    @GetMapping("/v1/akses/new")
    public String createAkses(Model model, WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("akses", new AksesDTO());
        model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
        model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya

        return "akses/create_akses";
    }

    @GetMapping("/v1/akses/edit/{id}")
    public String editAkses(Model model,WebRequest request,@PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        objectMapper = aksesService.findById(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut

        if((Boolean) objectMapper.get("success"))
        {
            AksesDTO aksesDTOForSelect = (AksesDTO) objectMapper.get("data");
            List<DivisiDTO> listDivisi = divisiService.getAllDivisi();
            List<MenuDTO> listMenu = menuService.getAllMenu();

//            List<MenuDTO> selectedMenuDTO = new ArrayList<MenuDTO>();
//            for (MenuDTO menuDTO:
//                 listMenuDTO) {
//                for (MenuDTO menuz:
//                        aksesDTOForSelect.getListMenuAkses()) {
//                    if(menuDTO.getIdMenu()==menuz.getIdMenu())
//                    {
//                        selectedMenuDTO.add(menuz);
//                    }
//                }
//            }
//            Set<Long> menuSelected = selectedMenuDTO.stream().map(MenuDTO::getIdMenu).collect(Collectors.toSet());
            model.addAttribute("akses", aksesDTOForSelect);
            model.addAttribute("listDivisi", listDivisi);//untuk parent nya
            model.addAttribute("listMenu", listMenu);//untuk parent nya
            if(aksesDTOForSelect.getDivisi() != null)
            {
                model.addAttribute("selectedValues", aksesDTOForSelect.getDivisi().getIdDivisi());//yang akan diselect saat modals muncul
            }
            return "akses/edit_akses";
        }
        else
        {
//            model.addAttribute("akses", new AksesDTO());
            model.addAttribute("akses", new Akses());
            return "redirect:/api/usrmgmnt/v1/akses/default";
        }
    }
    @PostMapping("/v1/akses/new")
    public String newAkses(@ModelAttribute(value = "akses")
                          @Valid Akses aksesDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
    )
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("akses",aksesDTO);
            model.addAttribute("status","error");
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya

            return "akses/create_akses";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("akses",aksesDTO);
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya
            return "akses/create_akses";
        }
        /* END OF VALIDATION */

        Akses akses = modelMapper.map(aksesDTO, new TypeToken<Akses>() {}.getType());
        objectMapper = aksesService.saveAkses(akses,request);

        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session ke attribut
        if((Boolean) objectMapper.get("success"))
        {
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/usrmgmnt/v1/akses/fbpsb/0/asc/id?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya
            model.addAttribute("akses",new AksesDTO());
            model.addAttribute("status","error");
            return "akses/create_akses";
        }
    }

    @PostMapping("/v1/akses/edit/{id}")
    public String editAkses(@ModelAttribute("akses")
                          @Valid Akses akses
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("akses",akses);
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya
            model.addAttribute("menuSelected", new ArrayList<MenuDTO>());//untuk parent nya
            return "akses/edit_akses";
        }

        /* END OF VALIDATION */

        objectMapper = aksesService.updateAkses(id,akses,request);

        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut
            model.addAttribute("akses",new AksesDTO());
            return "redirect:/api/usrmgmnt/v1/akses/fbpsb/0/asc/id?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("akses",new AksesDTO());
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya
            return "akses/edit_akses";
        }
    }


    @GetMapping("/v1/akses/default")
    public String getDefaultData(Model model,WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        Pageable pageable = PageRequest.of(0,5, Sort.by("idAkses"));
        objectMapper = aksesService.findAllAkses(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut;

        model.addAttribute("akses",new AksesDTO());
        model.addAttribute("sortBy","id");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/akses/akses";
    }

    @GetMapping("/v1/akses/fbpsb/{page}/{sort}/{sortby}")
    public String findByAkses(
            Model model,
            @PathVariable("page") Integer pagez,
            @PathVariable("sort") String sortz,
            @PathVariable("sortby") String sortzBy,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            @RequestParam String sizeComponent,
            WebRequest request
    ){
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"id":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = aksesService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut
        model.addAttribute("akses",new AksesDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/akses/akses";
    }

    @GetMapping("/v1/akses/vml/{id}")
    public String viewAksesMenu(Model model ,
                                WebRequest request,
                                @PathVariable("id")Long idAkses)//untuk menampilkan menu yang sudah dimapping ke akses
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        objectMapper = aksesService.findById(idAkses,request);
        AksesDTO aksesDTO = (AksesDTO) objectMapper.get("data");
        String[] arrColumnTitle = new String[3];
        arrColumnTitle[0] = "ID MENU";
        arrColumnTitle[1] = "NAMA MENU";
        arrColumnTitle[2] = "GROUP MENU";

        List<MenuDTO> listMenu = aksesDTO.getListMenuAkses();
        int listSize = listMenu.size();
        String[][] arrContent = new String[listSize][arrColumnTitle.length];

        for (int i=0;i<listMenu.size();i++)
        {
            arrContent[i][0] = listMenu.get(i).getIdMenu().toString();
            arrContent[i][1] = listMenu.get(i).getNamaMenu().toString();
            arrContent[i][2] = listMenu.get(i).getMenuHeader().getNamaMenuHeader();
        }
        model.addAttribute("arrColumnTitle",arrColumnTitle);
        model.addAttribute("arrContent",arrContent);
        model.addAttribute("title","LIST AKSES MENU "+aksesDTO.getNamaAkses());

        return "globalview";
    }

    @GetMapping("/v1/akses/delete/{id}")
    public String deleteAkses(Model model,WebRequest request,@PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        objectMapper = aksesService.deleteAkses(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut

        model.addAttribute("akses",new AksesDTO());
        return "redirect:/api/usrmgmnt/v1/akses/default";
    }

    private void mapSorting()
    {
        mapSorting.put("id","idAkses");
        mapSorting.put("nama","namaAkses");
    }
}