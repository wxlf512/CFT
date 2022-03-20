package dev.wxlf.cft;

public class Currency {

    private String charCode;
    private String name;
    private String value;

    public Currency(String charCode, String name, String value) {
        this.charCode = charCode;
        this.name = name;
        this.value = value;
    }

    public String getCharCode() {
        return this.charCode;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
