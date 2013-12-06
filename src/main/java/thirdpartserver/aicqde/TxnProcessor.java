package thirdpartserver.aicqde;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-11-27
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public interface TxnProcessor {
    void service(Request request, Response response);
}
