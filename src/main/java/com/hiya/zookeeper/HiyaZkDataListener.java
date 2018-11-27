package com.hiya.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;

public class HiyaZkDataListener implements IZkDataListener
{
    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception
    {
        System.out.println(dataPath);
        System.out.println(data.toString());
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception
    {
        System.out.println(dataPath);
    }
}