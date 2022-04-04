package com.dchealth.entity.common;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/6/19.
 */
@Entity
@Table(name = "yun_dictitem", schema = "emhbase", catalog = "")
public class YunDictitem {
    private String serialNo;
    private String typeIdDm;
    private String itemCode;
    private String itemName;
    private String inputCode;
    private String loincCode;

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setTypeIdDm(String typeIdDm) {
        this.typeIdDm = typeIdDm;
    }

    @Id
    @Column(name = "serial_no")
    @GenericGenerator(name="generator",strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    public String getSerialNo() {
        return serialNo;
    }

    @Basic
    @Column(name = "type_id_dm")
    public String getTypeIdDm() {
        return typeIdDm;
    }

    @Basic
    @Column(name = "item_code")
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Basic
    @Column(name = "item_name")
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Basic
    @Column(name = "input_code")
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    @Basic
    @Column(name = "loinc_code")
    public String getLoincCode() {
        return loincCode;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunDictitem that = (YunDictitem) o;

        if (serialNo != null ? !serialNo.equals(that.serialNo) : that.serialNo != null) return false;
        if (typeIdDm != null ? !typeIdDm.equals(that.typeIdDm) : that.typeIdDm != null) return false;
        if (itemCode != null ? !itemCode.equals(that.itemCode) : that.itemCode != null) return false;
        if (itemName != null ? !itemName.equals(that.itemName) : that.itemName != null) return false;
        if (inputCode != null ? !inputCode.equals(that.inputCode) : that.inputCode != null) return false;
        if (loincCode != null ? !loincCode.equals(that.loincCode) : that.loincCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = serialNo != null ? serialNo.hashCode() : 0;
        result = 31 * result + (typeIdDm != null ? typeIdDm.hashCode() : 0);
        result = 31 * result + (itemCode != null ? itemCode.hashCode() : 0);
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (inputCode != null ? inputCode.hashCode() : 0);
        result = 31 * result + (loincCode != null ? loincCode.hashCode() : 0);
        return result;
    }
}
