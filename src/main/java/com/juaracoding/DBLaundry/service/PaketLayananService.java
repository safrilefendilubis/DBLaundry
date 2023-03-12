package com.juaracoding.DBLaundry.service;

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.PaketLayananDTO;
import com.juaracoding.DBLaundry.handler.ResourceNotFoundException;
import com.juaracoding.DBLaundry.handler.ResponseHandler;
import com.juaracoding.DBLaundry.model.PaketLayanan;
import com.juaracoding.DBLaundry.repo.PaketLayananRepo;
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
public class PaketLayananService {

    private PaketLayananRepo paketLayananRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    public PaketLayananService(PaketLayananRepo paketLayananRepo) {
        strExceptionArr[0]="PaketLayananService";
        mapColumn();
        this.paketLayananRepo = paketLayananRepo;
    }
    private void mapColumn()
    {
        mapColumnSearch.put("id","ID PELANGGAN");
        mapColumnSearch.put("nama","NAMA PELANGGAN");
    }

    public Map<String, Object> savePaketLayanan(PaketLayanan paketLayanan, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV05001",request);
            }
            paketLayanan.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            paketLayanan.setCreatedDate(new Date());
            this.paketLayananRepo.save(paketLayanan);
        } catch (Exception e) {
            strExceptionArr[1] = "savePaketLayanan(PaketLayanan paketLayanan, WebRequest request) --- LINE 65";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE05001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, paketLayanan.getIdListHarga(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updatePaketLayanan(Long idPaketLayanan, PaketLayanan paketLayanan, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            PaketLayanan nextPaketLayanan = paketLayananRepo.findById(idPaketLayanan).orElseThrow(
                    ()->null
            );

            if(nextPaketLayanan==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_PAKET_LAYANAN_NOT_EXISTS,
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
            nextPaketLayanan.setTipeLayanan(paketLayanan.getTipeLayanan());
            nextPaketLayanan.setNamaPaket(paketLayanan.getNamaPaket());
            nextPaketLayanan.setHargaPerKilo(paketLayanan.getHargaPerKilo());
            nextPaketLayanan.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextPaketLayanan.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updatePaketLayanan(Long idPaketLayanan, PaketLayanan paketLayanan, WebRequest request) --- LINE 107";
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
    public Map<String, Object> saveUploadFilePaketLayanan(List<PaketLayanan> listPaketLayanan,
                                                          MultipartFile multipartFile,
                                                          WebRequest request) throws Exception {
        List<PaketLayanan> listPaketLayananResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            listPaketLayananResult = paketLayananRepo.saveAll(listPaketLayanan);
            if (listPaketLayananResult.size() == 0) {
                strExceptionArr[1] = "aveUploadFilePaketLayanan(List<PaketLayanan> listPaketLayanan, MultipartFile multipartFile, WebRequest request)  --- LINE 133";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV05004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "aveUploadFilePaketLayanan(List<PaketLayanan> listPaketLayanan, MultipartFile multipartFile, WebRequest request)  --- LINE 138";
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

    public Map<String,Object> findAllPaketLayanan(Pageable pageable, WebRequest request)
    {
        List<PaketLayananDTO> listPaketLayananDTO = null;
        Map<String,Object> mapResult = null;
        Page<PaketLayanan> pagePaketLayanan = null;
        List<PaketLayanan> listPaketLayanan = null;

        try
        {
            pagePaketLayanan = paketLayananRepo.findByIsDelete(pageable,(byte)1);
            listPaketLayanan = pagePaketLayanan.getContent();
            if(listPaketLayanan.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV05005",
                                request);
            }
            listPaketLayananDTO = modelMapper.map(listPaketLayanan, new TypeToken<List<PaketLayananDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listPaketLayananDTO,pagePaketLayanan,mapColumnSearch);
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllPaketLayanan(Pageable pageable, WebRequest request) --- LINE 177";
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
        Page<PaketLayanan> pagePaketLayanan = null;
        List<PaketLayanan> listPaketLayanan = null;
        List<PaketLayananDTO> listPaketLayananDTO = null;
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
            pagePaketLayanan = getDataByValue(pageable,columFirst,valueFirst);
            listPaketLayanan = pagePaketLayanan.getContent();
            if(listPaketLayanan.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV05006",
                                request);
            }
            listPaketLayananDTO = modelMapper.map(listPaketLayanan, new TypeToken<List<PaketLayananDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listPaketLayananDTO,pagePaketLayanan,mapColumnSearch);
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

    public Map<String,Object> findById(Long idPaketLayanan, WebRequest request)
    {
        PaketLayanan paketLayanan = paketLayananRepo.findById(idPaketLayanan).orElseThrow (
                ()-> null
        );
        if(paketLayanan == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_PAKET_LAYANAN_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV05007",request);
        }
        PaketLayananDTO paketLayananDTO = modelMapper.map(paketLayanan, new TypeToken<PaketLayananDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        paketLayananDTO,
                        null,
                        request);
    }


    public Map<String,Object> findAllPaketLayanan()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<PaketLayananDTO> listPaketLayananDTO = null;
        Map<String,Object> mapResult = null;
        List<PaketLayanan> listPaketLayanan = null;

        try
        {
            listPaketLayanan = paketLayananRepo.findByIsDelete((byte)1);
            if(listPaketLayanan.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                null,
                                null,
                                null);
            }
            listPaketLayananDTO = modelMapper.map(listPaketLayanan, new TypeToken<List<PaketLayananDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllPaketLayanan()--- LINE 304";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE05006", null);
        }



        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        listPaketLayananDTO,
                        null,
                        null);
    }

    public Map<String, Object> deletePaketLayanan(Long idPaketLayanan, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        PaketLayanan nextPaketLayanan = null;
        try {
            nextPaketLayanan = paketLayananRepo.findById(idPaketLayanan).orElseThrow(
                    ()->null
            );

            if(nextPaketLayanan==null)
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
            nextPaketLayanan.setIsDelete((byte)0);
            nextPaketLayanan.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextPaketLayanan.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " deletePaketLayanan(Long idPaketLayanan, WebRequest request) --- LINE 344";
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
    public List<PaketLayananDTO> getAllPaketLayanan()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<PaketLayananDTO> listPaketLayananDTO = null;
        Map<String,Object> mapResult = null;
        List<PaketLayanan> listPaketLayanan = null;

        try
        {
            listPaketLayanan = paketLayananRepo.findByIsDelete((byte)1);
            if(listPaketLayanan.size()==0)
            {
                return new ArrayList<PaketLayananDTO>();
            }
            listPaketLayananDTO = modelMapper.map(listPaketLayanan, new TypeToken<List<PaketLayananDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "getAllPaketLayanan() --- LINE 331";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return listPaketLayananDTO;
        }
        return listPaketLayananDTO;
    }

    private Page<PaketLayanan> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals("") || paramValue==null)
        {
            return paketLayananRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id"))
        {
            return paketLayananRepo.findByIsDeleteAndIdListHarga(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return paketLayananRepo.findByIsDeleteAndNamaPaketContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }

        return paketLayananRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }


}
