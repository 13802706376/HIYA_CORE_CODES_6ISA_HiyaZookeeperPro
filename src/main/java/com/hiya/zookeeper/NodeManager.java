package com.hiya.zookeeper;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class NodeManager
{
    /**
     * �����ڵ�
     *     @ PERSISTENT-�־û�Ŀ¼�ڵ� �ͻ�����zookeeper�Ͽ����Ӻ󣬸ýڵ����ɴ���
         * @ PERSISTENT_SEQUENTIAL-�־û�˳����Ŀ¼�ڵ�ͻ�����zookeeper�Ͽ����Ӻ󣬸ýڵ����ɴ��ڣ�ֻ��Zookeeper���ýڵ����ƽ���˳����
         * @ EPHEMERAL-��ʱĿ¼�ڵ� �ͻ�����zookeeper�Ͽ����Ӻ󣬸ýڵ㱻ɾ��
         * @ EPHEMERAL_SEQUENTIAL-��ʱ˳����Ŀ¼�ڵ㣬�ͻ�����zookeeper�Ͽ����Ӻ󣬸ýڵ㱻ɾ����ֻ��Zookeeper���ýڵ����ƽ���˳����
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
     * ��ȡ�Ӵ�����
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
     * �жϽڵ��Ƿ���� 
     * @param zkClient
     * @param nodeName
     */
    public static boolean checkNodeExists(ZkClient zkClient,String nodeName)
    {
        return zkClient.exists(nodeName);
    }
    
    
    /**
     * ɾ���ڵ� 
     * @param zkClient
     * @param nodeName
     */
    public static boolean deleteNode(ZkClient zkClient,String nodeName)
    {
        //ɾ������һ���ڵ㣬����true��ʾ�ɹ�  
        boolean e1 = zkClient.delete(nodeName);  
        
        //ɾ�������ӽڵ�����нڵ�  
        zkClient.deleteRecursive(nodeName);  
        return e1;
    }
    
    /**
     * �������� 
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
     * ���Ľڵ����Ϣ�ı䣨�����ڵ㣬ɾ���ڵ㣬����ӽڵ㣩
     * @param zkClient
     * @param nodeName
     */
    public static void subscribeChildChanges(ZkClient zkClient,String nodeName,IZkChildListener listener)
    {
        zkClient.subscribeChildChanges(nodeName, listener);  
    }
    
    /**
     * ���Ľڵ���������ݵı仯 ��ɾ�����ݣ�������ݣ�
     * @param zkClient
     * @param nodeName
     */
    public static void subscribeDataChanges(ZkClient zkClient,String nodeName,IZkDataListener listener)
    {
        zkClient.subscribeDataChanges(nodeName, listener);  
    }
}