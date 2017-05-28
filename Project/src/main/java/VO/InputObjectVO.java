package VO;

/**
 *
 * @author Radek
 */
public class InputObjectVO {
    public long Id;
    public String Device;
    public String ValueName;
    public String Value;
    
    public int IdTarget;
    
    public String From, To;
    public String Rep, Sep;
    public String Queue;
    
    public TargetVO Target;
    
    public int[] Telescopes;
}
