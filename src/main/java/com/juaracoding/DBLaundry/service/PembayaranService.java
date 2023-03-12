package com.juaracoding.DBLaundry.service;

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.PembayaranDTO;
import com.juaracoding.DBLaundry.handler.ResourceNotFoundException;
import com.juaracoding.DBLaundry.handler.ResponseHandler;
import com.juaracoding.DBLaundry.model.Pembayaran;
import com.juaracoding.DBLaundry.repo.PembayaranRepo;
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

/*
KODE MODUL 12
 */
@Service
@Transactional
public class PembayaranService {


    private PembayaranRepo pembayaranRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    public PembayaranService(PembayaranRepo pembayaranRepo) {
        strExceptionArr[0]="PembayaranService";
        mapColumn();
        this.pembayaranRepo = pembayaranRepo;
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID PEMBAYARAN");
        mapColumnSearch.put("nama","NAMA PEMBAYARAN");
    }

    public Map<String, Object> savePembayaran(Pembayaran pembayaran, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV12001",request);
            }
            pembayaran.setNamaPembayaran(pembayaran.getNamaPembayaran());
            pembayaran.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            pembayaran.setCreatedDate(new Date());
            pembayaranRepo.save(pembayaran);
        } catch (Exception e) {
            strExceptionArr[1] = "savePembayaran(Pembayaran pembayaran, WebRequest request) --- LINE 65";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE12001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, pembayaran.getIdPembayaran(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updatePembayaran(Long idPembayaran, Pembayaran pembayaran, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Pembayaran nextPembayaran = pembayaranRepo.findById(idPembayaran).orElseThrow(
                    ()->null
            );

            if(nextPembayaran==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_PEMBAYARAN_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV12002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV12003",request);
            }

            nextPembayaran.setNamaPembayaran(pembayaran.getNamaPembayaran());
            nextPembayaran.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextPembayaran.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updatePembayaran(Long idPembayaran, Pembayaran pembayaran, WebRequest request) --- LINE 107";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE12002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }



    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveUploadFilePembayaran(List<Pembayaran> listPembayaran,
                                                        MultipartFile multipartFile,
                                                        WebRequest request) throws Exception {
        List<Pembayaran> listPembayaranResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            listPembayaranResult = pembayaranRepo.saveAll(listPembayaran);
            if (listPembayaranResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFilePembayaran(List<Pembayaran> listPembayaran, MultipartFile multipartFile, WebRequest request) --- LINE 133";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV12004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFilePembayaran(List<Pembayaran> listPembayaran, MultipartFile multipartFile, WebRequest request) --- LINE 138";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE12002", request);
        }
        return new ResponseHandler().
                generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        null,
                        request);
    }

    public Map<String,Object> findAllPembayaran(Pageable pageable, WebRequest request)
    {
        List<PembayaranDTO> listPembayaranDTO = null;
        Map<String,Object> mapResult = null;
        Page<Pembayaran> pagePembayaran = null;
        List<Pembayaran> listPembayaran = null;

        try
        {
            pagePembayaran = pembayaranRepo.findByIsDelete(pageable,(byte)1);
            listPembayaran = pagePembayaran.getContent();
            if(listPembayaran.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV12005",
                                request);
            }
            listPembayaranDTO = modelMapper.map(listPembayaran, new TypeToken<List<PembayaranDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listPembayaranDTO,pagePembayaran,mapColumnSearch);
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllPembayaran(Pageable pageable, WebRequest request) --- LINE 177";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE12003", request);
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
        Page<Pembayaran> pagePembayaran = null;
        List<Pembayaran> listPembayaran = null;
        List<PembayaranDTO> listPembayaranDTO = null;
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
                                        "FE12004",
                                        request);
                    }
                }
            }
            pagePembayaran = getDataByValue(pageable,columFirst,valueFirst);
            listPembayaran = pagePembayaran.getContent();
            if(listPembayaran.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV12006",
                                request);
            }
            listPembayaranDTO = modelMapper.map(listPembayaran, new TypeToken<List<PembayaranDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listPembayaranDTO,pagePembayaran,mapColumnSearch);
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 243";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE12005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    public Map<String,Object> findById(Long idPembayaran, WebRequest request)
    {
        Pembayaran pembayaran = pembayaranRepo.findById(idPembayaran).orElseThrow (
                ()-> null
        );
        if(pembayaran == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_PEMBAYARAN_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV12007",request);
        }
        PembayaranDTO pembayaranDTO = modelMapper.map(pembayaran, new TypeToken<PembayaranDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        pembayaranDTO,
                        null,
                        request);
    }


    public Map<String,Object> findAllPembayaran()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<PembayaranDTO> listPembayaranDTO = null;
        Map<String,Object> mapResult = null;
        List<Pembayaran> listPembayaran = null;

        try
        {
            listPembayaran = pembayaranRepo.findByIsDelete((byte)1);
            if(listPembayaran.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                null,
                                null,
                                null);
            }
            listPembayaranDTO = modelMapper.map(listPembayaran, new TypeToken<List<PembayaranDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllPembayaran() --- LINE 304";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE12006", null);
        }



        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        listPembayaranDTO,
                        null,
                        null);
    }

    public Map<String, Object> deletePembayaran(Long idDemo, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Pembayaran nextPembayaran = null;
        try {
            nextPembayaran = pembayaranRepo.findById(idDemo).orElseThrow(
                    ()->null
            );

            if(nextPembayaran==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV12006",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV12007",request);
            }
            nextPembayaran.setIsDelete((byte)0);
            nextPembayaran.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextPembayaran.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " deletePembayaran(Long idDemo, WebRequest request) --- LINE 344";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE12007", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }
    public List<PembayaranDTO> getAllPembayaran()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<PembayaranDTO> listPembayaranDTO = null;
        Map<String,Object> mapResult = null;
        List<Pembayaran> listPembayaran = null;

        try
        {
            listPembayaran = pembayaranRepo.findByIsDelete((byte)1);
            if(listPembayaran.size()==0)
            {
                return new ArrayList<PembayaranDTO>();
            }
            listPembayaranDTO = modelMapper.map(listPembayaran, new TypeToken<List<PembayaranDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "getAllPembayaran() --- LINE 331";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return listPembayaranDTO;
        }
        return listPembayaranDTO;
    }

    private Page<Pembayaran> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals("") || paramValue==null)
        {
            return pembayaranRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id"))
        {
            return pembayaranRepo.findByIsDeleteAndIdPembayaran(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return pembayaranRepo.findByIsDeleteAndNamaPembayaranContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }

        return pembayaranRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }


}
