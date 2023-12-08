package com.food.lite.nckh.detection.model;

public class Category {
    int CateID;

    public int getLabelID() {
        return LabelID;
    }

    public void setLabelID(int labelID) {
        LabelID = labelID;
    }

    int LabelID;
    String CateName;
    String CateUnit;
    Integer CateCost;
    String CateOtherUnit;
    String CateOtherCost;
    String CateDescription;
    String CateState;
    String CateType;

    public byte[] getCateImg() {
        return CateImg;
    }

    public void setCateImg(byte[] cateImg) {
        CateImg = cateImg;
    }

    byte[] CateImg;




    public Category(int labelID, int cateID, String cateName, String cateType, String cateUnit, Integer cateCost, String cateOtherUnit, String cateOtherCost, String cateDescription, String cateState, byte[] cateImg) {
        CateID = cateID;
        CateName = cateName;
        CateType = cateType;
        CateUnit = cateUnit;
        CateCost = cateCost;
        CateOtherUnit = cateOtherUnit;
        CateOtherCost = cateOtherCost;
        CateDescription = cateDescription;
        CateState = cateState;
        CateImg = cateImg;
        LabelID = labelID;

    }


    public Category() {

    }


    public int getCateID() {
        return CateID;
    }

    public void setCateID(int cateID) {
        CateID = cateID;
    }

    public String getCateName() {
        return CateName;
    }

    public void setCateName(String cateName) {
        CateName = cateName;
    }

    public String getCateType() {
        return CateType;
    }

    public void setCateType(String cateType) {
        CateType = cateType;
    }

    public String getCateUnit() {
        return CateUnit;
    }

    public void setCateUnit(String cateUnit) {
        CateUnit = cateUnit;
    }

    public Integer getCateCost() {
        return CateCost;
    }

    public void setCateCost(Integer cateCost) {
        CateCost = cateCost;
    }

    public String getCateOtherUnit() {
        return CateOtherUnit;
    }

    public void setCateOtherUnit(String cateOtherUnit) {
        CateOtherUnit = cateOtherUnit;
    }

    public String getCateOtherCost() {
        return CateOtherCost;
    }

    public void setCateOtherCost(String cateOtherCost) {
        CateOtherCost = cateOtherCost;
    }

    public String getCateDescription() {
        return CateDescription;
    }

    public void setCateDescription(String cateDescription) {
        CateDescription = cateDescription;
    }

    public String getCateState() {
        return CateState;
    }

    public void setCateState(String cateState) {
        CateState = cateState;
    }



}
