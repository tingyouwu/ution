package com.xtion.sheet;

/**
 * Created by apple on 16/4/21.
 */
public final class ViewSet {

    //optional parameters - must initialized to default values
    public int rowBgColor;
    public int rowTextColor;

    public int columnBgColor;
    public int columnTextColor;

    private ViewSet(Builder builder){
        this.rowBgColor = builder.rowBgColor;
        this.rowTextColor = builder.rowTextColor;

        this.columnBgColor = builder.columnBgColor;
        this.columnTextColor = builder.columnTextColor;
    }



    //Builder class
    public static class Builder{

        public int rowBgColor = R.color.white;
        public int rowTextColor = R.color.gray_font_1;

        public int columnBgColor = R.color.blue_crm3;
        public int columnTextColor = R.color.white;

        //default constructor with required parameters
        public Builder setRow(int bgColor,int textColor){
            rowBgColor = bgColor;
            rowTextColor = textColor;
            return this;
        }

        public Builder setColumn(int bgColor,int textColor) {
            columnBgColor = bgColor;
            columnTextColor = textColor;
            return this;
        }

        public ViewSet build() {
            return new ViewSet(this);
        }
    }

}
