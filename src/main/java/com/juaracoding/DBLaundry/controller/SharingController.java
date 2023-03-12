package com.juaracoding.DBLaundry.controller;


import com.juaracoding.DBLaundry.dto.TestOne;
import com.juaracoding.DBLaundry.dto.TestThree;
import com.juaracoding.DBLaundry.dto.TestTwo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sharing")
public class SharingController {

    @GetMapping("/ifcondition")
    public String conditionLogic( Model model,WebRequest request) {

        request.setAttribute("MY_SESSION","PAUL",1);
        List<String> list = new ArrayList<String>();
        list.add("CONTENT-0");
        list.add("CONTENT-1");
        list.add("CONTENT-2");
        model.addAttribute("paramOne",null);
        model.addAttribute("paramTwo",null);
        model.addAttribute("ifCondition","ada");
        model.addAttribute("unlessCondition",null);
        model.addAttribute("listContentz",list);
        model.addAttribute("userName",null);
        System.out.println(request.getAttribute("USR_ID",1));

        List<TestThree> listTestThree = new ArrayList<TestThree>();
        List<TestTwo> listTestTwo = new ArrayList<TestTwo>();

        TestThree testThree = new TestThree();

        testThree.setIdSubMenu("1");
        testThree.setNamaSubMenu("SALESONE");
        testThree.setStrStringTestThree("TEST SALES ONE");
        testThree.setLinkSubMenu("/api/menu/salesone");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("2");
        testThree.setNamaSubMenu("SALESTWO");
        testThree.setStrStringTestThree("TEST SALES TWO");
        testThree.setLinkSubMenu("/api/menu/salestwo");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("3");
        testThree.setNamaSubMenu("SALES THREE");
        testThree.setStrStringTestThree("TEST SALES THREE");
        testThree.setLinkSubMenu("/api/menu/salesthree");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("4");
        testThree.setNamaSubMenu("SALES FOUR");
        testThree.setStrStringTestThree("TEST SALES FOUR");
        testThree.setLinkSubMenu("/api/menu/salesfour");
        listTestThree.add(testThree);

        TestTwo testTwo = new TestTwo();
        testTwo.setStrStringTestwo("SALES");
        testTwo.setListMenu(listTestThree);

        listTestTwo.add(testTwo);

        listTestThree = new ArrayList<TestThree>();

        testThree.setIdSubMenu("5");
        testThree.setNamaSubMenu("HRONE");
        testThree.setStrStringTestThree("TEST HR ONE");
        testThree.setLinkSubMenu("/api/menu/hrone");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("6");
        testThree.setNamaSubMenu("HRTWO");
        testThree.setStrStringTestThree("TEST HR TWO");
        testThree.setLinkSubMenu("/api/menu/hrtwo");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("7");
        testThree.setNamaSubMenu("HR THREE");
        testThree.setStrStringTestThree("TEST HR THREE");
        testThree.setLinkSubMenu("/api/menu/hrthree");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("8");
        testThree.setNamaSubMenu("HR FOUR");
        testThree.setStrStringTestThree("TEST HR FOUR");
        testThree.setLinkSubMenu("/api/menu/hrfour");
        listTestThree.add(testThree);

        testTwo = new TestTwo();
        testTwo.setStrStringTestwo("HRD");
        testTwo.setListMenu(listTestThree);
        listTestTwo.add(testTwo);

        listTestThree = new ArrayList<TestThree>();

        testThree.setIdSubMenu("9");
        testThree.setNamaSubMenu("FINANCEONE");
        testThree.setStrStringTestThree("TEST FINANCE ONE");
        testThree.setLinkSubMenu("/api/menu/financeone");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("10");
        testThree.setNamaSubMenu("FINANCETWO");
        testThree.setStrStringTestThree("TEST FINANCE TWO");
        testThree.setLinkSubMenu("/api/menu/financetwo");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("11");
        testThree.setNamaSubMenu("FINANCE THREE");
        testThree.setStrStringTestThree("TEST FINANCE THREE");
        testThree.setLinkSubMenu("/api/menu/financethree");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("12");
        testThree.setNamaSubMenu("FINANCE FOUR");
        testThree.setStrStringTestThree("TEST FINANCE FOUR");
        testThree.setLinkSubMenu("/api/menu/financefour");
        listTestThree.add(testThree);

        testTwo = new TestTwo();
        testTwo.setStrStringTestwo("FINANCE");
        testTwo.setListMenu(listTestThree);
        listTestTwo.add(testTwo);

        listTestThree = new ArrayList<TestThree>();

        testThree.setIdSubMenu("13");
        testThree.setNamaSubMenu("USRMGMNT ONE");
        testThree.setStrStringTestThree("TEST USERMANAGEMENT ONE");
        testThree.setLinkSubMenu("/api/menu/usrone");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("14");
        testThree.setNamaSubMenu("USRMGMNT TWO");
        testThree.setStrStringTestThree("TEST USERMANAGEMENT TWO");
        testThree.setLinkSubMenu("/api/menu/usrtwo");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("15");
        testThree.setNamaSubMenu("USRMGMNTTHREE");
        testThree.setStrStringTestThree("TEST USERMANAGEMENT THREE");
        testThree.setLinkSubMenu("/api/menu/usrthree");
        listTestThree.add(testThree);

        testThree = new TestThree();
        testThree.setIdSubMenu("16");
        testThree.setNamaSubMenu("USRMGMNT FOUR");
        testThree.setStrStringTestThree("TEST USERMANAGEMENT FOUR");
        testThree.setLinkSubMenu("/api/menu/usrfour");
        listTestThree.add(testThree);

        testTwo = new TestTwo();
        testTwo.setStrStringTestwo("USER MANAGEMENT");
        testTwo.setListMenu(listTestThree);
        listTestTwo.add(testTwo);

        TestOne testOne = new TestOne();
        testOne.setStrTestOne("ROLE ADMIN");
        testOne.setListTestTwo(listTestTwo);


        Map<String,Object> mapColumnSearch = new HashMap<String,Object>();

        mapColumnSearch.put("id","ID MENU");
        mapColumnSearch.put("nama","NAMA MENU");
        mapColumnSearch.put("path","PATH MENU");
        mapColumnSearch.put("point","END POINT");

        Map<String,Object> mapz = new HashMap<String,Object>();

        mapz.put("content",listTestTwo);
        mapz.put("currentPage",0);
        mapz.put("totalItems",5);
        mapz.put("totalPages",20);
        mapz.put("sort",testThree);
        mapz.put("numberOfElements",5);
        mapz.put("searchParam",mapColumnSearch);
        model.addAttribute("AKZEZ",listTestTwo);
        model.addAttribute("data",mapz);
        String arrTitle[] = new String [3];
        arrTitle[0] = "KOLOM 1";
        arrTitle[1] = "KOLOM 2";
        arrTitle[2] = "KOLOM 3";

        String [][] arr = new String[3][4];
        int intX = 1;

        for (int i=0;i<arr.length;i++)
        {
            for(int j=0;j<arr[i].length;j++)
            {
                arr[i][j]=String.valueOf(intX);
                intX++;
            }

        }
        for (int i=0;i<arr.length;i++)
        {
            for(int j=0;j<arr[i].length;j++)
            {
                System.out.print("Array ke |"+i+"|"+j+"| : "+arr[i][j]+",");
            }
            System.out.println();
        }
        model.addAttribute("arrTitle",arrTitle);
        model.addAttribute("arr",arr);

//        mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_TOKEN_IS_EMPTY);
//        model.addAttribute("tagHTMLz",x);
//        request.setAttribute("LINK_MENU",x,1);
        /*
        ${totalItems}
        ${numberOfElements}
        ${searchParam.get('path')}
        ${searchParam.get('nama')}
        ${searchParam.get('path')}
        ${searchParam.get('point')}

        ${sort.strStringTestThree}
        ${sort.namaSubMenu}
        ${sort.idSubMenu}
        ${sort.linkSubMenu}
        ${content.get(0).strStringTestwo}
        th:each="varX.varY : content" ${varX.get(varY.index).strStringTestwo}"

        testThree.setIdSubMenu("16");
        testThree.setNamaSubMenu("USRMGMNT FOUR");
        testThree.setStrStringTestThree("TEST USERMANAGEMENT FOUR");
        testThree.setLinkSubMenu("/api/menu/usrfour");

        listVariable.get(0).getIdSubMenu
         */
        return "test_aja";
    }
}