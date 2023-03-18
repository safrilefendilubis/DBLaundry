package com.juaracoding.DBLaundry.service;/*
Created By IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author Syarifudin a.k.a. Muhamad Syarifuidn
Java Developer
Created on 09/03/2023 13:00
@Last Modified 09/03/2023 13:00
Version 1.1
*/

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.PengeluaranDTO;
import com.juaracoding.DBLaundry.handler.ResourceNotFoundException;
import com.juaracoding.DBLaundry.handler.ResponseHandler;
import com.juaracoding.DBLaundry.model.Pengeluaran;
import com.juaracoding.DBLaundry.repo.PengeluaranRepo;
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
public class PengeluaranService {

    private PengeluaranRepo pengeluaranRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public PengeluaranService(PengeluaranRepo pengeluaranRepo) {
        strExceptionArr[0]="PengeluaranService";
        mapColumn();
        this.pengeluaranRepo = pengeluaranRepo;
    }

    public Map<String, Object> savePengeluaran(Pengeluaran pengeluaran, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV11001",request);
            }
            pengeluaran.setCreatedBy((byte) Integer.parseInt(strUserIdz.toString()));
            pengeluaran.setCreatedDate(new Date());
            pengeluaranRepo.save(pengeluaran);
        } catch (Exception e) {
            strExceptionArr[1] = "savePengeluaran(Pengeluaran pengeluaran, WebRequest request) --- LINE 72";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE11001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, pengeluaran.getIdPengeluaran(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updatePengeluaran(Long idPengeluaran,Pengeluaran pengeluaran, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Pengeluaran nextPengeluaran = null;
        try {
            nextPengeluaran = pengeluaranRepo.findById(idPengeluaran).orElseThrow(
                    ()->null
            );

            if(nextPengeluaran==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_PENGELUARAN_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV11002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV11003",request);
            }
            nextPengeluaran.setNamaPengeluaran(pengeluaran.getNamaPengeluaran());
            nextPengeluaran.setModifiedBy((byte) Integer.parseInt(strUserIdz.toString()));
            nextPengeluaran.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updatePengeluaran(Long idPengeluaran,Pengeluaran pengeluaran, WebRequest request) --- LINE 113";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE11002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }



    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveUploadFilePengeluaran(List<Pengeluaran> listPengeluaran,
                                                  MultipartFile multipartFile,
                                                  WebRequest request) throws Exception {
        List<Pengeluaran> listPengeluaranResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            listPengeluaranResult = pengeluaranRepo.saveAll(listPengeluaran);
            if (listPengeluaranResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFilePengeluaran(List<Pengeluaran> listPengeluaran,MultipartFile multipartFile,WebRequest request) --- LINE 140";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV11004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFilePengeluaran(List<Pengeluaran> listPengeluaran,MultipartFile multipartFile,WebRequest request) --- LINE 144";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE11002", request);
        }
        return new ResponseHandler().
                generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        null,
                        request);
    }

    public Map<String,Object> findAllPengeluaran(Pageable pageable, WebRequest request)
    {
        List<PengeluaranDTO> listPengeluaranDTO = null;
        Map<String,Object> mapResult = null;
        Page<Pengeluaran> pagePengeluaran = null;
        List<Pengeluaran> listPengeluaran = null;

        try
        {
            pagePengeluaran = pengeluaranRepo.findByIsDelete(pageable,(byte)1);
            listPengeluaran = pagePengeluaran.getContent();
            if(listPengeluaran.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV11005",
                                request);
            }
            listPengeluaranDTO = modelMapper.map(listPengeluaran, new TypeToken<List<PengeluaranDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listPengeluaranDTO,pagePengeluaran,mapColumnSearch);

        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllPengeluaran(Pageable pageable, WebRequest request) --- LINE 183";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE11003", request);
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
        Page<Pengeluaran> pagePengeluaran = null;
        List<Pengeluaran> listPengeluaran = null;
        List<PengeluaranDTO> listPengeluaranDTO = null;
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
                        strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 218";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                        return new ResponseHandler().
                                generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                        HttpStatus.OK,
                                        transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                        "FE11004",
                                        request);
                    }
                }

            }
            pagePengeluaran = getDataByValue(pageable,columFirst,valueFirst);
            listPengeluaran = pagePengeluaran.getContent();
            if(listPengeluaran.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV11006",
                                request);
            }
            listPengeluaranDTO = modelMapper.map(listPengeluaran, new TypeToken<List<PengeluaranDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listPengeluaranDTO,pagePengeluaran,mapColumnSearch);
            System.out.println("LIST DATA => "+listPengeluaranDTO.size());
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 248";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE11005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    public Map<String,Object> findById(Long id, WebRequest request)
    {
        Pengeluaran pengeluaran = pengeluaranRepo.findById(id).orElseThrow (
                ()-> null
        );
        if(pengeluaran == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_PENGELUARAN_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV11005",request);
        }
        PengeluaranDTO pengeluaranDTO = modelMapper.map(pengeluaran, new TypeToken<PengeluaranDTO>(){}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        pengeluaranDTO,
                        null,
                        request);
    }



    private void mapColumn()
    {
        mapColumnSearch.put("id","ID PENGELUARAN");
        mapColumnSearch.put("nama","NAMA PENGELUARAN");
        mapColumnSearch.put("biaya","BIAYA PENGELUARAN");

    }

    private Page<Pengeluaran> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals("") || paramValue==null)
        {
            return pengeluaranRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id"))
        {
            return pengeluaranRepo.findByIsDeleteAndIdPengeluaranContainsIgnoreCase(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return pengeluaranRepo.findByIsDeleteAndNamaPengeluaranContainsIgnoreCase(pageable,(byte) 1,paramValue);
        } else if (paramColumn.equals("biaya")) {
            return pengeluaranRepo.findByIsDeleteAndBiayaContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }

        return pengeluaranRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }

    public Map<String, Object> deletePengeluaran(byte idPengeluaran,WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Pengeluaran nextPengeluaran = null;
        try {
            nextPengeluaran = pengeluaranRepo.findById((long) idPengeluaran).orElseThrow(
                    ()->null
            );

            if(nextPengeluaran==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_PENGELUARAN_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV11002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV11003",request);
            }
            nextPengeluaran.setIsDelete((byte)0);
            nextPengeluaran.setModifiedBy((byte) Integer.parseInt(strUserIdz.toString()));
            nextPengeluaran.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updatePengeluaran(Long idPengeluaran,Pengeluaran pengeluaran, WebRequest request) --- LINE 340";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE11002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);    }
}