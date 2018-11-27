package com.hiya.zookeeper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdMaker
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ZkClient client = null;
    // �����ַ
    private final String server;
    // id���������ڵ�
    private final String root;
    // id�ڵ�
    private final String nodeName;
    // ����״̬: true:����;false:û��������Ĭ��û������
    private volatile boolean running = false;
    private ExecutorService cleanExector = null;

    public enum RemoveMethod
    {
        // ��������������
        NONE, IMMEDIATELY, DELAY
    }

    public IdMaker(String zkServer, String root, String nodeName)
    {
        this.server = zkServer;
        this.root = root;
        this.nodeName = nodeName;
    }

    /**
     * ����
     *
     * @version 2016��11��29������9:37:36
     * @author wuliu
     * @throws Exception
     */
    public void start() throws Exception
    {
        if (running)
            throw new Exception("server has stated...");
        running = true;
        init();
    }

    /**
     * ֹͣ����
     *
     * @version 2016��11��29������9:45:38
     * @author wuliu
     * @throws Exception
     */
    public void stop() throws Exception
    {
        if (!running)
            throw new Exception("server has stopped...");
        running = false;
        freeResource();
    }

    private void init()
    {
        client = new ZkClient(server, 5000, 5000, new BytesPushThroughSerializer());
        cleanExector = Executors.newFixedThreadPool(10);
        try
        {
            client.createPersistent(root, true);
        } catch (ZkNodeExistsException e)
        {
            logger.info("�ڵ��Ѿ�����,�ڵ�·��:" + root);
        }

    }

    /**
     * ��Դ�ͷ� T
     *
     * @version 2016��11��29������9:38:59
     * @author wuliu
     */
    private void freeResource()
    {
        cleanExector.shutdown();
        try
        {
            cleanExector.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
            cleanExector = null;
        }
        if (client != null)
        {
            client.close();
            client = null;
        }
    }

    /**
     * �ж��Ƿ���������
     *
     * @version 2016��11��29������9:39:58
     * @author wuliu
     * @throws Exception
     */
    private void checkRunning() throws Exception
    {
        if (!running)
            throw new Exception("���ȵ���start��������");
    }

    /**
     * ��ȡID
     *
     * @version 2016��11��29������9:46:48
     * @author wuliu
     * @param str
     * @return
     */
    private String extractId(String str)
    {
        int index = str.lastIndexOf(nodeName);// 20
        if (index >= 0)
        {
            index += nodeName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }

    /**
     * ��ȡid
     *
     * @version 2016��11��29������9:40:33
     * @author wuliu
     * @param removeMethod
     * @return
     * @throws Exception
     */
    public String generateId(RemoveMethod removeMethod) throws Exception
    {
        checkRunning();
        final String fullNodePath = root.concat("/").concat(nodeName);
        // ����˳��ڵ�ÿ�����ڵ��Ϊ���ĵ�һ���ӽڵ�ά��һ��ʱ�򣬻��¼ÿ���ӽڵ㴴�����Ⱥ�˳��
        // ����������ԣ��ڴ����ӽڵ��ʱ�򣬿�������������ԣ���ô�ڴ����ڵ�����У�
        // ZooKeeper���Զ�Ϊ�����ڵ�������һ�����ֺ�׺����Ϊ�µĽڵ���
        final String ourPath = client.createPersistentSequential(fullNodePath, null);
        if (removeMethod.equals(RemoveMethod.IMMEDIATELY))
        {
            // ����ɾ��
            client.delete(ourPath);
        } else if (removeMethod.equals(RemoveMethod.DELAY))
        {
            // ����ɾ��
            cleanExector.execute(new Runnable()
            {
                public void run()
                {
                    client.delete(ourPath);
                }
            });
        }
        return extractId(ourPath);
    }

}