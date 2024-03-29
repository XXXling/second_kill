/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zookeeper.test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.server.*;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.test.ClientBase.CONNECTION_TIMEOUT;

public class InvalidSnapshotTest extends ZKTestCase implements Watcher {
    private final static Logger LOG = LoggerFactory.getLogger(UpgradeTest.class);
    private static final String HOSTPORT =
            "127.0.0.1:" + PortAssignment.unique();

    private static final File testData = new File(
            System.getProperty("test.data.dir", "build/test/data"));
    private CountDownLatch startSignal;

    /**
     * Verify the LogFormatter by running it on a known file.
     */
    @Test
    public void testLogFormatter() throws Exception {
        File snapDir = new File(testData, "invalidsnap");
        File logfile = new File(new File(snapDir, "version-2"), "log.274");
        String[] args = {logfile.getCanonicalFile().toString()};
        LogFormatter.main(args);
    }
    

    /**
     * Verify the SnapshotFormatter by running it on a known file.
     */
    @Test
    public void testSnapshotFormatter() throws Exception {
        File snapDir = new File(testData, "invalidsnap");
        File snapfile = new File(new File(snapDir, "version-2"), "snapshot.272");
        String[] args = {snapfile.getCanonicalFile().toString()};
        SnapshotFormatter.main(args);
    }
    
    /**
     * Verify the SnapshotFormatter by running it on a known file with one null data.
     */
    @Test
    public void testSnapshotFormatterWithNull() throws Exception {
        File snapDir = new File(testData, "invalidsnap");
        File snapfile = new File(new File(snapDir, "version-2"), "snapshot.273");
        String[] args = {snapfile.getCanonicalFile().toString()};
        SnapshotFormatter.main(args);
    }
    
    /**
     * test the snapshot
     * @throws Exception an exception could be expected
     */
    @Test
    public void testSnapshot() throws Exception {
        File snapDir = new File(testData, "invalidsnap");
        ZooKeeperServer zks = new ZooKeeperServer(snapDir, snapDir, 3000);
        SyncRequestProcessor.setSnapCount(1000);
        final int PORT = Integer.parseInt(HOSTPORT.split(":")[1]);
        ServerCnxnFactory f = ServerCnxnFactory.createFactory(PORT, -1);
        f.startup(zks);
        LOG.info("starting up the zookeeper server .. waiting");
        Assert.assertTrue("waiting for server being up",
                ClientBase.waitForServerUp(HOSTPORT, CONNECTION_TIMEOUT));
        ZooKeeper zk = new ZooKeeper(HOSTPORT, 20000, this);
        try {
            // we know this from the data files
            // this node is the last node in the snapshot

            Assert.assertTrue(zk.exists("/9/9/8", false) != null);
        } finally {
            zk.close();
        }
        f.shutdown();
        zks.shutdown();
        Assert.assertTrue("waiting for server down",
                   ClientBase.waitForServerDown(HOSTPORT,
                           ClientBase.CONNECTION_TIMEOUT));

    }

    public void process(WatchedEvent event) {
        LOG.info("Event:" + event.getState() + " " + event.getType() + " " + event.getPath());
        if (event.getState() == KeeperState.SyncConnected
                && startSignal != null && startSignal.getCount() > 0)
        {
            startSignal.countDown();
        }
    }
}
