package com.juaracoding.DBLaundry.service;

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.MenuDTO;
import com.juaracoding.DBLaundry.dto.MenuHeaderDTO;
import com.juaracoding.DBLaundry.handler.ResponseHandler;
import com.juaracoding.DBLaundry.model.MenuHeader;
import com.juaracoding.DBLaundry.repo.MenuHeaderRepo;
import com.juaracoding.DBLaundry.utils.ConstantMessage;
import com.juaracoding.DBLaundry.utils.LoggingFile;
import com.juaracoding.DBLaundry.utils.TransformToDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
/*
    KODE MODUL 02
 */

@Service
@Transactional
public class MenuHeaderService {

    private MenuHeaderRepo menuHeaderRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private List<MenuHeader> lsCPUpload = new ArrayList<MenuHeader>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private String [] strColumnSearch = new String[2];


    @Autowired
    public MenuHeaderService(MenuHeaderRepo menuHeaderRepo) {
        strExceptionArr[0] = "MenuHeaderService";
        mapColumn();
        this.menuHeaderRepo = menuHeaderRepo;
    }



    public Map<String, Object> saveMenuHeader(MenuHeader menuHeader, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_IDZ",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV02001",request);
            }
            menuHeader.setDeskripsiMenuHeader(menuHeader.getDeskripsiMenuHeader());
            menuHeader.setNamaMenuHeader(menuHeader.getNamaMenuHeader());
            menuHeader.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            menuHeader.setCreatedDate(new Date());
            menuHeaderRepo.save(menuHeader);
        } catch (Exception e) {
            strExceptionArr[1] = "saveMenuHeader(MenuHeader menuHeader, WebRequest request) --- LINE 58";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE02001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED, null, null, request);
    }

    public Map<String, Object> updateMenuHeader(Long idMenuHeader, MenuHeader menuHeader, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_UPDATE;
        Object strUserIdz = request.getAttribute("USR_IDZ",1);

        try {
            MenuHeader nextMenuHeader = menuHeaderRepo.findById(idMenuHeader).orElseThrow(
                    ()->null
            );

            if(nextMenuHeader==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_MENU_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV02002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV02003",request);
            }
            nextMenuHeader.setNamaMenuHeader(menuHeader.getNamaMenuHeader());
            nextMenuHeader.setDeskripsiMenuHeader(menuHeader.getDeskripsiMenuHeader());
            nextMenuHeader.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextMenuHeader.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "updateMenu(Long idMenuHeader,MenuHeader menuHeader, WebRequest request)  --- LINE 91";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_UPDATE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE02002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED, null, null, request);
    }

    public Map<String,Object> findPageAllMenuHeader(Pageable pageable, WebRequest request)
    {
        List<MenuDTO> listMenuDTO = null;
        Map<String,Object> mapResult = null;
        Page<MenuHeader> pageMenu = null;
        List<MenuHeader> listMenuHeader = null;

        try
        {
            pageMenu = menuHeaderRepo.findByIsDelete(pageable,(byte)1);
            listMenuHeader = pageMenu.getContent();
            if(listMenuHeader.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                null,
                                null,
                                request);
            }
            listMenuDTO = modelMapper.map(listMenuHeader, new TypeToken<List<MenuDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listMenuDTO,pageMenu,mapColumnSearch);
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllMenu(Pageable pageable, WebRequest request) --- LINE 121";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE03003", request);
        }



        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

    public Map<String,Object> findAllMenuHeader(Pageable pageable, WebRequest request)
    {
        List<MenuHeaderDTO> listMenuHeaderDTO = null;
        Map<String,Object> mapResult = null;
        Page<MenuHeader> pageMenuHeader = null;
        List<MenuHeader> listMenuHeader = null;

        try
        {
            pageMenuHeader = menuHeaderRepo.findByIsDelete(pageable,(byte)1);
            listMenuHeader = pageMenuHeader.getContent();
            if(listMenuHeader.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV05005",
                                request);
            }
            listMenuHeaderDTO = modelMapper.map(listMenuHeader, new TypeToken<List<MenuHeaderDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listMenuHeaderDTO,pageMenuHeader,mapColumnSearch);
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllMenuHeader(Pageable pageable, WebRequest request) --- LINE 177";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE05003", request);
        }



        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

    public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst)
    {
        Page<MenuHeader> pageMenuHeader = null;
        List<MenuHeader> listMenuHeader = null;
        List<MenuHeaderDTO> listMenuHeaderDTO = null;
        Map<String,Object> mapResult = null;

        try
        {
            if(columFirst.equals("id"))
            {
                if(!valueFirst.equals("") && valueFirst!=null)
                {
                    try
                    {
                        Long.parseLong(valueFirst);
                    }
                    catch (Exception e)
                    {
                        strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 212";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                        return new ResponseHandler().
                                generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                        HttpStatus.OK,
                                        transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                        "FE05004",
                                        request);
                    }
                }
            }
            pageMenuHeader = getDataByValue(pageable,columFirst,valueFirst);
            listMenuHeader = pageMenuHeader.getContent();
            if(listMenuHeader.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV05006",
                                request);
            }
            listMenuHeaderDTO = modelMapper.map(listMenuHeader, new TypeToken<List<MenuHeaderDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listMenuHeaderDTO,pageMenuHeader,mapColumnSearch);
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 243";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE05005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }
//    public Map<String,Object> findAllMenuHeader()//KHUSUS UNTUK FORM INPUT SAJA di Menu Child nya
//    {
//        List<MenuHeaderDTO> listMenuHeaderDTO = null;
//        Map<String,Object> mapResult = null;
//        Page<MenuHeader> pageMenu = null;
//        List<MenuHeader> listMenu = null;
//
//        try
//        {
//            listMenu = menuHeaderRepo.findByIsDelete((byte)1);
//            if(listMenu.size()==0)
//            {
//                return new ResponseHandler().
//                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
//                                HttpStatus.OK,
//                                null,
//                                null,
//                                null);
//            }
//            listMenuHeaderDTO = modelMapper.map(listMenu, new TypeToken<List<MenuHeaderDTO>>() {}.getType());
//        }
//        catch (Exception e)
//        {
//            strExceptionArr[1] = "findAllMenu(Pageable pageable, WebRequest request) --- LINE 121";
//            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
//            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
//                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE03004", null);
//        }
//
//
//
//
//
//        return new ResponseHandler().
//                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
//                        HttpStatus.OK,
//                        listMenuHeaderDTO,
//                        null,
//                        null);
//    }

    public Map<String,Object> findById(Long id,WebRequest request)
    {
        MenuHeader menuHeader = menuHeaderRepo.findById(id).orElseThrow (
                ()-> null
        );
        if(menuHeader == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_MENU_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV03004",request);
        }
        MenuDTO menuDTO = modelMapper.map(menuHeader, new TypeToken<MenuDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        menuDTO,
                        null,
                        request);
    }

    public List<MenuHeaderDTO> getAllMenuHeader()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<MenuHeaderDTO> listMenuHeaderDTO = null;
        List<MenuHeader> listMenuHeader = null;
        try
        {
            listMenuHeader = menuHeaderRepo.findByIsDelete((byte)1);
            if(listMenuHeader.size()==0)
            {
                return new ArrayList<MenuHeaderDTO>();
            }
            listMenuHeaderDTO = modelMapper.map(listMenuHeader, new TypeToken<List<MenuHeaderDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "getAllDemo() --- LINE 223";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return listMenuHeaderDTO;
        }
        return listMenuHeaderDTO;
    }

    public Map<String, Object> deleteMenuHeader(Long idMenuHeader, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        MenuHeader nextMenuHeader = null;
        try {
            nextMenuHeader = menuHeaderRepo.findById(idMenuHeader).orElseThrow(
                    ()->null
            );

            if(nextMenuHeader==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV05006",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV05007",request);
            }
            nextMenuHeader.setIsDelete((byte)0);
            nextMenuHeader.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextMenuHeader.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " deleteMenuHeader(Long idDemo, WebRequest request) --- LINE 344";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE05007", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    public Map<String,String> mapColumn()
    {
        /*
        key = id di web, value = tampilan ke user
         */
        mapColumnSearch.put("id","ID");
        mapColumnSearch.put("nama","NAMA");
        mapColumnSearch.put("deskripsi","DESKRIPSI");

        return mapColumnSearch;
    }

    public Page<MenuHeader> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals(""))
        {
            return menuHeaderRepo.findByIsDelete(pageable,(byte) 1);
        }

        if(paramColumn.equals("id"))
        {
            menuHeaderRepo.findByIsDeleteAndIdMenuHeader(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            menuHeaderRepo.findByIsDeleteAndNamaMenuHeader(pageable,(byte) 1,paramValue);
        } else if (paramColumn.equals("deskripsi")) {
            menuHeaderRepo.findByIsDeleteAndDeskripsiMenuHeader(pageable,(byte) 1,paramValue);
        }

        return menuHeaderRepo.findByIsDelete(pageable,(byte) 1);
    }

}