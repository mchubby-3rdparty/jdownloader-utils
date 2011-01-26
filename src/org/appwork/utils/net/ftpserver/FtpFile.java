/**
 * Copyright (c) 2009 - 2011 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.net.ftpserver
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.net.ftpserver;

/**
 * @author thomas
 * 
 */
public class FtpFile {

    private final String  name;
    private final long    size;
    private final long    lastModified = 0;
    private final boolean isDirectory;

    /**
     * @param name
     * @param length
     * @param directory
     */
    public FtpFile(final String name, final long length, final boolean directory, final long lastMod) {
        this.name = name;
        size = length;
        isDirectory = directory;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

}
