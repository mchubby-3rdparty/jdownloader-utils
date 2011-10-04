/**
 * Copyright (c) 2009 - 2011 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.storage.config
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.storage.config.handler;

import java.lang.annotation.Annotation;

import org.appwork.storage.config.annotations.DefaultBooleanArrayValue;
import org.appwork.storage.config.annotations.DefaultBooleanValue;

/**
 * @author Thomas
 * 
 */
public class BooleanListHandler extends ListHandler<boolean[]> {

 

    /**
     * @param storageHandler
     * @param key
     */
    public BooleanListHandler(StorageHandler<?> storageHandler, String key) {
        super(storageHandler, key);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.storage.config.KeyHandler#initHandler()
     */
    @Override
    protected void initHandler() {
        try {
           defaultValue = getAnnotation(DefaultBooleanArrayValue.class).value();
           
         
        } catch (NullPointerException e) {
        }
        
     
    }
    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getAllowedAnnotations() {       
        return (Class<? extends Annotation>[]) new Class<?>[]{DefaultBooleanArrayValue.class};
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.appwork.storage.config.KeyHandler#validateValue(java.lang.Object)
     */
    @Override
    protected void validateValue(boolean[] object) throws Throwable {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.storage.config.KeyHandler#putValue(java.lang.Object)
     */
    @Override
    protected void putValue(boolean[] object) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.appwork.storage.config.handler.KeyHandler#getValue()
     */
    @Override
    public boolean[] getValue() {
        return primitiveStorage.get(getKey(), defaultValue);
    }

}
