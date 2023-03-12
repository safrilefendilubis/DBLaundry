package com.juaracoding.DBLaundry.service;

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.AksesDTO;
import com.juaracoding.DBLaundry.handler.ResourceNotFoundException;
import com.juaracoding.DBLaundry.handler.ResponseHandler;
import com.juaracoding.DBLaundry.model.Akses;
import com.juaracoding.DBLaundry.repo.AksesRepo;
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
public class AksesService {

    private AksesRepo aksesRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public AksesService(AksesRepo aksesRepo) {
        mapColumn();
        strExceptionArr[0] = "AksesService";
        this.aksesRepo = aksesRepo;
    }

    public Map<String, Object> saveAkses(Akses akses, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV04001",request);
            }
            akses.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            akses.setCreatedDate(new Date());
            aksesRepo.save(akses);
        } catch (Exception e) {
            strExceptionArr[1] = "saveAkses(Akses akses, WebRequest request) --- LINE 67";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE04001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, akses.getIdAkses(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updateAkses(Long idAkses, Akses akses, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_UPDATE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Akses nextAkses = aksesRepo.findById(idAkses).orElseThrow(
                    ()->null
            );

            if(nextAkses==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_AKSES_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV04002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV04003",request);
            }
            nextAkses.setNamaAkses(akses.getNamaAkses());
            nextAkses.setDivisi(akses.getDivisi());
            nextAkses.setListMenuAkses(akses.getListMenuAkses());
            nextAkses.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextAkses.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "updateAkses(Long idAkses, Akses akses, WebRequest request) --- LINE 92";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE04002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveUploadFileAkses(List<Akses> listAkses,
                                                   MultipartFile multipartFile,
                                                   WebRequest request) throws Exception {
        List<Akses> listAksesResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            listAksesResult = aksesRepo.saveAll(listAkses);
            if (listAksesResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFileAkses(List<Akses> listAkses, MultipartFile multipartFile, WebRequest request)  --- LINE 136";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV04004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFileAkses(List<Akses> listAkses, MultipartFile multipartFile, WebRequest request) --- LINE 140";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE04002", request);
        }
        return new ResponseHandler().
                generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        null,
                        request);
    }

    public Map<String,Object> findAllAkses(Pageable pageable, WebRequest request)
    {
        List<AksesDTO> listAksesDTO = null;
        Map<String,Object> mapResult = null;
        Page<Akses> pageAkses = null;
        List<Akses> listAkses = null;

        try
        {
            pageAkses = aksesRepo.findByIsDelete(pageable,(byte)1);
            listAkses = pageAkses.getContent();
            if(listAkses.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV04005",
                                request);
            }
            listAksesDTO = modelMapper.map(listAkses, new TypeToken<List<AksesDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listAksesDTO,pageAkses,mapColumnSearch);

        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllAkses(Pageable pageable, WebRequest request) --- LINE 182";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE04003", request);
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
        Page<Akses> pageAkses = null;
        List<Akses> listAkses = null;
        List<AksesDTO> listAksesDTO = null;
        Map<String,Object> mapResult = null;

        try
        {
            if(columFirst.equals("id"))
            {
                try
                {
                    Long.parseLong(valueFirst);
                }
                catch (Exception e)
                {
                    strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 209";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().
                            generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                    HttpStatus.OK,
                                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                    "FE04004",
                                    request);
                }
            }
            pageAkses = getDataByValue(pageable,columFirst,valueFirst);
            listAkses = pageAkses.getContent();
            if(listAkses.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV04006",
                                request);
            }
            listAksesDTO = modelMapper.map(listAkses, new TypeToken<List<AksesDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listAksesDTO,pageAkses,mapColumnSearch);
            System.out.println("LIST DATA => "+listAksesDTO.size());
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 243";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE04005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    public Map<String,Object> findById(Long idAkses, WebRequest request)
    {
        Akses akses = aksesRepo.findById(idAkses).orElseThrow (
                ()-> null
        );
        if(akses == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_AKSES_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV04005",request);
        }
        AksesDTO aksesDTO = modelMapper.map(akses, new TypeToken<AksesDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        aksesDTO,
                        null,
                        request);
    }

    public Map<String, Object> deleteAkses(Long idAkses, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Akses nextAkses = aksesRepo.findById(idAkses).orElseThrow(
                    ()->null
            );

            if(nextAkses==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_AKSES_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV04006",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV04007",request);
            }
            nextAkses.setIsDelete((byte)0);
            nextAkses.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextAkses.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "deleteAlses(Long idAkses, WebRequest request) --- LINE 303";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE04006", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID AKSES");
        mapColumnSearch.put("nama","NAMA AKSES");
    }

    private Page<Akses> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals(""))
        {
            return aksesRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id"))
        {
            return aksesRepo.findByIsDeleteAndIdAkses(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return aksesRepo.findByIsDeleteAndNamaAksesContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }

        return aksesRepo.findByIsDelete(pageable,(byte) 1);
    }

}