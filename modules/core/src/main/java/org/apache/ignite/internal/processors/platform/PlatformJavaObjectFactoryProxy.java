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

package org.apache.ignite.internal.processors.platform;

import org.apache.ignite.IgniteException;
import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;
import org.apache.ignite.internal.GridKernalContext;
import org.apache.ignite.internal.binary.BinaryRawReaderEx;
import org.apache.ignite.internal.binary.BinaryRawWriterEx;
import org.apache.ignite.internal.processors.platform.utils.PlatformUtils;
import org.apache.ignite.internal.util.tostring.GridToStringExclude;
import org.apache.ignite.internal.util.typedef.internal.S;
import org.apache.ignite.internal.util.typedef.internal.U;
import org.apache.ignite.platform.PlatformJavaObjectFactory;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for Java object factory.
 */
public class PlatformJavaObjectFactoryProxy implements Externalizable, Binarylizable {
    /** User-defined type. */
    private static final int TYP_USER = 0;

    /** Default factory. */
    private static final int TYP_DEFAULT = 1;

    /** Factory type. */
    private int factoryType;

    /** Factory class name. */
    private String factoryClsName;

    /** Optional payload for special factory types. */
    @GridToStringExclude
    private Object payload;

    /** Properties. */
    @GridToStringExclude
    private Map<String, Object> props;

    /**
     * Default constructor.
     */
    public PlatformJavaObjectFactoryProxy() {
        // No-op.
    }

    /**
     * Get factory instance.
     *
     * @param ctx Kernal context for injections.
     * @return Factory instance.
     */
    public PlatformJavaObjectFactory factory(GridKernalContext ctx) {
        // Create factory.
        PlatformJavaObjectFactory res;

        switch (factoryType) {
            case TYP_DEFAULT:
                res = new PlatformDefaultJavaObjectFactory();

                break;

            case TYP_USER:
                res = PlatformUtils.createJavaObject(factoryClsName);

                break;

            default:
                throw new IgniteException("Unsupported Java object factory type: " + factoryType);
        }

        // Initialize factory.
        if (res instanceof PlatformJavaObjectFactoryEx)
            ((PlatformJavaObjectFactoryEx)res).initialize(payload, props);
        else
            PlatformUtils.initializeJavaObject(res, factoryClsName, props, ctx);

        return res;
    }

    /** {@inheritDoc} */
    @Override public void writeBinary(BinaryWriter writer) throws BinaryObjectException {
        BinaryRawWriterEx rawWriter = (BinaryRawWriterEx)writer.rawWriter();

        rawWriter.writeInt(factoryType);
        rawWriter.writeString(factoryClsName);
        rawWriter.writeObjectDetached(payload);

        if (props != null) {
            rawWriter.writeInt(props.size());

            for (Map.Entry<String, Object> prop : props.entrySet()) {
                rawWriter.writeString(prop.getKey());
                rawWriter.writeObjectDetached(prop.getValue());
            }
        }
        else
            rawWriter.writeInt(0);
    }

    /** {@inheritDoc} */
    @Override public void readBinary(BinaryReader reader) throws BinaryObjectException {
        BinaryRawReaderEx rawReader = (BinaryRawReaderEx)reader.rawReader();

        factoryType = rawReader.readInt();
        factoryClsName = rawReader.readString();
        payload = rawReader.readObjectDetached();

        int propsSize = rawReader.readInt();

        if (propsSize > 0) {
            props = new HashMap<>(propsSize);

            for (int i = 0; i < propsSize; i++) {
                String key = rawReader.readString();
                Object val = rawReader.readObjectDetached();

                props.put(key, val);
            }
        }
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(factoryType);
        U.writeString(out, factoryClsName);
        out.writeObject(payload);
        U.writeMap(out, props);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        factoryType = in.readInt();
        factoryClsName = U.readString(in);
        payload = in.readObject();
        props = U.readMap(in);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(PlatformJavaObjectFactoryProxy.class, this);
    }
}