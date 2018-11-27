package com.hiya.zookeeper.registration;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class DistributedSystemServer
{

    private ZooKeeper zk = null;

    private void getZkClient() throws Exception
    {
        zk = new ZooKeeper(GlobalConstants.zkhosts, GlobalConstants.sessionTimeout, null);
    }

    /**
     * ��zookeeper�е�/servers�´����ӽڵ�
     * 
     * @throws InterruptedException
     * @throws KeeperException
     */
    private void connectZK(String serverName, String port) throws Exception
    {

        // �ȴ��������ڵ�
        if (zk.exists(GlobalConstants.parentZnodePath, false) == null)
        {
            zk.create(GlobalConstants.parentZnodePath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // ����zk����znode
        zk.create(GlobalConstants.parentZnodePath + "/", (serverName + ":" + port).getBytes(), Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("server " + serverName + " is online ......");

    }

    // �������ľ���ҵ������
    private void handle(String serverName) throws Exception
    {
        System.out.println("server " + serverName + " is waiting for task process......");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception
    {
        DistributedSystemServer server = new DistributedSystemServer();

        // ��ȡ��zookeeperͨ�ŵĿͻ�������
        server.getZkClient();

        // һ������ȥzookeeper��ע���������Ϣ������1�� �������������� ����2���������ļ����˿�
        server.connectZK(args[0], args[1]);

        // ����ҵ���߼���������
        server.handle(args[0]);
    }
}