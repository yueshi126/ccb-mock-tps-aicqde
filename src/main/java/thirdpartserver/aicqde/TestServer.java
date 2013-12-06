package thirdpartserver.aicqde;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 模拟测试SERVER 青岛市工商局 工商E线通
 * User: zhanrui
 */
public class TestServer implements Runnable {
    private static ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
    private Socket socket;

    TestServer(Socket sock) throws IOException {
        socket = sock;
    }

    public void run() {
        byte[] lenbuf = new byte[4];
        try {
            int readnumber = socket.getInputStream().read(lenbuf);
            if (readnumber != 4) {
                throw new RuntimeException("报文接收异常");
            }
            int size = new Integer(new String(lenbuf).trim());
            byte[] buf = new byte[size - 4];
            readnumber = socket.getInputStream().read(buf);
            if (readnumber != buf.length) {
                throw new RuntimeException("报文长度与实际不符。");
            }

            new Processor(buf, socket).run();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Processor implements Runnable {
        private byte[] msg;
        private Socket sock;

        Processor(byte[] buf, Socket s) {
            msg = buf;
            sock = s;
        }

        public void run() {
            try {
                String msgin = new String(msg);
                String txncode = msgin.substring(0, 4);

                Request request = new Request();
                request.setMsg(msgin);
                request.setTxnCode(txncode);
                Response response = new Response();

                Class clazz = Class.forName("thirdpartserver.aicqde.T" + txncode + "Processor");
                TxnProcessor processor = (TxnProcessor) clazz.newInstance();

                processor.service(request, response);

                String strLen = "" + (response.getMsg().getBytes("GBK").length + 4);
                String lpad = "";
                for (int i = 0; i < 4 - strLen.length(); i++) {
                    lpad += "0";
                }
                strLen = lpad + strLen;

                sock.getOutputStream().write((strLen + response.getMsg()).getBytes("GBK"));

                Thread.sleep(500);
                System.out.printf("%s 交易[%s]处理完成.\n", Thread.currentThread().getName(), txncode);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //==========================================================================
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(60001);
        int processCount = 0;
        while (true) {
            Socket sock = server.accept();
            System.out.printf(Thread.currentThread().getName() + " 新连接请求: %s %d \n", sock.getInetAddress().toString(), sock.getPort());
            new Thread(new TestServer(sock)).start();
            processCount++;
            System.out.println(Thread.currentThread().getName() + " 连接请求次数：" + processCount);
        }
    }
}
