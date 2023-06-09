package com.juaracoding.DBLaundry.controller;


import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.dto.PesananDTO;
import com.juaracoding.DBLaundry.model.Pesanan;
import com.juaracoding.DBLaundry.service.PaketLayananService;
import com.juaracoding.DBLaundry.service.PelangganService;
import com.juaracoding.DBLaundry.service.PembayaranService;
import com.juaracoding.DBLaundry.service.PesananService;
import com.juaracoding.DBLaundry.utils.ConstantMessage;
import com.juaracoding.DBLaundry.utils.ManipulationMap;
import com.juaracoding.DBLaundry.utils.MappingAttribute;
import com.juaracoding.DBLaundry.utils.PdfGeneratorLibre;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/usrmgmnt")
public class PesananController {

    private PesananService pesananService;

    private PaketLayananService paketLayananService;
    private PelangganService pelangganService;
    private PembayaranService pembayaranService;

    @Autowired
    private ModelMapper modelMapper;

    private Map<String, Object> objectMapper = new HashMap<String, Object>();
    private Map<String, String> mapSorting = new HashMap<String, String>();

    private String[] strExceptionArr = new String[2];

    private MappingAttribute mappingAttribute = new MappingAttribute();

    private PdfGeneratorLibre generator = null;

    private StringBuilder sBuild = new StringBuilder();

    private String[][] strBody = null;

    public PesananController(PesananService pesananService,
                             PaketLayananService paketLayananService,
                             PelangganService pelangganService,
                             PembayaranService pembayaranService) {
        strExceptionArr[0] = "PesananController";
        mapSorting();
        this.pesananService = pesananService;
        this.paketLayananService = paketLayananService;
        this.pelangganService = pelangganService;
        this.pembayaranService = pembayaranService;
    }

    @GetMapping("/v1/pesanan/new")
    public String createPesanan(Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("pesanan", new PesananDTO());
        model.addAttribute("listPelanggan", pelangganService.getAllPelanggan());//untuk parent nya
        model.addAttribute("listPaketLayanan", paketLayananService.getAllPaketLayanan());//untuk parent nya
        model.addAttribute("listPembayaran", pembayaranService.getAllPembayaran());//untuk parent nya
        return "pesanan/create_pesanan";
    }

