/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.plugin.extensions.communication;

import org.apache.ignite.lang.*;

import java.nio.*;
import java.util.*;

/**
 * Communication message reader.
 * <p>
 * Allows to customize the binary format of communication messages.
 */
public interface MessageReader {
    /**
     * Sets but buffer to read from.
     *
     * @param buf Byte buffer.
     */
    public void setBuffer(ByteBuffer buf);

    /**
     * Reads {@code byte} value.
     *
     * @param name Field name.
     * @return {@code byte} value.
     */
    public byte readByte(String name);

    /**
     * Reads {@code short} value.
     *
     * @param name Field name.
     * @return {@code short} value.
     */
    public short readShort(String name);

    /**
     * Reads {@code int} value.
     *
     * @param name Field name.
     * @return {@code int} value.
     */
    public int readInt(String name);

    /**
     * Reads {@code long} value.
     *
     * @param name Field name.
     * @return {@code long} value.
     */
    public long readLong(String name);

    /**
     * Reads {@code float} value.
     *
     * @param name Field name.
     * @return {@code float} value.
     */
    public float readFloat(String name);

    /**
     * Reads {@code double} value.
     *
     * @param name Field name.
     * @return {@code double} value.
     */
    public double readDouble(String name);

    /**
     * Reads {@code char} value.
     *
     * @param name Field name.
     * @return {@code char} value.
     */
    public char readChar(String name);

    /**
     * Reads {@code boolean} value.
     *
     * @param name Field name.
     * @return {@code boolean} value.
     */
    public boolean readBoolean(String name);

    /**
     * Reads {@code byte} array.
     *
     * @param name Field name.
     * @return {@code byte} array.
     */
    public byte[] readByteArray(String name);

    /**
     * Reads {@code short} array.
     *
     * @param name Field name.
     * @return {@code short} array.
     */
    public short[] readShortArray(String name);

    /**
     * Reads {@code int} array.
     *
     * @param name Field name.
     * @return {@code int} array.
     */
    public int[] readIntArray(String name);

    /**
     * Reads {@code long} array.
     *
     * @param name Field name.
     * @return {@code long} array.
     */
    public long[] readLongArray(String name);

    /**
     * Reads {@code float} array.
     *
     * @param name Field name.
     * @return {@code float} array.
     */
    public float[] readFloatArray(String name);

    /**
     * Reads {@code double} array.
     *
     * @param name Field name.
     * @return {@code double} array.
     */
    public double[] readDoubleArray(String name);

    /**
     * Reads {@code char} array.
     *
     * @param name Field name.
     * @return {@code char} array.
     */
    public char[] readCharArray(String name);

    /**
     * Reads {@code boolean} array.
     *
     * @param name Field name.
     * @return {@code boolean} array.
     */
    public boolean[] readBooleanArray(String name);

    /**
     * Reads {@link String}.
     *
     * @param name Field name.
     * @return {@link String}.
     */
    public String readString(String name);

    /**
     * Reads {@link BitSet}.
     *
     * @param name Field name.
     * @return {@link BitSet}.
     */
    public BitSet readBitSet(String name);

    /**
     * Reads {@link UUID}.
     *
     * @param name Field name.
     * @return {@link UUID}.
     */
    public UUID readUuid(String name);

    /**
     * Reads {@link IgniteUuid}.
     *
     * @param name Field name.
     * @return {@link IgniteUuid}.
     */
    public IgniteUuid readIgniteUuid(String name);

    /**
     * Reads nested message.
     *
     * @param name Field name.
     * @return Message.
     */
    public <T extends MessageAdapter> T readMessage(String name);

    /**
     * Reads array of objects.
     *
     * @param name Field name.
     * @param itemCls Array component type.
     * @return Array of objects.
     */
    public <T> T[] readObjectArray(String name, Class<T> itemCls);

    /**
     * Reads collection.
     *
     * @param name Field name.
     * @param itemCls Collection item type.
     * @return Collection.
     */
    public <C extends Collection<T>, T> C readCollection(String name, Class<T> itemCls);

    /**
     * Reads map.
     *
     * @param name Field name.
     * @param keyCls Map key type.
     * @param valCls Map value type.
     * @param linked Whether {@link LinkedHashMap} should be created.
     * @return Map.
     */
    public <M extends Map<K, V>, K, V> M readMap(String name, Class<K> keyCls, Class<V> valCls, boolean linked);

    /**
     * Tells whether last invocation of any of {@code readXXX(...)}
     * methods has fully written the value. {@code False} is returned
     * if there were not enough remaining bytes in byte buffer.
     *
     * @return Whether las value was fully read.
     */
    public boolean isLastRead();
}
