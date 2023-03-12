package com.juaracoding.DBLaundry.utils;

import com.juaracoding.DBLaundry.dto.MenuDTO;
import com.juaracoding.DBLaundry.model.Menu;

import java.util.List;

public class GenerateCheckBoxGrid {

    private StringBuilder sBuild = new StringBuilder();
    private String gridManage(List<MenuDTO> list)
    {
        sBuild.setLength(0);
        String grid = "";
        grid = sBuild.append("<div id=\"container\">").toString();
        for(int i=0;i<list.size();i++)
        {
            if(i%4==0 && i==0)
            {
                sBuild.setLength(0);
                grid = sBuild.append("<div class=\"cang\"> <div class=\"cut\"> ")
                        .append("<input type=\"checkbox\" name=\"listMenuAkses\" value=\"").
                        append(list.get(i).getIdMenu()).
                        append("\" id=\"listMenuAkses\">").
                        append("<input type=\"hidden\" name=\"_listMenuAkses\" value=\"on\">").
                        append("\"").append(list.get(i).getNamaMenu()).append("\"").
                        append("</div>").toString();
            }
            if(i%4!=0 && i!=0)
            {
                sBuild.setLength(0);
                grid = sBuild.append(grid).append("<div class=\"cut\"> ")
                        .append("<input type=\"checkbox\" name=\"listMenuAkses\" value=\"").
                        append(list.get(i).getIdMenu()).
                        append("\" id=\"listMenuAkses\">").
                        append("<input type=\"hidden\" name=\"_listMenuAkses\" value=\"on\">").
                        append("</div>").toString();
            }
        }
        return grid;
    }
    public static String gridMultipleCheckBoxMenu(List<Menu> listMenu)
    {
        return "";
    }
}
