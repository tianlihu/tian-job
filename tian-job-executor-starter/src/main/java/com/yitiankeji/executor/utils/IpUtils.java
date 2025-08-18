package com.yitiankeji.executor.utils;

import java.net.*;
import java.util.*;

/** IP地址工具类，提供IP地址相关的实用方法 */
public class IpUtils {

    /**
     * 主方法，用于测试同子网IP查找功能
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 测试IP列表1
        List<String> ipArray1 = Arrays.asList("172.17.0.1", "192.168.3.101", "192.168.3.102");
        // 获取本机所有IP地址
        List<String> ipArray2 = allIps();
        // 子网掩码
        String subnetMask = "255.255.255.0"; // 子网掩码

        // 查找同子网的IP地址
        List<String> sameSubnetIPs = sameSubnetIPs(ipArray1, ipArray2, subnetMask);
        // 打印结果
        for (String ipPair : sameSubnetIPs) {
            System.out.println(ipPair);
        }
    }

    /**
     * 从URL列表中提取主机名/IP地址
     *
     * @param urls 以逗号或分号分隔的URL列表
     * @return 包含所有主机名的列表
     */
    public static List<String> getHosts(String urls) {
        // 分割URL字符串
        String[] segments = urls.split("[,;]");
        List<String> ips = new ArrayList<>(segments.length);
        try {
            for (String segment : segments) {
                URL url = new URL(segment);
                ips.add(url.getHost());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return ips;
    }

    /**
     * 获取本机IP地址
     *
     * @return 本机IP地址字符串
     * @throws UnknownHostException 如果无法获取本地主机地址
     */
    public static String getLocalIpAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * 查找两个IP列表中在同一子网内的IP地址（使用默认子网掩码）
     *
     * @param ips1 第一个IP地址列表
     * @param ips2 第二个IP地址列表
     * @return 同一子网内的IP地址列表
     */
    public static List<String> sameSubnetIPs(List<String> ips1, List<String> ips2) {
        return sameSubnetIPs(ips1, ips2, "255.255.255.0");
    }

    /**
     * 查找两个IP列表中在同一子网内的IP地址
     *
     * @param ips1       第一个IP地址列表
     * @param ips2       第二个IP地址列表
     * @param subnetMask 子网掩码
     * @return 同一子网内的IP地址列表
     */
    public static List<String> sameSubnetIPs(List<String> ips1, List<String> ips2, String subnetMask) {
        try {
            Set<String> result = new HashSet<>();
            InetAddress mask = InetAddress.getByName(subnetMask);

            for (String ip1 : ips1) {
                InetAddress inetAddress1 = InetAddress.getByName(ip1);
                for (String ip2 : ips2) {
                    InetAddress inetAddress2 = InetAddress.getByName(ip2);
                    if (isSameSubnet(inetAddress1, inetAddress2, mask)) {
                        result.add(ip2);
                    }
                }
            }
            return new ArrayList<>(result);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断两个IP地址是否在同一子网内
     *
     * @param ip1  第一个IP地址
     * @param ip2  第二个IP地址
     * @param mask 子网掩码
     * @return 如果在同一子网内返回true，否则返回false
     */
    private static boolean isSameSubnet(InetAddress ip1, InetAddress ip2, InetAddress mask) {
        byte[] ip1Bytes = ip1.getAddress();
        byte[] ip2Bytes = ip2.getAddress();
        byte[] maskBytes = mask.getAddress();

        for (int i = 0; i < ip1Bytes.length; i++) {
            if ((ip1Bytes[i] & maskBytes[i]) != (ip2Bytes[i] & maskBytes[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取本机所有有效的IPv4地址
     *
     * @return 包含本机所有IPv4地址的列表
     */
    public static List<String> allIps() {
        try {
            List<String> ips = new ArrayList<>();
            // 获取此机器所有的网络接口
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces == null) {
                return ips;
            }
            // 遍历所有的网络接口
            for (NetworkInterface iface : Collections.list(interfaces)) {
                // 检查接口是否是回环接口（即127.0.0.1）或接口是否已启用
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                // 获取接口绑定的所有IP地址
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // 打印IP地址
                    if (addr instanceof Inet4Address) {
                        ips.add(addr.getHostAddress());
                    }
                }
            }
            return ips;
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
