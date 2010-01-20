/**
 * Copyright (c) 2009 - 2010 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.net.meteredconnection
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.net.meteredconnection;

import java.io.IOException;
import java.io.InputStream;

import org.appwork.utils.speedmeter.SpeedMeterInterface;

/**
 * @author daniel
 * 
 */
public class MeteredInputStream extends InputStream implements SpeedMeterInterface {

    private InputStream in;
    private SpeedMeterInterface speedmeter = null;
    private long transfered = 0;
    private long transfered2 = 0;
    private long time = 0;
    private int readTmp1;
    private int readTmp2;
    private long speed = 0;

    /**
     * constructor for MeterdInputStream
     * 
     * @param in
     */
    public MeteredInputStream(InputStream in) {
        this.in = in;
    }

    /**
     * constructor for MeteredInputStream with custom SpeedMeter
     * 
     * @param in
     * @param speedmeter
     */
    public MeteredInputStream(InputStream in, SpeedMeterInterface speedmeter) {
        this.in = in;
        this.speedmeter = speedmeter;
    }

    @Override
    public int read() throws IOException {
        readTmp1 = in.read();
        if (readTmp1 != -1) transfered++;
        return readTmp1;
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        readTmp2 = in.read(b, off, len);
        if (readTmp2 != -1) transfered += readTmp2;
        return readTmp2;
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

    @Override
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.utils.SpeedMeterInterface#getSpeedMeter()
     */
    @Override
    public synchronized long getSpeedMeter() {
        if (time == 0) {
            time = System.currentTimeMillis();
            transfered2 = transfered;
            return 0;
        }
        if (System.currentTimeMillis() - time < 1000) {
            if (speedmeter != null) return speedmeter.getSpeedMeter();
            return speed;
        }
        try {
            if (speedmeter != null) {
                speedmeter.putSpeedMeter(transfered - transfered2, System.currentTimeMillis() - time);
                return speedmeter.getSpeedMeter();
            } else {
                speed = ((transfered - transfered2) / (System.currentTimeMillis() - time)) * 1000;
                return speed;
            }
        } finally {
            transfered2 = transfered;
            time = System.currentTimeMillis();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.utils.SpeedMeterInterface#putSpeedMeter(long, long)
     */
    @Override
    public void putSpeedMeter(long bytes, long time) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.utils.SpeedMeterInterface#resetSpeedMeter()
     */
    @Override
    public synchronized void resetSpeedMeter() {
        if (speedmeter != null) {
            speedmeter.resetSpeedMeter();
        } else {
            transfered2 = transfered = 0;
        }
    }
}
