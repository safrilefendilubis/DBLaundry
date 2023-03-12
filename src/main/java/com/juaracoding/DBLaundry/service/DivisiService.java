package com.juaracoding.DBLaundry.service;

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.DivisiDTO;
import com.juaracoding.DBLaundry.handler.ResourceNotFoundException;
import com.juaracoding.DBLaundry.handler.ResponseHandler;
import com.juaracoding.DBLaundry.model.Divisi;
import com.juaracoding.DBLaundry.repo.DivisiRepo;
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
public class DivisiService {

    private DivisiRepo divisiRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public DivisiService(DivisiRepo divisiRepo) {
        strExceptionArr[0]="DivisiService";
        mapColumn();
        this.divisiRepo = divisiRepo;
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID DIVISI");
        mapColumnSearch.put("nama","NAMA DIVISI");
    }

    public Map<String, Object> saveDivisi(Divisi divisi, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV05001",request);
            }
            divisi.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            divisi.setCreatedDate(new Date());
            divisiRepo.save(divisi);
        } catch (Exception e) {
            strExceptionArr[1] = "saveDivisi(Divisi divisi, WebRequest request) --- LINE 65";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE05001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, divisi.getIdDivisi(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updateDivisi(Long idDivisi, Divisi divisi, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Divisi nextDivisi = divisiRepo.findById(idDivisi).orElseThrow(
                    ()->null
            );

            if(nextDivisi==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
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
            nextDivisi.setNamaDivisi(divisi.getNamaDivisi());
            nextDivisi.setKodeDivisi(divisi.getKodeDivisi());
            nextDivisi.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextDivisi.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updateDivisi(Long idDivisi, Divisi divisi, WebRequest request) --- LINE 107";
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
    public Map<String, Object> saveUploadFileDivisi(List<Divisi> listDivisi,
                                                    MultipartFile multipartFile,
                                                    WebRequest request) throws Exception {
        List<Divisi> listDivisiResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            listDivisiResult = divisiRepo.saveAll(listDivisi);
            if (listDivisiResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFileDivisi(List<Divisi> listDivisi, MultipartFile multipartFile, WebRequest request) --- LINE 133";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV05004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFileDivisi(List<Divisi> listDivisi, MultipartFile multipartFile, WebRequest request) --- LINE 138";
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

    public Map<String,Object> findAllDivisi(Pageable pageable, WebRequest request)
    {
        List<DivisiDTO> listDivisiDTO = null;
        Map<String,Object> mapResult = null;
        Page<Divisi> pageDivisi = null;
        List<Divisi> listDivisi = null;

        try
        {
            pageDivisi = divisiRepo.findByIsDelete(pageable,(byte)1);
            listDivisi = pageDivisi.getContent();
            if(listDivisi.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV05005",
                                request);
            }
            listDivisiDTO = modelMapper.map(listDivisi, new TypeToken<List<DivisiDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listDivisiDTO,pageDivisi,mapColumnSearch);
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllDivisi(Pageable pageable, WebRequest request) --- LINE 177";
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
        Page<Divisi> pageDivisi = null;
        List<Divisi> listDivisi = null;
        List<DivisiDTO> listDivisiDTO = null;
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
            pageDivisi = getDataByValue(pageable,columFirst,valueFirst);
            listDivisi = pageDivisi.getContent();
            if(listDivisi.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV05006",
                                request);
            }
            listDivisiDTO = modelMapper.map(listDivisi, new TypeToken<List<DivisiDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listDivisiDTO,pageDivisi,mapColumnSearch);
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

    public Map<String,Object> findById(Long idDivisi, WebRequest request)
    {
        Divisi divisi = divisiRepo.findById(idDivisi).orElseThrow (
                ()-> null
        );
        if(divisi == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV05007",request);
        }
        DivisiDTO divisiDTO = modelMapper.map(divisi, new TypeToken<DivisiDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        divisiDTO,
                        null,
                        request);
    }


    public Map<String,Object> findAllDivisi()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<DivisiDTO> listDivisiDTO = null;
        Map<String,Object> mapResult = null;
        List<Divisi> listDivisi = null;

        try
        {
            listDivisi = divisiRepo.findByIsDelete((byte)1);
            if(listDivisi.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                null,
                                null,
                                null);
            }
            listDivisiDTO = modelMapper.map(listDivisi, new TypeToken<List<DivisiDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllDivisi() --- LINE 304";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE05006", null);
        }



        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        listDivisiDTO,
                        null,
                        null);
    }

    public Map<String, Object> deleteDivisi(Long idDemo, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Divisi nextDivisi = null;
        try {
            nextDivisi = divisiRepo.findById(idDemo).orElseThrow(
                    ()->null
            );

            if(nextDivisi==null)
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
            nextDivisi.setIsDelete((byte)0);
            nextDivisi.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextDivisi.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " deleteDivisi(Long idDemo, WebRequest request) --- LINE 344";
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
    public List<DivisiDTO> getAllDivisi()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<DivisiDTO> listDivisiDTO = null;
        Map<String,Object> mapResult = null;
        List<Divisi> listDivisi = null;

        try
        {
            listDivisi = divisiRepo.findByIsDelete((byte)1);
            if(listDivisi.size()==0)
            {
                return new ArrayList<DivisiDTO>();
            }
            listDivisiDTO = modelMapper.map(listDivisi, new TypeToken<List<DivisiDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllDivisi() --- LINE 331";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return listDivisiDTO;
        }
        return listDivisiDTO;
    }

    private Page<Divisi> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals("") || paramValue==null)
        {
            return divisiRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id"))
        {
            return divisiRepo.findByIsDeleteAndIdDivisi(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return divisiRepo.findByIsDeleteAndNamaDivisiContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }

        return divisiRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }
}
