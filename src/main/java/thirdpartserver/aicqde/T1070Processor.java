package thirdpartserver.aicqde;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-11-27
 * Time: 上午10:44
 */
public class T1070Processor implements TxnProcessor{
    @Override
    public void service(Request request, Response response) {
        //1070：入资登记预交易 响应
        String msg = "" +
                "00" + //返回码	2	CHAR	成功返回00
                "1234567890123456789012" + //入资帐号	22	CHAR
                "企业名称XX12345678901234567890123456789012345678901234567890123456789012" + //企业名称	72	CHAR
                "入资银行名1234567890123456789012345678901234567890";  //入资银行名称	50	CHAR

        response.setMsg(msg);
    }
}
