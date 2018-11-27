package com.hiya.zookeeper;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class NodeManager
{
    /**
     * 创建节点
     *     @ PERSISTENT-持久化目录节点 客户端与zookeeper断开连接后，该节点依旧存在
         * @ PERSISTENT_SEQUENTIAL-持久化顺序编号目录节点客户端与zookeeper断开连接后，该节点依旧存在，只是Zookeeper给该节点名称进行顺序编号
         * @ EPHEMERAL-临时目录节点 客户端与zookeeper断开连接后，该节点被删除
         * @ EPHEMERAL_SEQUENTIAL-临时顺序编号目录节点，客户端与zookeeper断开连接后，该节点被删除，只是Zookeeper给该节点名称进行顺序编号
     * @param zkClient
     * @return
     */
    public static String createNode(ZkClient zkClient,String nodeName,Object dataObj)
    {
        String path = zkClient.create(nodeName, dataObj, CreateMode.PERSISTENT);
        System.out.println("created path:" + path);
        return path;
    }

    /**
     * 获取接待数据
     * @param zkClient
     * @param nodeName
     */
    public static <T>  T   getNodeData(ZkClient zkClient,String nodeName)
    {
        Stat stat = new Stat();  
        T result = zkClient.readData(nodeName, stat);
        return result;
    }
    
    /**
     * 判断节点是否存在 
     * @param zkClient
     * @param nodeName
     */
    public static boolean checkNodeExists(ZkClient zkClient,String nodeName)
    {
        return zkClient.exists(nodeName);
    }
    
    
    /**
     * 删除节点 
     * @param zkClient
     * @param nodeName
     */
    public static boolean deleteNode(ZkClient zkClient,String nodeName)
    {
        //删除单独一个节点，返回true表示成功  
        boolean e1 = zkClient.delete(nodeName);  
        
        //删除含有子节点的所有节点  
        zkClient.deleteRecursive(nodeName);  
        return e1;
    }
    
    /**
     * 更新数据 
     * @param zkClient
     * @param nodeName
     */
    public static <T> T  modifyNodeData(ZkClient zkClient,String nodeName,Object newObj)
    {
        zkClient.writeData(nodeName, newObj);  
        T result = getNodeData(zkClient, nodeName);
        return result;
    }
    
    /**
     * 订阅节点的信息改变（创建节点，删除节点，添加子节点）
     * @param zkClient
     * @param nodeName
     */
    public static void subscribeChildChanges(ZkClient zkClient,String nodeName,IZkChildListener listener)
    {
        zkClient.subscribeChildChanges(nodeName, listener);  
    }
    
    /**
     * 订阅节点的数据内容的变化 （删除内容，添加内容）
     * @param zkClient
     * @param nodeName
     */
    public static void subscribeDataChanges(ZkClient zkClient,String nodeName,IZkDataListener listener)
    {
        zkClient.subscribeDataChanges(nodeName, listener);  
    }
}