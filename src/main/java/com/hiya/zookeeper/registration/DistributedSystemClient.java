package com.hiya.zookeeper.registration;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

public class DistributedSystemClient
{

    private volatile List<String> servers = new ArrayList<>();
    private ZooKeeper zk = null;

    // ��ȡzk����
    private void getZkClient() throws Exception
    {
        // �������������в�����Ҫ���κμ���
        zk = new ZooKeeper(GlobalConstants.zkhosts, GlobalConstants.sessionTimeout, new Watcher()
        {
            @Override
            public void process(WatchedEvent event)
            {
                if (event.getType() == EventType.None)
                    return;
                try
                {
                    // ��ȡ�µķ������б�,����ע�����
                    updateServers();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * ��zk�л�ȡ���߷�������Ϣ
     */
    public void updateServers() throws Exception
    {
        // ��servers���ڵ��»�ȡ�������ӽڵ㣬��ע�����
        List<String> children = zk.getChildren(GlobalConstants.parentZnodePath, true);
        ArrayList<String> serverList = new ArrayList<String>();
        for (String child : children)
        {
            byte[] data = zk.getData(GlobalConstants.parentZnodePath + "/" + child, false, null);
            serverList.add(new String(data));
        }

        // ����ͻ�����һ�����̳߳��򣬶��Ҹ����̶߳��Ὰ������servers�б����ԣ��ڳ�Ա����volatile������һ��servers����
        // ���ڸ��·�������Ϣ����������У�����һ����ʱList���������и���
        servers = serverList;

        // ������֮��ķ������б���Ϣ��ӡ�ڿ���̨�۲�һ��
        for (String server : serverList)
        {
            System.out.println(server);
        }
        System.out.println("===================");
    }

    /**
     * ҵ���߼�
     * 
     * @throws InterruptedException
     */
    private void requestService() throws InterruptedException
    {
        Thread.sleep(Long.MAX_VALUE);

    }

    public static void main(String[] args) throws Exception
    {
        DistributedSystemClient client = new DistributedSystemClient();

        // �ȹ���һ��zk������
        client.getZkClient();

        // ��ȡ�������б�
        client.updateServers();

        // �ͻ��˽���ҵ�����̣�����������ķ���
        client.requestService();
    }
}