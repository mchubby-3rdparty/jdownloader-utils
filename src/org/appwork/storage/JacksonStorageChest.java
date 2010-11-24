package org.appwork.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.appwork.utils.logging.Log;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

public class JacksonStorageChest extends Storage {

    private final HashMap<String, Object> map;
    private final String                  name;
    private String                        filename = null;
    private final boolean                 plain;

    public JacksonStorageChest(final String name) throws StorageException {
        this(name, false);
    }

    public JacksonStorageChest(final String name, final boolean plain) throws StorageException {
        this.map = new HashMap<String, Object>();
        this.name = name;
        this.plain = plain;
        this.filename = "cfg/" + name + (plain ? ".json" : ".ejs");
        synchronized (JSonStorage.LOCK) {
            final HashMap<String, Object> load = JSonStorage.restoreFrom(this.filename, null, new HashMap<String, Object>());
            this.map.putAll(load);
        }
    }

    @Override
    public void clear() throws StorageException {
        Entry<String, Object> next;
        for (final Iterator<Entry<String, Object>> it = this.map.entrySet().iterator(); it.hasNext();) {
            next = it.next();
            this.getEventSender().fireEvent(new StorageValueChangeEvent<Object>(this, next.getKey(), next.getValue(), null));
        }
        this.map.clear();
    }

    @Override
    public long decrease(final String key) {
        long ret = this.get(key, 0l).intValue();
        this.put(key, --ret);
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E get(final String key, final E def) throws StorageException {
        final boolean contains = this.map.containsKey(key);
        Object ret = contains ? this.map.get(key) : null;

        if (ret != null && def != null && ret.getClass() != def.getClass()) {
            // Housten we have...
            // ... to convert

            if (def instanceof Long) {
                if (ret instanceof Integer) {
                    // this is normal, because jackson converts tiny longs to
                    // ints automatically
                    // Log.exception(Level.FINE, new
                    // Exception("Had to convert integer to long for storage " +
                    // this.name + "." + key + "=" + ret));

                    ret = new Long(((Integer) ret).longValue());
                }

            } else if (def instanceof Integer) {
                if (ret instanceof Long) {

                    // Log.exception(Level.FINE, new
                    // Exception("Had to convert long to integer for storage " +
                    // this.name + "." + key + "=" + ret));
                    ret = new Integer(((Long) ret).intValue());
                }
            }
        }
        // put entry if we have no entry
        if (!contains) {
            ret = def;
            if (def instanceof Boolean) {
                this.put(key, (Boolean) def);
            } else if (def instanceof Long) {
                this.put(key, (Long) def);
            } else if (def instanceof Integer) {
                this.put(key, (Integer) def);
            } else if (def instanceof Byte) {
                this.put(key, (Byte) def);
            } else if (def instanceof String || def == null) {
                this.put(key, (String) def);
            } else if (def instanceof Enum<?>) {
                this.put(key, (Enum<?>) def);
            } else if (def instanceof Double) {
                this.put(key, (Double) def);
            } else {
                throw new StorageException("Invalid datatype: " + (def != null ? def.getClass() : "null"));
            }
        }

        if (def instanceof Enum<?> && ret instanceof String) {
            try {
                ret = Enum.valueOf(((Enum<?>) def).getDeclaringClass(), (String) ret);
            } catch (final Throwable e) {
                Log.exception(e);
                this.put(key, (Enum<?>) def);
                ret = def;
            }
        }
        return (E) ret;

    }

    public String getFilename() {
        return this.filename;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public long increase(final String key) {
        long ret = this.get(key, 0).intValue();
        this.put(key, ++ret);
        return ret;
    }

    public boolean isPlain() {
        return this.plain;
    }

    @Override
    public void put(final String key, final Boolean value) throws StorageException {
        final Boolean old = this.map.containsKey(key) ? this.get(key, value) : null;
        this.map.put(key, value);
        this.getEventSender().fireEvent(new StorageValueChangeEvent<Boolean>(this, key, old, value));
    }

    @Override
    public void put(final String key, final Byte value) throws StorageException {
        final Byte old = this.map.containsKey(key) ? this.get(key, value) : null;
        this.map.put(key, value);
        this.getEventSender().fireEvent(new StorageValueChangeEvent<Byte>(this, key, old, value));
    }

    @Override
    public void put(final String key, final Double value) throws StorageException {
        final Double old = this.map.containsKey(key) ? this.get(key, value) : null;
        this.map.put(key, value);
        this.getEventSender().fireEvent(new StorageValueChangeEvent<Double>(this, key, old, value));
    }

    @Override
    public void put(final String key, final Enum<?> value) throws StorageException {
        final Enum<?> old = this.map.containsKey(key) ? this.get(key, value) : null;
        this.map.put(key, value);
        this.getEventSender().fireEvent(new StorageValueChangeEvent<Enum<?>>(this, key, old, value));
    }

    @Override
    public void put(final String key, final Float value) throws StorageException {
        final Float old = this.map.containsKey(key) ? this.get(key, value) : null;
        this.map.put(key, value);
        this.getEventSender().fireEvent(new StorageValueChangeEvent<Float>(this, key, old, value));
    }

    @Override
    public void put(final String key, final Integer value) throws StorageException {
        final Integer old = this.map.containsKey(key) ? this.get(key, value) : null;
        this.map.put(key, value);
        this.getEventSender().fireEvent(new StorageValueChangeEvent<Integer>(this, key, old, value));
    }

    @Override
    public void put(final String key, final Long value) throws StorageException {
        final Long old = this.map.containsKey(key) ? this.get(key, value) : null;
        this.map.put(key, value);
        this.getEventSender().fireEvent(new StorageValueChangeEvent<Long>(this, key, old, value));
    }

    @Override
    public void put(final String key, final String value) throws StorageException {
        final String old = this.map.containsKey(key) ? this.get(key, value) : null;
        this.map.put(key, value);
        this.getEventSender().fireEvent(new StorageValueChangeEvent<String>(this, key, old, value));
    }

    @Override
    public Object remove(final String key) {
        return this.map.remove(key);
    }

    @Override
    public void save() throws StorageException {
        synchronized (JSonStorage.LOCK) {
            try {
                String json = null;

                /*
                 * writer are not threadsafe,
                 * http://wiki.fasterxml.com/JacksonBestPracticeThreadSafety
                 */
                json = JSonStorage.getMapper().writeValueAsString(this.map);
                JSonStorage.saveTo(this.filename, json);
            } catch (final JsonGenerationException e) {
                Log.exception(e);
            } catch (final JsonMappingException e) {
                Log.exception(e);
            } catch (final IOException e) {
                Log.exception(e);
            }
        }
    }

    @Override
    public String toString() {
        try {
            return JSonStorage.getMapper().writeValueAsString(this.map);
        } catch (final JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this.map.toString();
    }

}
