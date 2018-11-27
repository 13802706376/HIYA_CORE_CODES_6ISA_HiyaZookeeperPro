package com.hiya.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 创建会话
 * @author zjq
 */
public class CreateSession
{
    public static ZkClient createSession(String zkServers)
    {
        ZkClient zkClient = new ZkClient(zkServers, 10000, 10000, new SerializableSerializer());
        return zkClient;
    }
}