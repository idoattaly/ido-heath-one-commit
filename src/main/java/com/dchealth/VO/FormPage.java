package com.dchealth.VO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * form-template对象
 * Created by Administrator on 2017/6/27.
 */
public class FormPage {


    private List<ModelPage> pages = new ArrayList<>();

    public List<ModelPage> getPages() {
        return pages;
    }

    public void setPages(List<ModelPage> pages) {
        this.pages = pages;
    }
}
