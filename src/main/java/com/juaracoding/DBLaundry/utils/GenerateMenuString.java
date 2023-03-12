package com.juaracoding.DBLaundry.utils;

import com.juaracoding.DBLaundry.model.Akses;
import com.juaracoding.DBLaundry.model.Menu;
import com.juaracoding.DBLaundry.model.MenuHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateMenuString {


    private StringBuilder sBuild = new StringBuilder();
    private List<MenuHeader> listMenuHeader = new ArrayList<MenuHeader>();

    private Map<String,String> mapx = new HashMap<String,String>();
    
    public void xFunction()
    {
        List<String> listObj=new ArrayList<String>();
        for (String strX:
             listObj) {
            
        }
    }
        public String menuInnerHtml(Akses akses)
    {

        List<Menu> listMenuz = akses.getListMenuAkses();
        String namaMenuHeader = "";
        String linkMenu = "";
        String strListMenuHtml = "";
        String [] strLinkArr = null;
        String [] strSplitLink= null;

        for (int i=0;i<listMenuz.size();i++)
        {
            namaMenuHeader = listMenuz.get(i).getMenuHeader().getNamaMenuHeader();
            sBuild.setLength(0);
            linkMenu = sBuild.append(listMenuz.get(i).getPathMenu()).append("-").
                    append(listMenuz.get(i).getNamaMenu()).toString();//index 0  path menu, 1 nama menu

            if(mapx.get(namaMenuHeader)==null)
            {
                mapx.put(namaMenuHeader,linkMenu);
            }
            else
            {
                sBuild.setLength(0);
                linkMenu = sBuild.append(mapx.get(namaMenuHeader)).append("#").append(linkMenu).toString();
                mapx.put(namaMenuHeader,linkMenu);
            }
        }

        for (Map.Entry<String,String> strMap:
             mapx.entrySet()) {
            sBuild.setLength(0);
            strListMenuHtml = sBuild.append(strListMenuHtml).append("<li>")
                    .append("<a href=\"#\">").append(strMap.getKey()).append("</a>")
                    .append("<ul class=\"menu-dropdown\">").toString();
            linkMenu = strMap.getValue();
            strLinkArr = linkMenu.split("#");
            for (int i=0;i<strLinkArr.length;i++)
            {
                strSplitLink = strLinkArr[i].split("-");
                sBuild.setLength(0);
                strListMenuHtml = sBuild.append(strListMenuHtml).
                        append("<li>").append("<a href=\"").append(strSplitLink[0]).append("\">")
                        .append(strSplitLink[1]).append("</a>").append("</li>").toString();
            }
            sBuild.setLength(0);
            strListMenuHtml = sBuild.append(strListMenuHtml)
                    .append("</ul>").append("</li>").toString();
        }
        return strListMenuHtml;
    }
}