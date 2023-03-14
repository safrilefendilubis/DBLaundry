package com.juaracoding.DBLaundry.service;/*
IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author User a.k.a. Safril Efendi Lubis
Java Developer
Created on 14/03/2023 13:00
@Last Modified 14/03/2023 13:00
Version 1.1
*/

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.TipeDTO;
import com.juaracoding.DBLaundry.handler.ResourceNotFoundException;
import com.juaracoding.DBLaundry.handler.ResponseHandler;
import com.juaracoding.DBLaundry.model.Tipe;
import com.juaracoding.DBLaundry.repo.TipeRepo;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
public class TipeService {

    private TipeRepo tipeRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    public TipeService(TipeRepo tipeRepo) {
        strExceptionArr[0]="TipeService";
        mapColumn();
        this.tipeRepo = tipeRepo;
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID TIPE");
        mapColumnSearch.put("nama","NAMA TIPE");
    }

    public Map<String, Object> saveTipe(Tipe tipe, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV05001",request);
            }
            tipe.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            tipe.setCreatedDate(new Date());
            tipeRepo.save(tipe);
        } catch (Exception e) {
            strExceptionArr[1] = "saveTipe(Tipe tipe, WebRequest request) --- LINE 65";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE05001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, tipe.getIdTipe(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updateTipe(Long idTipe, Tipe tipe, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Tipe nextTipe = tipeRepo.findById(idTipe).orElseThrow(
                    ()->null
            );

            if(nextTipe==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_TIPE_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV05002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV05003",request);
            }
            nextTipe.setNamaTipe(tipe.getNamaTipe());
            nextTipe.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextTipe.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updateTipe(Long idTipe, Tipe tipe, WebRequest request) --- LINE 107";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE05002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }



    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveUploadFileTipe(List<Tipe> listTipe,
                                                    MultipartFile multipartFile,
                                                    WebRequest request) throws Exception {
        List<Tipe> listTipeResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            listTipeResult = tipeRepo.saveAll(listTipe);
            if (listTipeResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFileTipe(List<Tipe> listTipe,MultipartFile multipartFile,WebRequest request) --- LINE 133";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV05004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFileTipe(List<Tipe> listTipe,MultipartFile multipartFile,WebRequest request) --- LINE 138";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE05002", request);
        }
        return new ResponseHandler().
                generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        null,
                        request);
    }

    public Map<String,Object> findAllTipe(Pageable pageable, WebRequest request)
    {
        List<TipeDTO> listTipeDTO = null;
        Map<String,Object> mapResult = null;
        Page<Tipe> pageTipe = null;
        List<Tipe> listTipe = null;

        try
        {
            pageTipe = tipeRepo.findByIsDelete(pageable,(byte)1);
            listTipe = pageTipe.getContent();
            if(listTipe.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV05005",
                                request);
            }
            listTipeDTO = modelMapper.map(listTipe, new TypeToken<List<TipeDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listTipeDTO,pageTipe,mapColumnSearch);
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllTipe(Pageable pageable, WebRequest request) --- LINE 177";
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
        Page<Tipe> pageTipe = null;
        List<Tipe> listTipe = null;
        List<TipeDTO> listTipeDTO = null;
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
            pageTipe = getDataByValue(pageable,columFirst,valueFirst);
            listTipe = pageTipe.getContent();
            if(listTipe.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV05006",
                                request);
            }
            listTipeDTO = modelMapper.map(listTipe, new TypeToken<List<TipeDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listTipeDTO,pageTipe,mapColumnSearch);
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

    public Map<String,Object> findById(Long idTipe, WebRequest request)
    {
        Tipe tipe = tipeRepo.findById(idTipe).orElseThrow (
                ()-> null
        );
        if(tipe == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_TIPE_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV05007",request);
        }
        TipeDTO tipeDTO = modelMapper.map(tipe, new TypeToken<TipeDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        tipeDTO,
                        null,
                        request);
    }


    public Map<String,Object> findAllTipe()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<TipeDTO> listTipeDTO = null;
        Map<String,Object> mapResult = null;
        List<Tipe> listTipe = null;

        try
        {
            listTipe = tipeRepo.findByIsDelete((byte)1);
            if(listTipe.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                null,
                                null,
                                null);
            }
            listTipeDTO = modelMapper.map(listTipe, new TypeToken<List<TipeDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllTipe() --- LINE 304";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE05006", null);
        }



        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        listTipeDTO,
                        null,
                        null);
    }

    public Map<String, Object> deleteTipe(Long idDemo, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Tipe nextTipe = null;
        try {
            nextTipe = tipeRepo.findById(idDemo).orElseThrow(
                    ()->null
            );

            if(nextTipe==null)
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
            nextTipe.setIsDelete((byte)0);
            nextTipe.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextTipe.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " deleteTipe(Long idDemo, WebRequest request) --- LINE 344";
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
    public List<TipeDTO> getAllTipe()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<TipeDTO> listTipeDTO = null;
        Map<String,Object> mapResult = null;
        List<Tipe> listTipe = null;

        try
        {
            listTipe = tipeRepo.findByIsDelete((byte)1);
            if(listTipe.size()==0)
            {
                return new ArrayList<TipeDTO>();
            }
            listTipeDTO = modelMapper.map(listTipe, new TypeToken<List<TipeDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllTipe() --- LINE 331";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return listTipeDTO;
        }
        return listTipeDTO;
    }

    private Page<Tipe> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals("") || paramValue==null)
        {
            return tipeRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id"))
        {
            return tipeRepo.findByIsDeleteAndIdTipe(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return tipeRepo.findByIsDeleteAndNamaTipeContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }

        return tipeRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }
}