    @GetMapping("/v1/pesanan/edit/{id}")
    public String editPesanan(Model model, WebRequest request, @PathVariable("id") Long id) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = pesananService.findById(id, request);
        PesananDTO pesananDTO = (objectMapper.get("data") == null ? null : (PesananDTO) objectMapper.get("data"));
        if ((Boolean) objectMapper.get("success")) {
            PesananDTO pesananDTOForSelect = (PesananDTO) objectMapper.get("data");
            model.addAttribute("pesanan", pesananDTO);
            model.addAttribute("listPaketLayanan", paketLayananService.getAllPaketLayanan());//untuk parent nya
            model.addAttribute("listPembayaran", pembayaranService.getAllPembayaran());//untuk parent nya
            model.addAttribute("selectedValuesPaket", pesananDTOForSelect.getPaketLayanan().getIdListHarga());//penunjuk yang sudah dibuat sebelumnya
            model.addAttribute("selectedValuesPembayaran", pesananDTOForSelect.getPembayaran().getIdPembayaran());//penunjuk yang sudah dibuat sebelumnya
            return "pesanan/edit_pesanan";

        } else {
            model.addAttribute("pesanan", new PesananDTO());
            return "redirect:/api/usrmgmnt/v1/pesanan/default";
        }
    }

    @PostMapping("/v1/pesanan/new")
    public String newPesanan(@ModelAttribute(value = "pesanan")
                             @Valid PesananDTO pesananDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
    ) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }

        /* START VALIDATION */
        if (bindingResult.hasErrors()) {
            model.addAttribute("pesanan", pesananDTO);
            model.addAttribute("status", "error");
            model.addAttribute("listPelanggan", pelangganService.getAllPelanggan());//untuk parent nya
            model.addAttribute("listPaketLayanan", paketLayananService.getAllPaketLayanan());//untuk parent nya
            model.addAttribute("listPembayaran", pembayaranService.getAllPembayaran());//untuk parent nya

            return "pesanan/create_pesanan";
        }
        Boolean isValid = true;

        Pesanan pesanan = modelMapper.map(pesananDTO, new TypeToken<Pesanan>() {
        }.getType());
        objectMapper = pesananService.savePesanan(pesanan, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("message", "DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave") == null ? 1 : Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/usrmgmnt/v1/pesanan/fbpsb/0/asc/idPesanan?columnFirst=id&valueFirst=" + idDataSave + "&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("listPelanggan", pelangganService.getAllPelanggan());//untuk parent nya
            model.addAttribute("listPaketLayanan", paketLayananService.getAllPaketLayanan());//untuk parent nya
            model.addAttribute("listPembayaran", pembayaranService.getAllPembayaran());//untuk parent nya
            model.addAttribute("pesanan", new PesananDTO());
            model.addAttribute("status", "error");
            return "pesanan/create_pesanan";
        }
    }

    @PostMapping("/v1/pesanan/edit/{id}")
    public String editPesanan(@ModelAttribute("pesanan")
                              @Valid PesananDTO pesananDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    ) {
        /* START VALIDATION */
        if (bindingResult.hasErrors()) {
            model.addAttribute("pesanan", pesananDTO);
            model.addAttribute("listPelanggan", pelangganService.getAllPelanggan());//untuk parent nya
            model.addAttribute("listPaketLayanan", paketLayananService.getAllPaketLayanan());//untuk parent nya
            model.addAttribute("listPembayaran", pembayaranService.getAllPembayaran());//untuk parent nya
            return "pesanan/edit_pesanan";
        }
        Boolean isValid = true;

        Pesanan pesanan = modelMapper.map(pesananDTO, new TypeToken<Pesanan>() {
        }.getType());
        objectMapper = pesananService.updatePesanan(id, pesanan, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("pesanan", new PesananDTO());
            return "redirect:/api/usrmgmnt/v1/pesanan/fbpsb/0/asc/id?columnFirst=id&valueFirst=" + id + "&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("pesanan", new PesananDTO());
            model.addAttribute("listPelanggan", pelangganService.getAllPelanggan());//untuk parent nya
            model.addAttribute("listPaketLayanan", paketLayananService.getAllPaketLayanan());//untuk parent nya
            model.addAttribute("listPembayaran", pembayaranService.getAllPembayaran());//untuk parent nya
            return "pesanan/edit_pesanan";
        }
    }


    @GetMapping("/v1/pesanan/default")
    public String getDefaultData(Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        Pageable pageable = PageRequest.of(0, 5, Sort.by("idPesanan"));
        objectMapper = pesananService.findAllPesanan(pageable, request);
        mappingAttribute.setAttribute(model, objectMapper, request);

        model.addAttribute("pesanan", new PesananDTO());
        model.addAttribute("sortBy", "idPesanan");
        model.addAttribute("currentPage", 1);
        model.addAttribute("asc", "asc");
        model.addAttribute("columnFirst", "");
        model.addAttribute("valueFirst", "");
        model.addAttribute("sizeComponent", 5);
        return "/pesanan/pesanan";
    }

    @GetMapping("/v1/pesanan/fbpsb/{page}/{sort}/{sortby}")
    public String findByPesanan(
            Model model,
            @PathVariable("page") Integer pagez,
            @PathVariable("sort") String sortz,
            @PathVariable("sortby") String sortzBy,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            @RequestParam String sizeComponent,
            WebRequest request
    ) {
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy == null ? "idPesanan" : sortzBy;
        Pageable pageable = PageRequest.of(pagez == 0 ? pagez : pagez - 1, Integer.parseInt(sizeComponent.equals("") ? "5" : sizeComponent), sortz.equals("asc") ? Sort.by(sortzBy) : Sort.by(sortzBy).descending());
        objectMapper = pesananService.findByPage(pageable, request, columnFirst, valueFirst);
        mappingAttribute.setAttribute(model, objectMapper, request);
        model.addAttribute("pesanan", new PesananDTO());
        model.addAttribute("currentPage", pagez == 0 ? 1 : pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting, sortzBy));
        model.addAttribute("columnFirst", columnFirst);
        model.addAttribute("valueFirst", valueFirst);
        model.addAttribute("sizeComponent", sizeComponent);

        return "/pesanan/pesanan";
    }

    @GetMapping("/v1/pesanan/delete/{id}")
    public String deletePesanan(Model model, WebRequest request, @PathVariable("id") Long id) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = pesananService.deletePesanan(id, request);
        PesananDTO pesananDTO = (objectMapper.get("data") == null ? null : (PesananDTO) objectMapper.get("data"));
        return "redirect:/api/usrmgmnt/v1/pesanan/default";
    }

    private void mapSorting() {
        mapSorting.put("id", "idPesanan");
    }

    @GetMapping("/v1/pesanan/xportpdflibre")
    public void exportToPDFLibre(
            Model model,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            WebRequest request,
            HttpServletResponse response
    ) {
        mappingAttribute.setAttribute(model, request);//untuk set session ke attribut
        List<PesananDTO> listPesananDTO = pesananService.dataToExport(request, columnFirst, valueFirst);
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYYMMDDHHMMSS.sss");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        sBuild.setLength(0);
        String headervalue = sBuild.append("attachment; filename=pesananlist").
                append(currentDateTime).append(".pdf").toString();
        response.setHeader(headerkey, headervalue);
        generator = new PdfGeneratorLibre();
        int intStrHeader = 9;// INI YANG DIRUBAH SESUAIKAN DENGAN JUMLAH KOLOM
        String[] strHeader = new String[intStrHeader];

        strHeader[0] = "ID";
        strHeader[1] = "NO";
        strHeader[2] = "NAMA PELANGGAN";
        strHeader[3] = "LAYANAN";
        strHeader[4] = "PAKET";
        strHeader[5] = "BERAT";
        strHeader[6] = "HARGA PERKILO";
        strHeader[7] = "TOTAL HARGA";
        strHeader[8] = "CARA BAYAR";

        int intListPesananDTO = listPesananDTO.size();
        strBody = new String[intListPesananDTO][intStrHeader];
        String strNamaPelanggan = "";
        String strLayanan = "";
        String strPaket = "";
        String strHargaPerkilo = "";
        String strCaraBayar = "";

        for (int i = 0; i < listPesananDTO.size(); i++) {
            /*
                INI KALIAN MAPPING TAPI HATI2 DENGAN OBJECT, HARUS DI HANDLE NULL NYA
             */

            strNamaPelanggan = listPesananDTO.get(i).getPelanggan() == null ? "-" : listPesananDTO.get(i).getPelanggan().getNamaLengkap();
            strLayanan = listPesananDTO.get(i).getPaketLayanan() == null ? "-" : listPesananDTO.get(i).getPaketLayanan().getNamaPaket();
            strHargaPerkilo = listPesananDTO.get(i).getPaketLayanan() == null ? "-" : String.valueOf(listPesananDTO.get(i).getPaketLayanan().getHargaPerKilo().intValue());
            strCaraBayar = listPesananDTO.get(i).getPembayaran() == null ? "-" : listPesananDTO.get(i).getPembayaran().getNamaPembayaran();
            strPaket = listPesananDTO.get(i).getPaketLayanan() == null ? "-" : listPesananDTO.get(i).getPaketLayanan().getTipeLayanan();
            strBody[i][0] = String.valueOf(i);
            strBody[i][1] = String.valueOf(listPesananDTO.get(i).getIdPesanan());
            strBody[i][2] = strNamaPelanggan;
            strBody[i][3] = strLayanan;
            strBody[i][4] = strPaket;
            strBody[i][5] = String.valueOf(listPesananDTO.get(i).getBerat());
            strBody[i][6] = strHargaPerkilo;
            strBody[i][7] = String.valueOf(Math.round(listPesananDTO.get(i).getTotalHarga()));
            strBody[i][8] = strCaraBayar;
        }

        sBuild.setLength(0);
        generator.generate(sBuild.
                append("LIST PESANAN \n").//JUDUL REPORT
                        append("total data : ").append(intListPesananDTO).//VARIABEL TOTAL DATA
                        toString(), strHeader, strBody, response);
    }
}
