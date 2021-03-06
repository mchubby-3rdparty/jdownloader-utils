/**
 * 
 * ====================================================================================================================================================
 *         "AppWork Utilities" License
 *         The "AppWork Utilities" will be called [The Product] from now on.
 * ====================================================================================================================================================
 *         Copyright (c) 2009-2015, AppWork GmbH <e-mail@appwork.org>
 *         Schwabacher Straße 117
 *         90763 Fürth
 *         Germany   
 * === Preamble ===
 *     This license establishes the terms under which the [The Product] Source Code & Binary files may be used, copied, modified, distributed, and/or redistributed.
 *     The intent is that the AppWork GmbH is able to provide their utilities library for free to non-commercial projects whereas commercial usage is only permitted after obtaining a commercial license.
 *     These terms apply to all files that have the [The Product] License header (IN the file), a <filename>.license or <filename>.info (like mylib.jar.info) file that contains a reference to this license.
 * 	
 * === 3rd Party Licences ===
 *     Some parts of the [The Product] use or reference 3rd party libraries and classes. These parts may have different licensing conditions. Please check the *.license and *.info files of included libraries
 *     to ensure that they are compatible to your use-case. Further more, some *.java have their own license. In this case, they have their license terms in the java file header. 	
 * 	
 * === Definition: Commercial Usage ===
 *     If anybody or any organization is generating income (directly or indirectly) by using [The Product] or if there's any commercial interest or aspect in what you are doing, we consider this as a commercial usage.
 *     If your use-case is neither strictly private nor strictly educational, it is commercial. If you are unsure whether your use-case is commercial or not, consider it as commercial or contact us.
 * === Dual Licensing ===
 * === Commercial Usage ===
 *     If you want to use [The Product] in a commercial way (see definition above), you have to obtain a paid license from AppWork GmbH.
 *     Contact AppWork for further details: <e-mail@appwork.org>
 * === Non-Commercial Usage ===
 *     If there is no commercial usage (see definition above), you may use [The Product] under the terms of the 
 *     "GNU Affero General Public License" (http://www.gnu.org/licenses/agpl-3.0.en.html).
 * 	
 *     If the AGPL does not fit your needs, please contact us. We'll find a solution.
 * ====================================================================================================================================================
 * ==================================================================================================================================================== */
package org.appwork.storage.simplejson.mapper;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.appwork.storage.TypeRef;
import org.appwork.storage.simplejson.JSonArray;
import org.appwork.storage.simplejson.JSonNode;
import org.appwork.storage.simplejson.JSonObject;
import org.appwork.storage.simplejson.JSonValue;
import org.appwork.utils.StringUtils;
import org.appwork.utils.reflection.Clazz;

/**
 * @author thomas
 *
 */
public class JSonMapper {

