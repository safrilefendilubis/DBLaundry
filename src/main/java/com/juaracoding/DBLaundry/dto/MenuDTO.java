package com.juaracoding.DBLaundry.dto;

import com.juaracoding.DBLaundry.utils.ConstantMessage;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class MenuDTO {

    private Long idMenu;
//    @NotNull
//    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 25)
    private String namaMenu;


//    @NotNull
//    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 50)
    private String pathMenu;

//    @NotNull
//    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 50)
    private String endPoint;


//    @JsonIgnoreProperties("listMenuAkses")
    private List<AksesDTO> listAksesMenu;

//    @NotNull
    private MenuHeaderDTO menuHeader;

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public MenuHeaderDTO getMenuHeader() {
        return menuHeader;
    }

    public void setMenuHeader(MenuHeaderDTO menuHeader) {
        this.menuHeader = menuHeader;
    }

    public List<AksesDTO> getListAksesMenu() {
        return listAksesMenu;
    }

    public void setListAksesMenu(List<AksesDTO> listAksesMenu) {
        this.listAksesMenu = listAksesMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getPathMenu() {
        return pathMenu;
    }

    public void setPathMenu(String pathMenu) {
        this.pathMenu = pathMenu;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}