package com.juaracoding.DBLaundry.utils;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Map;

public class MappingAttribute {

    //method digunakan untuk seluruh menu sebelum login ex : regis, lupa password, new token dll
    public void setAttribute(Model model,Map<String,Object> mapz)
    {
        model.addAttribute("message", mapz.get("message"));
        model.addAttribute("status", mapz.get("status"));
        model.addAttribute("data", mapz.get("data")==null?"":mapz.get("data"));
        model.addAttribute("timestamp", new Date());
        model.addAttribute("success",mapz.get("success"));
        if(mapz.get("errorCode") != null)
        {
            model.addAttribute("errorCode",mapz.get("errorCode"));
            model.addAttribute("path",mapz.get("path"));
        }
    }

    //method digunakan setelah user berhasil login untuk validasi session di seluruh API Transaction
    public void setAttribute(Model model, Map<String,Object> mapz, WebRequest request)
    {
        model.addAttribute("message", mapz.get("message"));
        model.addAttribute("status", mapz.get("status"));
        model.addAttribute("data", mapz.get("data")==null?"":mapz.get("data"));
        model.addAttribute("timestamp", new Date());
        model.addAttribute("success",mapz.get("success"));
        model.addAttribute("USR_IDZ",request.getAttribute("USR_ID",1));//panggil di html dengan nama ${USR_IDZ}
        model.addAttribute("NO_HPZ",request.getAttribute("NO_HP",1));//panggil di web dengan nama ${NO_HPZ}
        model.addAttribute("EMAILZ",request.getAttribute("EMAIL",1));//panggil di web dengan nama ${EMAILZ}
        model.addAttribute("USR_NAMEZ",request.getAttribute("USR_NAME",1));//panggil di web dengan nama ${USR_NAMEZ}
        model.addAttribute("HTML_MENUZ",request.getAttribute("HTML_MENU",1));//panggil di web dengan nama ${HTML_MENUZ}<07 03 2023>
        if(mapz.get("errorCode") != null)
        {
            model.addAttribute("errorCode",mapz.get("errorCode"));
            model.addAttribute("path",mapz.get("path"));
        }
    }

    public void setAttribute(Model model, WebRequest request)
    {
        model.addAttribute("USR_IDZ",request.getAttribute("USR_ID",1));//panggil di html dengan nama ${USR_IDZ}
        model.addAttribute("NO_HPZ",request.getAttribute("NO_HP",1));//panggil di web dengan nama ${NO_HPZ}
        model.addAttribute("EMAILZ",request.getAttribute("EMAIL",1));//panggil di web dengan nama ${EMAILZ}
        model.addAttribute("USR_NAMEZ",request.getAttribute("USR_NAME",1));//panggil di web dengan nama ${USR_NAMEZ}
        model.addAttribute("HTML_MENUZ",request.getAttribute("HTML_MENU",1));//panggil di web dengan nama ${HTML_MENUZ}<07 03 2023>
    }

    public BindingResult setErrorMessage(BindingResult br, String  strErrorMessage)
    {
        br.addError(new ObjectError("globalError",strErrorMessage));
        return br;
    }
}