    /**
     * @param value
     * @param type
     * @return
     */
    public static Object cast(Object v, final Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                v = ((Boolean) v).booleanValue();
            } else if (type == char.class) {
                v = (char) ((Number) v).byteValue();
            } else if (type == byte.class) {
                v = ((Number) v).byteValue();
            } else if (type == short.class) {
                v = ((Number) v).shortValue();
            } else if (type == int.class) {
                v = ((Number) v).intValue();
            } else if (type == long.class) {
                v = ((Number) v).longValue();
            } else if (type == float.class) {
                v = ((Number) v).floatValue();
            } else if (type == double.class) {
                //
                v = ((Number) v).doubleValue();

            }
        } else if (type == Boolean.class) {
            v = ((Boolean) v).booleanValue();
        } else if (type == Character.class) {
            v = (char) ((Number) v).byteValue();
        } else if (type == Byte.class) {
            v = ((Number) v).byteValue();
        } else if (type == Short.class) {
            v = ((Number) v).shortValue();
        } else if (type == Integer.class) {
            v = ((Number) v).intValue();
        } else if (type == Long.class) {
            v = ((Number) v).longValue();
        } else if (type == Float.class) {
            v = ((Number) v).floatValue();
        } else if (type == Double.class) {
            //
            v = ((Number) v).doubleValue();

        }

        return v;
    }

    private boolean                                ignorePrimitiveNullMapping    = false;

    private boolean                                ignoreIllegalArgumentMappings = false;

    /**
     * @param value
     * @param type
     * @return
     */
    private boolean                                ignoreIllegalEnumMappings     = false;

    private final HashMap<Class<?>, TypeMapper<?>> typeMapper;

    public JSonMapper() {

        typeMapper = new HashMap<Class<?>, TypeMapper<?>>();
        this.addMapper(File.class, new FileMapper());
        this.addMapper(Class.class, new ClassMapper());
        this.addMapper(URL.class, new URLMapper());
        this.addMapper(Date.class, new DateMapper());
    }

    /**
     * @param <T>
     * @param class1
     * @param fileMapper
     */
    public <T> void addMapper(final Class<T> class1, final TypeMapper<T> fileMapper) {
        typeMapper.put(class1, fileMapper);

    }

    /**
     * @param obj
     * @return
     * @throws MapperException
     */
    @SuppressWarnings("unchecked")
    public JSonNode create(final Object obj) throws MapperException {
        try {

            if (obj == null) {
                return new JSonValue(null);
            }
            final Class<? extends Object> clazz = obj.getClass();
            TypeMapper<?> mapper;
            if (clazz.isPrimitive()) {
                if (clazz == boolean.class) {
                    return new JSonValue((Boolean) obj);
                } else if (clazz == char.class) {
                    return new JSonValue(0 + ((Character) obj).charValue());
                } else if (clazz == byte.class) {
                    return new JSonValue(((Byte) obj).longValue());
                } else if (clazz == short.class) {
                    return new JSonValue(((Short) obj).longValue());
                } else if (clazz == int.class) {
                    return new JSonValue(((Integer) obj).longValue());
                } else if (clazz == long.class) {
                    return new JSonValue(((Long) obj).longValue());
                } else if (clazz == float.class) {
                    return new JSonValue(((Float) obj).doubleValue());
                } else if (clazz == double.class) {
                    return new JSonValue(((Double) obj).doubleValue());
                }
            } else if (clazz.isEnum()) {
                return new JSonValue(obj + "");
            } else if (obj instanceof Boolean) {
                return new JSonValue(((Boolean) obj).booleanValue());
            } else if (obj instanceof Character) {
                return new JSonValue(0 + ((Character) obj).charValue());
            } else if (obj instanceof Byte) {
                return new JSonValue(((Byte) obj).longValue());
            } else if (obj instanceof Short) {
                return new JSonValue(((Short) obj).longValue());
            } else if (obj instanceof Integer) {
                return new JSonValue(((Integer) obj).longValue());
            } else if (obj instanceof Long) {
                return new JSonValue(((Long) obj).longValue());
            } else if (obj instanceof Float) {
                return new JSonValue(((Float) obj).doubleValue());
            } else if (obj instanceof Double) {
                return new JSonValue(((Double) obj).doubleValue());

            } else if (obj instanceof String) {
                return new JSonValue((String) obj);
            } else if (obj instanceof Map) {

                final JSonObject ret = new JSonObject();
                Entry<Object, Object> next;
                for (final Iterator<Entry<Object, Object>> it = ((Map<Object, Object>) obj).entrySet().iterator(); it.hasNext();) {
                    next = it.next();
                    if (!(next.getKey() instanceof String)) {
                        throw new MapperException("Map keys have to be Strings: " + clazz + " Keyclass:" + (next.getKey() == null ? "<null>" : next.getKey().getClass()));
                    }
                    ret.put(next.getKey().toString(), create(next.getValue()));
                }
                return ret;
            } else if (obj instanceof Collection) {
                final JSonArray ret = new JSonArray();
                for (final Object o : (Collection<?>) obj) {
                    ret.add(create(o));
                }
                return ret;
            } else if (clazz.isArray()) {
                final JSonArray ret = new JSonArray();
                for (int i = 0; i < Array.getLength(obj); i++) {
                    ret.add(create(Array.get(obj, i)));
                }
                return ret;
            } else if (obj instanceof Class) {
                return new JSonValue(((Class<?>) obj).getName());
            } else if ((mapper = typeMapper.get(clazz)) != null) {
                return mapper.map(obj);
            } else/* if (obj instanceof Storable) */{
                final ClassCache cc = ClassCache.getClassCache(clazz);
                final JSonObject ret = new JSonObject();
                for (final Getter g : cc.getGetter()) {

                    ret.put(g.getKey(), create(g.getValue(obj)));
                }
                return ret;

            }
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        } catch (final SecurityException e) {

            e.printStackTrace();
        } catch (final NoSuchMethodException e) {

            e.printStackTrace();
        }

        return null;
    }

    public boolean isIgnoreIllegalArgumentMappings() {
        return ignoreIllegalArgumentMappings;
    }

    public boolean isIgnoreIllegalEnumMappings() {
        return ignoreIllegalEnumMappings;
    }

    /**
     * if json maps null to a primitive field
     *
     * @return
     */
    public boolean isIgnorePrimitiveNullMapping() {
        return ignorePrimitiveNullMapping;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object jsonToObject(final JSonNode json, Type type) throws MapperException {
        final ClassCache cc;
        try {

            Class<?> clazz = null;
            if (type instanceof ParameterizedType) {
                Type typ = ((ParameterizedType) type).getRawType();
                if (typ instanceof Class) {
                    clazz = (Class<?>) typ;
                }
            } else if (type instanceof Class) {
                clazz = (Class) type;
            } else if (type instanceof GenericArrayType) {
                // this is for 1.6
                // for 1.7 we do not get GenericArrayTypeImpl here but the
                // actual array class
                type = clazz = Array.newInstance((Class<?>) ((GenericArrayType) type).getGenericComponentType(), 0).getClass();
            }

            if (clazz == null || clazz == Object.class) {

                if (json instanceof JSonArray) {
                    type = clazz = LinkedList.class;
                } else if (json instanceof JSonObject) {
                    type = clazz = HashMap.class;
                } else if (json instanceof JSonValue) {
                    switch (((JSonValue) json).getType()) {
                    case BOOLEAN:
                        type = clazz = boolean.class;
                        break;
                    case DOUBLE:
                        type = clazz = double.class;
                        break;
                    case LONG:
                        type = clazz = long.class;
                        break;
                    case NULL:
                    case STRING:
                        type = clazz = String.class;

                    }

                }
            }
            final TypeMapper<?> tm = typeMapper.get(clazz);
            if (tm != null) {

                return tm.reverseMap(json);

            }
            if (json instanceof JSonValue) {
                if (!Clazz.isPrimitive(type) && !Clazz.isString(type) && type != Object.class && ((JSonValue) json).getValue() != null && !Clazz.isEnum(type)) {
                    //
                    throw new MapperException(json + " cannot be mapped to " + type);
                }
                switch (((JSonValue) json).getType()) {
                case BOOLEAN:
                case DOUBLE:
                case LONG:
                    if (type instanceof Class) {
                        return JSonMapper.cast(((JSonValue) json).getValue(), (Class) type);
                    } else {
                        return ((JSonValue) json).getValue();

                    }

                case STRING:
                    if (type instanceof Class && ((Class<?>) type).isEnum()) {
                        try {
                            return Enum.valueOf((Class<Enum>) type, ((JSonValue) json).getValue() + "");
                        } catch (final IllegalArgumentException e) {
                            if (isIgnoreIllegalArgumentMappings() || isIgnoreIllegalEnumMappings()) {
                                return null;
                            }
                            throw e;
                        }
                    } else {
                        return ((JSonValue) json).getValue();
                    }

                case NULL:
                    return null;

                }
            }
            if (type instanceof ParameterizedType) {
                final ParameterizedType pType = (ParameterizedType) type;
                Type raw = pType.getRawType();
                if (raw instanceof Class && Collection.class.isAssignableFrom((Class) raw)) {
                    final Collection<Object> inst = (Collection<Object>) mapClasses((Class) raw).newInstance();
                    final JSonArray obj = (JSonArray) json;
                    for (final JSonNode n : obj) {
                        inst.add(this.jsonToObject(n, pType.getActualTypeArguments()[0]));
                    }
                    return inst;
                } else if (raw instanceof Class && Map.class.isAssignableFrom((Class) raw)) {
                    final Map<String, Object> inst = (Map<String, Object>) mapClasses((Class) raw).newInstance();
                    final JSonObject obj = (JSonObject) json;
                    Entry<String, JSonNode> next;
                    for (final Iterator<Entry<String, JSonNode>> it = obj.entrySet().iterator(); it.hasNext();) {
                        next = it.next();
                        inst.put(next.getKey(), this.jsonToObject(next.getValue(), pType.getActualTypeArguments()[1]));
                    }
                    return inst;
                }
            }
            if (clazz != null) {
                if (clazz == Object.class) {
                    // guess type
                    if (json instanceof JSonArray) {
                        type = LinkedList.class;
                    } else if (json instanceof JSonObject) {
                        type = HashMap.class;
                    }

                }

                if (Collection.class.isAssignableFrom(clazz)) {
                    final Collection<Object> inst = (Collection<Object>) mapClasses(clazz).newInstance();
                    final JSonArray obj = (JSonArray) json;
                    final Type gs = clazz.getGenericSuperclass();
                    final Type gType;
                    if (gs instanceof ParameterizedType) {
                        gType = ((ParameterizedType) gs).getActualTypeArguments()[0];
                    } else {
                        gType = void.class;
                    }
                    for (final JSonNode n : obj) {
                        inst.add(this.jsonToObject(n, gType));
                    }
                    return inst;
                } else if (Map.class.isAssignableFrom(clazz)) {
                    final Map<String, Object> inst = (Map<String, Object>) mapClasses(clazz).newInstance();
                    final JSonObject obj = (JSonObject) json;
                    final Type gs = clazz.getGenericSuperclass();
                    final Type gType;
                    if (gs instanceof ParameterizedType) {
                        gType = ((ParameterizedType) gs).getActualTypeArguments()[1];
                    } else {
                        gType = void.class;
                    }

                    Entry<String, JSonNode> next;
                    for (final Iterator<Entry<String, JSonNode>> it = obj.entrySet().iterator(); it.hasNext();) {
                        next = it.next();
                        inst.put(next.getKey(), this.jsonToObject(next.getValue(), gType));
                    }

                    return inst;

                } else if (clazz.isArray()) {
                    final JSonArray obj = (JSonArray) json;
                    final Object arr = Array.newInstance(mapClasses(clazz.getComponentType()), obj.size());
                    for (int i = 0; i < obj.size(); i++) {
                        final Object v = this.jsonToObject(obj.get(i), clazz.getComponentType());

                        Array.set(arr, i, v);

                    }
                    return arr;
                } else {

                    if (json instanceof JSonArray) {

                        final java.util.List<Object> inst = new ArrayList<Object>();
                        final JSonArray obj = (JSonArray) json;
                        final Type gs = clazz.getGenericSuperclass();
                        final Type gType;
                        if (gs instanceof ParameterizedType) {
                            gType = ((ParameterizedType) gs).getActualTypeArguments()[0];
                        } else {
                            gType = Object.class;
                        }
                        for (final JSonNode n : obj) {
                            inst.add(this.jsonToObject(n, gType));
                        }
                        return inst;

                    } else {
                        final JSonObject obj = (JSonObject) json;
                        if (Clazz.isPrimitive(clazz)) {
                            //
                            if (isIgnoreIllegalArgumentMappings()) {
                                return null;
                            } else {
                                throw new IllegalArgumentException("Cannot Map " + obj + " to " + clazz);
                            }
                        }

                        cc = ClassCache.getClassCache(clazz);

                        final Object inst = cc.getInstance();
                        JSonNode value;
                        Object v;
                        for (final Setter s : cc.getSetter()) {

                            value = obj.get(s.getKey());
                            if (value == null) {
                                continue;
                            }
                            //
                            Type fieldType = s.getType();
                            // special handling for generic fields
                            if (fieldType instanceof TypeVariable) {
                                final Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
                                final TypeVariable<?>[] genericTypes = clazz.getTypeParameters();
                                for (int i = 0; i < genericTypes.length; i++) {
                                    if (StringUtils.equals(((TypeVariable) fieldType).getName(), genericTypes[i].getName())) {

                                        fieldType = actualTypes[i];
                                        break;
                                    }
                                }

                            }
                            v = this.jsonToObject(value, fieldType);
                            try {
                                s.setValue(inst, v);
                            } catch (final IllegalArgumentException e) {
                                if (isIgnoreIllegalArgumentMappings()) {
                                    continue;
                                } else if (v == null && isIgnorePrimitiveNullMapping()) {
                                    continue;
                                }
                                throw e;
                            }

                        }

                        return inst;
                    }
                }
            } else {
                System.err.println("TYPE?!");
            }
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param <T>
     * @param json
     * @param typeRef
     * @throws MapperException
     */
    @SuppressWarnings("unchecked")
    public <T> T jsonToObject(final JSonNode json, final TypeRef<T> type) throws MapperException {

        return (T) this.jsonToObject(json, type.getType());
    }

    /**
     * @param class1
     * @return
     * @throws MapperException
     */
    private Class<?> mapClasses(final Class<?> class1) throws MapperException {
        if (class1.isInterface()) {
            if (List.class.isAssignableFrom(class1)) {
                return ArrayList.class;
            } else if (Map.class.isAssignableFrom(class1)) {
                return HashMap.class;
            }

            throw new MapperException("Interface not supported: " + class1);

        }
        return class1;
    }

    public void setIgnoreIllegalArgumentMappings(final boolean ignoreIllegalArgumentMappings) {
        this.ignoreIllegalArgumentMappings = ignoreIllegalArgumentMappings;
    }

    public void setIgnoreIllegalEnumMappings(final boolean ignoreIllegalEnumMappings) {
        this.ignoreIllegalEnumMappings = ignoreIllegalEnumMappings;
    }

    public void setIgnorePrimitiveNullMapping(final boolean ignoreIllegalNullArguments) {
        ignorePrimitiveNullMapping = ignoreIllegalNullArguments;
    }

}
