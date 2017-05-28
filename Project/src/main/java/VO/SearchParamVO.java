package VO;

/**
 *
 * @author Radek
 */
public class SearchParamVO {
    public String Key;
    public String Value;
    public boolean IsEquals = true;
    public boolean IsNumber = false;
    public String Special = "";
    public boolean ForceOrBefore = false;
    public boolean CloseOrBefore = false;
    public boolean ForceAndBefore = false;
    public boolean IsDateTime = false;
}
