package thirdpartserver.aicqde;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-11-27
 * Time: 上午10:47
 */
public class Request {
    private String txnCode;
    private String msg;

    public String getTxnCode() {
        return txnCode;
    }

    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
