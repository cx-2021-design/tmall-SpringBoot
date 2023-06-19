package com.example.tmall.util;

import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.JOptionPane;

/**
 * 作者：cjy
 * 类名：PortUtil
 * 全路径类名：com.example.tmall.util.PortUtil
 * 父类或接口：
 * 描述：用于判断某个端口是否启动
 */
public class PortUtil {

    /**
     * 检查指定端口是否被使用了，并根据配置进行处理
     *
     * @param port     要检查的端口号
     * @param server   服务名称
     * @param shutdown 是否强制关闭程序
     */
    public static void checkPort(int port, String server, boolean shutdown) {
        if (!testPort(port)) {
            if (shutdown) {//端口关闭
                String message = String.format("在端口 %d 未检查得到 %s 启动%n", port, server);
                JOptionPane.showMessageDialog(null, message);
                System.exit(1);
            } else {
                String message = String.format("在端口 %d 未检查得到 %s 启动%n,是否继续?", port, server);
                if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null, message))
                    System.exit(1);
            }
        }
    }

    /**
     * 测试指定端口是否已经被使用了
     *
     * @param port 要测试的端口号
     * @return 如果端口可用返回 true，否则返回 false
     */
    public static boolean testPort(int port) {
        try {
            ServerSocket serverSockets = new ServerSocket(port);
            serverSockets.close();
            return false;//没有异常说明端口没有被服务开启
        } catch (java.net.BindException e) {
            return true;//出现异常说明端口已经被服务开启，自己没法去绑定这个端口
        } catch (IOException e) {
            return true;
        }
    }


}
