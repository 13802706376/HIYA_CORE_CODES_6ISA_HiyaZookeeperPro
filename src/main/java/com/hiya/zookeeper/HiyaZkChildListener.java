package com.hiya.zookeeper;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;

public class HiyaZkChildListener implements IZkChildListener
{
    /**
     * handleChildChange�� ��������������˷��͹�����֪ͨ parentPath����Ӧ�ĸ��ڵ��·��
     * currentChilds���ӽڵ�����·��
     */
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception
    {
        System.out.println(parentPath);
        System.out.println(currentChilds.toString());
    }
}