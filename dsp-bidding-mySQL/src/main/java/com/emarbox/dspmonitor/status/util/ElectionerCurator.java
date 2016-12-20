package com.emarbox.dspmonitor.status.util;

import java.io.IOException;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.util.ConfigUtil;

import com.emarbox.dspmonitor.status.data.NodeStatus;

public class ElectionerCurator extends LeaderSelectorListenerAdapter {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	static{
		ConfigUtil.addConfig("zookeeper");
	}
	
//	private final String name = ConfigUtil.getString("zoo.account.mod");
	private static final String connectString = ConfigUtil.getString("zoo.connectString");
	private final String path = ConfigUtil.getString("zoo.root") + "/" + ConfigUtil.getString("zoo.account.mod");
	
	private final LeaderSelector leaderSelector;
	private final Object lock = new Object();  
	
	public ElectionerCurator() throws IOException {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
		CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
		leaderSelector = new LeaderSelector(client, path, this);
		leaderSelector.autoRequeue();
		
		client.start();
	}

	public void start() throws IOException, InterruptedException {
		leaderSelector.start();
		 synchronized (lock) {
             lock.wait();  
         }
	}

	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {  
        log.info("I am leader...");  
        //如果takeLeadership方法被调用,说明此selector实例已经为leader  
        //此方法需要阻塞,直到selector放弃leader角色  
        NodeStatus.isMajorNode = true;  
        while (NodeStatus.isMajorNode) {
            synchronized (lock) {
                lock.wait();  
            }  
        }  
    }

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {  
        log.info("Connection state changed...");  
        //对于LeaderSelector,底层实现为对leaderPath节点使用了"排他锁",  
        //"排他锁"的本质,就是一个"临时节点"  
        //如果接收到LOST,说明此selector实例已经丢失了leader信息.  
        if (newState == ConnectionState.LOST) {
            NodeStatus.isMajorNode = false;  
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }
	
}
