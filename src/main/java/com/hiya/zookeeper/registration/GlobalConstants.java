package com.hiya.zookeeper.registration;

public class GlobalConstants
{
    // zk服务器列表
    public static final String zkhosts = "10.10.51.74:2181,10.10.51.77:2181,10.10.50.62:2181";
    // 连接的超时时间
    public static final int sessionTimeout = 2000;
    // 服务在zk下的路径
    public static final String parentZnodePath = "/servers";

}