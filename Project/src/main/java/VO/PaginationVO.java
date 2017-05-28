package VO;

import java.util.ArrayList;

/**
 *
 * @author Radek
 */
public class PaginationVO {
    public int ItemsPerPage;
    public int Page;
    public boolean Reverse;
    public String SortBy;
    public String FulltextSearchText;
    public ArrayList<SearchParamVO> ParamItemsList;
    public boolean IsAnd = true;
}
