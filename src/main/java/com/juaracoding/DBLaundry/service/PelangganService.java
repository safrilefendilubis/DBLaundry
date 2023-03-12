package com.juaracoding.DBLaundry.service;


import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.PelangganDTO;
import com.juaracoding.DBLaundry.handler.ResourceNotFoundException;
import com.juaracoding.DBLaundry.handler.ResponseHandler;
import com.juaracoding.DBLaundry.model.Pelanggan;
import com.juaracoding.DBLaundry.repo.PelangganRepo;
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
public class PelangganService {

    private PelangganRepo pelangganRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    public PelangganService(PelangganRepo pelangganRepo) {
        strExceptionArr[0]="PelangganService";
        mapColumn();
        this.pelangganRepo = pelangganRepo;
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID PELANGGAN");
        mapColumnSearch.put("nama","NAMA PELANGGAN");
    }

    public Map<String, Object> savePelanggan(Pelanggan pelanggan, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV05001",request);
            }
            pelanggan.setNamaLengkap(pelanggan.getNamaLengkap());
            pelanggan.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            pelanggan.setCreatedDate(new Date());
            pelangganRepo.save(pelanggan);
        } catch (Exception e) {
            strExceptionArr[1] = "savePelanggan(Pelanggan pelanggan, WebRequest request)--- LINE 65";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE05001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, pelanggan.getIdPelanggan(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updatePelanggan(Long idPelanggan, Pelanggan pelanggan, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Pelanggan nextPelanggan = pelangganRepo.findById(idPelanggan).orElseThrow(
                    ()->null
            );

            if(nextPelanggan==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_PELANGGAN_NOT_EXISTS,
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
            nextPelanggan.setNamaLengkap(pelanggan.getNamaLengkap());
            nextPelanggan.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextPelanggan.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updatePelanggan(Long idPelanggan, Pelanggan pelanggan, WebRequest request) --- LINE 107";
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
    public Map<String, Object> saveUploadFilePelanggan(List<Pelanggan> listPelanggan,
                                                       MultipartFile multipartFile,
                                                       WebRequest request) throws Exception {
        List<Pelanggan> listPelangganResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            listPelangganResult = pelangganRepo.saveAll(listPelanggan);
            if (listPelangganResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFilePelanggan(List<Pelanggan> listPelanggan, MultipartFile multipartFile, WebRequest request)  --- LINE 133";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV05004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFilePelanggan(List<Pelanggan> listPelanggan, MultipartFile multipartFile, WebRequest request) --- LINE 138";
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

    public Map<String,Object> findAllPelanggan(Pageable pageable, WebRequest request)
    {
        List<PelangganDTO> listPelangganDTO = null;
        Map<String,Object> mapResult = null;
        Page<Pelanggan> pagePelanggan = null;
        List<Pelanggan> listPelanggan = null;

        try
        {
            pagePelanggan = pelangganRepo.findByIsDelete(pageable,(byte)1);
            listPelanggan = pagePelanggan.getContent();
            if(listPelanggan.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV05005",
                                request);
            }
            listPelangganDTO = modelMapper.map(listPelanggan, new TypeToken<List<PelangganDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listPelangganDTO,pagePelanggan,mapColumnSearch);
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllPelanggan(Pageable pageable, WebRequest request) --- LINE 177";
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
        Page<Pelanggan> pagePelanggan = null;
        List<Pelanggan> listPelanggan = null;
        List<PelangganDTO> listPelangganDTO = null;
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
            pagePelanggan = getDataByValue(pageable,columFirst,valueFirst);
            listPelanggan = pagePelanggan.getContent();
            if(listPelanggan.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV05006",
                                request);
            }
            listPelangganDTO = modelMapper.map(listPelanggan, new TypeToken<List<PelangganDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listPelangganDTO,pagePelanggan,mapColumnSearch);
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

    public Map<String,Object> findById(Long idPelanggan, WebRequest request)
    {
        Pelanggan pelanggan = pelangganRepo.findById(idPelanggan).orElseThrow (
                ()-> null
        );
        if(pelanggan == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_PELANGGAN_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV05007",request);
        }
        PelangganDTO pelangganDTO = modelMapper.map(pelanggan, new TypeToken<PelangganDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        pelangganDTO,
                        null,
                        request);
    }


    public Map<String,Object> findAllPelanggan()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<PelangganDTO> listPelangganDTO = null;
        Map<String,Object> mapResult = null;
        List<Pelanggan> listPelanggan = null;

        try
        {
            listPelanggan = pelangganRepo.findByIsDelete((byte)1);
            if(listPelanggan.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                null,
                                null,
                                null);
            }
            listPelangganDTO = modelMapper.map(listPelanggan, new TypeToken<List<PelangganDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = " findAllPelanggan() --- LINE 304";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE05006", null);
        }



        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        listPelangganDTO,
                        null,
                        null);
    }

    public Map<String, Object> deletePelanggan(Long idPelanggan, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Pelanggan nextPelanggan = null;
        try {
            nextPelanggan = pelangganRepo.findById(idPelanggan).orElseThrow(
                    ()->null
            );

            if(nextPelanggan==null)
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
            nextPelanggan.setIsDelete((byte)0);
            nextPelanggan.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextPelanggan.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " deletePelanggan(Long idPelanggan, WebRequest request) --- LINE 344";
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
    public List<PelangganDTO> getAllPelanggan()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<PelangganDTO> listPelangganDTO = null;
        Map<String,Object> mapResult = null;
        List<Pelanggan> listPelanggan = null;

        try
        {
            listPelanggan = pelangganRepo.findByIsDelete((byte)1);
            if(listPelanggan.size()==0)
            {
                return new ArrayList<PelangganDTO>();
            }
            listPelangganDTO = modelMapper.map(listPelanggan, new TypeToken<List<PelangganDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "getAllPelanggan() --- LINE 331";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return listPelangganDTO;
        }
        return listPelangganDTO;
    }

    private Page<Pelanggan> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals("") || paramValue==null)
        {
            return pelangganRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id"))
        {
            return pelangganRepo.findByIsDeleteAndIdPelanggan(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return pelangganRepo.findByIsDeleteAndNamaLengkapContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }

        return pelangganRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }


}
