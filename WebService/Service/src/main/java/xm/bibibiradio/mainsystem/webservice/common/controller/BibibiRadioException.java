package xm.bibibiradio.mainsystem.webservice.common.controller;

public class BibibiRadioException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private int errCode;
    private String errMsg;
    
    public BibibiRadioException(int errCode,String errMsg){
        super(errMsg);
        this.errMsg = errMsg;
        this.errCode = errCode;
    }
    
    public int getErrCode() {
        return errCode;
    }
    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
    public String getErrMsg() {
        return errMsg;
    }
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    
    

}
