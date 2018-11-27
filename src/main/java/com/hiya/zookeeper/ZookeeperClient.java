package com.hiya.zookeeper;

import org.I0Itec.zkclient.ZkClient;

import com.hiya.zookeeper.IdMaker.RemoveMethod;

public class ZookeeperClient
{
    static String nodeName = "/hiyaNode1";
    static String zkServers = "10.10.51.74:2181,10.10.51.77:2181";
    
    public static void main(String[] args) throws Exception
    {
        //创建session
        ZkClient zkClient = CreateSession.createSession(zkServers);
        
        //创建节点
        UserObj u1 = new UserObj("1","wade");
        NodeManager.createNode(zkClient, nodeName, u1);
        
        //判断节点是否存在 
        boolean isExists = NodeManager.checkNodeExists(zkClient, nodeName);
        System.out.println(isExists);
        
        //获取接待数据
        UserObj obj = NodeManager.getNodeData(zkClient, nodeName);
        System.out.println(obj);
        
        //订阅节点的信息改变（创建节点，删除节点，添加子节点）
        NodeManager.subscribeChildChanges(zkClient, nodeName, new HiyaZkChildListener());
        
        //订阅节点的数据内容的变化 （删除内容，添加内容）
        NodeManager.subscribeDataChanges(zkClient, nodeName, new HiyaZkDataListener());
        
        //更新数据 
        UserObj u2 = new UserObj("1","wade399");
        UserObj obj2 = NodeManager.modifyNodeData(zkClient, nodeName, u2);
        System.out.println(obj2);
        
        // 删除节点 
        //boolean isDelete = NodeManager.deleteNode(zkClient, nodeName);
        //System.out.println(isDelete);
        
        //
        IdMaker idMaker = new IdMaker(zkServers,"/NameService/IdGen", "ID-");
        idMaker.start();

        try
        {
            for (int i = 0; i < 2; i++)
            {
                String id = idMaker.generateId(RemoveMethod.DELAY);
                System.out.println(id);
            }
        } finally
        {
            idMaker.stop();
        }
    }

    
}
