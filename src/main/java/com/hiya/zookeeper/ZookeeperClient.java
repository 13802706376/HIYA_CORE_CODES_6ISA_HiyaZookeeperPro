package com.hiya.zookeeper;

import org.I0Itec.zkclient.ZkClient;

import com.hiya.zookeeper.IdMaker.RemoveMethod;

public class ZookeeperClient
{
    static String nodeName = "/hiyaNode1";
    static String zkServers = "10.10.51.74:2181,10.10.51.77:2181";
    
    public static void main(String[] args) throws Exception
    {
        //����session
        ZkClient zkClient = CreateSession.createSession(zkServers);
        
        //�����ڵ�
        UserObj u1 = new UserObj("1","wade");
        NodeManager.createNode(zkClient, nodeName, u1);
        
        //�жϽڵ��Ƿ���� 
        boolean isExists = NodeManager.checkNodeExists(zkClient, nodeName);
        System.out.println(isExists);
        
        //��ȡ�Ӵ�����
        UserObj obj = NodeManager.getNodeData(zkClient, nodeName);
        System.out.println(obj);
        
        //���Ľڵ����Ϣ�ı䣨�����ڵ㣬ɾ���ڵ㣬����ӽڵ㣩
        NodeManager.subscribeChildChanges(zkClient, nodeName, new HiyaZkChildListener());
        
        //���Ľڵ���������ݵı仯 ��ɾ�����ݣ�������ݣ�
        NodeManager.subscribeDataChanges(zkClient, nodeName, new HiyaZkDataListener());
        
        //�������� 
        UserObj u2 = new UserObj("1","wade399");
        UserObj obj2 = NodeManager.modifyNodeData(zkClient, nodeName, u2);
        System.out.println(obj2);
        
        // ɾ���ڵ� 
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
